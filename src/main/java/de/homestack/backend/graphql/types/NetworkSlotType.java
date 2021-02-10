package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.device.NetworkSlotDataModel
 */
@Type("NetworkSlot")
public class NetworkSlotType
{

  /**
   * Global unique id of the slot
   */
  @Id
  @NonNull
  public String id;

  /**
   * Current state
   */
  public ENetworkSlotStateType state;

  /**
   * The slot, if this slot is connected to another
   */
  public NetworkSlotType targetSlot;

  @NotNull
  public NetworkSlotType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public NetworkSlotType state(@Nullable ENetworkSlotStateType pState)
  {
    state = pState;
    return this;
  }

  @NotNull
  public NetworkSlotType targetSlot(@Nullable NetworkSlotType pTargetSlot)
  {
    targetSlot = pTargetSlot;
    return this;
  }

}
