package io.conceptive.homestack.backend.satellite.session;

import io.conceptive.homestack.backend.satellite.auth.ISatelliteAuthenticator;
import io.conceptive.homestack.model.data.*;
import io.conceptive.homestack.model.satellite.SatelliteConfigurationDataModel;
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
  private static final String _SESSIONKEY_SATELLITE_ID = "id";
  private static final String _SESSIONKEY_SATELLITE_VERSION = "version";
  private final ISatelliteAuthenticator authenticator;
  private final Session session;

  public SatelliteMessageHandler(@NotNull ISatelliteAuthenticator pAuthenticator, @NotNull Session pSession)
  {
    authenticator = pAuthenticator;
    session = pSession;
  }

  @Override
  public void onMessage(@Nullable WebsocketEvent pEvent)
  {
    if (pEvent == null)
      return;

    // Determine, if authorized
    if (Boolean.TRUE.equals(session.getUserProperties().get(_SESSIONKEY_AUTHORIZED)))
      _handleEventAuthorized(pEvent);
    else
      _handleEventUnauthorized(pEvent);
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
      String satelliteID = null;
      String version = null;

      try
      {
        AuthenticateEventData data = SatelliteWebSocketEvents.AUTHENTICATE.payloadOf(pEvent);
        satelliteID = data.id;
        version = data.version;

        // just ignore invalid requests
        if (satelliteID == null || satelliteID.isBlank() || data.token == null || data.token.isBlank())
          return;

        // Authenticate (throws Exception, if invalid)
        authenticator.authenticate(satelliteID, data.token);

        // set information to session because we know, that we are now authenticated - because of JWT decoded sucessfully
        session.getUserProperties().put(_SESSIONKEY_AUTHORIZED, true);
        session.getUserProperties().put(_SESSIONKEY_SATELLITE_ID, satelliteID);
        session.getUserProperties().put(_SESSIONKEY_SATELLITE_VERSION, version);

        // answer with (initial) config
        session.getAsyncRemote().sendObject(SatelliteWebSocketEvents.CONFIG.payload(_createConfig()));
      }
      catch (Exception e)
      {
        _killSession();
        Logger.getLogger(SatelliteMessageHandler.class).warn("Failed to authenticate satellite (" + satelliteID + ", " + version + ")");
      }
    }
    else
      _killSession();
  }

  /**
   * Gets called, if an event happened and the session is already authenticated
   *
   * @param pEvent event that happened
   */
  private void _handleEventAuthorized(@NotNull WebsocketEvent<?> pEvent)
  {
    Logger.getLogger(SatelliteMessageHandler.class).warn(pEvent + " was not handled, because no handler was registered for it");
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

  /**
   * @return
   */
  @NotNull
  private SatelliteConfigurationDataModel _createConfig()
  {
    // todo
    SatelliteConfigurationDataModel model = new SatelliteConfigurationDataModel();
    DeviceDataModel devModel = new DeviceDataModel();
    devModel.id = "newDev";
    devModel.address = "127.0.0.1";
    model.devices = Set.of(devModel);
    MetricDataModel metric = new MetricDataModel();
    metric.type = "ping";
    metric.deviceID = "newDev";
    metric.enabled = true;
    model.metrics = Set.of(metric);
    return model;
  }

}
