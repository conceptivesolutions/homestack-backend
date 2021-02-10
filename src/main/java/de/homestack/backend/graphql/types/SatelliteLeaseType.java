package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.*;
import org.jetbrains.annotations.*;

import java.util.Date;

/**
 * @author w.glanzer, 10.02.2021
 * @see io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel
 */
@Type("Lease")
public class SatelliteLeaseType
{
  /**
   * ID of the lease
   */
  @Id
  @NonNull
  public String id;

  /**
   * ID of the user, who owns this lease
   */
  public String userID;

  /**
   * Date, when this lease was revoked by user
   */
  public Date revokedDate;

  /**
   * Token for the satellite
   */
  public String token;

  @NotNull
  public SatelliteLeaseType id(@NotNull String pId)
  {
    id = pId;
    return this;
  }

  @NotNull
  public SatelliteLeaseType userID(@Nullable String pUserID)
  {
    userID = pUserID;
    return this;
  }

  @NotNull
  public SatelliteLeaseType revokedDate(@Nullable Date pRevokedDate)
  {
    revokedDate = pRevokedDate;
    return this;
  }

  @NotNull
  public SatelliteLeaseType token(@Nullable String pToken)
  {
    token = pToken;
    return this;
  }
}
