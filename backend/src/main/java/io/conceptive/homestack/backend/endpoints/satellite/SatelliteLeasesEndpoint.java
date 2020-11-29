package io.conceptive.homestack.backend.endpoints.satellite;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.satellite.LeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

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
  protected ISatelliteRepository satelliteRepository;

  /**
   * Generates a new lease for a satellite to connect
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public LeaseDataModel generateLease()
  {
    return satelliteRepository.generateLease(token.getSubject());
  }

  /**
   * Returns all valid leases (without token) for the current user
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<LeaseDataModel> get()
  {
    return satelliteRepository.findAll(token.getSubject()).stream()
        .filter(pModel -> pModel.revokedDate == null)
        .peek(pModel -> pModel.token = null) // token should definitely not be publicly present
        .collect(Collectors.toSet());
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
    LeaseDataModel lease = satelliteRepository.findByID(pLeaseID);

    // found?
    if (lease == null)
      throw new NotFoundException();

    // already revoked?
    if (lease.revokedDate != null)
      return Response.notModified().build();

    lease.revokedDate = Instant.now();
    satelliteRepository.upsertLease(lease);

    // ok, revoked
    return Response.ok().build();
  }

}
