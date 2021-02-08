package io.conceptive.homestack.backend.repository.impl.user.object;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.backend.repository.api.user.IDeviceUserRepository;
import io.conceptive.homestack.model.data.DeviceDataModel;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * IDeviceRepository-Impl
 *
 * @author w.glanzer, 13.09.2020
 */
@Dependent
class DeviceUserRepositoryImpl extends AbstractObjectUserRepository<DeviceDataModel> implements IDeviceUserRepository
{

  @NotNull
  @Override
  public synchronized Set<DeviceDataModel> findByStackID(@NotNull String pStackID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("stackID", pStackID)));
  }

  @Nullable
  @Override
  public DeviceDataModel findBySlotID(@NotNull String pSlotID)
  {
    return getCollection().find(Filters.elemMatch("slots", new BasicDBObject("$elemMatch", Filters.eq("_id", pSlotID)))).first();
  }

  @NotNull
  @Override
  protected Class<DeviceDataModel> getCollectionType()
  {
    return DeviceDataModel.class;
  }

  @Nullable
  @Override
  protected String getID(@NotNull DeviceDataModel pModel)
  {
    return pModel.id;
  }

}
