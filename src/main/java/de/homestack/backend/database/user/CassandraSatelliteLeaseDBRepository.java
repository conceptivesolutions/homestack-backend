package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import de.homestack.backend.database.system.ISatelliteLeaseSystemDBRepository;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class CassandraSatelliteLeaseDBRepository extends AbstractCassandraDBFacade implements ISatelliteLeaseDBRepository
{

  private static final String _TABLE_LEASES_BY_SATELLITEID = "satelliteleases_by_satelliteid";

  @Inject
  protected ISatelliteLeaseSystemDBRepository systemRepository;

  @NotNull
  @Override
  public List<SatelliteLeaseDataModel> getLeasesBySatelliteID(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_LEASES_BY_SATELLITEID)
                       .columns("id", "satelliteid", "userid", "revokeddate", "accesstoken")
                       .whereColumn("satelliteid").isEqualTo(QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                       .build())
        .map(pRow -> SatelliteLeaseDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .satelliteID(String.valueOf(pRow.getUuid(1)))
            .userID(pRow.getString(2))
            .revokedDate(pRow.getInstant(3) == null ? null : new Date(Objects.requireNonNull(pRow.getInstant(3)).toEpochMilli()))
            .token(pRow.getString(4))
            .build())
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public SatelliteLeaseDataModel getLeaseByID(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_LEASES_BY_SATELLITEID)
                       .columns("id", "satelliteid", "userid", "revokeddate", "accesstoken")
                       .whereColumn("satelliteid").isEqualTo(QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                       .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pLeaseID)))
                       .build())
        .map(pRow -> SatelliteLeaseDataModel.builder()
            .id(String.valueOf(pRow.getUuid(0)))
            .satelliteID(String.valueOf(pRow.getUuid(1)))
            .userID(pRow.getString(2))
            .revokedDate(pRow.getInstant(3) == null ? null : new Date(Objects.requireNonNull(pRow.getInstant(3)).toEpochMilli()))
            .token(pRow.getString(4))
            .build())
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public SatelliteLeaseDataModel createLease(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    SatelliteLeaseDataModel model = SatelliteLeaseDataModel.builder()
        .id(UUID.randomUUID().toString())
        .userID(pUserID)
        .satelliteID(pSatelliteID)
        .token(RandomStringUtils.randomAlphanumeric(256))
        .build();

    // insert new lease
    execute(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_LEASES_BY_SATELLITEID)
                .value("id", QueryBuilder.literal(UUID.fromString(model.id)))
                .value("satelliteid", QueryBuilder.literal(UUID.fromString(model.satelliteID)))
                .value("userid", QueryBuilder.literal(model.userID))
                .value("accesstoken", QueryBuilder.literal(model.token))
                .build());

    // register lease in system
    systemRepository.registerLease(pUserID, pSatelliteID, model.id);

    return model;
  }

  @Nullable
  @Override
  public SatelliteLeaseDataModel revokeLease(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID, @NotNull Date pRevokeDate)
  {
    execute(QueryBuilder.update(sessionProvider.getKeyspaceName(pUserID), _TABLE_LEASES_BY_SATELLITEID)
                .setColumn("revokeddate", QueryBuilder.literal(pRevokeDate.toInstant()))
                .whereColumn("satelliteid").isEqualTo(QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pLeaseID)))
                .build());
    return getLeaseByID(pUserID, pSatelliteID, pLeaseID);
  }

}
