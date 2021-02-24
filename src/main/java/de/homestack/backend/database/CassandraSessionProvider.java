package de.homestack.backend.database;

import com.datastax.oss.driver.api.core.*;
import com.datastax.oss.driver.api.core.config.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Optional;

/**
 * @author w.glanzer, 10.02.2021
 */
@ApplicationScoped
public class CassandraSessionProvider
{

  @ConfigProperty(name = "homestack.cassandra.host")
  protected String cassandraHost;

  @ConfigProperty(name = "homestack.cassandra.port", defaultValue = "9042")
  protected String cassandraPort;

  @ConfigProperty(name = "homestack.cassandra.datacenter")
  protected String cassandraDataCenter;

  @ConfigProperty(name = "homestack.cassandra.username")
  protected Optional<String> cassandraUsername;

  @ConfigProperty(name = "homestack.cassandra.password")
  protected Optional<String> cassandraPassword;

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
      session = create();
    return session;
  }

  /**
   * Creates always a new cql session
   *
   * @return the session
   */
  @NotNull
  public synchronized CqlSession create()
  {
    CqlSessionBuilder builder = CqlSession.builder()
        .addContactPoint(new InetSocketAddress(cassandraHost, Integer.parseInt(cassandraPort)))
        .withLocalDatacenter(cassandraDataCenter)
        .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                              .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(15))
                              .withBoolean(DefaultDriverOption.REQUEST_WARN_IF_SET_KEYSPACE, false)
                              .withInt(DefaultDriverOption.SESSION_LEAK_THRESHOLD, 10)
                              .withString(DefaultDriverOption.PROTOCOL_VERSION, "V5")
                              .build());

    // specify user/password, if necessary
    if (cassandraUsername.isPresent() || cassandraPassword.isPresent())
      builder = builder.withAuthCredentials(cassandraUsername.orElse(""), cassandraPassword.orElse(""));

    return builder.build();
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
    // specialhandling for auth0, because the ID contains a "|" symbol
    return "homestack_user_" + pUserID.replace("-", "").replace("|", "");
  }

}
