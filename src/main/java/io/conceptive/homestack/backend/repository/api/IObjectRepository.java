package io.conceptive.homestack.backend.repository.api;

import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * Repository that contains objects of a specific type
 *
 * @author w.glanzer, 06.12.2020
 */
public interface IObjectRepository<S>
{

  /**
   * @return all currently known objects
   */
  @NotNull
  Set<S> findAll();

  /**
   * Returns the object with the given id
   *
   * @param pID ID of the object
   * @return the object, or NULL if not found
   */
  @Nullable
  S findByID(@NotNull String pID);

  /**
   * Inserts / Updates the given object
   *
   * @param pModel object with updated data (identified by id)
   */
  void upsert(@NotNull S pModel);

  /**
   * Deletes the object with the given id
   *
   * @param pID ID of the object to delete
   * @return true, if deleted
   */
  boolean deleteByID(@NotNull String pID);

}
