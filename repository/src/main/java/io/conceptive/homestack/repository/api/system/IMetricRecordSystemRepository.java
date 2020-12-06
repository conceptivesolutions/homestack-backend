package io.conceptive.homestack.repository.api.system;

import io.conceptive.homestack.model.data.MetricRecordDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 06.12.2020
 */
public interface IMetricRecordSystemRepository
{

  /**
   * Inserts all records for the given user
   *
   * @param pUserID  ID of the user to insert the records for
   * @param pRecords The records to insert
   */
  void insert(@NotNull String pUserID, @NotNull MetricRecordDataModel[] pRecords);

}
