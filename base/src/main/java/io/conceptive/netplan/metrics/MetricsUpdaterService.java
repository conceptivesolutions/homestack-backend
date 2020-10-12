package io.conceptive.netplan.metrics;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.conceptive.netplan.core.model.Device;
import io.conceptive.netplan.metrics.api.*;
import io.conceptive.netplan.repository.IDeviceRepository;
import io.quarkus.runtime.Startup;
import io.reactivex.*;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
  protected IDeviceRepository deviceRepository;

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
        .filter(pL -> deviceRepository != null)

        // Backpressure
        .onBackpressureDrop()

        // Observe on different thread pool
        .observeOn(Schedulers.from(Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                                                                     .setNameFormat("tMetricsUpdater-%d")
                                                                     .build())))

        // Map to all devices
        .map(pTime -> {
          try
          {
            return deviceRepository.findAll();
          }
          catch(Throwable e)
          {
            _LOGGER.error("Failed to update device metric state", e);
            return Set.<Device>of();
          }
        })

        // Subscribe and check
        .subscribe(pDevices -> pDevices.forEach(this::_updateDeviceBlocking));
  }

  /**
   * Updates a given device - blocks the current thread
   *
   * @param pDevice device to update
   */
  private void _updateDeviceBlocking(@NotNull Device pDevice)
  {
    try
    {
      // Analyze Metrics
      Observable.fromIterable(metricsExecutors)

          // Execute all executors in parallel
          .map(pExecutor -> {
            try
            {
              return Optional.of(pExecutor.execute(pDevice));
            }
            catch(Throwable ex)
            {
              _LOGGER.error("Failed to execute metric " + pExecutor + " for device with id " + pDevice.id, ex);
              return Optional.<IMetricsResult>empty();
            }
          })

          // Only if available
          .filter(Optional::isPresent)
          .map(Optional::get)

          // Update
          .blockingForEach(pMetric -> deviceRepository.updateMetric(pDevice, _toMetric(pMetric)));
    }
    catch(Throwable ex)
    {
      _LOGGER.error("Failed to update device with ID: " + pDevice.id, ex);
    }
  }

  /**
   * Converts a metricsResult to the serializable metric object
   *
   * @param pResult result to convert
   * @return converted result
   */
  @NotNull
  private Device.Metric _toMetric(@NotNull IMetricsResult pResult)
  {
    Device.Metric metric = new Device.Metric();
    metric.recordTime = new Date();
    metric.type = "UNKOWN";
    metric.state = Device.EMetricState.valueOf(pResult.getState().name());
    metric.stateDescription = pResult.getStateDescription();
    metric.executedCommand = pResult.getExecutedCommand();
    metric.commandResult = pResult.getCommandResult();
    return metric;
  }
}
