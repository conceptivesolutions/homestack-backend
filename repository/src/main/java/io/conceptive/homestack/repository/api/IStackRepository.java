package io.conceptive.homestack.repository.api;

import io.conceptive.homestack.model.data.StackDataModel;
import org.jetbrains.annotations.*;

import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
public interface IStackRepository
{

  /**
   * Returns all currently known stacks
   *
   * @return all stacks
   */
  @NotNull
  Set<StackDataModel> findAll();

  /**
   * Tries to find the stack by the given id
   *
   * @param pStackID ID of the stack to search for
   * @return Stack or NULL if not found
   */
  @Nullable
  StackDataModel findByID(@NotNull String pStackID);

  /**
   * Inserts a single stack into the repository
   *
   * @param pStack Stack to insert
   */
  void insert(@NotNull StackDataModel pStack);

  /**
   * Updates a single stack from the repository
   *
   * @param pStack Stack to update
   */
  void update(@NotNull StackDataModel pStack);

  /**
   * Deletes the stack (if any) specified by given id
   *
   * @param pStackID ID of the stack to be deleted
   * @return true, if stack was deleted
   */
  boolean deleteByID(@NotNull String pStackID);

}
