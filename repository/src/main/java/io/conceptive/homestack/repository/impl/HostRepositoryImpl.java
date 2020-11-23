package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.HostDataModel;
import io.conceptive.homestack.repository.api.IHostRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
@Dependent
class HostRepositoryImpl extends AbstractRepository<HostDataModel> implements IHostRepository
{

  @NotNull
  @Override
  public Set<HostDataModel> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @Nullable
  @Override
  public HostDataModel findHostByID(@NotNull String pHostID)
  {
    return getCollection().find(Filters.eq("_id", pHostID), HostDataModel.class).first();
  }

  @Override
  public void insertHost(@NotNull HostDataModel pHost)
  {
    getCollection().insertOne(pHost);
  }

  @Override
  public void updateHost(@NotNull HostDataModel pHost)
  {
    getCollection().replaceOne(Filters.eq("_id", pHost.id), pHost, UPSERT);
  }

  @Override
  public boolean deleteHost(@NotNull String pHostID)
  {
    return getCollection().deleteOne(Filters.eq("_id", pHostID)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<HostDataModel> getCollectionType()
  {
    return HostDataModel.class;
  }

}