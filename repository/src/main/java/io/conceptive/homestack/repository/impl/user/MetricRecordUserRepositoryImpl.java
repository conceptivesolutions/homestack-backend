package io.conceptive.homestack.repository.impl.user;

import com.google.common.collect.Sets;
import com.mongodb.client.model.*;
import io.conceptive.homestack.model.data.MetricRecordDataModel;
import io.conceptive.homestack.repository.api.user.IMetricRecordUserRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
class MetricRecordUserRepositoryImpl extends AbstractUserRepository<MetricRecordDataModel> implements IMetricRecordUserRepository
{

  @NotNull
  @Override
  public Set<MetricRecordDataModel> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().aggregate(List.of(
        Aggregates.match(Filters.eq("deviceID", pDeviceID)),
        Aggregates.sort(Sorts.descending("recordTime")),
        Aggregates.group("$type", Accumulators.first("doc", "$$ROOT")),
        Aggregates.replaceRoot("$doc")
    )));
  }

  @Nullable
  @Override
  public MetricRecordDataModel findByType(@NotNull String pDeviceID, @NotNull String pType)
  {
    return getCollection()
        .find(Filters.and(Filters.eq("deviceID", pDeviceID), Filters.eq("type", pType)))
        .sort(Sorts.descending("recordTime"))
        .first();
  }

  @NotNull
  @Override
  protected Class<MetricRecordDataModel> getCollectionType()
  {
    return MetricRecordDataModel.class;
  }

}
