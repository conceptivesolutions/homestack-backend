package de.homestack.backend.database.system;

import com.datastax.oss.driver.api.core.cql.*;
import de.homestack.backend.database.CassandraSessionProvider;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.stream.*;

/**
 * @author w.glanzer, 20.02.2021
 */
abstract class AbstractCassandraSystemDBFacade
{

  @Inject
  protected CassandraSessionProvider sessionProvider;

  /**
   * Executes the given statement and returns an appropriate stream
   *
   * @param pStatement statement to execute
   * @return the stream
   */
  @NotNull
  @Timed(name = "db_systemQueries", description = "contains all executed database queries of the system", absolute = true)
  protected Stream<Row> executeQuery(@NotNull Statement<?> pStatement)
  {
    return StreamSupport.stream(sessionProvider.get()
                                    .execute(pStatement)
                                    .spliterator(), false);
  }

  /**
   * Executes the given update statement and returns true if something changed
   *
   * @param pStatement statement to execute
   * @return true, if something changed
   */
  @Timed(name = "db_systemUpdates", description = "contains all executed database updates of the system", absolute = true)
  protected boolean executeUpdate(@NotNull Statement<?> pStatement)
  {
    return sessionProvider.get()
        .execute(pStatement)
        .wasApplied();
  }

}
