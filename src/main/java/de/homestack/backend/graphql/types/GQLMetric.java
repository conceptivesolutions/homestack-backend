package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Metric")
@NoArgsConstructor
@AllArgsConstructor
public class GQLMetric
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
  public List<GQLProperty> settings;

  /**
   * Contains records for this metric
   */
  public List<GQLMetricRecord> records;

}
