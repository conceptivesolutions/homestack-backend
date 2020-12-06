package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.*;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.user.*;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
@Path("/stacks")
@RolesAllowed(IRole.DEFAULT)
public class StackEndpoint
{

  @Inject
  protected IStackUserRepository stackUserRepository;

  @Inject
  protected ISatelliteUserRepository satelliteUserRepository;

  @Inject
  protected IDeviceUserRepository deviceUserRepository;

  /**
   * Returns all available stacks
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<StackDataModel> get()
  {
    return stackUserRepository.findAll();
  }

  /**
   * Searches the stack with the given id
   *
   * @param pID ID to search for
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public StackDataModel getByID(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    StackDataModel stack = stackUserRepository.findByID(pID);
    if (stack == null)
      throw new NotFoundException();

    return stack;
  }

  /**
   * Returns all available satellites in this stack
   */
  @GET
  @Path("/{id}/satellites")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<SatelliteDataModel> getSatellites(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    return satelliteUserRepository.findByStackID(pID);
  }

  /**
   * Returns all available devices in this stack
   *
   * @return the devices
   */
  @GET
  @Path("/{id}/devices")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<DeviceDataModel> getDevices(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    return deviceUserRepository.findByStackID(pID);
  }

  /**
   * Creates a new stack in the repository
   *
   * @param pID    ID of the stack to insert
   * @param pStack Stack
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public StackDataModel put(@PathParam("id") String pID, @Nullable StackDataModel pStack)
  {
    if (pID == null || pID.isBlank() || pStack == null)
      throw new BadRequestException();

    // force IDs to be set and "correct"
    pStack.id = pID;

    // insert
    stackUserRepository.upsert(pStack);
    return pStack;
  }

  /**
   * Deletes a stack with the given id
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

    boolean deleted = stackUserRepository.deleteByID(pID);
    if (!deleted)
      throw new NotFoundException();
  }

}
