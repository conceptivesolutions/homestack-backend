package io.conceptive.netplan.backend.endpoints;

import io.conceptive.netplan.backend.rbac.IRole;
import io.conceptive.netplan.model.data.HostDataModel;
import io.conceptive.netplan.repository.api.IHostRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
@Path("/hosts")
@RolesAllowed(IRole.DEFAULT)
public class HostEndpoint
{

  @Inject
  protected IHostRepository hostRepository;

  /**
   * Returns all available hosts
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<HostDataModel> get()
  {
    return hostRepository.findAll();
  }

  /**
   * Searches the host with the given id
   *
   * @param pID ID to search for
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public HostDataModel getByID(@PathParam("id") String pID)
  {
    if (pID == null || pID.isBlank())
      throw new BadRequestException();

    HostDataModel host = hostRepository.findHostByID(pID);
    if (host == null)
      throw new NotFoundException();

    return host;
  }

  /**
   * Creates a new host in the repository
   *
   * @param pID   ID of the host to insert
   * @param pHost Host
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public HostDataModel put(@PathParam("id") String pID, @Nullable HostDataModel pHost)
  {
    if (pID == null || pID.isBlank() || pHost == null)
      throw new BadRequestException();

    // force IDs to be set and "correct"
    pHost.id = pID;

    // insert
    hostRepository.insertHost(pHost);
    return pHost;
  }

  /**
   * Update a host in the repository
   *
   * @param pID   ID of the device to update
   * @param pHost Host
   */
  @PATCH
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public HostDataModel patch(@PathParam("id") String pID, @Nullable HostDataModel pHost)
  {
    if (pID == null || pID.isBlank() || pHost == null)
      throw new BadRequestException();

    HostDataModel host = hostRepository.findHostByID(pHost.id);
    if (host == null)
      throw new NotFoundException();

    // Update Data
    host.displayName = ObjectUtils.firstNonNull(pHost.displayName, host.displayName);
    hostRepository.updateHost(host);
    return host;
  }

  /**
   * Deletes a host with the given id
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

    boolean deleted = hostRepository.deleteHost(pID);
    if (!deleted)
      throw new NotFoundException();
  }

}