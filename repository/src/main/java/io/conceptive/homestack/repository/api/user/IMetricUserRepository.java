package io.conceptive.homestack.repository.api.user;

import io.conceptive.homestack.model.data.MetricDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Repository that contains metrics
 *
 * @author w.glanzer, 02.11.2020
 */
public interface IMetricUserRepository
{

  /**
   * Returns all metrics
   *
   * @return the metrics
   */
  @NotNull
  Set<MetricDataModel> findAll();

  /**
   * Returns all metrics for a single device
   *
   * @param pDeviceID ID of the device
   * @return the metrics
   */
  @NotNull
  Set<MetricDataModel> findAllByDeviceID(@NotNull String pDeviceID);

  /**
   * Inserts a new or modified metric
   *
   * @param pMetric Metric to insert
   */
  void upsert(@NotNull MetricDataModel pMetric);

  /**
   * Deletes a single metric
   *
   * @param pDeviceID ID of the device
   * @param pType     Type of the metric to be deleted
   * @return true, if metric was deleted
   */
  boolean delete(@NotNull String pDeviceID, @NotNull String pType);

}
