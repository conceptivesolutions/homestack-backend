package io.conceptive.netplan.core.model;

import java.util.*;

/**
 * POJO for a single metric record for a single device
 * Care, this will be used in REST directly
 *
 * @author w.glanzer, 12.10.2020
 */
public class MetricRecord
{
  /**
   * ID of the device this metric belongs to
   */
  public String deviceID;

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
  public EState state;

  /**
   * Additional properties to the current state
   */
  public Map<String, String> result;

  /**
   * State to determine, what to expect from a device
   */
  @SuppressWarnings("unused") // valueOf()
  public enum EState
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
