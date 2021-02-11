package de.homestack.backend.database.migration;

/**
 * Exception that gets thrown, if the migration failed
 *
 * @author w.glanzer, 10.02.2021
 */
public class MigrationFailedException extends Exception
{

  public MigrationFailedException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
