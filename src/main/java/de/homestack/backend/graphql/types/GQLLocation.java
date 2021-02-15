package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.Type;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Location")
@NoArgsConstructor
@AllArgsConstructor
public class GQLLocation
{

  /**
   * X Position on screen
   */
  public float x;

  /**
   * Y Position on screen
   */
  public float y;

}
