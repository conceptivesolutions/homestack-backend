package de.homestack.backend.database;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import de.homestack.backend.database.user.IDeviceDBRepository;
import org.cognitor.cassandra.migration.*;
import org.cognitor.cassandra.migration.collector.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;

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

  private static final ScriptCollector SCRIPT_COLLECTOR = new _ScriptCollectorBuilder(IDeviceDBRepository.class)
      .register("001_init.cql");

  @ConfigProperty(name = "homestack.cassandra.user.keyspace.replication")
  protected Optional<String> keyspaceReplication;

  @Inject
  protected CassandraSessionProvider sessionProvider;

  @Override
  public void migrateUserToLatest(@NotNull String pUserID)
  {
    try (CqlSession session = sessionProvider.create())
    {
      String keyspaceName = sessionProvider.getKeyspaceName(pUserID);

      // Create Keyspace
      session.execute(SchemaBuilder.createKeyspace(keyspaceName)
                          .ifNotExists()
                          .withSimpleStrategy(keyspaceReplication.map(Integer::valueOf).orElse(1))
                          .build());

      // Migrate Keyspace to latest
      new MigrationTask(new Database(session, keyspaceName), new _MigrationRepository(SCRIPT_COLLECTOR)).migrate();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialize user space for user " + pUserID, e);
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
