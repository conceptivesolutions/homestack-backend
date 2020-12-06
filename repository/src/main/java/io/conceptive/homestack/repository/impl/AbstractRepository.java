package io.conceptive.homestack.repository.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * @author w.glanzer, 16.10.2020
 */
public abstract class AbstractRepository<T>
{
  public static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

  @Inject
  protected MongoClient mongoClient;

  /**
   * @return the type of the collection
   */
  @NotNull
  protected abstract Class<T> getCollectionType();

  /**
   * @return the name of the collection
   */
  @NotNull
  protected String getCollectionName()
  {
    return getCollectionType().getSimpleName().toLowerCase().replace("datamodel", "");
  }

  /**
   * @param pUserID ID of the user
   * @return the collection to use for this repository
   */
  @NotNull
  protected MongoCollection<T> getCollectionForUser(@NotNull String pUserID)
  {
    Class<T> type = getCollectionType();
    return mongoClient
        .getDatabase(pUserID)
        .getCollection(getCollectionName(), type);
  }

}
