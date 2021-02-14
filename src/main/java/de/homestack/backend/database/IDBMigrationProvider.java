package de.homestack.backend.database;

import org.jetbrains.annotations.NotNull;

/**
 * Provides functionality to migrate databases
 *
 * @author w.glanzer, 14.02.2021
 */
public interface IDBMigrationProvider
{

  /**
   * Migrates the given user to the latest database version.
   * If the user does not exist, the corresponding tables will be created (if necessary).
   * If the user is already on the latest version, nothing will happen.
   *
   * @param pUserID ID of the user to migrate to latest version
   */
  void migrateUserToLatest(@NotNull String pUserID);

}
