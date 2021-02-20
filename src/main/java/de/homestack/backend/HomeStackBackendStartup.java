package de.homestack.backend;

import de.homestack.backend.database.IDBMigrationProvider;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
public class HomeStackBackendStartup
{

  @Inject
  protected Logger logger;

  @Inject
  protected IDBMigrationProvider migrationProvider;

  protected void onStart(@Observes StartupEvent ev)
  {
    // Migrate System on startup
    migrationProvider.migrateSystemToLatest();

    // Finished
    logger.info("HomeStack Backend Initialized Successfully");
  }

}
