package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteLeaseRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author w.glanzer, 29.11.2020
 */
@ApplicationScoped
class SatelliteLeaseRepositoryImpl implements ISatelliteLeaseRepository
{

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<SatelliteLeaseDataModel> findAll(@NotNull String pUserID)
  {
    return Sets.newHashSet(_getCollection().find(Filters.eq("userID", pUserID)));
  }

  @Nullable
  @Override
  public SatelliteLeaseDataModel findByID(@NotNull String pLeaseID)
  {
    return _getCollection().find(Filters.eq("_id", pLeaseID)).first();
  }

  @Override
  public void upsertLease(@NotNull SatelliteLeaseDataModel pLease)
  {
    _getCollection().replaceOne(Filters.eq("_id", pLease.id), pLease, AbstractRepository.UPSERT);
  }

  @NotNull
  @Override
  public SatelliteLeaseDataModel generateLease(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    SatelliteLeaseDataModel lease = new SatelliteLeaseDataModel();
    lease.userID = pUserID;
    lease.satelliteID = pSatelliteID;
    lease.id = RandomStringUtils.randomAlphanumeric(64);
    lease.token = RandomStringUtils.randomAlphanumeric(32);
    upsertLease(lease);
    return lease;
  }

  @Override
  public void deleteBySatelliteID(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    _getCollection().deleteMany(Filters.and(Filters.eq("userID", pUserID), Filters.eq("satelliteID", pSatelliteID)));
  }

  @NotNull
  private MongoCollection<SatelliteLeaseDataModel> _getCollection()
  {
    return mongoClient.getDatabase("_____SYSTEM")
        .getCollection("satellite_leases", SatelliteLeaseDataModel.class);
  }

}
