package io.conceptive.netplan.repository.impl;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoClient;
import io.conceptive.netplan.repository.IUserRepository;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
@ApplicationScoped
public class UserRepositoryImpl implements IUserRepository
{

  @Inject
  protected MongoClient mongoClient;

  @NotNull
  @Override
  public Set<String> findAll()
  {
    return Sets.newHashSet(mongoClient.listDatabaseNames());
  }

}
