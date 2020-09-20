package io.conceptive.netplan.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import io.conceptive.netplan.IDBConstants;
import io.conceptive.netplan.core.model.Device;
import org.bson.*;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

  @Nullable
  @Override
  public Device findDeviceById(@NotNull String pID)
  {
    return _getCollection().find(new Document("_id", pID), Device.class).first();
  }

  @Override
  public void updateOrInsertDevice(@NotNull Device pDevice)
  {
    pDevice.checkValid();
    _getCollection().replaceOne(new BasicDBObject("_id", pDevice.id), pDevice, _UPSERT);
  }

  @Override
  public boolean deleteDeviceByID(@NotNull String pID)
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
