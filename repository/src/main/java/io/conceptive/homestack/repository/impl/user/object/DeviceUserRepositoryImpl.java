package io.conceptive.homestack.repository.impl.user.object;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.DeviceDataModel;
import io.conceptive.homestack.repository.api.user.IDeviceUserRepository;
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
