package io.conceptive.homestack.repository.api.system;

import io.conceptive.homestack.model.data.MetricDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 06.12.2020
 */
public interface IMetricSystemRepository
{

  /**
   * Returns all metrics for a single user for the given Devices
   *
   * @param pUserID    ID of the user
   * @param pDeviceIDs ID of the devices
   * @return the metrics for the given devices
   */
  @NotNull
  Set<MetricDataModel> findByDeviceID(@NotNull String pUserID, @NotNull String[] pDeviceIDs);

}
