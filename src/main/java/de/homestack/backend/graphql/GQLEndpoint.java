package de.homestack.backend.graphql;

import de.homestack.backend.database.user.*;
import de.homestack.backend.graphql.types.*;
import de.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.StackDataModel;
import io.conceptive.homestack.model.data.device.DeviceDataModel;
import io.conceptive.homestack.model.data.metric.MetricDataModel;
import io.conceptive.homestack.model.data.satellite.*;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Endpoint for retrieving / mutating data
 *
 * @author w.glanzer, 10.02.2021
 */
@GraphQLApi
@RolesAllowed(IRole.DEFAULT)
public class GQLEndpoint
{

  @Inject
  protected JsonWebToken userToken;

  @Inject
  protected IStackDBRepository stackRepository;

  @Inject
  protected IDeviceDBRepository deviceRepository;

  @Inject
  protected IMetricDBRepository metricRepository;

  @Inject
  protected IMetricRecordDBRepository metricRecordRepository;

  @Inject
  protected ISatelliteDBRepository satelliteRepository;

  @Inject
  protected ISatelliteLeaseDBRepository satelliteLeaseRepository;

  @Inject
  protected GraphQLTypeFactory typeFactory;

  /**
   * @return Returns all stacks for the current user
   */
  @Query
  @NonNull
  public List<GQLStack> getStacks()
  {
    return stackRepository.getStacks(_getUserID()).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all devices of a single stack
   *
   * @param pStack Stack to search devices for
   * @return the list of devices
   */
  @Query
  @NonNull
  public List<GQLDevice> getDevices(@NonNull @Source @Name("stack") GQLStack pStack)
  {
    return deviceRepository.getDevicesByStackID(_getUserID(), pStack.id).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all metrics of a single device
   *
   * @param pDevice Device to search metrics for
   * @return the list of metrics
   */
  @Query
  @NonNull
  public List<GQLMetric> getMetrics(@NonNull @Source @Name("device") GQLDevice pDevice)
  {
    return metricRepository.getMetricsByDeviceID(_getUserID(), pDevice.id).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all records of a single metric
   *
   * @param pMetric Metric to search records for
   * @return the list of records
   */
  @Query
  @NonNull
  public List<GQLMetricRecord> getRecords(@NotNull @Source @Name("metric") GQLMetric pMetric, @NonNull @Name("type") GQLRecordFetchType pFetchType)
  {
    return metricRecordRepository.getRecordsByMetricID(_getUserID(), pMetric.id, typeFactory.toModel(pFetchType)).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all satellites of a single stack
   *
   * @param pStack Stack to search satellites for
   * @return the list of satellites
   */
  @Query
  @NonNull
  public List<GQLSatellite> getSatellites(@NonNull @Source @Name("stack") GQLStack pStack)
  {
    return satelliteRepository.getSatellitesByStackID(_getUserID(), pStack.id).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all leases of a single satellite
   *
   * @param pSatellite Satellite to search leases for
   * @return the list of leases
   */
  @Query
  @NonNull
  public List<GQLSatelliteLease> getLeases(@NonNull @Source @Name("satellite") GQLSatellite pSatellite)
  {
    return satelliteLeaseRepository.getLeasesBySatelliteID(_getUserID(), pSatellite.id).stream()
        .map(typeFactory::fromModel)
        .peek(pLease -> pLease.token = null) // do not expose tokens
        .collect(Collectors.toList());
  }

  /**
   * Searches for a stack with the given id
   *
   * @param pStackID ID of the stack to search for
   * @return the stack or null, if not found
   */
  @Query
  public GQLStack getStack(@NonNull @Name("id") String pStackID)
  {
    StackDataModel stack = stackRepository.getStackByID(_getUserID(), pStackID);
    if (stack == null)
      return null;
    return typeFactory.fromModel(stack);
  }

  /**
   * Searches for a device with the given id in the given stack
   *
   * @param pStackID  ID of the stack to search in
   * @param pDeviceID ID of the device to search for
   * @return the device or null, if not found
   */
  @Query
  public GQLDevice getDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pDeviceID)
  {
    DeviceDataModel device = deviceRepository.getDeviceByID(_getUserID(), pStackID, pDeviceID);
    if (device == null)
      return null;
    return typeFactory.fromModel(device);
  }

  /**
   * Searches for a metric with the given id in the given device
   *
   * @param pDeviceID ID of the device to search in
   * @param pMetricID ID of the metric to search for
   * @return the metric or null, if not found
   */
  @Query
  public GQLMetric getMetric(@NonNull @Name("deviceID") String pDeviceID, @NonNull @Name("id") String pMetricID)
  {
    MetricDataModel model = metricRepository.getMetricByID(_getUserID(), pDeviceID, pMetricID);
    if (model == null)
      return null;
    return typeFactory.fromModel(model);
  }

  /**
   * Searches for a satellite with the given id in the given stack
   *
   * @param pStackID     ID of the stack to search in
   * @param pSatelliteID ID of the satellite to search for
   * @return the satellite or null, if not found
   */
  @Query
  public GQLSatellite getSatellite(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pSatelliteID)
  {
    SatelliteDataModel satellite = satelliteRepository.getSatelliteByID(_getUserID(), pStackID, pSatelliteID);
    if (satellite == null)
      return null;
    return typeFactory.fromModel(satellite);
  }

  /**
   * Searches for a lease with the given id in the given satellite
   *
   * @param pSatelliteID ID of the satellite to search in
   * @param pLeaseID     ID of the lease to search for
   * @return the lease or null, if not found
   */
  @Query
  public GQLSatelliteLease getLease(@NonNull @Name("satelliteID") String pSatelliteID, @NonNull @Name("id") String pLeaseID)
  {
    SatelliteLeaseDataModel lease = satelliteLeaseRepository.getLeaseByID(_getUserID(), pLeaseID, pSatelliteID);
    if (lease == null)
      return null;
    lease.token = null; // do not expose tokens
    return typeFactory.fromModel(lease);
  }

  /**
   * Inserts / Updates the given stack
   *
   * @param pStack Stack to update / insert
   * @return the updated / inserted stack
   */
  @Mutation
  public GQLStack upsertStack(@NonNull @Name("stack") GQLStack pStack)
  {
    return typeFactory.fromModel(stackRepository.upsertStack(_getUserID(), typeFactory.toModel(pStack)));
  }

  /**
   * Inserts / Updates the given device
   *
   * @param pStackID ID of the stack where the device belongs to
   * @param pDevice  Device to update / insert
   * @return the updated / inserted device
   */
  @Mutation
  public GQLDevice upsertDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("device") GQLDevice pDevice)
  {
    return typeFactory.fromModel(deviceRepository.upsertDevice(_getUserID(), typeFactory.toModel(pDevice, pStackID)));
  }

  /**
   * Inserts / Updates the given metric
   *
   * @param pDeviceID ID of the device where the metric belongs to
   * @param pMetric   Metric to update / insert
   * @return the updated / inserted metric
   */
  @Mutation
  public GQLMetric upsertMetric(@NonNull @Name("deviceID") String pDeviceID, @NonNull @Name("metric") GQLMetric pMetric)
  {
    return typeFactory.fromModel(metricRepository.upsertMetric(_getUserID(), typeFactory.toModel(pMetric, pDeviceID)));
  }

  /**
   * Inserts / Updates the given satellite
   *
   * @param pStackID   ID of the stack where the satellite belongs to
   * @param pSatellite Satellite to update / insert
   * @return the updated / inserted satellite
   */
  @Mutation
  public GQLSatellite upsertSatellite(@NonNull @Name("stackID") String pStackID, @NonNull @Name("satellite") GQLSatellite pSatellite)
  {
    return typeFactory.fromModel(satelliteRepository.upsertSatellite(_getUserID(), typeFactory.toModel(pSatellite, pStackID)));
  }

  /**
   * Creates a new lease
   *
   * @param pSatelliteID ID of the satellite where the lease belongs to
   * @return the created lease, with token
   */
  @Mutation
  public GQLSatelliteLease createLease(@NonNull @Name("satelliteID") String pSatelliteID)
  {
    return typeFactory.fromModel(satelliteLeaseRepository.createLease(_getUserID(), pSatelliteID));
  }

  /**
   * Revokes a lease with the given id
   *
   * @param pSatelliteID ID of the satellite that the lease belongs to
   * @param pLeaseID     ID of the lease to revoke
   * @return the mutated lease
   */
  @Mutation
  public GQLSatelliteLease revokeLease(@NonNull @Name("satelliteID") String pSatelliteID, @NonNull @Name("id") String pLeaseID)
  {
    SatelliteLeaseDataModel revoked = satelliteLeaseRepository.revokeLease(_getUserID(), pSatelliteID, pLeaseID, new Date());
    if (revoked == null)
      return null;
    revoked.token = null; // do not expose tokens
    return typeFactory.fromModel(revoked);
  }

  /**
   * Deletes a stack with the given id
   *
   * @param pStackID ID of the stack to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteStack(@NonNull @Name("id") String pStackID)
  {
    return stackRepository.deleteStack(_getUserID(), pStackID);
  }

  /**
   * Deletes a device with the given id
   *
   * @param pStackID  ID of the stack that the device belongs to
   * @param pDeviceID ID of the device to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pDeviceID)
  {
    return deviceRepository.deleteDevice(_getUserID(), pStackID, pDeviceID);
  }

  /**
   * Deletes a metric with the given id
   *
   * @param pDeviceID ID of the device that the metric belongs to
   * @param pMetricID ID of the metric to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteMetric(@NonNull @Name("deviceID") String pDeviceID, @NonNull @Name("id") String pMetricID)
  {
    return metricRepository.deleteMetric(_getUserID(), pDeviceID, pMetricID);
  }

  /**
   * Deletes a satellite with the given id
   *
   * @param pStackID     ID of the stack that the device belongs to
   * @param pSatelliteID ID of the satellite to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteSatellite(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pSatelliteID)
  {
    return satelliteRepository.deleteSatellite(_getUserID(), pStackID, pSatelliteID);
  }

  /**
   * @return ID of the user who is accessing this repository
   */
  @NotNull
  private String _getUserID()
  {
    String subject = userToken.getSubject();
    if (subject == null || subject.isBlank())
      throw new UnauthorizedException();
    return subject;
  }

}
