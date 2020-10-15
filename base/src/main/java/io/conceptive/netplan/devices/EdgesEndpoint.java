package io.conceptive.netplan.devices;

import io.conceptive.netplan.core.IRole;
import io.conceptive.netplan.repository.IEdgeRepository;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/devices/{deviceID}/edges")
public class EdgesEndpoint
{

  @Inject
  protected IEdgeRepository edgeRepository;

  /**
   * Returns all edges of a given device
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
    return Response.ok(edgeRepository.findAll(pDeviceID)).build();
  }

  /**
   * Adds a new edge between two devices
   *
   * @param pDeviceID       ID of the source device
   * @param pTargetDeviceID Target Device ID
   */
  @POST
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response put(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable String pTargetDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pTargetDeviceID == null || pTargetDeviceID.isBlank())
      return Response.status(Response.Status.BAD_REQUEST).build();

    if (edgeRepository.addEdge(pDeviceID, pTargetDeviceID))
      return Response.ok().build();

    return Response.notModified().build();
  }

  /**
   * Deletes a single edge from one target to another
   *
   * @param pDeviceID ID of the device
   * @param pTargetID Target ID of the device
   */
  @DELETE
  @Path("/{targetID}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("targetID") String pTargetID)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pTargetID == null || pTargetID.isBlank())
      return Response.status(Response.Status.BAD_REQUEST).build();

    if (edgeRepository.removeEdge(pDeviceID, pTargetID))
      return Response.ok().build();

    return Response.notModified().build();
  }
}
