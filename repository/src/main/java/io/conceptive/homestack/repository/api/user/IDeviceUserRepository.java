package io.conceptive.homestack.repository.api.user;

import io.conceptive.homestack.model.data.DeviceDataModel;
import io.conceptive.homestack.repository.api.IObjectRepository;
import org.jetbrains.annotations.NotNull;

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

}
