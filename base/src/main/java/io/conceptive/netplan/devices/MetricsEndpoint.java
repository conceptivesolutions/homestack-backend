package io.conceptive.netplan.devices;

import io.conceptive.netplan.core.IRole;
import io.conceptive.netplan.core.model.MetricRecord;
import io.conceptive.netplan.repository.IMetricRecordRepository;
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
  protected IMetricRecordRepository metricsRepository;

  /**
   * Returns the latest records, combined by type
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/records")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<MetricRecord> get(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return metricsRepository.findAll(pDeviceID);
  }

}
