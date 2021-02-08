package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.backend.repository.api.user.IDeviceUserRepository;
import io.conceptive.homestack.backend.util.Devices;
import io.conceptive.homestack.model.data.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author w.glanzer, 17.01.2021
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/slots")
public class SlotsEndpoint
{

  @Inject
  protected IDeviceUserRepository deviceRepository;


  /**
   * Invalidates the incoming and outgoing connections for a single slot,
   * so it is not connected to any other slot anymore
   *
   * @param pSlotID id of the slot
   */
  @DELETE
  @Path("/{id}/target")
  public void deleteConnections(@PathParam("id") String pSlotID)
  {
    DeviceDataModel device = deviceRepository.findBySlotID(pSlotID);

    if (device == null || device.slots == null)
      return;

    NetworkSlotDataModel sourceSlot = Devices.getSlotByID(device, pSlotID);

    // Invalidate Target
    String oldTargetSlotID = sourceSlot.targetSlotID;
    DeviceDataModel oldTargetDevice = deviceRepository.findBySlotID(oldTargetSlotID);
    if (oldTargetDevice != null)
    {
      Devices.getSlotByID(oldTargetDevice, oldTargetSlotID).targetSlotID = null;
      deviceRepository.upsert(oldTargetDevice);
    }

    // Invalidate Self
    sourceSlot.targetSlotID = null;
    deviceRepository.upsert(device);
  }

  /**
   * Adds a new connection between pSourceID (slot) and pTargetID (slot).
   * If there was a connection
   */
  @PUT
  @Path("/{id}/target")
  @Produces(MediaType.APPLICATION_JSON)
  public Response changeTarget(@PathParam("id") String pSourceID, String pTargetID)
  {
    if (pSourceID == null || pSourceID.isBlank() || pTargetID == null || pTargetID.isBlank())
      throw new BadRequestException();

    // invalidate old destinations (incoming / outgoing connections)
    deleteConnections(pSourceID);
    deleteConnections(pTargetID);

    // find current destinations
    DeviceDataModel sourceDevice = deviceRepository.findBySlotID(pSourceID);
    DeviceDataModel targetDevice = deviceRepository.findBySlotID(pTargetID);

    if (sourceDevice == null || targetDevice == null)
      throw new NotFoundException();

    // add new destinations
    Devices.getSlotByID(sourceDevice, pSourceID).targetSlotID = pTargetID;
    deviceRepository.upsert(sourceDevice);
    Devices.getSlotByID(targetDevice, pTargetID).targetSlotID = pSourceID;
    deviceRepository.upsert(targetDevice);

    // updated
    return Response.ok().build();
  }

}
