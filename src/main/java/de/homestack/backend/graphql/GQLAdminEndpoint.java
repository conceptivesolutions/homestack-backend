package de.homestack.backend.graphql;

import de.homestack.backend.database.ISystemDBFacade;
import de.homestack.backend.rbac.IRole;
import org.eclipse.microprofile.graphql.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

/**
 * Contains all methods for administrators only
 *
 * @author w.glanzer, 10.02.2021
 */
@GraphQLApi
@RolesAllowed(IRole.ADMIN)
public class GQLAdminEndpoint
{

  @Inject
  protected ISystemDBFacade systemDBFacade;

  /**
   * Migrates the user with the given ID to the newest database version
   *
   * @param pUserID ID of the user to migrate
   * @return the migrated user id
   */
  @Mutation("migrateUser")
  @NonNull
  public String migrateUser(@NonNull @Name("id") String pUserID)
  {
    systemDBFacade.migrateToLatest(pUserID);
    return pUserID;
  }

}
