package io.conceptive.homestack.repository.impl.user;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.MetricDataModel;
import io.conceptive.homestack.repository.api.user.IMetricUserRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 02.11.2020
 */
@Dependent
class MetricUserRepositoryImpl extends AbstractUserRepository<MetricDataModel> implements IMetricUserRepository
{
  @NotNull
  @Override
  public Set<MetricDataModel> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @NotNull
  @Override
  public Set<MetricDataModel> findAllByDeviceID(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("deviceID", pDeviceID)));
  }

  @Override
  public void upsert(@NotNull MetricDataModel pMetric)
  {
    getCollection().replaceOne(Filters.and(Filters.eq("type", pMetric.type), Filters.eq("deviceID", pMetric.deviceID)), pMetric, UPSERT);
  }

  @Override
  public boolean delete(@NotNull String pDeviceID, @NotNull String pType)
  {
    return getCollection().deleteMany(Filters.and(Filters.eq("type", pType), Filters.eq("deviceID", pDeviceID))).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<MetricDataModel> getCollectionType()
  {
    return MetricDataModel.class;
  }
}