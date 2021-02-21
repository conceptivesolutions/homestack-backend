package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Device")
@NoArgsConstructor
@AllArgsConstructor
public class GQLDevice
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
  public GQLLocation location;

  /**
   * Contains all available slots with "row" and "column" coordinates
   */
  public List<List<GQLNetworkSlot>> slots;

  /**
   * All available metrics
   */
  public List<GQLMetric> metrics;

}
