package de.homestack.backend.database.system;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.*;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface ISatelliteLeaseSystemDBRepository
{

  /**
   * Registers a new lease id for the given user and satellite
   *
   * @param pUserID      ID of the user
   * @param pSatelliteID ID of the satellite
   * @param pLeaseID     ID of the lease
   */
  void registerLease(@NotNull String pUserID, @NotNull String pSatelliteID, @NotNull String pLeaseID);

  /**
   * Tries to find the appropriate user and sallite id for the given lease
   *
   * @param pLeaseID ID of the lease to search
   * @return the lease
   */
  @Nullable
  Pair<String, String> getUserAndSatelliteIDByLeaseID(@NotNull String pLeaseID);

}
