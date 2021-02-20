package de.homestack.backend.database.system;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import de.homestack.backend.database.IDBConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class CassandraSatelliteLeaseSystemDBRepository extends AbstractCassandraSystemDBFacade implements ISatelliteLeaseSystemDBRepository
{

  private static final String _TABLE_LEASES = "satelliteleases";

  @Override
  public void registerLease(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID)
  {
    execute(QueryBuilder.insertInto(IDBConstants.SYSTEM_KEYSPACE, _TABLE_LEASES)
                .value("satelliteid", QueryBuilder.literal(UUID.fromString(pSatelliteID)))
                .value("leaseid", QueryBuilder.literal(UUID.fromString(pLeaseID)))
                .value("userid", QueryBuilder.literal(UUID.fromString(pUserID)))
                .build());
  }

  @Nullable
  @Override
  public Pair<String, String> getUserAndSatelliteIDByLeaseID(@NotNull String pLeaseID)
  {
    return execute(QueryBuilder.selectFrom(IDBConstants.SYSTEM_KEYSPACE, _TABLE_LEASES)
                       .columns("satelliteid", "userid")
                       .whereColumn("leaseid").isEqualTo(QueryBuilder.literal(UUID.fromString(pLeaseID)))
                       .build())
        .map(pRow -> Pair.of(String.valueOf(pRow.getUuid(0)), String.valueOf(pRow.getUuid(1))))
        .findFirst()
        .orElse(null);
  }

}
