package io.conceptive.homestack.repository.impl.system;

import io.conceptive.homestack.model.data.MetricRecordDataModel;
import io.conceptive.homestack.repository.api.system.IMetricRecordSystemRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class MetricRecordSystemRepositoryImpl extends AbstractSystemRepository<MetricRecordDataModel> implements IMetricRecordSystemRepository
{

  @Override
  public void insert(@NotNull String pUserID, @NotNull MetricRecordDataModel[] pRecords)
  {
    getCollectionForUser(pUserID).insertMany(List.of(pRecords));
  }

  @NotNull
  @Override
  protected Class<MetricRecordDataModel> getCollectionType()
  {
    return MetricRecordDataModel.class;
  }

}
