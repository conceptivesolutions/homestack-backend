package io.conceptive.homestack.satellite.auth;

import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteLeaseSystemRepository;
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
  protected ISatelliteLeaseSystemRepository satelliteLeaseRepository;

  @NotNull
  @Override
  public String authenticate(@NotNull String pLeaseID, @NotNull String pLeaseToken) throws AuthenticationFailedException
  {
    SatelliteLeaseDataModel lease = satelliteLeaseRepository.findByID(pLeaseID);

    // No Lease found
    if (lease == null)
      throw new AuthenticationFailedException("No valid lease found (" + pLeaseID + ")");

    // Already revoked by user
    if (lease.revokedDate != null && lease.revokedDate.isAfter(Instant.now()))
      throw new AuthenticationFailedException("Lease was revoked (" + pLeaseID + ")");

    // Check Token
    if (!Objects.equals(lease.token, pLeaseToken))
      throw new AuthenticationFailedException("Valid lease found, but tokens do not match (" + pLeaseID + ")");

    return lease.satelliteID;
  }

}
