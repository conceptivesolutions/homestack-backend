package io.conceptive.homestack.backend.endpoints.satellite;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.backend.repository.api.system.ISatelliteLeaseSystemRepository;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Endpoint to retrieve / create leases for satellites
 *
 * @author w.glanzer, 29.11.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/satellites/{id}/leases")
public class SatelliteLeasesEndpoint
{

  @Inject
  protected JsonWebToken token;

  @Inject
  protected ISatelliteLeaseSystemRepository satelliteLeaseRepository;

  /**
   * Returns all currently known leases for this satellite,
   * including ones that were revoked.
   * Does not include important data, such as token.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<SatelliteLeaseDataModel> getLeaseIDs(@NotNull @PathParam("id") String pSatelliteID)
  {
    return satelliteLeaseRepository.findAll(token.getSubject()).stream()
        .filter(pLease -> Objects.equals(pSatelliteID, pLease.satelliteID))
        .peek(pModel -> pModel.token = null)
        .collect(Collectors.toSet());
  }

  /**
   * Generates a new lease for a satellite to connect
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public SatelliteLeaseDataModel generateLease(@NotNull @PathParam("id") String pSatelliteID)
  {
    return satelliteLeaseRepository.generateLease(token.getSubject(), pSatelliteID);
  }

  /**
   * Revokes the token with the given ID
   *
   * @param pLeaseID ID of the id to revoke
   */
  @DELETE
  @Path("/{leaseID}")
  public Response revoke(@NotNull @PathParam("id") String pSatelliteID, @NotNull @PathParam("leaseID") String pLeaseID)
  {
    SatelliteLeaseDataModel lease = satelliteLeaseRepository.findByID(pLeaseID);

    // found?
    if (lease == null || !Objects.equals(lease.satelliteID, pSatelliteID))
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
