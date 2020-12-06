package io.conceptive.homestack.repository.impl.user.object;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.satellite.SatelliteDataModel;
import io.conceptive.homestack.repository.api.user.ISatelliteUserRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * @author w.glanzer, 30.11.2020
 */
@Dependent
class SatelliteUserRepository extends AbstractObjectUserRepository<SatelliteDataModel> implements ISatelliteUserRepository
{

  @NotNull
  @Override
  public Set<SatelliteDataModel> findByStackID(@NotNull String pStackID)
  {
    return Sets.newHashSet(getCollection().find(Filters.eq("stackID", pStackID)));
  }

  @NotNull
  @Override
  protected Class<SatelliteDataModel> getCollectionType()
  {
    return SatelliteDataModel.class;
  }

  @Nullable
  @Override
  protected String getID(@NotNull SatelliteDataModel pModel)
  {
    return pModel.id;
  }

}
