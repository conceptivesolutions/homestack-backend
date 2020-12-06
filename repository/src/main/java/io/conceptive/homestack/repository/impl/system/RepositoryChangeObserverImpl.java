package io.conceptive.homestack.repository.impl.system;

import com.mongodb.client.*;
import io.conceptive.homestack.repository.api.system.IRepositoryChangeObserver;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class RepositoryChangeObserverImpl implements IRepositoryChangeObserver
{

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Observable<Long> observeChangesForUser(@NotNull String pUserID)
  {
    ChangeStreamIterable<Document> iter = mongoClient.getDatabase(pUserID).watch();
    return Observable.fromIterable(iter)

        // close on dispose
        .doOnDispose(() -> iter.cursor().close())

        // subscribe on a different thread, so it wont block until cursors closes
        .subscribeOn(Schedulers.io())

        // map to milliseconds and throttle, so that the satellite wont be overflooded with events
        .map(pDoc -> System.currentTimeMillis())
        .throttleFirst(500, TimeUnit.MILLISECONDS);
  }

}
