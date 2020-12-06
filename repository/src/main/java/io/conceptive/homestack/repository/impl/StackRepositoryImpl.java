package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.StackDataModel;
import io.conceptive.homestack.repository.api.IStackRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
@Dependent
class StackRepositoryImpl extends AbstractRepository<StackDataModel> implements IStackRepository
{

  @NotNull
  @Override
  public Set<StackDataModel> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @Nullable
  @Override
  public StackDataModel findByID(@NotNull String pStackID)
  {
    return getCollection().find(Filters.eq("_id", pStackID), StackDataModel.class).first();
  }

  @Override
  public void insert(@NotNull StackDataModel pStack)
  {
    getCollection().insertOne(pStack);
  }

  @Override
  public void update(@NotNull StackDataModel pStack)
  {
    getCollection().replaceOne(Filters.eq("_id", pStack.id), pStack, UPSERT);
  }

  @Override
  public boolean deleteByID(@NotNull String pStackID)
  {
    return getCollection().deleteOne(Filters.eq("_id", pStackID)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<StackDataModel> getCollectionType()
  {
    return StackDataModel.class;
  }

}
