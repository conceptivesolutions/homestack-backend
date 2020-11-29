package io.conceptive.homestack.backend.satellite.auth;

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
   * @param pSatelliteID ID of the satellite to authenticate
   * @param pToken       The token
   * @throws AuthenticationFailedException if the authentication and token verification failed
   */
  void authenticate(@NotNull String pSatelliteID, @NotNull String pToken) throws AuthenticationFailedException;

}
