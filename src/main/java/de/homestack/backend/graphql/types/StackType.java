package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.StackDataModel
 */
@Type("Stack")
public class StackType
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
  public List<DeviceType> devices;

  /**
   * Satellites in this stack
   */
  public List<SatelliteType> satellites;

  @NotNull
  public StackType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public StackType displayName(@Nullable String pDisplayName)
  {
    displayName = pDisplayName;
    return this;
  }

  @NotNull
  public StackType devices(@Nullable List<DeviceType> pDevices)
  {
    devices = pDevices;
    return this;
  }

  @NotNull
  public StackType satellites(@Nullable List<SatelliteType> pSatellites)
  {
    satellites = pSatellites;
    return this;
  }
}
