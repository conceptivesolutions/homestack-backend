package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Device;
import org.jetbrains.annotations.*;

/**
 * @author w.glanzer, 13.09.2020
 */
public interface IDeviceRepository
{

  /**
   * Tries to find the device by the given id
   *
   * @param pID ID of the device to search for
   * @return Device or NULL if not found
   */
  @Nullable
  Device findDeviceById(@NotNull String pID);

  /**
   * Updates or inserts (if not existing) a single device into the repository
   *
   * @param pDevice Device to update or insert
   */
  void updateOrInsertDevice(@NotNull Device pDevice);

  /**
   * Deletes the device (if any) specified by given id
   *
   * @param pID ID of the device to be deleted
   * @return true, if device was deleted
   */
  boolean deleteDeviceByID(@NotNull String pID);

}
