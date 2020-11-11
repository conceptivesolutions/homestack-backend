package io.conceptive.netplan.backend.endpoints;

import io.conceptive.netplan.backend.rbac.IRole;
import io.conceptive.netplan.model.data.DeviceDataModel;
import io.conceptive.netplan.repository.api.IDeviceRepository;
import org.apache.commons.lang3.ObjectUtils;
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
  protected IDeviceRepository deviceRepository;

  /**
   * Returns all available devices
   *
   * @return the devices
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<DeviceDataModel> get(@QueryParam("host") @Nullable String pHostID)
  {
    if (pHostID == null || pHostID.isBlank())
      return deviceRepository.findAll();
    return deviceRepository.findByHost(pHostID);
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

    DeviceDataModel device = deviceRepository.findDeviceById(pID);
    if (device == null)
      throw new NotFoundException();

    return device;
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
  public DeviceDataModel put(@PathParam("id") String pID, @Nullable DeviceDataModel pDevice)
  {
    if (pID == null || pID.isBlank() || pDevice == null)
      throw new BadRequestException();

    if (pDevice.hostID == null || pDevice.hostID.isBlank())
      throw new BadRequestException("hostID has to be specified");

    // force ID to be set and "correct"
    pDevice.id = pID;

    // insert
    deviceRepository.insertDevice(pDevice);
    return pDevice;
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
  public DeviceDataModel patch(@PathParam("id") String pID, @Nullable DeviceDataModel pDevice)
  {
    if (pID == null || pID.isBlank() || pDevice == null)
      throw new BadRequestException();

    DeviceDataModel device = deviceRepository.findDeviceById(pID);
    if (device == null)
      throw new NotFoundException();

    // Update Data
    device.address = ObjectUtils.firstNonNull(pDevice.address, device.address);
    device.location = ObjectUtils.firstNonNull(pDevice.location, device.location);
    device.hostID = ObjectUtils.firstNonNull(pDevice.hostID, device.hostID);
    device.icon = ObjectUtils.firstNonNull(pDevice.icon, device.icon);
    deviceRepository.updateDevice(device);
    return device;
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

    boolean deleted = deviceRepository.deleteDeviceByID(pID);
    if (!deleted)
      throw new NotFoundException();
  }


}
