package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.metric.MetricRecordDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface IMetricRecordDBRepository
{

  /**
   * Retrieves all records of a single metric
   *
   * @param pUserID    user to query
   * @param pMetricID  metric to query
   * @param pFetchType determines, how much of the records should be fetched
   * @return the records
   */
  @NotNull
  List<MetricRecordDataModel> getRecordsByMetricID(@NotNull String pUserID, @NotNull String pMetricID, @NotNull EFetchType pFetchType);

  /**
   * Inserts / Updates the metric record
   *
   * @param pUserID user to query
   * @param pModel  record to update / insert
   * @return the inserted / updated metric record
   */
  @NotNull
  MetricRecordDataModel upsertRecord(@NotNull String pUserID, @NotNull MetricRecordDataModel pModel);

  /**
   * Determines, how much of the records should be fetched
   */
  enum EFetchType
  {
    ALL,
    LATEST
  }

}
