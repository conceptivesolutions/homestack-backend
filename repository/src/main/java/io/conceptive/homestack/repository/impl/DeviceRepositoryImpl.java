package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.DeviceDataModel;
import io.conceptive.homestack.repository.api.IDeviceRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * IDeviceRepository-Impl
 *
 * @author w.glanzer, 13.09.2020
 */
@Dependent
class DeviceRepositoryImpl extends AbstractRepository<DeviceDataModel> implements IDeviceRepository, IDeviceRepository.ITokenlessRepository
{

  @NotNull
  @Override
  public Set<DeviceDataModel> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @NotNull
  @Override
  public synchronized Set<DeviceDataModel> findByStackID(@NotNull String pStackID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("stackID", pStackID)));
  }

  @Nullable
  @Override
  public synchronized DeviceDataModel findByID(@NotNull String pID)
  {
    return getCollection().find(Filters.eq("_id", pID), DeviceDataModel.class).first();
  }

  @Override
  public synchronized void insert(@NotNull DeviceDataModel pDevice)
  {
    getCollection().insertOne(pDevice);
  }

  @Override
  public synchronized void update(@NotNull DeviceDataModel pDevice)
  {
    getCollection().replaceOne(Filters.eq("_id", pDevice.id), pDevice, UPSERT);
  }

  @Override
  public synchronized boolean deleteByID(@NotNull String pID)
  {
    return getCollection().deleteOne(new BasicDBObject("_id", pID)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  public Set<DeviceDataModel> findAll(@NotNull String pUserID)
  {
    return Sets.newHashSet(getCollectionForUser(pUserID).find());
  }

  @NotNull
  @Override
  protected Class<DeviceDataModel> getCollectionType()
  {
    return DeviceDataModel.class;
  }
}
