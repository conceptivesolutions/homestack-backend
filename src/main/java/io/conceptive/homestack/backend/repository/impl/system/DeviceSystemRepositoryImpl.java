package io.conceptive.homestack.backend.repository.impl.system;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.backend.repository.api.system.IDeviceSystemRepository;
import io.conceptive.homestack.model.data.DeviceDataModel;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class DeviceSystemRepositoryImpl extends AbstractSystemRepository<DeviceDataModel> implements IDeviceSystemRepository
{

  @NotNull
  @Override
  public Set<DeviceDataModel> findByStackID(@NotNull String pUserID, @NotNull String pStackID)
  {
    return Sets.newHashSet(getCollectionForUser(pUserID).find(Filters.eq("stackID", pStackID)));
  }

  @NotNull
  @Override
  protected Class<DeviceDataModel> getCollectionType()
  {
    return DeviceDataModel.class;
  }

}
