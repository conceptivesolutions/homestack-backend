package io.conceptive.netplan.metrics.api;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Simple Metric Record implementation
 *
 * @author w.glanzer, 02.11.2020
 */
public class SimpleMetricRecord implements IMetricRecord
{
  private final EState state;
  private final Map<String, String> result = new HashMap<>();

  public SimpleMetricRecord(@NotNull EState pState)
  {
    state = pState;
  }

  @NotNull
  @Override
  public EState getState()
  {
    return state;
  }

  @Nullable
  @Override
  public Map<String, String> getResult()
  {
    if (!result.isEmpty())
      return result;
    return null;
  }

  /**
   * Adds a new result key to this record
   */
  @NotNull
  public io.conceptive.netplan.metrics.api.SimpleMetricRecord withResult(@NotNull String pKey, @NotNull String pValue)
  {
    result.put(pKey, pValue);
    return this;
  }
}
