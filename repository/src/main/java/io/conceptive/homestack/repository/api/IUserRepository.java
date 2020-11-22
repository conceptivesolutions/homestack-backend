package io.conceptive.homestack.repository.api;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author w.glanzer, 16.10.2020
 */
public interface IUserRepository
{

  /**
   * @return a list of all currently known users, which contain data
   */
  @NotNull
  Set<String> findAll();

}
