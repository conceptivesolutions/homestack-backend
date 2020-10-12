package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Metric;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
public interface IMetricsRepository
{

  /**
   * Searches all metrics for a single device
   *
   * @param pDeviceID ID of the device
   * @return all metrics for the given device
   */
  @NotNull
  Set<Metric> findAll(@NotNull String pDeviceID);

  /**
   * Updates the metric of a single device
   *
   * @param pMetric Metric to add / update
   */
  void updateMetric(@NotNull Metric pMetric);

}
