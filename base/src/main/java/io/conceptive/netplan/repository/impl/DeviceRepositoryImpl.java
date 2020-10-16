package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.core.model.Device;
import io.conceptive.netplan.repository.IDeviceRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * IDeviceRepository-Impl
 *
 * @author w.glanzer, 13.09.2020
 */
@Dependent
public class DeviceRepositoryImpl extends AbstractRepository<Device> implements IDeviceRepository
{

  @NotNull
  @Override
  public synchronized Set<Device> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @Nullable
  @Override
  public synchronized Device findDeviceById(@NotNull String pID)
  {
    return getCollection().find(Filters.eq("_id", pID), Device.class).first();
  }

  @Override
  public synchronized void insertDevice(@NotNull Device pDevice)
  {
    pDevice.checkValid();
    getCollection().insertOne(pDevice);
  }

  @Override
  public synchronized void updateDevice(@NotNull Device pDevice)
  {
    pDevice.checkValid();
    getCollection().replaceOne(Filters.eq("_id", pDevice.id), pDevice, UPSERT);
  }

  @Override
  public synchronized boolean deleteDeviceByID(@NotNull String pID)
  {
    return getCollection().deleteOne(new BasicDBObject("_id", pID)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<Device> getCollectionType()
  {
    return Device.class;
  }

}
