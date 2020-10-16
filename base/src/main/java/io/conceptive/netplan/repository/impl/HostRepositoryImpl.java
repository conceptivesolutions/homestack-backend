package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import io.conceptive.netplan.IDBConstants;
import io.conceptive.netplan.core.model.Host;
import io.conceptive.netplan.repository.IHostRepository;
import org.jetbrains.annotations.*;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
public class HostRepositoryImpl implements IHostRepository
{

  private static final ReplaceOptions _UPSERT = new ReplaceOptions().upsert(true);

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<Host> findAll()
  {
    return Sets.newHashSet(_getCollection().find());
  }

  @Nullable
  @Override
  public Host findHostByID(@NotNull String pHostID)
  {
    return _getCollection().find(Filters.eq("_id", pHostID), Host.class).first();
  }

  @Override
  public void insertHost(@NotNull Host pHost)
  {
    _getCollection().insertOne(pHost);
  }

  @Override
  public void updateHost(@NotNull Host pHost)
  {
    _getCollection().replaceOne(Filters.eq("_id", pHost.id), pHost, _UPSERT);
  }

  @Override
  public boolean deleteHost(@NotNull String pHostID)
  {
    return _getCollection().deleteOne(Filters.eq("_id", pHostID)).getDeletedCount() > 0;
  }

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  private MongoCollection<Host> _getCollection()
  {
    return mongoClient
        .getDatabase(IDBConstants.DB_NAME)
        .getCollection("hosts", Host.class);
  }
}
