package de.homestack.backend.satellite.auth;

import de.homestack.backend.database.system.ISatelliteLeaseSystemDBRepository;
import de.homestack.backend.database.user.ISatelliteLeaseDBRepository;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.quarkus.security.AuthenticationFailedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * @author w.glanzer, 20.02.2021
 */
@ApplicationScoped
class SatelliteAuthenticator implements ISatelliteAuthenticator
{

  @Inject
  protected ISatelliteLeaseSystemDBRepository satelliteLeaseSystemRepository;

  @Inject
  protected ISatelliteLeaseDBRepository satelliteLeaseRepository;

  @NotNull
  @Override
  public SatelliteLeaseDataModel authenticate(@NotNull String pLeaseID, @NotNull String pLeaseToken) throws AuthenticationFailedException
  {
    Pair<String, String> userAndSatelliteIDByLeaseID = satelliteLeaseSystemRepository.getUserAndSatelliteIDByLeaseID(pLeaseID);

    // no user / satellite id found
    if (userAndSatelliteIDByLeaseID == null)
      throw new AuthenticationFailedException("No valid lease found (" + pLeaseID + ")");

    String userID = userAndSatelliteIDByLeaseID.getKey();
    String satelliteID = userAndSatelliteIDByLeaseID.getValue();
    SatelliteLeaseDataModel lease = satelliteLeaseRepository.getLeaseByID(userID, satelliteID, pLeaseID);

    // No Lease found
    if (lease == null)
      throw new AuthenticationFailedException("No valid lease found (" + pLeaseID + ")");

    // Already revoked by user
    if (lease.revokedDate != null && lease.revokedDate.after(new Date()))
      throw new AuthenticationFailedException("Lease was revoked (" + pLeaseID + ")");

    // Check Token
    if (!Objects.equals(lease.token, pLeaseToken))
      throw new AuthenticationFailedException("Valid lease found, but tokens do not match (" + pLeaseID + ")");

    return lease;
  }

}
