package io.conceptive.netplan.backend.device;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.backend.AbstractRepository;
import io.conceptive.netplan.core.model.Device;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * IDeviceRepository-Impl
 *
 * @author w.glanzer, 13.09.2020
 */
@Dependent
public class DeviceRepositoryImpl extends AbstractRepository<Device> implements IDeviceRepository, IDeviceRepository.ITokenlessRepository
{

  @NotNull
  @Override
  public Set<Device> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @NotNull
  @Override
  public synchronized Set<Device> findByHost(@NotNull String pHostID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("hostID", pHostID)));
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
  public Set<Device> findAll(@NotNull String pUserID)
  {
    return Sets.newHashSet(getCollectionForUser(pUserID).find());
  }

  @NotNull
  @Override
  protected Class<Device> getCollectionType()
  {
    return Device.class;
  }
}
