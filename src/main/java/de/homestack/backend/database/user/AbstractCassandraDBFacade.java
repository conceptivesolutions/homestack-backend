package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.core.cql.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.homestack.backend.database.CassandraSessionProvider;
import de.homestack.backend.database.change.IRepositoryChangeObserver;
import lombok.SneakyThrows;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jetbrains.annotations.*;

import javax.inject.Inject;
import java.util.stream.*;

/**
 * @author w.glanzer, 15.02.2021
 */
abstract class AbstractCassandraDBFacade
{

  private static final ObjectMapper mapper = new ObjectMapper();

  @Inject
  protected CassandraSessionProvider sessionProvider;

  @Inject
  protected IRepositoryChangeObserver changeObserver;

  /**
   * Executes the given statement and returns an appropriate stream
   *
   * @param pStatement statement to execute
   * @return the stream
   */
  @NotNull
  @Timed(name = "db_userQueries", description = "contains all executed database queries of a user", absolute = true)
  protected Stream<Row> executeQuery(@NotNull Statement<?> pStatement, @Nullable String pUserID)
  {
    return StreamSupport.stream(sessionProvider.get()
                                    .execute(pStatement)
                                    .spliterator(), false);
  }

  /**
   * Executes the given statement and returns, if the statement was applied
   *
   * @param pStatement statement to execute
   * @return true, if it was applied
   */
  @Timed(name = "db_userUpdates", description = "contains all executed database updates of a user", absolute = true)
  protected boolean executeUpdate(@NotNull Statement<?> pStatement, @Nullable String pUserID)
  {
    boolean wasApplied = sessionProvider.get()
        .execute(pStatement)
        .wasApplied();

    if (pUserID != null && wasApplied)
      changeObserver.fireChangeForUser(pUserID);
    return wasApplied;
  }

  /**
   * Converts the given object to a json string
   *
   * @param pObject object to convert
   * @return the string or null, if pObject is null
   */
  @Nullable
  @SneakyThrows
  public String toJSON(@Nullable Object pObject)
  {
    if (pObject == null)
      return null;
    return mapper.writeValueAsString(pObject);
  }

  /**
   * Converts the given string back to an object
   *
   * @param pType       Type
   * @param pSerialized Serialized string
   * @return the object
   */
  @SneakyThrows
  @Contract("_, null -> null")
  public <T> T fromJSON(@NotNull Class<T> pType, @Nullable String pSerialized)
  {
    if (pSerialized == null)
      return null;
    return mapper.readValue(pSerialized, pType);
  }

}
