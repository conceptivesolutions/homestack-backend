package de.homestack.backend.database;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Tests the migration provider
 *
 * @author w.glanzer, 14.02.2021
 * @see CassandraMigrationProvider
 */
@QuarkusTest
@QuarkusTestResource(CassandraResource.class)
public class IntegrationTest_CassandraMigrationProvider
{

  private CassandraSessionProvider sessionProvider;
  private CassandraMigrationProvider migrationProvider;

  @BeforeEach
  void setUp()
  {
    sessionProvider = new CassandraSessionProvider();
    sessionProvider.cassandraHost = System.getProperty("homestack.cassandra.host");
    sessionProvider.cassandraPort = System.getProperty("homestack.cassandra.port");
    sessionProvider.cassandraDataCenter = System.getProperty("homestack.cassandra.datacenter");
    sessionProvider.cassandraUsername = Optional.ofNullable(System.getProperty("homestack.cassandra.username"));
    sessionProvider.cassandraPassword = Optional.ofNullable(System.getProperty("homestack.cassandra.password"));
    migrationProvider = new CassandraMigrationProvider();
    migrationProvider.keyspaceReplication = Optional.empty();
    migrationProvider.sessionProvider = sessionProvider;
  }

  @Test
  public void test_migration()
  {
    String userID = "--my-dummy-user";

    // migrate user
    migrationProvider.migrateUserToLatest(userID);

    // create session
    try (CqlSession session = sessionProvider.create())
    {
      // check keyspace existence
      Assertions.assertTrue(_getKeyspaces(session).contains(sessionProvider.getKeyspaceName(userID)));

      // check keyspace contains something
      Assertions.assertTrue(session.execute("DESCRIBE " + sessionProvider.getKeyspaceName(userID)).all().size() > 1);
    }
  }

  /**
   * @return All available keyspaces
   */
  @NotNull
  private Set<String> _getKeyspaces(@NotNull CqlSession pSession)
  {
    Set<String> keyspaces = new HashSet<>();
    for (Row keyspaceRow : pSession.execute("DESCRIBE keyspaces;").all())
    {
      String keyspace_name = keyspaceRow.getString(0);
      if (keyspace_name != null)
        keyspaces.add(keyspace_name);
    }
    return keyspaces;
  }

}
