package io.conceptive.homestack.repository.api.user;

import io.conceptive.homestack.model.data.MetricRecordDataModel;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
public interface IMetricRecordUserRepository
{

  /**
   * Searches all metric records for a single device
   *
   * @param pDeviceID ID of the device
   * @return all metrics for the given device
   */
  @NotNull
  Set<MetricRecordDataModel> findAll(@NotNull String pDeviceID);

  /**
   * Searches the record for a single device with the given metric type
   *
   * @param pDeviceID ID of the device
   * @param pType     Type of the record to search for
   * @return the record
   */
  @Nullable
  MetricRecordDataModel findByType(@NotNull String pDeviceID, @NotNull String pType);

}
