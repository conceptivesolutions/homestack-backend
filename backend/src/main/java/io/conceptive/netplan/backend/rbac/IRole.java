package io.conceptive.netplan.backend.rbac;

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
   * Role for satellite user
   */
  String SATELLITE = "satellite";

  /**
   * Admin-User
   */
  String ADMIN = "admin";

}
