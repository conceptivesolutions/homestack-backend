package de.homestack.backend.graphql.types;

import io.conceptive.homestack.model.data.*;
import io.conceptive.homestack.model.data.device.*;
import io.conceptive.homestack.model.data.metric.*;
import io.conceptive.homestack.model.data.satellite.*;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 15.02.2021
 */
@ApplicationScoped
public class GraphQLTypeFactory
{

  @NotNull
  public GQLDevice fromModel(@NotNull DeviceDataModel pModel)
  {
    return new GQLDevice(pModel.id, pModel.icon, pModel.address,
                         pModel.location == null ? null : fromModel(pModel.location),
                         pModel.slots == null ? null : pModel.slots.stream()
                             .map(pSlots -> pSlots.stream()
                                 .map(this::fromModel)
                                 .collect(Collectors.toList()))
                             .collect(Collectors.toList()),
                         null);
  }

  @NotNull
  public GQLLocation fromModel(@NotNull ScreenLocationDataModel pModel)
  {
    return new GQLLocation(pModel.x, pModel.y);
  }

  @NotNull
  public GQLMetric fromModel(@NotNull MetricDataModel pModel)
  {
    return new GQLMetric(pModel.id, pModel.type, pModel.enabled,
                         pModel.settings == null ? null : pModel.settings.entrySet().stream()
                             .map(pEntry -> new GQLProperty(pEntry.getKey(), pEntry.getValue()))
                             .collect(Collectors.toList()),
                         null);
  }

  @NotNull
  public GQLMetricRecord fromModel(@NotNull MetricRecordDataModel pModel)
  {
    return new GQLMetricRecord(pModel.id, pModel.recordDate,
                               pModel.state == null ? null : fromModel(pModel.state),
                               pModel.result == null ? null : pModel.result.entrySet().stream()
                                   .map(pEntry -> new GQLProperty(pEntry.getKey(), pEntry.getValue()))
                                   .collect(Collectors.toList()));
  }

  @NotNull
  public GQLNetworkSlot fromModel(@NotNull NetworkSlotDataModel pModel)
  {
    return new GQLNetworkSlot(pModel.id,
                              pModel.state == null ? null : fromModel(pModel.state),
                              pModel.targetSlotID);
  }

  @NotNull
  public GQLSatellite fromModel(@NotNull SatelliteDataModel pModel)
  {
    return new GQLSatellite(pModel.id, null);
  }

  @NotNull
  public GQLSatelliteLease fromModel(@NotNull SatelliteLeaseDataModel pModel)
  {
    return new GQLSatelliteLease(pModel.id, pModel.userID, pModel.revokedDate, pModel.token);
  }

  @NotNull
  public GQLStack fromModel(@NotNull StackDataModel pModel)
  {
    return new GQLStack(pModel.id, pModel.displayName, null, null);
  }

  @NotNull
  public GQLMetricRecordState fromModel(@NotNull EMetricRecordState pModel)
  {
    return GQLMetricRecordState.valueOf(pModel.name());
  }

  @NotNull
  public GQLNetworkSlotState fromModel(@NotNull ENetworkSlotState pModel)
  {
    return GQLNetworkSlotState.valueOf(pModel.name());
  }

  @NotNull
  public DeviceDataModel toModel(@NotNull GQLDevice pModel, @NotNull String pStackID)
  {
    return new DeviceDataModel(pModel.id, pStackID, pModel.icon, pModel.address,
                               pModel.location == null ? null : toModel(pModel.location),
                               pModel.slots == null ? null : pModel.slots.stream()
                                   .map(pSlots -> pSlots.stream()
                                       .map(this::toModel)
                                       .collect(Collectors.toList()))
                                   .collect(Collectors.toList()));
  }

  @NotNull
  public ScreenLocationDataModel toModel(@NotNull GQLLocation pModel)
  {
    return new ScreenLocationDataModel(pModel.x, pModel.y);
  }

  @NotNull
  public MetricDataModel toModel(@NotNull GQLMetric pModel, @NotNull String pDeviceID)
  {
    return new MetricDataModel(pModel.id, pDeviceID, pModel.type, pModel.enabled,
                               pModel.settings == null ? null : pModel.settings.stream()
                                   .collect(Collectors.toMap(pProp -> pProp.key, pProp -> pProp.value)));
  }

  @NotNull
  public MetricRecordDataModel toModel(@NotNull GQLMetricRecord pModel, @NotNull String pMetricID)
  {
    return new MetricRecordDataModel(pModel.id, pMetricID, pModel.recordTime,
                                     pModel.state == null ? null : toModel(pModel.state),
                                     pModel.result == null ? null : pModel.result.stream()
                                         .collect(Collectors.toMap(pProp -> pProp.key, pProp -> pProp.value)));
  }

  @NotNull
  public NetworkSlotDataModel toModel(@NotNull GQLNetworkSlot pModel)
  {
    return new NetworkSlotDataModel(pModel.id,
                                    pModel.state == null ? null : toModel(pModel.state),
                                    pModel.targetSlotID);
  }

  @NotNull
  public SatelliteDataModel toModel(@NotNull GQLSatellite pModel, @NotNull String pStackID)
  {
    return new SatelliteDataModel(pModel.id, pStackID);
  }

  @NotNull
  public SatelliteLeaseDataModel toModel(@NotNull GQLSatelliteLease pModel, @NotNull String pSatelliteID)
  {
    return new SatelliteLeaseDataModel(pModel.id, pSatelliteID, pModel.userID, pModel.revokedDate, pModel.token);
  }

  @NotNull
  public StackDataModel toModel(@NotNull GQLStack pModel)
  {
    return new StackDataModel(pModel.id, pModel.displayName);
  }

  @NotNull
  public EMetricRecordState toModel(@NotNull GQLMetricRecordState pModel)
  {
    return EMetricRecordState.valueOf(pModel.name());
  }

  @NotNull
  public ENetworkSlotState toModel(@NotNull GQLNetworkSlotState pModel)
  {
    return ENetworkSlotState.valueOf(pModel.name());
  }

}
