package io.conceptive.netplan.metrics.api;

import org.jetbrains.annotations.*;

import java.util.Map;

/**
 * Specifies the result for a metrics analyzer
 *
 * @author w.glanzer, 18.09.2020
 */
public interface IMetricRecord
{

  enum EState
  {
    FAILURE,
    WARNING,
    SUCCESS,
    UNKNOWN
  }

  /**
   * @return the current state of this metrics
   */
  @NotNull
  EState getState();

  /**
   * @return additional description to the current state
   */
  @Nullable
  Map<String, String> getResult();

}
