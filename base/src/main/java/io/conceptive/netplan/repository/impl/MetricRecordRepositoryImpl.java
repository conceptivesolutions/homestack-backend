package io.conceptive.netplan.repository.impl;

import com.mongodb.client.model.*;
import io.conceptive.netplan.core.model.MetricRecord;
import io.conceptive.netplan.repository.IMetricRecordRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;
import java.util.stream.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
public class MetricRecordRepositoryImpl extends AbstractRepository<MetricRecord> implements IMetricRecordRepository, IMetricRecordRepository.ITokenlessRepository
{

  private static final String _RECORDCOLLECTION_PREFIX = "records_";

  @NotNull
  @Override
  public Set<MetricRecord> findAll(@NotNull String pDeviceID)
  {
    return StreamSupport.stream(mongoClient.getDatabase(getUserID())
                                    .listCollectionNames()
                                    .spliterator(), false)
        .filter(pName -> pName.startsWith(_RECORDCOLLECTION_PREFIX))
        .map(pCollectionName -> mongoClient.getDatabase(getUserID())
            .getCollection(pCollectionName, getCollectionType())
            .find(Filters.eq("deviceID", pDeviceID))
            .sort(Sorts.descending("recordTime"))
            .first())
        .collect(Collectors.toSet());
  }

  @Nullable
  @Override
  public MetricRecord findByType(@NotNull String pDeviceID, @NotNull String pType)
  {
    return mongoClient.getDatabase(getUserID())
        .getCollection(_RECORDCOLLECTION_PREFIX + pType, MetricRecord.class)
        .find(Filters.eq("deviceID", pDeviceID))
        .sort(Sorts.descending("recordTime"))
        .first();
  }

  @Override
  public void addMetricRecord(@NotNull String pUserID, @NotNull MetricRecord pMetricRecord)
  {
    mongoClient.getDatabase(pUserID)
        .getCollection(_RECORDCOLLECTION_PREFIX + pMetricRecord.type, getCollectionType())
        .insertOne(pMetricRecord);
  }

  @NotNull
  @Override
  protected Class<MetricRecord> getCollectionType()
  {
    return MetricRecord.class;
  }

}
