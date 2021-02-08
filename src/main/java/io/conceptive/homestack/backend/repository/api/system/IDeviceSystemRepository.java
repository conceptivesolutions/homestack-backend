package io.conceptive.homestack.backend.repository.api.system;

import io.conceptive.homestack.model.data.DeviceDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 06.12.2020
 */
public interface IDeviceSystemRepository
{

  /**
   * Returns all devices in the given stack
   *
   * @param pUserID  ID of the user
   * @param pStackID ID of the stack
   * @return the devices in this stack
   */
  @NotNull
  Set<DeviceDataModel> findByStackID(@NotNull String pUserID, @NotNull String pStackID);

}
