package io.conceptive.homestack.backend.endpoints;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.StackDataModel;
import io.conceptive.homestack.repository.api.IStackRepository;
import org.apache.commons.lang3.ObjectUtils;
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
  protected IStackRepository stackRepository;

  /**
   * Returns all available stacks
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<StackDataModel> get()
  {
    return stackRepository.findAll();
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

    StackDataModel stack = stackRepository.findByID(pID);
    if (stack == null)
      throw new NotFoundException();

    return stack;
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
    stackRepository.insert(pStack);
    return pStack;
  }

  /**
   * Update a stack in the repository
   *
   * @param pID    ID of the device to update
   * @param pStack Stack
   */
  @PATCH
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public StackDataModel patch(@PathParam("id") String pID, @Nullable StackDataModel pStack)
  {
    if (pID == null || pID.isBlank() || pStack == null)
      throw new BadRequestException();

    StackDataModel stack = stackRepository.findByID(pStack.id);
    if (stack == null)
      throw new NotFoundException();

    // Update Data
    stack.displayName = ObjectUtils.firstNonNull(pStack.displayName, stack.displayName);
    stackRepository.update(stack);
    return stack;
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

    boolean deleted = stackRepository.deleteByID(pID);
    if (!deleted)
      throw new NotFoundException();
  }

}
