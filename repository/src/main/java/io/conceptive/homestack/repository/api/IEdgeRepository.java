package io.conceptive.homestack.repository.api;

import io.conceptive.homestack.model.data.EdgeDataModel;
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
  Set<EdgeDataModel> findAll(@NotNull String pDeviceID);

  /**
   * Adds a new edge between two devices
   *
   * @param pSourceID ID of the source device
   * @param pTargetID ID of the target device
   * @return Edge that was added
   */
  @NotNull
  EdgeDataModel insert(@NotNull String pSourceID, @NotNull String pTargetID);

  /**
   * Removes the edge between pSourceID and pTargetID
   *
   * @param pSourceID ID of the source device
   * @param pTargetID ID of the target device
   * @return true, if edge was removed - false, if it did not exist
   */
  boolean delete(@NotNull String pSourceID, @NotNull String pTargetID);

}
