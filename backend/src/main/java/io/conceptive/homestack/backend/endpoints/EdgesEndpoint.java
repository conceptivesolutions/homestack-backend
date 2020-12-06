package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.EdgeDataModel;
import io.conceptive.homestack.repository.api.user.IEdgeUserRepository;
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
@Path("/devices/{deviceID}/edges")
public class EdgesEndpoint
{

  @Inject
  protected IEdgeUserRepository edgeRepository;

  /**
   * Returns all edges of a given device
   *
   * @param pDeviceID ID of the device
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<EdgeDataModel> get(@Nullable @PathParam("deviceID") String pDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank())
      throw new BadRequestException();

    return edgeRepository.findAll(pDeviceID);
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
  public EdgeDataModel put(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable String pTargetDeviceID)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pTargetDeviceID == null || pTargetDeviceID.isBlank())
      throw new BadRequestException();

    return edgeRepository.insert(pDeviceID, pTargetDeviceID);
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
  public void delete(@Nullable @PathParam("deviceID") String pDeviceID, @Nullable @PathParam("targetID") String pTargetID)
  {
    if (pDeviceID == null || pDeviceID.isBlank() || pTargetID == null || pTargetID.isBlank())
      throw new BadRequestException();

    if (!edgeRepository.delete(pDeviceID, pTargetID))
      throw new NotFoundException();
  }
}
