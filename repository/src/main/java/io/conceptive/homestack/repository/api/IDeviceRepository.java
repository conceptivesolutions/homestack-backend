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
   * Returns all currently known devices for a given stack
   *
   * @param pStackID ID of the stack
   * @return all devices
   */
  @NotNull
  Set<DeviceDataModel> findByStackID(@NotNull String pStackID);

  /**
   * Tries to find the device by the given id
   *
   * @param pID ID of the device to search for
   * @return Device or NULL if not found
   */
  @Nullable
  DeviceDataModel findByID(@NotNull String pID);

  /**
   * Inserts a single device into the repository
   *
   * @param pDevice Device to insert
   */
  void insert(@NotNull DeviceDataModel pDevice);

  /**
   * Update a single device from the repository
   *
   * @param pDevice Device to update
   */
  void update(@NotNull DeviceDataModel pDevice);

  /**
   * Deletes the device (if any) specified by given id
   *
   * @param pID ID of the device to be deleted
   * @return true, if device was deleted
   */
  boolean deleteByID(@NotNull String pID);

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
