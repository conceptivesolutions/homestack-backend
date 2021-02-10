package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.Enum;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.device.ENetworkSlotState
 */
@Enum("SlotState")
public enum ENetworkSlotStateType
{
  /**
   * All ok, slot is currently active and working correctly
   */
  ONLINE,

  /**
   * Slot is connected, but not ready to send anything
   */
  OFFLINE,

  /**
   * Slot is disabled
   */
  DISABLED
}
