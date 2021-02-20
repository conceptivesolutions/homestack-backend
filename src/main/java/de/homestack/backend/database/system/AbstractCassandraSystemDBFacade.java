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
  @Timed
  protected Stream<Row> execute(@NotNull Statement<?> pStatement)
  {
    return StreamSupport.stream(sessionProvider.get()
                                    .execute(pStatement)
                                    .spliterator(), false);
  }

}
