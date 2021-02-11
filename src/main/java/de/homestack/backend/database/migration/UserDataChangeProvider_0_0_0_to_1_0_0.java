package de.homestack.backend.database.migration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Creates all necessary initial tables
 *
 * @author w.glanzer, 10.02.2021
 */
class UserDataChangeProvider_0_0_0_to_1_0_0 implements ICassandraChangeProvider
{

  @Override
  public void execute(@NotNull CqlSession pSession, @NotNull String pKeyspaceName, @NotNull String pUserID)
  {
    // Create Keyspace
    pSession.execute(SchemaBuilder.createKeyspace(pKeyspaceName)
                         .ifNotExists()
                         .withSimpleStrategy(3)
                         .build());

    // Create stacks_by_id table
    pSession.execute(SchemaBuilder.createTable("stacks_by_id")
                         .ifNotExists()
                         .withPartitionKey("id", DataTypes.UUID)
                         .withColumn("displayName", DataTypes.TEXT)
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create device_by_stackid table
    pSession.execute(SchemaBuilder.createTable("devices_by_stackid")
                         .ifNotExists()
                         .withPartitionKey("stackid", DataTypes.UUID)
                         .withClusteringColumn("id", DataTypes.UUID)
                         .withColumn("icon", DataTypes.TEXT)
                         .withColumn("address", DataTypes.TEXT)
                         .withColumn("location", DataTypes.TEXT)
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create slots_by_deviceid table
    pSession.execute(SchemaBuilder.createTable("slots_by_deviceid")
                         .ifNotExists()
                         .withPartitionKey("deviceid", DataTypes.UUID)
                         .withClusteringColumn("id", DataTypes.UUID)
                         .withColumn("state", DataTypes.TEXT)
                         .withColumn("targetid", DataTypes.UUID)
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create metrics_by_deviceid table
    pSession.execute(SchemaBuilder.createTable("metrics_by_deviceid")
                         .ifNotExists()
                         .withPartitionKey("deviceid", DataTypes.UUID)
                         .withClusteringColumn("id", DataTypes.UUID)
                         .withColumn("type", DataTypes.TEXT)
                         .withColumn("enabled", DataTypes.BOOLEAN)
                         .withColumn("settings", DataTypes.mapOf(DataTypes.TEXT, DataTypes.TEXT))
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create satellites_by_stackid table
    pSession.execute(SchemaBuilder.createTable("satellites_by_stackid")
                         .ifNotExists()
                         .withPartitionKey("stackid", DataTypes.UUID)
                         .withClusteringColumn("id", DataTypes.UUID)
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create satelliteleases_by_satelliteid table
    pSession.execute(SchemaBuilder.createTable("satelliteleases_by_satelliteid")
                         .ifNotExists()
                         .withPartitionKey("satelliteid", DataTypes.UUID)
                         .withClusteringColumn("id", DataTypes.UUID)
                         .withColumn("userid", DataTypes.UUID)
                         .withColumn("revokeddate", DataTypes.TIMESTAMP)
                         .withColumn("accesstoken", DataTypes.TEXT)
                         .build()
                         .setKeyspace(pKeyspaceName));

    // Create records_by_metricid table
    pSession.execute(SchemaBuilder.createTable("records_by_metricid")
                         .ifNotExists()
                         .withPartitionKey("metricid", DataTypes.UUID)
                         .withClusteringColumn("recorddate", DataTypes.TIMESTAMP)
                         .withColumn("id", DataTypes.UUID)
                         .withColumn("state", DataTypes.TEXT)
                         .withColumn("result", DataTypes.mapOf(DataTypes.TEXT, DataTypes.TEXT))
                         .build()
                         .setKeyspace(pKeyspaceName));
  }

}
