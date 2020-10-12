package io.conceptive.netplan.core.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

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
   * Address (IP or Hostname) of the device
   */
  public String address;

  /**
   * Determines, where this device is located
   */
  public Location location;

  /**
   * Contains all currently known metrics data of the device
   */
  public Set<Metric> metrics;

  /**
   * Contains all "neighbour" edges
   */
  public Set<Edge> edges;

  /**
   * Updates this device with all values set in pOther
   *
   * @param pOther Other Device that contains all properties that should be modified
   * @return true, if something has changed
   */
  public boolean updateWith(@NotNull Device pOther)
  {
    boolean changed = false;

    if(pOther.address != null)
    {
      address = pOther.address;
      changed = true;
    }

    if(pOther.location != null)
    {
      location = pOther.location;
      changed = true;
    }

    if(pOther.edges != null)
    {
      if(edges == null)
        edges = new HashSet<>();
      changed = edges.addAll(pOther.edges);
    }

    return changed;
  }
  
  /**
   * Checks, if this device could be valid
   */
  public void checkValid()
  {
    if (id == null || id.isBlank())
      throw new RuntimeException("id empty");
  }

  /**
   * POJO for a single metric for a single device
   * Care, this will be used in REST directly
   */
  public static class Metric
  {
    /**
     * Time when this metric was recorded
     */
    public Date recordTime;

    /**
     * Type of this metric (PING, TRACERT, etc.)
     */
    public String type;

    /**
     * State of this metric
     */
    public EMetricState state;

    /**
     * Description, why the state is failed, warned, etc.
     */
    public String stateDescription;

    /**
     * Command which was executed to determine its state
     */
    public String executedCommand;

    /**
     * Plain result of the executed command
     */
    public String commandResult;
  }

  /**
   * State to determine, what to expect from a device
   */
  public enum EMetricState
  {
    /**
     * Device FAILED, so it can not be used anywhere
     */
    FAILURE,

    /**
     * Device can be used, but may not work correctly
     */
    WARNING,

    /**
     * Device is OK and ready to use
     */
    SUCCESS,

    /**
     * State could not be determined
     */
    UNKNOWN
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

  /**
   * Simple relation to another device
   */
  public static class Edge
  {
    /**
     * ID of the target device
     */
    public String deviceID;
  }

}
