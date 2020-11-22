package io.conceptive.homestack.repository.api;

import io.conceptive.homestack.model.data.DeviceDataModel;
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
  Set<DeviceDataModel> findAll();

  /**
   * Returns all currently known devices for a given host
   *
   * @param pHostID ID of the host
   * @return all devices
   */
  @NotNull
  Set<DeviceDataModel> findByHost(@NotNull String pHostID);

  /**
   * Tries to find the device by the given id
   *
   * @param pID ID of the device to search for
   * @return Device or NULL if not found
   */
  @Nullable
  DeviceDataModel findDeviceById(@NotNull String pID);

  /**
   * Inserts a single device into the repository
   *
   * @param pDevice Device to insert
   */
  void insertDevice(@NotNull DeviceDataModel pDevice);

  /**
   * Update a single device from the repository
   *
   * @param pDevice Device to update
   */
  void updateDevice(@NotNull DeviceDataModel pDevice);

  /**
   * Deletes the device (if any) specified by given id
   *
   * @param pID ID of the device to be deleted
   * @return true, if device was deleted
   */
  boolean deleteDeviceByID(@NotNull String pID);

  /**
   * Contains all methods for tokenless access to the device repository
   */
  interface ITokenlessRepository
  {
    /**
     * Returns all currently known devices in the database of a single user
     *
     * @param pUserID ID of the user
     * @return all devices
     */
    @NotNull
    Set<DeviceDataModel> findAll(@NotNull String pUserID);
  }

}
