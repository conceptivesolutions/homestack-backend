package de.homestack.backend.database;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import de.homestack.backend.database.system.ISatelliteLeaseSystemDBRepository;
import de.homestack.backend.database.user.IDeviceDBRepository;
import org.cognitor.cassandra.migration.*;
import org.cognitor.cassandra.migration.collector.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URL;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.cognitor.cassandra.migration.MigrationRepository.VERSION_NAME_DELIMITER;

/**
 * Provider for database migrations of cassandra
 *
 * @author w.glanzer, 14.02.2021
 */
@ApplicationScoped
class CassandraMigrationProvider implements IDBMigrationProvider
{

  private static final ScriptCollector SCRIPT_COLLECTOR_SYSTEM = new _ScriptCollectorBuilder(ISatelliteLeaseSystemDBRepository.class)
      .register("001_initLeases.cql");

  private static final ScriptCollector SCRIPT_COLLECTOR_USER = new _ScriptCollectorBuilder(IDeviceDBRepository.class)
      .register("001_init.cql");

  @ConfigProperty(name = "homestack.cassandra.user.keyspace.replication")
  protected Optional<String> keyspaceReplication;

  @ConfigProperty(name = "homestack.cassandra.system.keyspace.replication")
  protected Optional<String> systemKeyspaceReplication;

  @Inject
  protected CassandraSessionProvider sessionProvider;

  @Override
  public void migrateSystemToLatest()
  {
    _migrate(IDBConstants.SYSTEM_KEYSPACE, systemKeyspaceReplication.map(Integer::valueOf).orElse(null), SCRIPT_COLLECTOR_SYSTEM);
  }

  @Override
  public void migrateUserToLatest(@NotNull String pUserID)
  {
    _migrate(sessionProvider.getKeyspaceName(pUserID), keyspaceReplication.map(Integer::valueOf).orElse(null), SCRIPT_COLLECTOR_USER);
  }

  /**
   * Migrates the given keyspace to the latest version.
   * Scripts are collected by the given collector.
   *
   * @param pKeyspace         Keyspace
   * @param pRelicationFactor Factor
   * @param pCollector        Collector
   */
  private void _migrate(@NotNull String pKeyspace, @Nullable Integer pRelicationFactor, @NotNull ScriptCollector pCollector)
  {
    try (CqlSession session = sessionProvider.create())
    {
      // Create Keyspace
      session.execute(SchemaBuilder.createKeyspace(pKeyspace)
                          .ifNotExists()
                          .withSimpleStrategy(pRelicationFactor == null ? 1 : pRelicationFactor)
                          .build());

      // Migrate Keyspace to latest
      new MigrationTask(new Database(session, pKeyspace), new _MigrationRepository(pCollector)).migrate();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialize key space " + pKeyspace, e);
    }
  }

  /**
   * ScriptCollector, that will allow you to register a resource
   */
  private static class _ScriptCollectorBuilder implements ScriptCollector
  {
    private final Set<ScriptFile> files = new HashSet<>();
    private final Class<?> referenceClass;

    public _ScriptCollectorBuilder(@NotNull Class<?> pReferenceClass)
    {

      referenceClass = pReferenceClass;
    }

    @Override
    public void collect(ScriptFile scriptFile)
    {
    }

    @Override
    public Collection<ScriptFile> getScriptFiles()
    {
      return files;
    }

    /**
     * Registers a new resource
     *
     * @param pName Name of the resource
     * @return the builder
     */
    @NotNull
    public _ScriptCollectorBuilder register(@NotNull String pName)
    {
      URL resource = referenceClass.getResource(pName);
      if (resource == null)
        throw new IllegalArgumentException("Failed to find update script for name " + pName + " at class " + referenceClass.getName());
      files.add(new ScriptFile(extractScriptVersion(pName), referenceClass.getPackageName().replace(".", "/") + "/" + pName, extractScriptName(pName)));
      return this;
    }

    private static int extractScriptVersion(String scriptName)
    {
      try
      {
        String[] splittedName = scriptName.split(VERSION_NAME_DELIMITER);
        int folderSeperatorPos = splittedName[0].lastIndexOf("/");
        String versionString;
        if (folderSeperatorPos >= 0)
          versionString = splittedName[0].substring(folderSeperatorPos + 1);
        else
          versionString = splittedName[0];

        return parseInt(versionString);
      }
      catch (NumberFormatException exception)
      {
        throw new MigrationException(format("Error for script %s. Unable to extract version.", scriptName), exception, scriptName);
      }
    }

    private String extractScriptName(String resourceName)
    {
      int slashIndex = resourceName.lastIndexOf("/");
      if (slashIndex > -1)
        return resourceName.substring(slashIndex + 1);
      return resourceName;
    }

  }

  /**
   * Repository has to be overriden, because of different quarkus classloader
   */
  private static class _MigrationRepository extends MigrationRepository
  {
    public _MigrationRepository(@NotNull ScriptCollector scriptCollector)
    {
      super(DEFAULT_SCRIPT_PATH, scriptCollector);
    }
  }

}
