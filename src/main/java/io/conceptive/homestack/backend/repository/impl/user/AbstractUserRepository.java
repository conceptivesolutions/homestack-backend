package io.conceptive.homestack.backend.repository.impl.user;

import com.mongodb.client.MongoCollection;
import io.conceptive.homestack.backend.repository.impl.AbstractRepository;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Repository bound to a single user (identified by injected token)
 *
 * @author w.glanzer, 16.10.2020
 */
public abstract class AbstractUserRepository<T> extends AbstractRepository<T>
{

  @Inject
  protected JsonWebToken token;

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
   * @return the collection to use for this repository
   */
  @NotNull
  protected MongoCollection<T> getCollection()
  {
    return getCollectionForUser(getUserID());
  }

}
