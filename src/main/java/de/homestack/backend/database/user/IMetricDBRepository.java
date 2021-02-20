package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.metric.MetricDataModel;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface IMetricDBRepository
{

  /**
   * Retrieves all metrics of a single device
   *
   * @param pUserID   user to query
   * @param pDeviceID device to query
   * @return the metrics
   */
  @NotNull
  List<MetricDataModel> getMetricsByDeviceID(@NotNull String pUserID, @NotNull String pDeviceID);

  /**
   * Returns a single metric by id
   *
   * @param pUserID   user to query
   * @param pDeviceID device to query
   * @param pMetricID id of the metric to search
   * @return the model, or null if not found
   */
  @Nullable
  MetricDataModel getMetricByID(@NotNull String pUserID, @NotNull String pDeviceID, @NotNull String pMetricID);

  /**
   * Inserts / Updates the given metric
   *
   * @param pUserID user to query
   * @param pModel  model to insert / update
   * @return the updated model
   */
  @NotNull
  MetricDataModel upsertMetric(@NotNull String pUserID, @NotNull MetricDataModel pModel);

  /**
   * Deletes the given metric
   *
   * @param pUserID   user to query
   * @param pDeviceID device to query
   * @param pMetricID id to delete
   * @return true, if something was deleted
   */
  boolean deleteMetric(@NotNull String pUserID, @NotNull String pDeviceID, @NotNull String pMetricID);

}
