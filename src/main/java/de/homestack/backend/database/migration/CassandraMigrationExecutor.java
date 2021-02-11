package de.homestack.backend.database.migration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

/**
 * Executes all migrations necessary for the current cassandra implementation
 * //todo VERSION-TABLE so we know, what version a usertable has
 *
 * @author w.glanzer, 10.02.2021
 */
@ApplicationScoped
class CassandraMigrationExecutor implements ICassandraMigrationExecutor
{

  private static final Map<String, ICassandraChangeProvider> CHANGEPROVDERS = Map.of(
      "0.0.0", new UserDataChangeProvider_0_0_0_to_1_0_0()
  );

  @Override
  public void migrateToLatest(@NotNull CqlSession pSession, @NotNull String pKeyspaceName, @NotNull String pUserID) throws MigrationFailedException
  {
    try
    {
      CHANGEPROVDERS.entrySet().stream()
          .sorted(Comparator.comparing(pEntry -> Integer.parseInt(pEntry.getKey().replace(".", ""))))
          .forEachOrdered(pEntry -> {
            try
            {
              pEntry.getValue().execute(pSession, pKeyspaceName, pUserID);
            }
            catch (Exception e)
            {
              throw new RuntimeException("Failed to migrate userspace " + pUserID + " to version " + pEntry.getKey(), e);
            }
          });
    }
    catch (Throwable e)
    {
      throw new MigrationFailedException("Failed to migrate userspace to latest version (" + pUserID + ")", e);
    }
  }

}
