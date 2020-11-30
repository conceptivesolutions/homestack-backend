package io.conceptive.homestack.backend.endpoints.satellite;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteLeaseRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Instant;

/**
 * Endpoint to retrieve / create leases for satellites
 *
 * @author w.glanzer, 29.11.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/satellites/leases")
public class SatelliteLeasesEndpoint
{

  @Inject
  protected JsonWebToken token;

  @Inject
  protected ISatelliteLeaseRepository satelliteLeaseRepository;

  /**
   * Generates a new lease for a satellite to connect
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public SatelliteLeaseDataModel generateLease()
  {
    return satelliteLeaseRepository.generateLease(token.getSubject());
  }

  /**
   * Revokes the token with the given ID
   *
   * @param pLeaseID ID of the id to revoke
   */
  @DELETE
  @Path("/{id}")
  public Response revoke(@NotNull @PathParam("id") String pLeaseID)
  {
    SatelliteLeaseDataModel lease = satelliteLeaseRepository.findByID(pLeaseID);

    // found?
    if (lease == null)
      throw new NotFoundException();

    // already revoked?
    if (lease.revokedDate != null)
      return Response.notModified().build();

    lease.revokedDate = Instant.now();
    satelliteLeaseRepository.upsertLease(lease);

    // ok, revoked
    return Response.ok().build();
  }

}
