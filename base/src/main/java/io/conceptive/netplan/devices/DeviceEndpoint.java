package io.conceptive.netplan.devices;

import io.conceptive.netplan.core.model.Device;
import io.conceptive.netplan.repository.IDeviceRepository;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Contains all methods for (a single) device
 *
 * @author w.glanzer, 13.09.2020
 */
@Path("/devices")
public class DeviceEndpoint
{

  @Inject
  protected IDeviceRepository deviceRepository;

  /**
   * Returns all available devices
   *
   * @return the devices
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get()
  {
    return Response.ok(deviceRepository.findAll()).build();
  }

  /**
   * Searches a device with the given id
   *
   * @param pID ID to search for
   * @return the device
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("id") String pID)
  {
    if(pID == null || pID.isBlank())
      return Response.status(Response.Status.BAD_REQUEST).build();

    Device device = deviceRepository.findDeviceById(pID);
    if(device == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    return Response.ok(device).build();
  }

  /**
   * Create a device in the repository
   *
   * @param pID     ID of the device to insert
   * @param pDevice Device
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response put(@PathParam("id") String pID, @Nullable Device pDevice)
  {
    if(pID == null || pID.isBlank() || pDevice == null)
      return Response.status(Response.Status.BAD_REQUEST).build();

    // Do not allow inserting metrics
    if(pDevice.metrics != null)
      return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Metrics can not be updated").build();

    // force ID to be set and "correct"
    pDevice.id = pID;

    // insert
    deviceRepository.insertDevice(pDevice);
    return Response.ok(pDevice).build();
  }

  /**
   * Update a device in the repository
   *
   * @param pID     ID of the device to update
   * @param pDevice Device
   */
  @PATCH
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response patch(@PathParam("id") String pID, @Nullable Device pDevice)
  {
    if(pID == null || pID.isBlank() || pDevice == null)
      return Response.status(Response.Status.BAD_REQUEST).build();

    Device device = deviceRepository.findDeviceById(pID);
    if(device == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    if(!device.updateWith(pDevice))
      return Response.status(Response.Status.NOT_MODIFIED).build();

    // Update
    deviceRepository.updateDevice(device);
    return Response.ok(pDevice).build();
  }

  /**
   * Deletes a device with the given id
   *
   * @param pID ID to search for
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") String pID)
  {
    if(pID == null || pID.isBlank())
      return Response.status(Response.Status.BAD_REQUEST).build();

    boolean deleted = deviceRepository.deleteDeviceByID(pID);
    if(deleted)
      return Response.ok().build();

    return Response.notModified().build();
  }


}
