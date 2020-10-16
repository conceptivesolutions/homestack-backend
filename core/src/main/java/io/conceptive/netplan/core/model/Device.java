package io.conceptive.netplan.core.model;

/**
 * POJO for a single device.
 * Care, this will be used in REST directly
 *
 * @author w.glanzer, 13.09.2020
 */
public class Device
{

  /**
   * Unique ID of the device
   */
  public String id;

  /**
   * ID of the host this device belongs to
   */
  public String hostID;

  /**
   * Address (IP or Hostname) of the device
   */
  public String address;

  /**
   * Determines, where this device is located
   */
  public Location location;
  
  /**
   * Checks, if this device could be valid
   */
  public void checkValid()
  {
    if (id == null || id.isBlank())
      throw new RuntimeException("id empty");
  }

  /**
   * Location of a device
   */
  public static class Location
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

}
