package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.Enum;

/**
 * @author w.glanzer, 10.02.2021
 */
@Enum("SlotState")
public enum GQLNetworkSlotState
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
