package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.ScreenLocationDataModel;
import io.conceptive.homestack.model.data.device.DeviceDataModel;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 17.02.2021
 */
@ApplicationScoped
class CassandraDeviceDBFacade extends AbstractCassandraDBFacade implements IDeviceDBRepository
{

  private static final String _TABLE_DEVICES_BY_STACKID = "devices_by_stackid";

  @NotNull
  @Override
  public List<DeviceDataModel> getDevicesByStackID(@NotNull String pUserID, @NotNull String pStackID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                       .columns("id", "icon", "address", "location")
                       .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                       .build())
        .map(pRow -> _toDevice(pStackID, pRow.getUuid(0), pRow.getString(1), pRow.getString(2), pRow.getString(3)))
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public DeviceDataModel getDeviceByID(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID)
  {
    return execute(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                       .columns("id", "icon", "address", "location")
                       .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                       .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                       .build())
        .map(pRow -> _toDevice(pStackID, pRow.getUuid(0), pRow.getString(1), pRow.getString(2), pRow.getString(3)))
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public DeviceDataModel upsertDevice(@NotNull String pUserID, @NotNull DeviceDataModel pModel)
  {
    execute(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                .value("stackid", QueryBuilder.literal(UUID.fromString(pModel.stackID)))
                .value("id", QueryBuilder.literal(UUID.fromString(pModel.id)))
                .value("icon", QueryBuilder.literal(pModel.icon))
                .value("address", QueryBuilder.literal(pModel.address))
                .value("location", QueryBuilder.literal(pModel.location == null ? null : pModel.location.x + "," + pModel.location.y))
                .build());
    return pModel;
  }

  @Override
  public boolean deleteDevice(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID)
  {
    return sessionProvider.get()
        .execute(QueryBuilder.deleteFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                     .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                     .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                     .build())

        // wasApplied() returns true if the query was applied, and not if the stack was really deleted.
        .wasApplied();
  }

  @Nullable
  private DeviceDataModel _toDevice(@NotNull String pStackID, @Nullable UUID pID, @Nullable String pIcon, @Nullable String pAddress, @Nullable String pLocation)
  {
    return DeviceDataModel.builder()
        .id(String.valueOf(pID))
        .stackID(pStackID)
        .icon(pIcon)
        .address(pAddress)
        .location(pLocation == null ? null : ScreenLocationDataModel.builder()
            .x(Float.parseFloat(Objects.requireNonNull(pLocation).split(",")[0]))
            .y(Float.parseFloat(Objects.requireNonNull(pLocation).split(",")[1]))
            .build())
        .build();
  }

}
