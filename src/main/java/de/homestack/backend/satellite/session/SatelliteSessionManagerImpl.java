package de.homestack.backend.satellite.session;

import de.homestack.backend.database.change.IRepositoryChangeObserver;
import de.homestack.backend.database.user.*;
import de.homestack.backend.satellite.auth.ISatelliteAuthenticator;
import de.homestack.backend.satellite.config.ISatelliteConfigFactory;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.*;
import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

  @Inject
  protected Provider<SatelliteMessageHandler> messageHandlerFactory;

  private final AtomicLong sessionCounter = new AtomicLong(0L);

  @Override
  public void registerSession(@NotNull Session pSession)
  {
    // create and attach message handler
    messageHandlerFactory.get().attach(pSession);

    // increment session counter
    sessionCounter.incrementAndGet();
  }

  @Override
  public void unregisterSession(@NotNull Session pSession)
  {
    List<SatelliteMessageHandler> handlersToDispose = new ArrayList<>(pSession.getMessageHandlers()).stream()
        .filter(SatelliteMessageHandler.class::isInstance)
        .map(SatelliteMessageHandler.class::cast)
        .collect(Collectors.toList());

    // decrement counter, if handler was attached
    if (!handlersToDispose.isEmpty())
      sessionCounter.decrementAndGet();

    for (SatelliteMessageHandler handler : handlersToDispose)
      handler.dispose();
  }

  /**
   * @return the current satellite session count
   */
  @Gauge(name = "satellite_sessions", description = "contains the information about the currently logged in satellite session count", absolute = true, unit = "Sessions")
  protected long getSatelliteSessions()
  {
    return sessionCounter.get();
  }

}
