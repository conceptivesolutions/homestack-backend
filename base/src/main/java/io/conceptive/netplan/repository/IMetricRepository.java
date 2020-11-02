package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Metric;
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
   * Returns all metrics for a single device
   *
   * @param pDeviceID ID of the device
   * @return the metrics
   */
  @NotNull
  Set<Metric> findAll(@NotNull String pDeviceID);

  /**
   * Inserts a new or modified metric
   *
   * @param pMetric Metric to insert
   */
  void insertMetric(@NotNull Metric pMetric);

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
    Set<Metric> findAll(@NotNull String pUserID, @NotNull String pDeviceID);
  }
}
