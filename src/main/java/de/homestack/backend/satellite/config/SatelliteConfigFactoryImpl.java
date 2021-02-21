package de.homestack.backend.satellite.config;

import de.homestack.backend.database.user.*;
import io.conceptive.homestack.model.satellite.SatelliteConfigurationDataModel;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class SatelliteConfigFactoryImpl implements ISatelliteConfigFactory
{

  @Inject
  protected IDeviceDBRepository deviceRepository;

  @Inject
  protected IMetricDBRepository metricRepository;

  @NotNull
  @Override
  public SatelliteConfigurationDataModel create(@NotNull String pUserID)
  {
    return SatelliteConfigurationDataModel.builder()
        .devices(deviceRepository.getDevices(pUserID))
        .metrics(metricRepository.getMetrics(pUserID))
        .build();
  }

}