package io.conceptive.homestack.repository.impl.user.object;

import io.conceptive.homestack.model.data.StackDataModel;
import io.conceptive.homestack.repository.api.user.IStackUserRepository;
import org.jetbrains.annotations.*;

import javax.enterprise.context.Dependent;

/**
 * @author w.glanzer, 16.10.2020
 */
@Dependent
class StackUserRepositoryImpl extends AbstractObjectUserRepository<StackDataModel> implements IStackUserRepository
{

  @NotNull
  @Override
  protected Class<StackDataModel> getCollectionType()
  {
    return StackDataModel.class;
  }

  @Nullable
  @Override
  protected String getID(@NotNull StackDataModel pModel)
  {
    return pModel.id;
  }

}
