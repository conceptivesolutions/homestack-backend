package io.conceptive.homestack.repository.impl.system;

import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.system.ISatelliteSystemRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author w.glanzer, 06.12.2020
 */
@ApplicationScoped
class SatelliteSystemRepositoryImpl extends AbstractSystemRepository<SatelliteDataModel> implements ISatelliteSystemRepository
{

  @Nullable
  @Override
  public SatelliteDataModel findByID(@NotNull String pUserID, @NotNull String pID)
  {
    return getCollectionForUser(pUserID).find(Filters.eq("_id", pID)).first();
  }

  @NotNull
  @Override
  protected Class<SatelliteDataModel> getCollectionType()
  {
    return SatelliteDataModel.class;
  }

}
