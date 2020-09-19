package io.conceptive.netplan.core.model;

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
   * Contains all currently known metrics data of the device
   */
  public Set<Metric> metrics;

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

}
