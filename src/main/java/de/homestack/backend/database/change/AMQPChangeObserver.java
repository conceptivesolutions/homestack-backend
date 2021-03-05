package de.homestack.backend.database.change;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Change-Observer with amqp features, to allow inter-server-communication
 *
 * @author w.glanzer, 04.03.2021
 */
@ApplicationScoped
class AMQPChangeObserver implements IRepositoryChangeObserver
{

  private final PublishSubject<String> changes = PublishSubject.create();

  @Inject
  @Channel("fire-userdata-changed")
  protected Emitter<String> changeEmitter;

  @NotNull
  @Override
  public Observable<Long> observeChangesForUser(@NotNull String pUserID)
  {
    return changes
        .filter(pUser -> Objects.equals(pUser, pUserID))
        .map(pUser -> System.currentTimeMillis());
  }

  @Override
  @Metered(name = "firedUserChanges", description = "meters how much changes a backend propagates to amqp server", absolute = true)
  public void fireChangeForUser(@NotNull String pUserID)
  {
    changeEmitter.send(pUserID);
  }

  /**
   * Gets called, if data of a single user changed
   *
   * @param pUserID ID of the user, whos data changed
   */
  @Incoming("userdata-changed")
  protected void onChangeForUser(@NotNull String pUserID)
  {
    changes.onNext(pUserID);
  }

}
