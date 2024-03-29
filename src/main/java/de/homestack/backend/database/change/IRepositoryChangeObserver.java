package de.homestack.backend.database.change;

import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

/**
 * Observer that fires, if a repository for a user changes
 *
 * @author w.glanzer, 06.12.2020
 */
public interface IRepositoryChangeObserver
{

  /**
   * Observes changes for a user
   *
   * @param pUserID ID of the user
   * @return Observable with the timestamp of the last change
   */
  @NotNull
  Observable<Long> observeChangesForUser(@NotNull String pUserID);

  /**
   * Fires, that the repository of a user changed
   *
   * @param pUserID ID of the user
   */
  void fireChangeForUser(@NotNull String pUserID);

}
