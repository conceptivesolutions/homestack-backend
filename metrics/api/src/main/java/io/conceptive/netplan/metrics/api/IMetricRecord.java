package io.conceptive.netplan.metrics.api;

import org.jetbrains.annotations.*;

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
   * @return description, why the state is offline / unavailable / etc.
   */
  @Nullable
  String getStateDescription();

  /**
   * @return the command that was executed by this metrics analyzer
   */
  @Nullable
  default String getExecutedCommand()
  {
    return null;
  }

  /**
   * @return the command result, if a command was executed
   */
  @Nullable
  default String getCommandResult()
  {
    return null;
  }

}
