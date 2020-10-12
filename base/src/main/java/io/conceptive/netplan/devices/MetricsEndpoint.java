package io.conceptive.netplan.devices;

import io.conceptive.netplan.repository.IMetricsRepository;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Path("/metrics/{deviceID}")
public class MetricsEndpoint
{

  @Inject
  protected IMetricsRepository metricsRepository;

  /**
   * Returns all metrics for a single device
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      return Response.status(Response.Status.BAD_REQUEST).build();
    return Response.ok(metricsRepository.findAll(pDeviceID)).build();
  }

}
