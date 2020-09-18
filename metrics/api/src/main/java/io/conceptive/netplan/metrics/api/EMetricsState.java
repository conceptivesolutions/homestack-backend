package io.conceptive.netplan.metrics.api;

/**
 * The state for a single metric analyzer
 *
 * @author w.glanzer, 18.09.2020
 */
public enum EMetricsState
{
  /**
   * Specifies, that the analyzer returned, that it a device not available
   */
  FAILURE,

  /**
   * Specifies, that a device is available, but does not work as expected
   */
  WARNING,

  /**
   * Specifies, that a device is available and is working correctly
   */
  SUCCESS,

  /**
   * The analyzer does not know, if the device is available
   */
  UNKNOWN
}