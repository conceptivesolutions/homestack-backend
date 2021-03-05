package de.homestack.backend.satellite.session;

import de.homestack.backend.database.change.IRepositoryChangeObserver;
import de.homestack.backend.database.user.*;
import de.homestack.backend.satellite.auth.ISatelliteAuthenticator;
import de.homestack.backend.satellite.config.ISatelliteConfigFactory;
import io.conceptive.homestack.model.websocket.WebsocketEvent;
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

  @Inject
  protected ISatelliteConfigFactory configFactory;

  @Inject
  protected IDeviceDBRepository deviceRepository;

  @Inject
  protected IMetricRecordDBRepository metricRecordRepository;

  @Inject
  protected IRepositoryChangeObserver repositoryChangeObserver;

  @Override
  public void registerSession(@NotNull Session pSession)
  {
    pSession.addMessageHandler(WebsocketEvent.class, new SatelliteMessageHandler(authenticator, configFactory, metricRecordRepository, repositoryChangeObserver, pSession));
  }

  @Override
  public void unregisterSession(@NotNull Session pSession)
  {
    pSession.getMessageHandlers().stream()
        .filter(SatelliteMessageHandler.class::isInstance)
        .map(SatelliteMessageHandler.class::cast)
        .peek(SatelliteMessageHandler::dispose)
        .forEach(pSession::removeMessageHandler);
  }

}
