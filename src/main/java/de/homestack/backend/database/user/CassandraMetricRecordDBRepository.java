package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import io.conceptive.homestack.model.data.metric.*;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class CassandraMetricRecordDBRepository extends AbstractCassandraDBFacade implements IMetricRecordDBRepository
{

  private static final String _TABLE_RECORDS_BY_METRICID = "records_by_metricid";

  @NotNull
  @Override
  public List<MetricRecordDataModel> getRecordsByMetricID(@NotNull String pUserID, @NotNull String pMetricID, @NotNull EFetchType pType)
  {
    Select select = QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_RECORDS_BY_METRICID)
        .columns("id", "metricid", "recorddate", "state", "result")
        .whereColumn("metricid").isEqualTo(QueryBuilder.literal(UUID.fromString(pMetricID)));

    // only fetch latest record
    if (pType == EFetchType.LATEST)
      select = select.orderBy("recorddate", ClusteringOrder.DESC)
          .limit(1);

    return executeQuery(select.build(), pUserID)
        .map(pRow -> MetricRecordDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .metricID(String.valueOf(pRow.getUuid(1)))
            .recordDate(pRow.getInstant(2) == null ? null : new Date(Objects.requireNonNull(pRow.getInstant(2)).toEpochMilli()))
            .state(pRow.getString(3) == null ? null : EMetricRecordState.valueOf(pRow.getString(3)))
            .result(pRow.getMap(4, String.class, String.class))
            .build())
        .collect(Collectors.toList());
  }

  @NotNull
  @Override
  @Metered(name = "db_upsertRecord", description = "meters how much metric records are upserted", absolute = true)
  public MetricRecordDataModel upsertRecord(@NotNull String pUserID, @NotNull MetricRecordDataModel pModel)
  {
    executeUpdate(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_RECORDS_BY_METRICID)
                      .value("id", QueryBuilder.literal(UUID.fromString(pModel.id)))
                      .value("metricid", QueryBuilder.literal(UUID.fromString(pModel.metricID)))
                      .value("recorddate", QueryBuilder.literal(pModel.recordDate == null ? null : pModel.recordDate.toInstant()))
                      .value("state", QueryBuilder.literal(pModel.state == null ? null : pModel.state.name()))
                      .value("result", QueryBuilder.literal(pModel.result))
                      .build(), null); // do not track - no userid
    return pModel;
  }

}
