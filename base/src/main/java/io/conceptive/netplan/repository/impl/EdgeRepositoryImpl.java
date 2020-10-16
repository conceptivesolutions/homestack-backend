package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.conceptive.netplan.IDBConstants;
import io.conceptive.netplan.core.model.Edge;
import io.conceptive.netplan.repository.IEdgeRepository;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * @author w.glanzer, 12.10.2020
 */
@ApplicationScoped
public class EdgeRepositoryImpl implements IEdgeRepository
{

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<Edge> findAll(@NotNull String pDeviceID)
  {
    return Sets.newHashSet(_getCollection().find(Filters.or(Filters.eq("sourceID", pDeviceID), Filters.eq("targetID", pDeviceID)), Edge.class));
  }

  @NotNull
  @Override
  public Edge addEdge(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));

    Edge edge = _getCollection().find(Filters.or(filter1, filter2)).first();

    // Only insert if it does not exist - no duplicate
    if (edge == null)
    {
      edge = new Edge();
      edge.id = UUID.randomUUID().toString();
      edge.sourceID = pSourceID;
      edge.targetID = pTargetID;
      _getCollection().insertOne(edge);
    }

    return edge;
  }

  @Override
  public boolean removeEdge(@NotNull String pSourceID, @NotNull String pTargetID)
  {
    Bson filter1 = Filters.and(Filters.eq("sourceID", pSourceID), Filters.eq("targetID", pTargetID));
    Bson filter2 = Filters.and(Filters.eq("sourceID", pTargetID), Filters.eq("targetID", pSourceID));
    return _getCollection().deleteMany(Filters.or(filter1, filter2)).getDeletedCount() > 0;
  }

  /**
   * @return the collection to use for this repository
   */
  @NotNull
  private MongoCollection<Edge> _getCollection()
  {
    return mongoClient
        .getDatabase(IDBConstants.DB_NAME)
        .getCollection("edges", Edge.class);
  }
}
