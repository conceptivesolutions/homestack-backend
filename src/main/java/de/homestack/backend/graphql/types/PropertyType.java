package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Property")
public class PropertyType
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

  @NotNull
  public PropertyType key(@NotNull String pKey)
  {
    key = pKey;
    return this;
  }

  @NotNull
  public PropertyType value(@Nullable String pValue)
  {
    value = pValue;
    return this;
  }
}
