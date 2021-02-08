package io.conceptive.homestack.backend.repository.api.system;

import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * Contains all information about satellites
 *
 * @author w.glanzer, 29.11.2020
 */
public interface ISatelliteLeaseSystemRepository
{

  /**
   * Returns all leases for the given user.
   * Including ones, that were not authorized by user.
   *
   * @param pUserID ID of the user
   * @return the leases for the user and its satellites
   */
  @NotNull
  Set<SatelliteLeaseDataModel> findAll(@NotNull String pUserID);

  /**
   * Searches a lease with the given ID
   *
   * @param pLeaseID ID of the lease
   * @return the lease, or null if not found
   */
  @Nullable
  SatelliteLeaseDataModel findByID(@NotNull String pLeaseID);

  /**
   * Inserts or updates the lease
   *
   * @param pLease given lease to be updated / inserted (determined by id)
   */
  void upsertLease(@NotNull SatelliteLeaseDataModel pLease);

  /**
   * Generates a new lease for a single satellite
   *
   * @param pUserID ID of the user
   * @return a new lease to use
   */
  @NotNull
  SatelliteLeaseDataModel generateLease(@NotNull String pUserID, @NotNull String pSatelliteID);

  /**
   * Deletes all leases with the given satellite id for the given user
   *
   * @param pUserID      ID of the user
   * @param pSatelliteID ID of the satellite to delete the leases for
   */
  void deleteBySatelliteID(@NotNull String pUserID, @NotNull String pSatelliteID);

}
