package io.conceptive.homestack.backend.repository.impl.user.object;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.backend.repository.api.IObjectRepository;
import io.conceptive.homestack.backend.repository.impl.user.AbstractUserRepository;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * Abstract repository that contains all pre-implemented logic
 * to get / retrieve / modify / delete objects in collection.
 * Bound to a single user (by injected token), because of collection retrieval.
 *
 * @author w.glanzer, 16.10.2020
 */
abstract class AbstractObjectUserRepository<T> extends AbstractUserRepository<T> implements IObjectRepository<T>
{

  @NotNull
  @Override
  public Set<T> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @Nullable
  @Override
  public T findByID(@NotNull String pID)
  {
    return getCollection().find(Filters.eq("_id", pID), getCollectionType()).first();
  }

  @Override
  public void upsert(@NotNull T pModel)
  {
    String id = getID(pModel);
    if (id != null)
      getCollection().replaceOne(Filters.eq("_id", id), pModel, UPSERT);
  }

  @Override
  public boolean deleteByID(@NotNull String pID)
  {
    return getCollection().deleteMany(Filters.eq("_id", pID)).getDeletedCount() > 0;
  }

  /**
   * @return the ID of the model
   */
  @Nullable
  protected abstract String getID(@NotNull T pModel);

}
