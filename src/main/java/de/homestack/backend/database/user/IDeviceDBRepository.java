package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.device.DeviceDataModel;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 17.02.2021
 */
public interface IDeviceDBRepository
{

  /**
   * Retrieves all devices
   *
   * @param pUserID user to query
   * @return the devices
   */
  @NotNull
  List<DeviceDataModel> getDevices(@NotNull String pUserID);

  /**
   * Retrieves all devices of a single stack
   *
   * @param pUserID  user to query
   * @param pStackID stack to query
   * @return the devices
   */
  @NotNull
  List<DeviceDataModel> getDevicesByStackID(@NotNull String pUserID, @NotNull String pStackID);

  /**
   * Returns a single device by id
   *
   * @param pUserID   user to query
   * @param pStackID  stack to query
   * @param pDeviceID id of the device to search
   * @return the model, or null if not found
   */
  @Nullable
  DeviceDataModel getDeviceByID(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID);

  /**
   * Inserts / Updates the given device
   *
   * @param pUserID user to query
   * @param pModel  model to insert / update
   * @return the updated model
   */
  @NotNull
  DeviceDataModel upsertDevice(@NotNull String pUserID, @NotNull DeviceDataModel pModel);

  /**
   * Deletes the given device
   *
   * @param pUserID   user to query
   * @param pStackID  stack to query
   * @param pDeviceID id to delete
   * @return true, if something was deleted
   */
  boolean deleteDevice(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID);

}
