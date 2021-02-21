package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Property")
@NoArgsConstructor
@AllArgsConstructor
public class GQLProperty
{

  /**
   * Property Key
   */
  @NonNull
  public String key;

  /**
   * Property Value
   */
  public String value;

}
