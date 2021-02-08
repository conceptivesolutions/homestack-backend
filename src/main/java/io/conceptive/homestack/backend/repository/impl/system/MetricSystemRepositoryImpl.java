package io.conceptive.homestack.backend.repository.impl.system;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.backend.repository.api.system.IMetricSystemRepository;
import io.conceptive.homestack.model.data.MetricDataModel;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class MetricSystemRepositoryImpl extends AbstractSystemRepository<MetricDataModel> implements IMetricSystemRepository
{

  @NotNull
  @Override
  public Set<MetricDataModel> findByDeviceID(@NotNull String pUserID, @NotNull String[] pDeviceIDs)
  {
    return Sets.newHashSet(getCollectionForUser(pUserID).find(Filters.in("deviceID", pDeviceIDs)));
  }

  @NotNull
  @Override
  protected Class<MetricDataModel> getCollectionType()
  {
    return MetricDataModel.class;
  }

}
