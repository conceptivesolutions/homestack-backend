package io.conceptive.netplan.core.model;

/**
 * POJO for a single host.
 * Care, this will be used in REST directly
 *
 * @author w.glanzer, 15.10.2020
 */
public class Host
{

  /**
   * ID of the host
   */
  public String id;

  /**
   * ID of the user this host belongs to
   */
  public String userID;

  /**
   * Displayable String
   */
  public String displayName;

}
