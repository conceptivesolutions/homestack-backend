package io.conceptive.homestack.repository.impl.user;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.homestack.model.data.EdgeDataModel;
import io.conceptive.homestack.repository.api.user.IEdgeUserRepository;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
class EdgeUserRepositoryImpl extends AbstractUserRepository<EdgeDataModel> implements IEdgeUserRepository
{

  @NotNull
  @Override
  public Set<EdgeDataModel> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().find(Filters.or(Filters.eq("sourceID", pDeviceID), Filters.eq("targetID", pDeviceID)), EdgeDataModel.class));
  }

  @NotNull
  @Override
  public EdgeDataModel insert(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));

    EdgeDataModel edge = getCollection().find(Filters.or(filter1, filter2)).first();

    // Only insert if it does not exist - no duplicate
    if (edge == null)
    {
      edge = new EdgeDataModel();
      edge.id = UUID.randomUUID().toString();
      edge.sourceID = pSourceID;
      edge.targetID = pTargetID;
      getCollection().insertOne(edge);
    }

    return edge;
  }

  @Override
  public boolean delete(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));
    return getCollection().deleteMany(Filters.or(filter1, filter2)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<EdgeDataModel> getCollectionType()
  {
    return EdgeDataModel.class;
  }

}
