package de.homestack.backend.database;

import de.homestack.backend.database.migration.ICassandraMigrationExecutor;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author w.glanzer, 10.02.2021
 */
@ApplicationScoped
class CassandraSystemDBFacade implements ISystemDBFacade
{

  @Inject
  protected CassandraSessionProvider sessionProvider;

  @Inject
  protected ICassandraMigrationExecutor migrationExecutor;

  @Override
  public void migrateToLatest(@NotNull String pUserID)
  {
    try
    {
      migrationExecutor.migrateToLatest(sessionProvider.get(), sessionProvider.getKeyspaceName(pUserID), pUserID);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialize user space for user " + pUserID, e);
    }
  }

}
