package io.conceptive.homestack.satellite.session;

import io.conceptive.homestack.model.websocket.WebsocketEvent;
import io.conceptive.homestack.satellite.auth.ISatelliteAuthenticator;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

/**
 * @author w.glanzer, 13.11.2020
 */
@ApplicationScoped
class SatelliteSessionManagerImpl implements ISatelliteSessionManager
{

  @Inject
  protected ISatelliteAuthenticator authenticator;

  @Override
  public void registerSession(@NotNull Session pSession)
  {
    pSession.addMessageHandler(WebsocketEvent.class, new SatelliteMessageHandler(authenticator, pSession));
  }

  @Override
  public void unregisterSession(@NotNull Session pSession)
  {
    pSession.getMessageHandlers().stream()
        .filter(SatelliteMessageHandler.class::isInstance)
        .forEach(pSession::removeMessageHandler);
  }

}
