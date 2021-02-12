package de.homestack.backend.database;

import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.cognitor.cassandra.migration.*;
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

  @Override
  public void migrateToLatest(@NotNull String pUserID)
  {
    try
    {
      String keyspaceName = sessionProvider.getKeyspaceName(pUserID);

      // Create Keyspace
      sessionProvider.get().execute(SchemaBuilder.createKeyspace(keyspaceName)
                                        .ifNotExists()
                                        .withSimpleStrategy(1)
                                        .build());

      // Migrate Keyspace to latest
      new MigrationTask(new Database(sessionProvider.create(), keyspaceName), new _MigrationRepository()).migrate();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialize user space for user " + pUserID, e);
    }
  }

  /**
   * We have to override the repository, because of a different classloader
   */
  private static class _MigrationRepository extends MigrationRepository
  {
  }

}
