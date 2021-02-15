package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Stack")
@NoArgsConstructor
@AllArgsConstructor
public class GQLStack
{

  /**
   * ID of the stack
   */
  @Id
  @NonNull
  public String id;

  /**
   * Displayable String
   */
  public String displayName;

  /**
   * Devices in this stack
   */
  public List<GQLDevice> devices;

  /**
   * Satellites in this stack
   */
  public List<GQLSatellite> satellites;

}
