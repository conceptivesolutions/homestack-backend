package io.conceptive.netplan.core;

/**
 * Contains all RBAC-Role-Names
 *
 * @author w.glanzer, 15.10.2020
 */
public interface IRole
{

  /**
   * Default-User that has all default permissions
   */
  String DEFAULT = "user";

  /**
   * Admin-User
   */
  String ADMIN = "admin";

}
