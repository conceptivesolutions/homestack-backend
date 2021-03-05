package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class CassandraSatelliteDBRepository extends AbstractCassandraDBFacade implements ISatelliteDBRepository
{

  private static final String _TABLE_SATELLITES_BY_STACKID = "satellites_by_stackid";

  @NotNull
  @Override
  public List<SatelliteDataModel> getSatellitesByStackID(@NotNull String pUserID, @NotNull String pStackID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_SATELLITES_BY_STACKID)
                            .columns("id", "stackid")
                            .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                            .build(), pUserID)
        .map(pRow -> SatelliteDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .stackID(String.valueOf(pRow.getUuid(1)))
            .build())
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public SatelliteDataModel getSatelliteByID(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pSatelliteID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_SATELLITES_BY_STACKID)
                            .columns("id", "stackid")
                            .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                            .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                            .build(), pUserID)
        .map(pRow -> SatelliteDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .stackID(String.valueOf(pRow.getUuid(1)))
            .build())
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public SatelliteDataModel upsertSatellite(@NotNull String pUserID, @NotNull SatelliteDataModel pModel)
  {
    executeUpdate(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_SATELLITES_BY_STACKID)
                      .value("id", QueryBuilder.literal(UUID.fromString(pModel.id)))
                      .value("stackid", QueryBuilder.literal(UUID.fromString(pModel.stackID)))
                      .build(), pUserID);
    return pModel;
  }

  @Override
  public boolean deleteSatellite(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pSatelliteID)
  {
    return executeUpdate(QueryBuilder.deleteFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_SATELLITES_BY_STACKID)
                             .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                             .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                             .build(), pUserID);
  }

}
