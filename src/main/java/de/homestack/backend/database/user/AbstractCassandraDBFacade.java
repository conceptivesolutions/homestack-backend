package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.core.cql.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.homestack.backend.database.CassandraSessionProvider;
import lombok.SneakyThrows;
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
