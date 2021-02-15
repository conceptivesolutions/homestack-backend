package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.*;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Record")
@NoArgsConstructor
@AllArgsConstructor
public class GQLMetricRecord
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
  public GQLMetricRecordState state;

  /**
   * Additional properties to the current state
   */
  public List<GQLProperty> result;

}
