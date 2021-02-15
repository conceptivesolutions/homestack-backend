package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.core.cql.*;
import de.homestack.backend.database.CassandraSessionProvider;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.stream.*;

/**
 * @author w.glanzer, 15.02.2021
 */
abstract class AbstractCassandraDBFacade
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
  protected Stream<Row> execute(@NotNull Statement<?> pStatement)
  {
    return StreamSupport.stream(sessionProvider.get()
                                    .execute(pStatement)
                                    .spliterator(), false);
  }

}
