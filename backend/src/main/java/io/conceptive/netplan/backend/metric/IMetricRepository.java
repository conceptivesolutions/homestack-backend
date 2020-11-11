package io.conceptive.netplan.backend.metric;

import io.conceptive.netplan.model.data.MetricDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Repository that contains metrics
 *
 * @author w.glanzer, 02.11.2020
 */
public interface IMetricRepository
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
  Set<MetricDataModel> findAll(@NotNull String pDeviceID);

  /**
   * Inserts a new or modified metric
   *
   * @param pMetric Metric to insert
   */
  void insertMetric(@NotNull MetricDataModel pMetric);

  /**
   * Deletes a single metric
   *
   * @param pDeviceID ID of the device
   * @param pType     Type of the metric to be deleted
   * @return true, if metric was deleted
   */
  boolean deleteMetric(@NotNull String pDeviceID, @NotNull String pType);

  /**
   * Contains all methods for tokenless access to the metrics repository
   */
  interface ITokenlessRepository
  {
    /**
     * Returns all metrics for a single device
     *
     * @param pUserID   ID of the user
     * @param pDeviceID ID of the device
     * @return the metrics
     */
    @NotNull
    Set<MetricDataModel> findAll(@NotNull String pUserID, @NotNull String pDeviceID);
  }
}
