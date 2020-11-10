package io.conceptive.netplan.backend.metric;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.backend.AbstractRepository;
import io.conceptive.netplan.core.model.Metric;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 02.11.2020
 */
@Dependent
public class MetricRepositoryImpl extends AbstractRepository<Metric> implements IMetricRepository, IMetricRepository.ITokenlessRepository
{
  @NotNull
  @Override
  public Set<Metric> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("deviceID", pDeviceID)));
  }

  @Override
  public void insertMetric(@NotNull Metric pMetric)
  {
    getCollection().replaceOne(Filters.and(Filters.eq("type", pMetric.type), Filters.eq("deviceID", pMetric.deviceID)), pMetric, UPSERT);
  }

  @Override
  public boolean deleteMetric(@NotNull String pDeviceID, @NotNull String pType)
  {
    return getCollection().deleteMany(Filters.and(Filters.eq("type", pType), Filters.eq("deviceID", pDeviceID))).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  public Set<Metric> findAll(@NotNull String pUserID, @NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollectionForUser(pUserID).find(Filters.eq("deviceID", pDeviceID)));
  }

  @NotNull
  @Override
  protected Class<Metric> getCollectionType()
  {
    return Metric.class;
  }
}
