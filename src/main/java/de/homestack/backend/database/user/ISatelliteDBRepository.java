package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface ISatelliteDBRepository
{

  /**
   * Retrieves all satellites of a single stack
   *
   * @param pUserID  user to query
   * @param pStackID stack to query
   * @return the satellites
   */
  @NotNull
  List<SatelliteDataModel> getSatellitesByStackID(@NotNull String pUserID, @NotNull String pStackID);

  /**
   * Returns a single satellite by id
   *
   * @param pUserID      user to query
   * @param pStackID     stack to query
   * @param pSatelliteID id of the satellite to search
   * @return the model, or null if not found
   */
  @Nullable
  SatelliteDataModel getSatelliteByID(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pSatelliteID);

  /**
   * Inserts / Updates the given satellite
   *
   * @param pUserID user to query
   * @param pModel  model to insert / update
   * @return the updated model
   */
  @NotNull
  SatelliteDataModel upsertSatellite(@NotNull String pUserID, @NotNull SatelliteDataModel pModel);

  /**
   * Deletes the given satellite
   *
   * @param pUserID      user to query
   * @param pStackID     stack to query
   * @param pSatelliteID id to delete
   * @return true, if something was deleted
   */
  boolean deleteSatellite(@NotNull String pUserID, @NotNull String pStackID, @NotNull String pSatelliteID);

}
