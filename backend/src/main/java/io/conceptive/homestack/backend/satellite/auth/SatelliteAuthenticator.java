package io.conceptive.homestack.backend.satellite.auth;

import io.conceptive.homestack.model.data.satellite.LeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteRepository;
import io.quarkus.security.AuthenticationFailedException;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Objects;

/**
 * @author w.glanzer, 16.11.2020
 */
@ApplicationScoped
class SatelliteAuthenticator implements ISatelliteAuthenticator
{

  @Inject
  protected ISatelliteRepository satelliteRepository;

  @Override
  public void authenticate(@NotNull String pSatelliteID, @NotNull String pToken) throws AuthenticationFailedException
  {
    LeaseDataModel lease = satelliteRepository.findByID(pSatelliteID);

    // No Lease found
    if (lease == null)
      throw new AuthenticationFailedException("No valid lease found (" + pSatelliteID + ")");

    // Already revoked by user
    if (lease.revokedDate != null && lease.revokedDate.isAfter(Instant.now()))
      throw new AuthenticationFailedException("Lease was revoked (" + pSatelliteID + ")");

    // Check Token
    if (!Objects.equals(lease.token, pToken))
      throw new AuthenticationFailedException("Valid lease found, but tokens do not match (" + pSatelliteID + ")");
  }

}
