package io.conceptive.netplan.devices;

import io.conceptive.netplan.core.IRole;
import io.conceptive.netplan.core.model.*;
import io.conceptive.netplan.repository.*;
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
  protected IMetricRepository metricRepository;

  @Inject
  protected IMetricRecordRepository metricsRecordRepository;

  /**
   * Returns all metrics for a single device
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<Metric> get(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return metricRepository.findAll(pDeviceID);
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
  public Metric put(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("metricType") String pMetricType, @Nullable Metric pMetric)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pMetricType == null || pMetricType.isBlank() || pMetric == null)
      throw new BadRequestException();

    // enforce correct deviceID and type
    pMetric.deviceID = pDeviceID;
    pMetric.type = pMetricType;

    metricRepository.insertMetric(pMetric);
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

    if (!metricRepository.deleteMetric(pDeviceID, pMetricType))
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
  public Set<MetricRecord> getRecords(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return metricsRecordRepository.findAll(pDeviceID);
  }

}
