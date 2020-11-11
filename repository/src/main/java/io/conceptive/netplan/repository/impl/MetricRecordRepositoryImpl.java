package io.conceptive.netplan.repository.impl;

import com.mongodb.client.model.*;
import io.conceptive.netplan.model.data.MetricRecordDataModel;
import io.conceptive.netplan.repository.api.IMetricRecordRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.*;
import java.util.stream.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
public class MetricRecordRepositoryImpl extends AbstractRepository<MetricRecordDataModel> implements IMetricRecordRepository, IMetricRecordRepository.ITokenlessRepository
{

  private static final String _RECORDCOLLECTION_PREFIX = "records_";

  @NotNull
  @Override
  public Set<MetricRecordDataModel> findAll(@NotNull String pDeviceID)
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
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  @Nullable
  @Override
  public MetricRecordDataModel findByType(@NotNull String pDeviceID, @NotNull String pType)
  {
    return mongoClient.getDatabase(getUserID())
        .getCollection(_RECORDCOLLECTION_PREFIX + pType, MetricRecordDataModel.class)
        .find(Filters.eq("deviceID", pDeviceID))
        .sort(Sorts.descending("recordTime"))
        .first();
  }

  @Override
  public void addMetricRecord(@NotNull String pUserID, @NotNull MetricRecordDataModel pMetricRecord)
  {
    mongoClient.getDatabase(pUserID)
        .getCollection(_RECORDCOLLECTION_PREFIX + pMetricRecord.type, getCollectionType())
        .insertOne(pMetricRecord);
  }

  @NotNull
  @Override
  protected Class<MetricRecordDataModel> getCollectionType()
  {
    return MetricRecordDataModel.class;
  }

}
