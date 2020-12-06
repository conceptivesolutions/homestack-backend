package io.conceptive.homestack.backend.endpoints.satellite;

import io.conceptive.homestack.backend.rbac.IRole;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.system.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Endpoint to retrieve information about satellites
 *
 * @author w.glanzer, 30.11.2020
 */
@RolesAllowed(IRole.DEFAULT)
@Path("/satellites")
public class SatelliteEndpoint
{

  @Inject
  protected JsonWebToken token;

  @Inject
  protected ISatelliteRepository satelliteRepository;

  @Inject
  protected ISatelliteLeaseRepository satelliteLeaseRepository;

  /**
   * Returns all currently known satellites for the user
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<SatelliteDataModel> get(@QueryParam("stack") @Nullable String pStackID)
  {
    if (pStackID == null || pStackID.isBlank())
      return satelliteRepository.findAll();
    return satelliteRepository.findByStackID(pStackID);
  }

  /**
   * Searches the satellite with the given id
   *
   * @param pID ID of the satellite
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public SatelliteDataModel getByID(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    SatelliteDataModel satellite = satelliteRepository.findByID(pID);
    if (satellite == null)
      throw new NotFoundException();

    return satellite;
  }

  /**
   * Update a satellite in the repository
   *
   * @param pID    ID of the satellite to update
   * @param pModel Satellite with the updated values
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public SatelliteDataModel put(@PathParam("id") String pID, @Nullable SatelliteDataModel pModel)
  {
    if (pID == null || pID.isBlank() || pModel == null)
      throw new BadRequestException();

    // force IDs to be set equally
    pModel.id = pID;

    // upsert
    satelliteRepository.upsert(pModel);
    return pModel;
  }

  /**
   * Deletes a satellite with the given id
   *
   * @param pID ID to search for
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public void delete(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    // delete leases anyway
    satelliteLeaseRepository.deleteBySatelliteID(token.getSubject(), pID);

    boolean deleted = satelliteRepository.delete(pID);
    if (!deleted)
      throw new NotFoundException();
  }

}
