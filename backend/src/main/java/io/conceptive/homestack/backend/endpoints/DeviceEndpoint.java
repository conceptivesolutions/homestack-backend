package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.DeviceDataModel;
import io.conceptive.homestack.repository.api.user.IDeviceUserRepository;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Contains all methods for (a single) device
 *
 * @author w.glanzer, 13.09.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/devices")
public class DeviceEndpoint
{

  @Inject
  protected IDeviceUserRepository deviceRepository;

  /**
   * Returns all available devices
   *
   * @return the devices
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<DeviceDataModel> get(@QueryParam("stack") @Nullable String pStackID)
  {
    if (pStackID == null || pStackID.isBlank())
      return deviceRepository.findAll();
    return deviceRepository.findByStackID(pStackID);
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
  public DeviceDataModel getByID(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    DeviceDataModel device = deviceRepository.findByID(pID);
    if (device == null)
      throw new NotFoundException();

    return device;
  }

  /**
   * Create / Updates a device in the repository
   *
   * @param pID     ID of the device to insert
   * @param pDevice Device
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public DeviceDataModel put(@PathParam("id") String pID, @Nullable DeviceDataModel pDevice)
  {
    if (pID == null || pID.isBlank() || pDevice == null)
      throw new BadRequestException();

    if (pDevice.stackID == null || pDevice.stackID.isBlank())
      throw new BadRequestException("stackID has to be specified");

    // force ID to be set and "correct"
    pDevice.id = pID;

    // insert
    deviceRepository.upsert(pDevice);
    return pDevice;
  }

  /**
   * Update a device partially in the repository
   *
   * @param pID     ID of the device to update
   * @param pDevice Device
   */
  @PATCH
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public DeviceDataModel patch(@PathParam("id") String pID, @Nullable DeviceDataModel pDevice)
  {
    if (pID == null || pID.isBlank() || pDevice == null)
      throw new BadRequestException();

    if (pDevice.stackID == null || pDevice.stackID.isBlank())
      throw new BadRequestException("stackID has to be specified");

    // force ID to be set and "correct"
    pDevice.id = pID;

    deviceRepository.upsert(pDevice);
    return pDevice;
  }

  /**
   * Deletes a device with the given id
   *
   * @param pID ID to search for
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public void delete(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    boolean deleted = deviceRepository.deleteByID(pID);
    if (!deleted)
      throw new NotFoundException();
  }


}
