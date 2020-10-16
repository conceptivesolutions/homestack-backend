package io.conceptive.netplan.metrics;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.conceptive.netplan.core.model.*;
import io.conceptive.netplan.metrics.api.*;
import io.conceptive.netplan.repository.*;
import io.quarkus.runtime.Startup;
import io.reactivex.Observable;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.*;

/**
 * Service that updates the devices and performs health checks
 *
 * @author w.glanzer, 20.09.2020
 */
@Startup
@ApplicationScoped
public class MetricsUpdaterService
{
  private static final Logger _LOGGER = Logger.getLogger(MetricsUpdaterService.class.getName());

  @Inject
  protected IUserRepository userRepository;

  @Inject
  protected IDeviceRepository.ITokenlessRepository deviceRepository;

  @Inject
  protected IMetricsRepository.ITokenlessRepository metricsRepository;

  @Inject
  protected Instance<IMetricsExecutor> metricsExecutors;

  @SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression"})
  private final Disposable updaterDisposable;

  public MetricsUpdaterService()
  {
    updaterDisposable = _startUpdater();
  }

  @NotNull
  private Disposable _startUpdater()
  {
    return Flowable.interval(5, TimeUnit.SECONDS)

        // Only execute if it is correctly initialized
        .filter(pL -> deviceRepository != null && metricsRepository != null)

        // Backpressure
        .onBackpressureDrop()

        // Observe on different thread pool
        .observeOn(Schedulers.from(Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                                                                     .setNameFormat("tMetricsUpdater-%d")
                                                                     .build())))

        // Map to all users
        .map(pTime -> {
          try
          {
            return userRepository.findAll();
          }
          catch (Throwable e)
          {
            _LOGGER.error("Failed to update device metric state", e);
            return Set.<String>of();
          }
        })

        // Subscribe and check
        .subscribe(pUser -> pUser.forEach(this::_updateUserBlocking));
  }

  /**
   * Updates all devices for the given user - blocks the current thread
   *
   * @param pUserID ID of the user to update
   */
  private void _updateUserBlocking(@NotNull String pUserID)
  {
    try
    {
      // Retrieve devices
      Observable.fromIterable(deviceRepository.findAll(pUserID))

          // Combine with executor
          .flatMap(pDevice -> Observable.fromIterable(metricsExecutors)
              .map(pExecutor -> Pair.of(pDevice, pExecutor)))

          // Execute all device/executor pairs in parallel
          .map(pExecInfo -> {
            Device device = pExecInfo.getLeft();
            IMetricsExecutor executor = pExecInfo.getRight();

            try
            {
              IMetricsResult result = executor.execute(device);
              return Optional.of(Pair.of(device, result));
            }
            catch (Throwable ex)
            {
              _LOGGER.error("Failed to execute metric " + executor + " for device with id " + device.id, ex);
              return Optional.<Pair<Device, IMetricsResult>>empty();
            }
          })

          // Only if available
          .filter(Optional::isPresent)
          .map(Optional::get)

          // Update
          .blockingForEach(pDeviceMetricsPair -> metricsRepository.updateMetric(pUserID, _toMetric(pDeviceMetricsPair.getLeft(), pDeviceMetricsPair.getRight())));
    }
    catch (Throwable ex)
    {
      _LOGGER.error("Failed to update devices for user " + pUserID, ex);
    }
  }

  /**
   * Converts a metricsResult to the serializable metric object
   *
   * @param pDevice Device which the metric belongs to
   * @param pResult result to convert
   * @return converted result
   */
  @NotNull
  private Metric _toMetric(@NotNull Device pDevice, @NotNull IMetricsResult pResult)
  {
    Metric metric = new Metric();
    metric.deviceID = pDevice.id;
    metric.recordTime = new Date();
    metric.type = "UNKOWN";
    metric.state = Metric.EMetricState.valueOf(pResult.getState().name());
    metric.stateDescription = pResult.getStateDescription();
    metric.executedCommand = pResult.getExecutedCommand();
    metric.commandResult = pResult.getCommandResult();
    return metric;
  }
}
