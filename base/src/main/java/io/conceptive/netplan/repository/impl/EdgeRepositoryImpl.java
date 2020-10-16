package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.core.model.Edge;
import io.conceptive.netplan.repository.IEdgeRepository;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@Dependent
public class EdgeRepositoryImpl extends AbstractRepository<Edge> implements IEdgeRepository
{

  @NotNull
  @Override
  public Set<Edge> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(getCollection().find(Filters.or(Filters.eq("sourceID", pDeviceID), Filters.eq("targetID", pDeviceID)), Edge.class));
  }

  @NotNull
  @Override
  public Edge addEdge(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));

    Edge edge = getCollection().find(Filters.or(filter1, filter2)).first();

    // Only insert if it does not exist - no duplicate
    if (edge == null)
    {
      edge = new Edge();
      edge.id = UUID.randomUUID().toString();
      edge.sourceID = pSourceID;
      edge.targetID = pTargetID;
      getCollection().insertOne(edge);
    }

    return edge;
  }

  @Override
  public boolean removeEdge(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));
    return getCollection().deleteMany(Filters.or(filter1, filter2)).getDeletedCount() > 0;
  }

  @NotNull
  @Override
  protected Class<Edge> getCollectionType()
  {
    return Edge.class;
  }

}
