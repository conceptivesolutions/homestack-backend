package de.homestack.backend.database.migration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 10.02.2021
 */
public interface ICassandraMigrationExecutor
{

  /**
   * Migrates the user space to the latest version
   *
   * @param pSession      Session for db queries
   * @param pKeyspaceName Keyspace to update
   * @param pUserID       ID of the user to migrate
   */
  void migrateToLatest(@NotNull CqlSession pSession, @NotNull String pKeyspaceName, @NotNull String pUserID) throws MigrationFailedException;

}
