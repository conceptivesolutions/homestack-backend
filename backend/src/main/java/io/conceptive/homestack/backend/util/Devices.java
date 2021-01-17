package io.conceptive.homestack.backend.util;

import io.conceptive.homestack.model.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author w.glanzer, 17.01.2021
 */
public class Devices
{

  /**
   * Returns the slot with the given slot id
   *
   * @param pDevice Device of the slot to search
   * @param pSlotID ID of the slot
   * @return the slot or exception, if not present
   */
  @NotNull
  public static NetworkSlotDataModel getSlotByID(@NotNull DeviceDataModel pDevice, @NotNull String pSlotID)
  {
    return pDevice.slots.stream()
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .filter(pSlot -> Objects.equals(pSlot.id, pSlotID))
        .findFirst()
        .orElseThrow();
  }

  /**
   * Returns the slot with the given slot id
   *
   * @param pDevice Device of the slot to search
   * @param pSlotID ID of the slot
   * @return the slot or null, if it could not be found
   */
  @Nullable
  public static NetworkSlotDataModel findSlotByID(@NotNull DeviceDataModel pDevice, @NotNull String pSlotID)
  {
    try
    {
      return getSlotByID(pDevice, pSlotID);
    }
    catch (Exception e)
    {
      return null;
    }
  }

}
