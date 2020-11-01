package io.conceptive.netplan.metrics;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.conceptive.netplan.core.model.*;
import io.conceptive.netplan.metrics.api.*;
import io.conceptive.netplan.repository.*;
import io.quarkus.runtime.*;
import io.reactivex.Observable;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
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
public class MetricRecordService
{
  private static final Logger _LOGGER = Logger.getLogger(MetricRecordService.class.getName());

  @Inject
  protected IUserRepository userRepository;

  @Inject
  protected IDeviceRepository.ITokenlessRepository deviceRepository;

  @Inject
  protected IMetricRecordRepository.ITokenlessRepository metricRepository;

  @Inject
  protected Instance<IMetricExecutor> metricExecutors;

  private Disposable updaterDisposable;

  @SuppressWarnings("unused")
    // Reflection
  void onStart(@Observes StartupEvent pStartupEvent)
  {
    if (updaterDisposable != null)
      updaterDisposable.dispose();
    updaterDisposable = _startUpdater();
    Logger.getLogger(getClass()).info("Started Device Updater");
  }

  @SuppressWarnings("unused")
    // Reflection
  void onStop(@Observes ShutdownEvent pShutdownEvent)
  {
    if (updaterDisposable != null)
    {
      updaterDisposable.dispose();
      Logger.getLogger(getClass()).info("Disposed Device Updater");
    }
  }

  @NotNull
  private Disposable _startUpdater()
  {
    return Flowable.interval(5, TimeUnit.SECONDS)

        // Only execute if it is correctly initialized
        .filter(pL -> deviceRepository != null && metricRepository != null)

        // Backpressure
        .onBackpressureDrop()

        // Observe on different thread pool
        .observeOn(Schedulers.from(Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                                                                     .setNameFormat("tMetricUpdater-%d")
                                                                     .build())))

        // Map to all users
        .map(pTime -> {
          try
          {
            return userRepository.findAll();
          }
          catch (Throwable e)
          {
            _LOGGER.error("Failed to update device metric record", e);
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
          .flatMap(pDevice -> Observable.fromIterable(metricExecutors)
              .map(pExecutor -> Pair.of(pDevice, pExecutor)))

          // Execute all device/executor pairs in parallel
          .map(pExecInfo -> {
            Device device = pExecInfo.getLeft();
            IMetricExecutor executor = pExecInfo.getRight();

            try
            {
              IMetricRecord result = executor.execute(device);
              return Optional.of(_toMetricRecord(device, executor, result));
            }
            catch (Throwable ex)
            {
              _LOGGER.error("Failed to execute metric " + executor + " for device with id " + device.id, ex);
              return Optional.<MetricRecord>empty();
            }
          })

          // Only if available
          .filter(Optional::isPresent)
          .map(Optional::get)

          // Insert new metric
          .blockingForEach(pRecord -> metricRepository.addMetricRecord(pUserID, pRecord));
    }
    catch (Throwable ex)
    {
      _LOGGER.error("Failed to update devices for user " + pUserID, ex);
    }
  }

  /**
   * Converts a metricsResult to the serializable metric result object
   *
   * @param pDevice   Device which the metric belongs to
   * @param pExecutor the executor that issued the given record
   * @param pResult   result to convert
   * @return converted result
   */
  @NotNull
  private MetricRecord _toMetricRecord(@NotNull Device pDevice, @NotNull IMetricExecutor pExecutor, @NotNull IMetricRecord pResult)
  {
    MetricRecord metricRecord = new MetricRecord();
    metricRecord.deviceID = pDevice.id;
    metricRecord.recordTime = new Date();
    metricRecord.type = pExecutor.getType();
    metricRecord.state = MetricRecord.EState.valueOf(pResult.getState().name());
    metricRecord.result = pResult.getResult();
    return metricRecord;
  }
}
