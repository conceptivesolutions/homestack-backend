package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.satellite.SatelliteDataModel
 */
@Type("Satellite")
public class SatelliteType
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
  public List<SatelliteLeaseType> leases;

  @NotNull
  public SatelliteType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public SatelliteType leases(@Nullable List<SatelliteLeaseType> pLeases)
  {
    leases = pLeases;
    return this;
  }

}
