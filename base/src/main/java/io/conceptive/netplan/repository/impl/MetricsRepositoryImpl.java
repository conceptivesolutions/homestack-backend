package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.core.model.Metric;
import io.conceptive.netplan.repository.IMetricsRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
public class MetricsRepositoryImpl extends AbstractRepository<Metric> implements IMetricsRepository, IMetricsRepository.ITokenlessRepository
{

  @NotNull
  @Override
  public Set<Metric> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("deviceID", pDeviceID)));
  }

  @Override
  public void updateMetric(@NotNull String pUserID, @NotNull Metric pMetric)
  {
    getCollectionForUser(pUserID).replaceOne(Filters.and(Filters.eq("deviceID", pMetric.deviceID), Filters.eq("type", pMetric.type)), pMetric, UPSERT);
  }

  @NotNull
  @Override
  protected Class<Metric> getCollectionType()
  {
    return Metric.class;
  }

}
