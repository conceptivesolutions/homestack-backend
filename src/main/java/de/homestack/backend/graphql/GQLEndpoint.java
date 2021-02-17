package de.homestack.backend.graphql;

import de.homestack.backend.database.user.*;
import de.homestack.backend.graphql.types.*;
import de.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.StackDataModel;
import io.conceptive.homestack.model.data.device.DeviceDataModel;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint for retrieving / mutating data
 *
 * @author w.glanzer, 10.02.2021
 */
@GraphQLApi
@RolesAllowed(IRole.DEFAULT)
public class GQLEndpoint
{

  @Inject
  protected JsonWebToken userToken;

  @Inject
  protected IStackDBFacade stacksFacade;

  @Inject
  protected IDeviceDBRepository deviceRepository;

  @Inject
  protected GraphQLTypeFactory typeFactory;

  /**
   * @return Returns all stacks for the current user
   */
  @Query
  @NonNull
  public List<GQLStack> getStacks()
  {
    return stacksFacade.getStacks(_getUserID()).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Returns all devices of a single stack
   *
   * @param pStack Stack to search devices for
   * @return the list of devices
   */
  @Query
  @NonNull
  public List<GQLDevice> getDevices(@NonNull @Source @Name("stack") GQLStack pStack)
  {
    return deviceRepository.getDevicesByStackID(_getUserID(), pStack.id).stream()
        .map(typeFactory::fromModel)
        .collect(Collectors.toList());
  }

  /**
   * Searches for a stack with the given id
   *
   * @param pStackID ID of the stack to search for
   * @return the stack or null, if not found
   */
  @Query
  public GQLStack getStack(@NonNull @Name("id") String pStackID)
  {
    StackDataModel stack = stacksFacade.getStackByID(_getUserID(), pStackID);
    if (stack == null)
      return null;
    return typeFactory.fromModel(stack);
  }

  /**
   * Searches for a device with the given id in the given stack
   *
   * @param pStackID  ID of the stack to search in
   * @param pDeviceID ID of the device to search for
   * @return the device or null, if not found
   */
  @Query
  public GQLDevice getDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pDeviceID)
  {
    DeviceDataModel device = deviceRepository.getDeviceByID(_getUserID(), pStackID, pDeviceID);
    if (device == null)
      return null;
    return typeFactory.fromModel(device);
  }

  /**
   * Inserts / Updates the given stack
   *
   * @param pStack Stack to update / insert
   * @return the updated / inserted stack
   */
  @Mutation
  public GQLStack upsertStack(@NonNull @Name("stack") GQLStack pStack)
  {
    return typeFactory.fromModel(stacksFacade.upsertStack(_getUserID(), typeFactory.toModel(pStack)));
  }

  /**
   * Inserts / Updates the given device
   *
   * @param pStackID ID of the stack where the device belongs to
   * @param pDevice  Device to update / insert
   * @return the updated / inserted device
   */
  @Mutation
  public GQLDevice upsertDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("device") GQLDevice pDevice)
  {
    return typeFactory.fromModel(deviceRepository.upsertDevice(_getUserID(), typeFactory.toModel(pDevice, pStackID)));
  }

  /**
   * Deletes a stack with the given id
   *
   * @param pStackID ID of the stack to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteStack(@NonNull @Name("id") String pStackID)
  {
    return stacksFacade.deleteStack(_getUserID(), pStackID);
  }

  /**
   * Deletes a device with the given id
   *
   * @param pStackID  ID of the stack that the device belongs to
   * @param pDeviceID ID of the device to delete
   * @return true, if it was deleted
   */
  @Mutation
  public boolean deleteDevice(@NonNull @Name("stackID") String pStackID, @NonNull @Name("id") String pDeviceID)
  {
    return deviceRepository.deleteDevice(_getUserID(), pStackID, pDeviceID);
  }

  /**
   * @return ID of the user who is accessing this repository
   */
  @NotNull
  private String _getUserID()
  {
    String subject = userToken.getSubject();
    if (subject == null || subject.isBlank())
      throw new UnauthorizedException();
    return subject;
  }

}
