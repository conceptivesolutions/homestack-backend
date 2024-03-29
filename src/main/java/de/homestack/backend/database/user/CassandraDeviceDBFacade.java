package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.ScreenLocationDataModel;
import io.conceptive.homestack.model.data.device.*;
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
  public List<DeviceDataModel> getDevices(@NotNull String pUserID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                            .columns("id", "stackid", "icon", "address", "location", "slots")
                            .build(), pUserID)
        .map(pRow -> _toDevice(String.valueOf(pRow.getUuid(1)), pRow.getUuid(0), pRow.getString(2), pRow.getString(3), pRow.getString(4), pRow.getString(5)))
        .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<DeviceDataModel> getDevicesByStackID(@NotNull String pUserID, @NotNull String pStackID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                            .columns("id", "icon", "address", "location", "slots")
                            .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                            .build(), pUserID)
        .map(pRow -> _toDevice(pStackID, pRow.getUuid(0), pRow.getString(1), pRow.getString(2), pRow.getString(3), pRow.getString(4)))
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public DeviceDataModel getDeviceByID(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                            .columns("id", "icon", "address", "location", "slots")
                            .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                            .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                            .build(), pUserID)
        .map(pRow -> _toDevice(pStackID, pRow.getUuid(0), pRow.getString(1), pRow.getString(2), pRow.getString(3), pRow.getString(4)))
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public DeviceDataModel upsertDevice(@NotNull String pUserID, @NotNull DeviceDataModel pModel)
  {
    executeUpdate(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                      .value("stackid", QueryBuilder.literal(UUID.fromString(pModel.stackID)))
                      .value("id", QueryBuilder.literal(UUID.fromString(pModel.id)))
                      .value("icon", QueryBuilder.literal(pModel.icon))
                      .value("address", QueryBuilder.literal(pModel.address))
                      .value("location", QueryBuilder.literal(pModel.location == null ? null : pModel.location.x + "," + pModel.location.y))
                      .value("slots", QueryBuilder.literal(pModel.slots == null ? null : toJSON(pModel.slots.stream()
                                                                                                    .map(List::toArray)
                                                                                                    .toArray())))
                      .build(), pUserID);
    return pModel;
  }

  @Override
  public boolean deleteDevice(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pDeviceID)
  {
    return executeUpdate(QueryBuilder.deleteFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_DEVICES_BY_STACKID)
                             .whereColumn("stackid").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                             .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pDeviceID)))
                             .build(), pUserID);
  }

  @Nullable
  private DeviceDataModel _toDevice(@NotNull String pStackID, @Nullable UUID pID, @Nullable String pIcon, @Nullable String pAddress, @Nullable String pLocation, @Nullable String pSlots)
  {
    return DeviceDataModel.builder()
        .id(String.valueOf(pID))
        .stackID(pStackID)
        .icon(pIcon)
        .address(pAddress)
        .slots(pSlots == null ? null : Arrays.stream(fromJSON(NetworkSlotDataModel[][].class, pSlots))
            .map(Arrays::asList)
            .collect(Collectors.toList()))
        .location(pLocation == null ? null : ScreenLocationDataModel.builder()
            .x(Float.parseFloat(Objects.requireNonNull(pLocation).split(",")[0]))
            .y(Float.parseFloat(Objects.requireNonNull(pLocation).split(",")[1]))
            .build())
        .build();
  }

}
