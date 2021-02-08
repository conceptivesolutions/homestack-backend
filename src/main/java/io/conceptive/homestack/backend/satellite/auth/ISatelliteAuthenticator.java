package io.conceptive.homestack.backend.satellite.auth;

import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.quarkus.security.AuthenticationFailedException;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 28.11.2020
 */
public interface ISatelliteAuthenticator
{

  /**
   * Tries to authenticate the satellite with the given ID and the given token
   *
   * @param pLeaseID    ID of the lease to authenticate with
   * @param pLeaseToken The token
   * @return the lease that authenticated successfully
   * @throws AuthenticationFailedException if the authentication and token verification failed
   */
  @NotNull
  SatelliteLeaseDataModel authenticate(@NotNull String pLeaseID, @NotNull String pLeaseToken) throws AuthenticationFailedException;

}
