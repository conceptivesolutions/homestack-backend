package de.homestack.backend.database.user;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.conceptive.homestack.model.data.StackDataModel;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 10.02.2021
 */
@ApplicationScoped
class CassandraStackDBRepository extends AbstractCassandraDBFacade implements IStackDBRepository
{

  private static final String _TABLE_STACKS_BY_ID = "stacks_by_id";

  @NotNull
  @Override
  public List<StackDataModel> getStacks(@NotNull String pUserID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_STACKS_BY_ID)
                            .columns("id", "displayName")
                            .build(), pUserID)
        .filter(pRow -> pRow.getUuid(0) != null)
        .map(pRow -> StackDataModel.builder()
            .id(Objects.requireNonNull(pRow.getUuid(0)).toString())
            .displayName(pRow.getString(1))
            .build())
        .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public StackDataModel getStackByID(@NotNull String pUserID, @NotNull String pStackID)
  {
    return executeQuery(QueryBuilder.selectFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_STACKS_BY_ID)
                            .columns("id", "displayName")
                            .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                            .build(), pUserID)
        .map(pRow -> StackDataModel.builder()
            .id(Objects.requireNonNull(pRow.getUuid(0)).toString())
            .displayName(pRow.getString(1))
            .build())
        .findFirst()
        .orElse(null);
  }

  @NotNull
  @Override
  public StackDataModel upsertStack(@NotNull String pUserID, @NotNull StackDataModel pStack)
  {
    executeUpdate(QueryBuilder.insertInto(sessionProvider.getKeyspaceName(pUserID), _TABLE_STACKS_BY_ID)
                      .value("id", QueryBuilder.literal(UUID.fromString(pStack.id)))
                      .value("displayName", QueryBuilder.literal(pStack.displayName))
                      .build(), pUserID);
    return pStack;
  }

  @Override
  public boolean deleteStack(@NotNull String pUserID, @NotNull String pStackID)
  {
    return executeUpdate(QueryBuilder.deleteFrom(sessionProvider.getKeyspaceName(pUserID), _TABLE_STACKS_BY_ID)
                             .whereColumn("id").isEqualTo(QueryBuilder.literal(UUID.fromString(pStackID)))
                             .build(), pUserID);
  }

}
