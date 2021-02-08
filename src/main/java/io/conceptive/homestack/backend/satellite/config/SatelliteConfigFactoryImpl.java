package io.conceptive.homestack.backend.satellite.config;

import io.conceptive.homestack.backend.repository.api.system.*;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.model.satellite.SatelliteConfigurationDataModel;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class SatelliteConfigFactoryImpl implements ISatelliteConfigFactory
{

  @Inject
  protected ISatelliteSystemRepository satelliteSystemRepository;

  @Inject
  protected IDeviceSystemRepository deviceSystemRepository;

  @Inject
  protected IMetricSystemRepository metricSystemRepository;

  @NotNull
  @Override
  public SatelliteConfigurationDataModel create(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    SatelliteDataModel satelliteModel = satelliteSystemRepository.findByID(pUserID, pSatelliteID);

    // should never happen
    if (satelliteModel == null)
      throw new NotFoundException("Satellite " + pSatelliteID + " for user " + pUserID + " not found");

    SatelliteConfigurationDataModel configModel = new SatelliteConfigurationDataModel();
    if (satelliteModel.stackID != null)
    {
      configModel.devices = deviceSystemRepository.findByStackID(pUserID, satelliteModel.stackID);
      configModel.metrics = metricSystemRepository.findByDeviceID(pUserID, configModel.devices.stream()
          .map(pModel -> pModel.id)
          .toArray(String[]::new));
    }
    return configModel;
  }

}
