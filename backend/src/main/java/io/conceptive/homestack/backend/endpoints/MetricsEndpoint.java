package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.*;
import io.conceptive.homestack.repository.api.user.*;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/metrics/{deviceID}")
public class MetricsEndpoint
{

  @Inject
  protected IMetricUserRepository metricRepository;

  @Inject
  protected IMetricRecordUserRepository metricsRecordRepository;

  /**
   * Returns all metrics for a single device
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<MetricDataModel> get(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return metricRepository.findAllByDeviceID(pDeviceID);
  }

  /**
   * Inserts (or modifies) the metric for a device ID
   *
   * @param pDeviceID ID of the device
   * @param pMetric   Metric that should be inserted / updated
   * @return the inserted metric
   */
  @PUT
  @Path("/{metricType}")
  @Produces(MediaType.APPLICATION_JSON)
  public MetricDataModel put(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("metricType") String pMetricType, @Nullable MetricDataModel pMetric)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pMetricType == null || pMetricType.isBlank() || pMetric == null)
      throw new BadRequestException();

    // enforce correct deviceID and type
    pMetric.deviceID = pDeviceID;
    pMetric.type = pMetricType;

    metricRepository.upsert(pMetric);
    return pMetric;
  }

  /**
   * Deletes a single metric for a single device
   *
   * @param pDeviceID   ID of the device
   * @param pMetricType ID of the metric that should be deleted
   */
  @DELETE
  @Path("/{metricType}")
  public void delete(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("metricType") String pMetricType)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pMetricType == null || pMetricType.isBlank())
      throw new BadRequestException();

    if (!metricRepository.delete(pDeviceID, pMetricType))
      throw new NotFoundException();
  }

  /**
   * Returns the latest records, combined by type
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/records")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<MetricRecordDataModel> getRecords(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return metricsRecordRepository.findAll(pDeviceID);
  }

  /**
   * Returns the latest record for a single metric type
   *
   * @param pDeviceID   ID of the device
   * @param pMetricType Type of the metric to search
   */
  @GET
  @Path("/records/{metricType}")
  @Produces(MediaType.APPLICATION_JSON)
  public MetricRecordDataModel getRecord(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("metricType") String pMetricType)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pMetricType == null || pMetricType.isBlank())
      throw new BadRequestException();

    MetricRecordDataModel record = metricsRecordRepository.findByType(pDeviceID, pMetricType);
    if (record == null)
      throw new NotFoundException();

    return record;
  }

}
