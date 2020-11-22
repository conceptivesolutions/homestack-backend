package io.conceptive.homestack.backend.satellite.session;

import io.conceptive.homestack.model.data.DeviceDataModel;
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
  private final Session session;

  public SatelliteMessageHandler(@NotNull Session pSession)
  {
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
      AuthenticateEventData data = SatelliteWebSocketEvents.AUTHENTICATE.payloadOf(pEvent);
      session.getUserProperties().put(_SESSIONKEY_AUTHORIZED, true);
      session.getUserProperties().put(_SESSIONKEY_SATELLITE_ID, data.id);
      session.getUserProperties().put(_SESSIONKEY_SATELLITE_VERSION, data.version);

      // answer with (initial) config
      session.getAsyncRemote().sendObject(SatelliteWebSocketEvents.CONFIG.payload(_getConfig()));
    }
    else
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

  /**
   * Gets called, if an event happened and the session is already authenticated
   *
   * @param pEvent event that happened
   */
  private void _handleEventAuthorized(@NotNull WebsocketEvent<?> pEvent)
  {
    Logger.getLogger(SatelliteMessageHandler.class).warn(pEvent + " was not handled, because no handler was registered for it");
  }

  //todo
  private SatelliteConfigurationDataModel _getConfig()
  {
    // todo
    SatelliteConfigurationDataModel model = new SatelliteConfigurationDataModel();
    DeviceDataModel devModel = new DeviceDataModel();
    devModel.id = "newDev";
    model.devices = Set.of(devModel);
    return model;
  }

}
