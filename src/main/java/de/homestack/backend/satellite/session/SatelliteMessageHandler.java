package de.homestack.backend.satellite.session;

import de.homestack.backend.database.user.IMetricRecordDBRepository;
import de.homestack.backend.satellite.auth.ISatelliteAuthenticator;
import de.homestack.backend.satellite.config.ISatelliteConfigFactory;
import io.conceptive.homestack.model.data.metric.MetricRecordDataModel;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.conceptive.homestack.model.satellite.events.SatelliteWebSocketEvents;
import io.conceptive.homestack.model.satellite.events.data.AuthenticateEventData;
import io.conceptive.homestack.model.websocket.WebsocketEvent;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.*;

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
  private final ISatelliteAuthenticator authenticator;
  private final ISatelliteConfigFactory configFactory;
  private final IMetricRecordDBRepository metricRecordRepository;
  private final Session session;

  public SatelliteMessageHandler(@NotNull ISatelliteAuthenticator pAuthenticator, @NotNull ISatelliteConfigFactory pConfigFactory, @NotNull IMetricRecordDBRepository pMetricRecordRepository,
                                 @NotNull Session pSession)
  {
    authenticator = pAuthenticator;
    configFactory = pConfigFactory;
    metricRecordRepository = pMetricRecordRepository;
    session = pSession;
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
        if (!_handleEventAuthorized((String) userID, pEvent))
          _handleEventUnauthorized(pEvent);
      }
      else
        _handleEventUnauthorized(pEvent);
    }
    catch (Exception e)
    {
      Logger.getLogger(getClass()).error("Failed to process message from satellite", e);
    }
  }

  /**
   * Disposes all opened stuff
   */
  public void dispose()
  {
  }

  /**
   * Gets called, if an event happened, but the session is not authenticated yet
   *
   * @param pEvent event that happened
   */
  private void _handleEventUnauthorized(@NotNull WebsocketEvent<?> pEvent)
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
        session.getAsyncRemote().sendObject(SatelliteWebSocketEvents.CONFIG.payload(configFactory.create(lease.userID)));
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
  private boolean _handleEventAuthorized(@NotNull String pUserID, @NotNull WebsocketEvent<?> pEvent)
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
