package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Device;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 13.09.2020
 */
public interface IDeviceRepository
{

  /**
   * Returns all currently known devices
   *
   * @return all devices
   */
  @NotNull
  Set<Device> findAll();

  /**
   * Tries to find the device by the given id
   *
   * @param pID ID of the device to search for
   * @return Device or NULL if not found
   */
  @Nullable
  Device findDeviceById(@NotNull String pID);

  /**
   * Inserts a single device into the repository
   *
   * @param pDevice Device to insert
   */
  void insertDevice(@NotNull Device pDevice);

  /**
   * Update a single device from the repository
   *
   * @param pDevice Device to update
   */
  void updateDevice(@NotNull Device pDevice);

  /**
   * Updates the metric of a single device
   *
   * @param pDevice Device to update
   * @param pMetric Metric to add / update
   */
  void updateMetric(@NotNull Device pDevice, @NotNull Device.Metric pMetric);

  /**
   * Deletes the device (if any) specified by given id
   *
   * @param pID ID of the device to be deleted
   * @return true, if device was deleted
   */
  boolean deleteDeviceByID(@NotNull String pID);

}