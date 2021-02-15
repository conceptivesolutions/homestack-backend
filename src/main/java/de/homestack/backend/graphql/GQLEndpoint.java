package de.homestack.backend.graphql;

import de.homestack.backend.database.user.IStackDBFacade;
import de.homestack.backend.graphql.types.*;
import de.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.StackDataModel;
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
