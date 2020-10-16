package io.conceptive.netplan.repository.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * @author w.glanzer, 16.10.2020
 */
abstract class AbstractRepository<T>
{
  protected static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

  @Inject
  protected JsonWebToken token;

  @Inject
  protected MongoClient mongoClient;

  /**
   * @return ID of the user who is accessing this repository
   */
  @NotNull
  public String getUserID()
  {
    String subject = token.getSubject();
    if (subject == null || subject.isBlank())
      throw new UnauthorizedException();
    return subject;
  }

  /**
   * @return the type of the collection
   */
  @NotNull
  protected abstract Class<T> getCollectionType();

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  protected MongoCollection<T> getCollection()
  {
    return getCollectionForUser(getUserID());
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
        .getCollection(type.getName().toLowerCase(), type);
  }

}
