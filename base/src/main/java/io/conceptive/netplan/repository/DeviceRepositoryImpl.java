package io.conceptive.netplan.repository;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import io.conceptive.netplan.IDBConstants;
import io.conceptive.netplan.core.model.Device;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * IDeviceRepository-Impl
 *
 * @author w.glanzer, 13.09.2020
 */
@ApplicationScoped
public class DeviceRepositoryImpl implements IDeviceRepository
{

  private static final ReplaceOptions _UPSERT = new ReplaceOptions().upsert(true);

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public synchronized Set<Device> findAll()
  {
    return Sets.newHashSet(_getCollection().find());
  }

  @Nullable
  @Override
  public synchronized Device findDeviceById(@NotNull String pID)
  {
    return _getCollection().find(Filters.eq("_id", pID), Device.class).first();
  }

  @Override
  public synchronized void insertDevice(@NotNull Device pDevice)
  {
    pDevice.checkValid();
    _getCollection().insertOne(pDevice);
  }

  @Override
  public synchronized void updateDevice(@NotNull Device pDevice)
  {
    pDevice.checkValid();
    _getCollection().replaceOne(Filters.eq("_id", pDevice.id), pDevice, _UPSERT);
  }

  @Override
  public synchronized boolean deleteDeviceByID(@NotNull String pID)
  {
    return _getCollection().deleteOne(new BasicDBObject("_id", pID)).getDeletedCount() > 0;
  }

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  private MongoCollection<Device> _getCollection()
  {
    return mongoClient
        .getDatabase(IDBConstants.DB_NAME)
        .getCollection("devices", Device.class);
  }

}
