package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.metric.MetricDataModel
 */
@Type("Metric")
public class MetricType
{

  /**
   * ID of the device, this metric is applicable for
   */
  @Id
  @NonNull
  public String id;

  /**
   * Type of the metric (acts like an ID in combination with deviceID)
   * something like "ping, tracert, ..."
   */
  public String type;

  /**
   * Determines, if this metric is enabled. NULL == FALSE
   */
  public Boolean enabled;

  /**
   * Contains all necessary settings for the given type
   */
  public List<PropertyType> settings;

  /**
   * Contains records for this metric
   */
  public List<MetricRecordType> records;

  @NotNull
  public MetricType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public MetricType type(@Nullable String pType)
  {
    type = pType;
    return this;
  }

  @NotNull
  public MetricType enabled(@Nullable Boolean pEnabled)
  {
    enabled = pEnabled;
    return this;
  }

  @NotNull
  public MetricType settings(@Nullable Map<String, String> pSettings)
  {
    settings = pSettings == null ? null : pSettings.entrySet().stream()
        .map(pEntry -> new PropertyType()
            .key(pEntry.getKey())
            .value(pEntry.getValue()))
        .collect(Collectors.toList());
    return this;
  }

  @NotNull
  public MetricType records(@Nullable List<MetricRecordType> pRecords)
  {
    records = pRecords;
    return this;
  }
}
