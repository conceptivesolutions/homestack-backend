package io.conceptive.netplan.backend.endpoints.satellite;

import io.conceptive.netplan.backend.satellite.session.ISatelliteSessionManager;
import io.conceptive.netplan.model.websocket.WebsocketEventCoder;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Endpoint for the websocket connection for satellites
 *
 * @author w.glanzer, 10.11.2020
 */
@ApplicationScoped
@ServerEndpoint(value = "/satellites", decoders = WebsocketEventCoder.class, encoders = WebsocketEventCoder.class)
public class SatelliteWebSocketEndpoint
{

  @Inject
  ISatelliteSessionManager sessionManager;

  /**
   * Gets called, if this websocket endpoint connection was opened by a single client
   *
   * @param pSession Session that opened
   */
  @OnOpen
  public void onOpen(@NotNull Session pSession)
  {
    sessionManager.registerSession(pSession);
  }

  /**
   * Gets called, if this websocket endpoint connection was closed by a single client
   *
   * @param pSession Session that opened
   */
  @OnClose
  public void onClose(@NotNull Session pSession)
  {
    sessionManager.unregisterSession(pSession);
  }

  /**
   * Gets called, if this websocket endpoint connection was closed by exception by a single client
   *
   * @param pSession Session that opened
   * @param pEx      Exception
   */
  @OnError
  public void onError(@NotNull Session pSession, @Nullable Throwable pEx)
  {
    sessionManager.unregisterSession(pSession);
  }

}
