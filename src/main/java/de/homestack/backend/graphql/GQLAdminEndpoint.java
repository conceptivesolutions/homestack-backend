package de.homestack.backend.graphql;

import de.homestack.backend.database.IDBMigrationProvider;
import de.homestack.backend.rbac.IRole;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.concurrent.*;

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
  protected IDBMigrationProvider dbMigrationProvider;

  @Inject
  protected Logger logger;

  @Inject
  protected JsonWebToken token;

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
    dbMigrationProvider.migrateUserToLatest(pUserID);
    return pUserID;
  }

  /**
   * Queues a shutdown of the whole server
   *
   * @return true, if it was sucessfully queued
   */
  @Query("shutdown")
  @NonNull
  public Boolean shutdown()
  {
    // Log about shutdown
    logger.warn("Server shutdown queued by user " + token.getSubject());

    // Schedule Shutdown
    Executors.newSingleThreadScheduledExecutor().schedule(() -> {
      logger.warn("Exitting...");
      System.exit(0);
    }, 1, TimeUnit.SECONDS);

    // return true always..
    return true;
  }

}
