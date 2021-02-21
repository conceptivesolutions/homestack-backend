package de.homestack.backend.graphql.types;

import lombok.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.*;

import java.util.Date;

/**
 * @author w.glanzer, 10.02.2021
 */
@Type("Lease")
@NoArgsConstructor
@AllArgsConstructor
public class GQLSatelliteLease
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

}
