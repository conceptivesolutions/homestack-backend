package io.conceptive.homestack.backend.repository.api.system;

import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import org.jetbrains.annotations.*;

/**
 * @author w.glanzer, 06.12.2020
 */
public interface ISatelliteSystemRepository
{

  /**
   * Returns the satellite with the given id
   *
   * @param pUser ID of the user
   * @param pID   ID of the satellite
   * @return the satellite, or NULL if not found
   */
  @Nullable
  SatelliteDataModel findByID(@NotNull String pUser, @NotNull String pID);

}
