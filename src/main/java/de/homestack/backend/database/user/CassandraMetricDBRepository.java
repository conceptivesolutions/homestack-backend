package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.metric.MetricDataModel;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class CassandraMetricDBRepository extends AbstractCassandraDBFacade implements IMetricDBRepository
{

  private static final String _TABLE_METRICS_BY_DEVICEID = "metrics_by_deviceid";

  @NotNull
  @Override
  public List<MetricDataModel> getMetricsByDeviceID(@NotNull String pUserID, @NotNull String pDeviceID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_METRICS_BY_DEVICEID)
                       .columns("id", "deviceid", "type", "enabled", "settings")
                       .whereColumn("deviceid").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                       .build())
        .map(pRow -> MetricDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .deviceID(String.valueOf(pRow.getUuid(1)))
            .type(pRow.getString(2))
            .enabled(pRow.getBoolean(3))
            .settings(pRow.getMap(4, String.class, String.class))
            .build())
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public MetricDataModel getMetricByID(@NotNull String pUserID, @NotNull String pDeviceID, @NotNull String pMetricID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_METRICS_BY_DEVICEID)
                       .columns("id", "deviceid", "type", "enabled", "settings")
                       .whereColumn("deviceid").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                       .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pMetricID)))
                       .build())
        .map(pRow -> MetricDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .type(pRow.getString(2))
            .enabled(pRow.getBoolean(3))
            .settings(pRow.getMap(4, String.class, String.class))
            .build())
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public MetricDataModel upsertMetric(@NotNull String pUserID, @NotNull MetricDataModel pModel)
  {
    execute(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_METRICS_BY_DEVICEID)
                .value("deviceid", QueryBuilder.literal(UUID.fromString(pModel.deviceID)))
                .value("id", QueryBuilder.literal(UUID.fromString(pModel.id)))
                .value("type", QueryBuilder.literal(pModel.type))
                .value("enabled", QueryBuilder.literal(pModel.enabled))
                .value("settings", QueryBuilder.literal(pModel.settings))
                .build());
    return pModel;
  }

  @Override
  public boolean deleteMetric(@NotNull String pUserID, @NotNull String pDeviceID, @NotNull String pMetricID)
  {
    return sessionProvider.get()
        .execute(QueryBuilder.deleteFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_METRICS_BY_DEVICEID)
                     .whereColumn("deviceid").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                     .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pMetricID)))
                     .build())

        // wasApplied() returns true if the query was applied, and not if the metric was really deleted.
        .wasApplied();
  }

}
