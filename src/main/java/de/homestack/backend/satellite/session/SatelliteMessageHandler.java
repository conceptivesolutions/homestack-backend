package de.homestack.backend.satellite.session;

import de.homestack.backend.database.change.IRepositoryChangeObserver;
import de.homestack.backend.database.user.IMetricRecordDBRepository;
import de.homestack.backend.satellite.auth.ISatelliteAuthenticator;
import de.homestack.backend.satellite.config.ISatelliteConfigFactory;
import io.conceptive.homestack.model.data.metric.MetricRecordDataModel;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.conceptive.homestack.model.satellite.events.SatelliteWebSocketEvents;
import io.conceptive.homestack.model.satellite.events.data.AuthenticateEventData;
import io.conceptive.homestack.model.websocket.WebsocketEvent;
import io.reactivex.disposables.Disposable;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.*;

import javax.inject.Inject;
import javax.websocket.*;
import java.util.Set;

/**
 * Handles all websocket events
 *
 * @author wlganzer, 13.11.2020
 */
@SuppressWarnings("rawtypes")
class SatelliteMessageHandler implements MessageHandler.Whole<WebsocketEvent>
{
  private static final String _SESSIONKEY_AUTHORIZED = "authorized";
  private static final String _SESSIONKEY_USER_ID = "userId";
  private static final String _SESSIONKEY_SATELLITE_ID = "id";
  private static final String _SESSIONKEY_SATELLITE_VERSION = "version";

  @Inject
  protected ISatelliteAuthenticator authenticator;

  @Inject
  protected ISatelliteConfigFactory configFactory;

  @Inject
  protected IMetricRecordDBRepository metricRecordRepository;

  @Inject
  protected IRepositoryChangeObserver repositoryChangeObserver;

  private Session session;
  private Disposable changeDisposable;

  /**
   * Attaches this handler to the givben session
   *
   * @param pSession Session to attach to
   */
  public void attach(@NotNull Session pSession)
  {
    if (session != null)
      throw new RuntimeException("MessageHandle already bound to session. Re-Attach not supported.");
    session = pSession;
    session.addMessageHandler(WebsocketEvent.class, this);
  }

  /**
   * Disposes all opened stuff
   */
  public void dispose()
  {
    if (session != null)
      session.removeMessageHandler(this);
    session = null;
    if (changeDisposable != null && !changeDisposable.isDisposed())
      changeDisposable.dispose();
  }

  @Override
  public void onMessage(@Nullable WebsocketEvent pEvent)
  {
    if (pEvent == null)
      return;

    try
    {
      Object userID = session.getUserProperties().get(_SESSIONKEY_USER_ID);
      Object authorized = session.getUserProperties().get(_SESSIONKEY_AUTHORIZED);

      // Determine, if authorized
      if (Boolean.TRUE.equals(authorized) && userID != null)
      {
        // First handle it authorized - if not handled, try with "unauthorized" events
        if (!handleEventAuthorized((String) userID, pEvent))
          handleEventUnauthorized(pEvent);
      }
      else
        handleEventUnauthorized(pEvent);
    }
    catch (Exception e)
    {
      Logger.getLogger(getClass()).error("Failed to process message from satellite", e);
    }
  }

  /**
   * Gets called, if an event happened, but the session is not authenticated yet
   *
   * @param pEvent event that happened
   */
  @Metered(name = "satellite_unauthorizedEvents", description = "meters how much unauthorized events were received", absolute = true)
  protected void handleEventUnauthorized(@NotNull WebsocketEvent<?> pEvent)
  {
    if (pEvent.equalType(SatelliteWebSocketEvents.AUTHENTICATE))
    {
      String leaseID = null;
      String version = null;

      try
      {
        AuthenticateEventData data = SatelliteWebSocketEvents.AUTHENTICATE.payloadOf(pEvent);
        version = data.version;

        // just ignore invalid requests
        leaseID = data.leaseID;
        if (data.leaseID == null || data.leaseID.isBlank() || data.leaseToken == null || data.leaseToken.isBlank())
          return;

        // Authenticate (throws Exception, if invalid) and get satellite id
        SatelliteLeaseDataModel lease = authenticator.authenticate(data.leaseID, data.leaseToken);

        // set information to session because we know, that we are now authenticated - because of JWT decoded sucessfully
        session.getUserProperties().put(_SESSIONKEY_AUTHORIZED, true);
        session.getUserProperties().put(_SESSIONKEY_USER_ID, lease.userID);
        session.getUserProperties().put(_SESSIONKEY_SATELLITE_ID, lease.satelliteID);
        session.getUserProperties().put(_SESSIONKEY_SATELLITE_VERSION, version);

        // answer with (initial) config
        renewConfig(lease.userID);

        // watch all changes to notify the satellite if something changes
        if (changeDisposable == null || changeDisposable.isDisposed())
          changeDisposable = repositoryChangeObserver.observeChangesForUser(lease.userID)
              .subscribe(pTimestamp -> renewConfig(lease.userID));
      }
      catch (Exception e)
      {
        _killSession();
        Logger.getLogger(SatelliteMessageHandler.class).warn("Failed to authenticate satellite with lease (" + leaseID + ", " + version + ")", e);
      }
    }
    else
      _killSession();
  }

  /**
   * Gets called, if an event happened and the session is already authenticated
   *
   * @param pUserID ID of the authorized user
   * @param pEvent  event that happened
   * @return true, if event was handled
   */
  @Metered(name = "satellite_authorizedEvents", description = "meters how much authorized events were received", absolute = true)
  protected boolean handleEventAuthorized(@NotNull String pUserID, @NotNull WebsocketEvent<?> pEvent)
  {
    if (pEvent.equalType(SatelliteWebSocketEvents.RECORDS))
    {
      Set<MetricRecordDataModel> records = SatelliteWebSocketEvents.RECORDS.payloadOf(pEvent).records;
      if (records != null && !records.isEmpty())
        records.forEach(pRecord -> metricRecordRepository.upsertRecord(pUserID, pRecord));
      return true;
    }
    else
    {
      Logger.getLogger(SatelliteMessageHandler.class).warn(pEvent + " was not handled, because no handler was registered for it");
      return false;
    }
  }

  /**
   * Renews the satellite configuration
   *
   * @param pUserID ID of the current user
   */
  @Metered(name = "satellite_configRenewal", description = "meters how much satellite configs renew", absolute = true)
  protected void renewConfig(@NotNull String pUserID)
  {
    session.getAsyncRemote().sendObject(SatelliteWebSocketEvents.CONFIG.payload(configFactory.create(pUserID)));
  }

  /**
   * Kills the current
   */
  private void _killSession()
  {
    try
    {
      session.getUserProperties().put(_SESSIONKEY_AUTHORIZED, false);
      session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Unauthorized"));
    }
    catch (Exception e)
    {
      // should not happen - the session is already closed
    }
  }

}
