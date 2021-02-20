package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface ISatelliteLeaseDBRepository
{

  /**
   * Retrieves all leases of a single satellite
   *
   * @param pUserID      user to query
   * @param pSatelliteID satellite to query
   * @return the leases
   */
  @NotNull
  List<SatelliteLeaseDataModel> getLeasesBySatelliteID(@NotNull String pUserID, @NotNull String pSatelliteID);

  /**
   * Returns a single lease by id
   *
   * @param pUserID      user to query
   * @param pSatelliteID satellite to query
   * @param pLeaseID     id of the lease to search
   * @return the model, or null if not found
   */
  @Nullable
  SatelliteLeaseDataModel getLeaseByID(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID);

  /**
   * Inserts a new lease
   *
   * @param pUserID      user to query
   * @param pSatelliteID ID of the satellite to create the lease for
   * @return the created model
   */
  @NotNull
  SatelliteLeaseDataModel createLease(@NotNull String pUserID, @NotNull String pSatelliteID);

  /**
   * Revokes the given lease
   *
   * @param pUserID      user to query
   * @param pSatelliteID satellite to query
   * @param pLeaseID     id to revoke
   * @return the lease
   */
  @Nullable
  SatelliteLeaseDataModel revokeLease(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID, @NotNull Date pRevokeDate);

}
