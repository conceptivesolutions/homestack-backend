package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.device.DeviceDataModel
 */
@Type("Device")
public class DeviceType
{

  /**
   * Unique ID of the device
   */
  @Id
  @NonNull
  public String id;

  /**
   * Name of the icon for this device (mainly present in gui)
   */
  public String icon;

  /**
   * Address (IP or Hostname) of the device
   */
  public String address;

  /**
   * Determines, where this device is located
   */
  public LocationType location;

  /**
   * Contains all available slots with "row" and "column" coordinates
   */
  public List<List<NetworkSlotType>> slots;

  /**
   * All available metrics
   */
  public List<MetricType> metrics;

  @NotNull
  public DeviceType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public DeviceType icon(@Nullable String pIcon)
  {
    icon = pIcon;
    return this;
  }

  @NotNull
  public DeviceType address(@Nullable String pAddress)
  {
    address = pAddress;
    return this;
  }

  @NotNull
  public DeviceType location(@Nullable LocationType pLocation)
  {
    location = pLocation;
    return this;
  }

  @NotNull
  public DeviceType slots(@Nullable List<List<NetworkSlotType>> pSlots)
  {
    slots = pSlots;
    return this;
  }

  @NotNull
  public DeviceType metrics(@Nullable List<MetricType> pMetrics)
  {
    metrics = pMetrics;
    return this;
  }

}
