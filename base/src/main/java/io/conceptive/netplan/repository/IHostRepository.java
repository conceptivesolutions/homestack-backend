package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Host;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
public interface IHostRepository
{

  /**
   * Returns all currently known hosts
   *
   * @return all hosts
   */
  @NotNull
  Set<Host> findAll();

  /**
   * Tries to find the host by the given id
   *
   * @param pHostID ID of the host to search for
   * @return Host or NULL if not found
   */
  @Nullable
  Host findHostByID(@NotNull String pHostID);

  /**
   * Inserts a single host into the repository
   *
   * @param pHost Host to insert
   */
  void insertHost(@NotNull Host pHost);

  /**
   * Updates a single host from the repository
   *
   * @param pHost Host to update
   */
  void updateHost(@NotNull Host pHost);

  /**
   * Deletes the host (if any) specified by given id
   *
   * @param pHostID ID of the host to be deleted
   * @return true, if host was deleted
   */
  boolean deleteHost(@NotNull String pHostID);

}
