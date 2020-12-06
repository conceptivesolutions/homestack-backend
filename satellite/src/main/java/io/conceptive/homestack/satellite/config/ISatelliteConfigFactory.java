package io.conceptive.homestack.satellite.config;

import io.conceptive.homestack.model.satellite.SatelliteConfigurationDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 06.12.2020
 */
public interface ISatelliteConfigFactory
{

  /**
   * Creates a new configuration model to know, what a sattelite has to do
   *
   * @param pUserID      ID of the user
   * @param pSatelliteID ID of the satellite
   * @return the configuration model for the satellite
   */
  @NotNull
  SatelliteConfigurationDataModel create(@NotNull String pUserID, @NotNull String pSatelliteID);

}
