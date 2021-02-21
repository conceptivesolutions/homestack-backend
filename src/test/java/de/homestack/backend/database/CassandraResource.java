package de.homestack.backend.database;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.util.Map;

/**
 * Cassandra Resource for Quarkus Unit Tests
 *
 * @author w.glanzer, 14.02.2021
 */
public class CassandraResource implements QuarkusTestResourceLifecycleManager
{

  private final GenericContainer<?> cassandraContainer = new GenericContainer<>("cassandra:4.0")
      .withExposedPorts(9042)
      .waitingFor(Wait.forLogMessage(".*Startup complete.*\\n", 1)
                      .withStartupTimeout(Duration.ofMinutes(10)));

  @Override
  public Map<String, String> start()
  {
    cassandraContainer.start();
    return Map.of(
        "homestack.cassandra.host", cassandraContainer.getContainerIpAddress(),
        "homestack.cassandra.port", String.valueOf(cassandraContainer.getMappedPort(9042)),
        "homestack.cassandra.datacenter", "datacenter1"
    );
  }

  @Override
  public void stop()
  {
    cassandraContainer.stop();
  }

}
