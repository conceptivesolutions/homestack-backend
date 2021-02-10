package de.homestack.backend.graphql;

import de.homestack.backend.graphql.types.*;
import de.homestack.backend.rbac.IRole;
import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.microprofile.graphql.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Endpoint for common GraphQL communication
 *
 * @author w.glanzer, 10.02.2021
 */
@GraphQLApi
@RolesAllowed(IRole.DEFAULT)
public class GQLEndpoint
{

  @Query
  @NonNull
  public List<StackType> getStacks()
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public StackType getStack(@NonNull @Name("id") String pStackID)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public List<DeviceType> getDevices(@NonNull @Source @Name("stack") StackType pStack)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public DeviceType getDevice(@NonNull @Name("id") String pDeviceID)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public List<SatelliteType> getSatellites(@NonNull @Source @Name("stack") StackType pStack)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public SatelliteType getSatellite(@NonNull @Name("id") String pSatelliteID)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public List<SatelliteLeaseType> getLeases(@NonNull @Source @Name("satellite") SatelliteType pSatellite)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public SatelliteLeaseType getLease(@NonNull @Name("id") String pLeaseID)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public List<MetricType> getMetrics(@NonNull @Source @Name("device") DeviceType pDevice)
  {
    throw new NotImplementedException();
  }

  @Query
  @NonNull
  public MetricType getMetric(@NonNull @Name("id") String pMetricID)
  {
    throw new NotImplementedException();
  }

}
