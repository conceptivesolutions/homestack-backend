package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Satellite")
@NoArgsConstructor
@AllArgsConstructor
public class GQLSatellite
{

  /**
   * ID of the satellite
   */
  @Id
  @NonNull
  public String id;

  /**
   * Leases
   */
  public List<GQLSatelliteLease> leases;

}
