package de.homestack.backend.graphql.types;

import org.eclipse.microprofile.graphql.Enum;

/**
 * @author w.glanzer, 10.02.2021
 */
@Enum("RecordState")
public enum GQLMetricRecordState
{
  /**
   * Device FAILED, so it can not be used anywhere
   */
  FAILURE,

  /**
   * Device can be used, but may not work correctly
   */
  WARNING,

  /**
   * Device is OK and ready to use
   */
  SUCCESS,

  /**
   * State could not be determined
   */
  UNKNOWN
}
