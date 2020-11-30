package io.conceptive.homestack.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 30.11.2020
 */
@Dependent
class SatelliteRepository extends AbstractRepository<SatelliteDataModel> implements ISatelliteRepository
{

  @NotNull
  @Override
  public Set<SatelliteDataModel> findAll()
  {
    return Sets.newHashSet(getCollection().find());
  }

  @NotNull
  @Override
  public Set<SatelliteDataModel> findByHostID(@NotNull String pHostID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("hostID", pHostID)));
  }

  @Nullable
  @Override
  public SatelliteDataModel findByID(@NotNull String pSatelliteID)
  {
    return getCollection().find(Filters.eq("_id", pSatelliteID)).first();
  }

  @Override
  public void upsert(@NotNull SatelliteDataModel pModel)
  {
    getCollection().replaceOne(Filters.eq("_id", pModel.id), pModel, UPSERT);
  }

  @Override
  public boolean delete(@NotNull String pSatelliteID)
  {
    return getCollection().deleteMany(Filters.eq("_id", pSatelliteID)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<SatelliteDataModel> getCollectionType()
  {
    return SatelliteDataModel.class;
  }

}
