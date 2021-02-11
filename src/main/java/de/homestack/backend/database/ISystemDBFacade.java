package de.homestack.backend.database;

import org.jetbrains.annotations.NotNull;

/**
 * Contains all system relevant database functionality
 *
 * @author w.glanzer, 10.02.2021
 */
public interface ISystemDBFacade
{

  /**
   * Migrates the given user to the latest db version
   *
   * @param pUserID ID of the user to migrate to the latest version
   */
  void migrateToLatest(@NotNull String pUserID);

}
