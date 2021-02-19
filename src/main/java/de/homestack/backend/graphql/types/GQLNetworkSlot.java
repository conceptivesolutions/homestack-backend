package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("NetworkSlot")
@NoArgsConstructor
@AllArgsConstructor
public class GQLNetworkSlot
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
  public GQLNetworkSlotState state;

  /**
   * The slot ID, if this slot is connected to another
   */
  public String targetSlotID;

}
