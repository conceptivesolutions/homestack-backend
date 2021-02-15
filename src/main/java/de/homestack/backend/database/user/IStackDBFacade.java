package de.homestack.backend.database.user;

import io.conceptive.homestack.model.data.StackDataModel;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * DB Facade that contains all operations for stacks of a single user
 *
 * @author w.glanzer, 10.02.2021
 */
public interface IStackDBFacade
{

  /**
   * Retrieve all stacks from the database
   *
   * @param pUserID user to query
   * @return the stacks
   */
  @NotNull
  List<StackDataModel> getStacks(@NotNull String pUserID);

  /**
   * Returns a single stack by id
   *
   * @param pUserID  user to query
   * @param pStackID id of the stack to search
   * @return the stack, or null if not found
   */
  @Nullable
  StackDataModel getStackByID(@NotNull String pUserID, @NotNull String pStackID);

  /**
   * Inserts / Updates the given stack
   *
   * @param pUserID user to query
   * @param pStack  Stack to insert / update
   * @return the updated model
   */
  @NotNull
  StackDataModel upsertStack(@NotNull String pUserID, @NotNull StackDataModel pStack);

  /**
   * Deletes the given stack id
   *
   * @param pUserID  user to query
   * @param pStackID ID to delete
   * @return true, if something was deleted
   */
  boolean deleteStack(@NotNull String pUserID, @NotNull String pStackID);

}
