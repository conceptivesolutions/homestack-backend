package io.conceptive.homestack.repository.api.system;

import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 30.11.2020
 */
public interface ISatelliteRepository
{

  /**
   * Returns all currently known satellites for the current user
   *
   * @return all satellites
   */
  @NotNull
  Set<SatelliteDataModel> findAll();

  /**
   * Returns all currently known satellites for the current user for the given host
   *
   * @param pHostID ID of the host
   * @return all satellites
   */
  @NotNull
  Set<SatelliteDataModel> findByHostID(@NotNull String pHostID);

  /**
   * Tries to find the satellite by the given id for the current user
   *
   * @param pSatelliteID ID of the satellite to search for
   * @return the satellite or null if not found
   */
  @Nullable
  SatelliteDataModel findByID(@NotNull String pSatelliteID);

  /**
   * Inserts / Updates the given satellite
   *
   * @param pModel satellite with updated data (identified by id)
   */
  void upsert(@NotNull SatelliteDataModel pModel);

  /**
   * Deletes the satellite with the given id
   *
   * @param pSatelliteID ID of the satellite to delete
   * @return true, if deleted
   */
  boolean delete(@NotNull String pSatelliteID);

}
