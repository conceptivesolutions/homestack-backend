package io.conceptive.homestack.repository.api.user;

import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.IObjectRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 30.11.2020
 */
public interface ISatelliteUserRepository extends IObjectRepository<SatelliteDataModel>
{

  /**
   * Returns all currently known satellites for the current user for the given stack
   *
   * @param pStackID ID of the stack
   * @return all satellites
   */
  @NotNull
  Set<SatelliteDataModel> findByStackID(@NotNull String pStackID);

}
