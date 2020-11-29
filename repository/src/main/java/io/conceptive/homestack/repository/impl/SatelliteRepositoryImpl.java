package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.satellite.LeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author w.glanzer, 29.11.2020
 */
@ApplicationScoped
class SatelliteRepositoryImpl implements ISatelliteRepository
{

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<LeaseDataModel> findAll(@NotNull String pUserID)
  {
    return Sets.newHashSet(_getCollection().find(Filters.eq("userID", pUserID)));
  }

  @Nullable
  @Override
  public LeaseDataModel findByID(@NotNull String pLeaseID)
  {
    return _getCollection().find(Filters.eq("_id", pLeaseID)).first();
  }

  @Override
  public void upsertLease(@NotNull LeaseDataModel pLease)
  {
    _getCollection().replaceOne(Filters.eq("_id", pLease.id), pLease, AbstractRepository.UPSERT);
  }

  @NotNull
  @Override
  public LeaseDataModel generateLease(@NotNull String pUserID)
  {
    LeaseDataModel lease = new LeaseDataModel();
    lease.userID = pUserID;
    lease.id = RandomStringUtils.randomAlphanumeric(64);
    lease.token = RandomStringUtils.randomAlphanumeric(32);
    upsertLease(lease);
    return lease;
  }

  @NotNull
  private MongoCollection<LeaseDataModel> _getCollection()
  {
    return mongoClient.getDatabase("_____SYSTEM")
        .getCollection("satellite_leases", LeaseDataModel.class);
  }

}
