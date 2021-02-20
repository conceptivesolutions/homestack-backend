package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.metric.*;
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
  public List<MetricRecordDataModel> getRecordsByMetricID(@NotNull String pUserID, @NotNull String pMetricID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_RECORDS_BY_METRICID)
                       .columns("id", "metricid", "recorddate", "state", "result")
                       .whereColumn("metricid").isEqualTo(QueryBuilder.literal(UUID.fromString(pMetricID)))
                       .build())
        .map(pRow -> MetricRecordDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .metricID(String.valueOf(pRow.getUuid(1)))
            .recordDate(new Date(Objects.requireNonNull(pRow.getInstant(2)).toEpochMilli()))
            .state(EMetricRecordState.valueOf(pRow.getString(3)))
            .result(pRow.getMap(4, String.class, String.class))
            .build())
        .collect(Collectors.toList());
  }

}
