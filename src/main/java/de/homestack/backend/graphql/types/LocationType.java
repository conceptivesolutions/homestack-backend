package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.ScreenLocationDataModel
 */
@Type("Location")
public class LocationType
{

  /**
   * X Position on screen
   */
  public float x;

  /**
   * Y Position on screen
   */
  public float y;

  @NotNull
  public LocationType x(float pX)
  {
    x = pX;
    return this;
  }

  @NotNull
  public LocationType y(float pY)
  {
    y = pY;
    return this;
  }

}
