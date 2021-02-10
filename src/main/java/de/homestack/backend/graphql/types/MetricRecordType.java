package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.metric.MetricRecordDataModel
 */
@Type("Record")
public class MetricRecordType
{

  /**
   * Unique ID of this metric
   */
  @Id
  @NonNull
  public String id;

  /**
   * Time when this metric was recorded
   */
  public Date recordTime;

  /**
   * State of this metric
   */
  public EMetricRecordStateType state;

  /**
   * Additional properties to the current state
   */
  public List<PropertyType> result;

  @NotNull
  public MetricRecordType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public MetricRecordType recordTime(@Nullable Date pRecordTime)
  {
    recordTime = pRecordTime;
    return this;
  }

  @NotNull
  public MetricRecordType state(@Nullable EMetricRecordStateType pState)
  {
    state = pState;
    return this;
  }

  @NotNull
  public MetricRecordType result(@Nullable Map<String, String> pResult)
  {
    result = pResult == null ? null : pResult.entrySet().stream()
        .map(pEntry -> new PropertyType()
            .key(pEntry.getKey())
            .value(pEntry.getValue()))
        .collect(Collectors.toList());
    return this;
  }
}
