package de.homestack.backend.database;

import com.datastax.oss.driver.api.core.CqlSession;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.net.InetSocketAddress;

/**
 * @author w.glanzer, 10.02.2021
 */
@ApplicationScoped
class CassandraSessionProvider
{

  @ConfigProperty(name = "homestack.cassandra.host")
  protected String cassandraHost;

  @ConfigProperty(name = "homestack.cassandra.port", defaultValue = "9042")
  protected String cassandraPort;

  @ConfigProperty(name = "homestack.cassandra.username")
  protected String cassandraUsername;

  @ConfigProperty(name = "homestack.cassandra.password")
  protected String cassandraPassword;

  @ConfigProperty(name = "homestack.cassandra.datacenter")
  protected String cassandraDataCenter;

  private CqlSession session;

  /**
   * Returns the current cql session or opens a new one, if it is closed
   *
   * @return the session
   */
  @NotNull
  public synchronized CqlSession get()
  {
    if (session == null || session.isClosed())
      session = CqlSession.builder()
          .addContactPoint(new InetSocketAddress(cassandraHost, Integer.parseInt(cassandraPort)))
          .withAuthCredentials(cassandraUsername, cassandraPassword)
          .withLocalDatacenter(cassandraDataCenter)
          .build();
    return session;
  }

  /**
   * Returns the name of the keyspace a user has
   *
   * @param pUserID ID of the user
   * @return the keyspace name
   */
  @NotNull
  public String getKeyspaceName(@NotNull String pUserID)
  {
    return "homestack_user_" + pUserID.replace("-", "");
  }

}
