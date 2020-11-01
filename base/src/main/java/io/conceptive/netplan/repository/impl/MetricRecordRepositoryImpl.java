package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.*;
import io.conceptive.netplan.core.model.MetricRecord;
import io.conceptive.netplan.repository.IMetricRecordRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
public class MetricRecordRepositoryImpl extends AbstractRepository<MetricRecord> implements IMetricRecordRepository, IMetricRecordRepository.ITokenlessRepository
{

  @NotNull
  @Override
  public Set<MetricRecord> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().aggregate(List.of(
        Aggregates.match(Filters.eq("deviceID", pDeviceID)), // filter only for device
        Aggregates.sort(Sorts.descending("recordTime")), // sort all records by time
        Aggregates.group("type", Accumulators.first("recordID", "$_id")), // group by type and get recentliest record
        Aggregates.lookup(getCollectionName(), "recordID", "_id", "records"), // join ID on record
        Aggregates.replaceRoot(new BasicDBObject("$arrayElemAt", new Object[]{"$records", 0})), // replace root
        Aggregates.project(Projections.exclude("records")) // exclude previously selected records
    )));
  }

  @Override
  public void addMetricRecord(@NotNull String pUserID, @NotNull MetricRecord pMetricRecord)
  {
    getCollectionForUser(pUserID).insertOne(pMetricRecord);
  }

  @NotNull
  @Override
  protected Class<MetricRecord> getCollectionType()
  {
    return MetricRecord.class;
  }

}
