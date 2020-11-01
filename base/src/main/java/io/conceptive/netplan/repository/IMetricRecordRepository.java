package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.MetricRecord;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
public interface IMetricRecordRepository
{

  /**
   * Searches all metric records for a single device
   *
   * @param pDeviceID ID of the device
   * @return all metrics for the given device
   */
  @NotNull
  Set<MetricRecord> findAll(@NotNull String pDeviceID);

  /**
   * Contains all methods for tokenless access to the metrics repository
   */
  interface ITokenlessRepository
  {
    /**
     * Adds a new metric record to the database
     *
     * @param pUserID       ID of the user, that owns the device of the metric record
     * @param pMetricRecord Record to insert
     */
    void addMetricRecord(@NotNull String pUserID, @NotNull MetricRecord pMetricRecord);
  }

}
