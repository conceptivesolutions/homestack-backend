package io.conceptive.netplan.core.model;

import java.util.Date;

/**
 * POJO for a single metric for a single device
 * Care, this will be used in REST directly
 *
 * @author w.glanzer, 12.10.2020
 */
public class Metric
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
