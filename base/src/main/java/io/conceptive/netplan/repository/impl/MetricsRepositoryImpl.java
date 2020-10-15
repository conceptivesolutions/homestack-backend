package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import io.conceptive.netplan.IDBConstants;
import io.conceptive.netplan.core.model.Metric;
import io.conceptive.netplan.repository.IMetricsRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
@ApplicationScoped
public class MetricsRepositoryImpl implements IMetricsRepository
{
  private static final ReplaceOptions _UPSERT = new ReplaceOptions().upsert(true);

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<Metric> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(_getCollection().find(Filters.eq("deviceID", pDeviceID)));
  }

  @Override
  public void updateMetric(@NotNull Metric pMetric)
  {
    _getCollection().replaceOne(Filters.and(Filters.eq("deviceID", pMetric.deviceID), Filters.eq("type", pMetric.type)), pMetric, _UPSERT);
  }

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  private MongoCollection<Metric> _getCollection()
  {
    return mongoClient
        .getDatabase(IDBConstants.DB_NAME)
        .getCollection("metrics", Metric.class);
  }

}
