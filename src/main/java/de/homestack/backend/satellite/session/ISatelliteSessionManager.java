package de.homestack.backend.satellite.session;

import org.jetbrains.annotations.NotNull;

import javax.websocket.Session;

/**
 * @author w.glanzer, 20.02.2021
 */
public interface ISatelliteSessionManager
{

  /**
   * Registers a new session in this manager
   *
   * @param pSession Session to add
   */
  void registerSession(@NotNull Session pSession);

  /**
   * Notifies the manager, that a session should be unregistered
   * (because of connection error / connection closed)
   *
   * @param pSession Session to unregister
   */
  void unregisterSession(@NotNull Session pSession);

}
