package io.conceptive.homestack.backend.repository.api.user;

import io.conceptive.homestack.backend.repository.api.IObjectRepository;
import io.conceptive.homestack.model.data.DeviceDataModel;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 13.09.2020
 */
public interface IDeviceUserRepository extends IObjectRepository<DeviceDataModel>
{

  /**
   * Returns all currently known devices for a given stack
   *
   * @param pStackID ID of the stack
   * @return all devices
   */
  @NotNull
  Set<DeviceDataModel> findByStackID(@NotNull String pStackID);

  /**
   * Returns the device that owns the slot with the given id
   *
   * @param pSlotID ID of the slot
   * @return the device or null, if it could not be found
   */
  @Nullable
  DeviceDataModel findBySlotID(@NotNull String pSlotID);

}
