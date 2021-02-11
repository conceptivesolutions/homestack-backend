package de.homestack.backend.database.migration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 10.02.2021
 */
interface ICassandraChangeProvider
{

  /**
   * Executes all changes that are necessary to update to the desired version
   *
   * @param pSession      Session to update
   * @param pKeyspaceName Name of the keyspace
   * @param pUserID       ID of the user
   */
  void execute(@NotNull CqlSession pSession, @NotNull String pKeyspaceName, @NotNull String pUserID) throws Exception;

}
