package io.conceptive.homestack.repository.impl.system;

import com.mongodb.client.MongoCollection;
import io.conceptive.homestack.repository.impl.AbstractRepository;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 16.10.2020
 */
public abstract class AbstractSystemRepository<T> extends AbstractRepository<T>
{

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  protected MongoCollection<T> getCollection()
  {
    return mongoClient.getDatabase("_____SYSTEM")
        .getCollection(getCollectionName(), getCollectionType());
  }

}
