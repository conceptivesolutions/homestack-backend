package io.conceptive.homestack.backend.repository.impl.system;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.backend.repository.api.system.ISatelliteLeaseSystemRepository;
import io.conceptive.homestack.model.data.satellite.SatelliteLeaseDataModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

/**
 * @author w.glanzer, 29.11.2020
 */
@ApplicationScoped
class SatelliteLeaseSystemRepositoryImpl extends AbstractSystemRepository<SatelliteLeaseDataModel> implements ISatelliteLeaseSystemRepository
{

  @NotNull
  @Override
  public Set<SatelliteLeaseDataModel> findAll(@NotNull String pUserID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("userID", pUserID)));
  }

  @Nullable
  @Override
  public SatelliteLeaseDataModel findByID(@NotNull String pLeaseID)
  {
    return getCollection().find(Filters.eq("_id", pLeaseID)).first();
  }

  @Override
  public void upsertLease(@NotNull SatelliteLeaseDataModel pLease)
  {
    getCollection().replaceOne(Filters.eq("_id", pLease.id), pLease, UPSERT);
  }

  @NotNull
  @Override
  public SatelliteLeaseDataModel generateLease(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    SatelliteLeaseDataModel lease = new SatelliteLeaseDataModel();
    lease.userID = pUserID;
    lease.satelliteID = pSatelliteID;
    lease.id = RandomStringUtils.randomAlphanumeric(32);
    lease.token = RandomStringUtils.randomAlphanumeric(192);
    upsertLease(lease);
    return lease;
  }

  @Override
  public void deleteBySatelliteID(@NotNull String pUserID, @NotNull String pSatelliteID)
  {
    getCollection().deleteMany(Filters.and(Filters.eq("userID", pUserID), Filters.eq("satelliteID", pSatelliteID)));
  }

  @NotNull
  @Override
  protected Class<SatelliteLeaseDataModel> getCollectionType()
  {
    return SatelliteLeaseDataModel.class;
  }

}
