package io.conceptive.netplan.repository;

import io.conceptive.netplan.core.model.Edge;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 12.10.2020
 */
public interface IEdgeRepository
{

  /**
   * Searches all edges of a given device
   *
   * @param pDeviceID ID of the device to search for
   * @return all edges
   */
  @NotNull
  Set<Edge> findAll(@NotNull String pDeviceID);

  /**
   * Adds a new edge between two devices
   *
   * @param pSourceID ID of the source device
   * @param pTargetID ID of the target device
   * @return Edge that was added
   */
  @NotNull
  Edge addEdge(@NotNull String pSourceID, @NotNull String pTargetID);

  /**
   * Removes the edge between pSourceID and pTargetID
   *
   * @param pSourceID ID of the source device
   * @param pTargetID ID of the target device
   * @return true, if edge was removed - false, if it did not exist
   */
  boolean removeEdge(@NotNull String pSourceID, @NotNull String pTargetID);

}
