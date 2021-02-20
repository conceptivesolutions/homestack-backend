package de.homestack.backend.satellite.config;

import io.conceptive.homestack.model.satellite.SatelliteConfigurationDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface ISatelliteConfigFactory
{

  /**
   * Creates a new configuration model to know, what a sattelite has to do
   *
   * @param pUserID ID of the user
   * @return the configuration model for the satellite
   */
  @NotNull
  SatelliteConfigurationDataModel create(@NotNull String pUserID);

}
