package co.mv.stm.impl.database.mysql;

import co.mv.stm.impl.BaseTransition;
import co.mv.stm.impl.database.DatabaseHelper;
import co.mv.stm.model.ModelExtensions;
import co.mv.stm.model.ResourceInstance;
import co.mv.stm.model.Transition;
import co.mv.stm.model.TransitionFailedException;
import co.mv.stm.model.TransitionFaultException;
import co.mv.stm.model.TransitionType;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlCreateDatabaseTransition extends BaseTransition implements Transition
{
	public MySqlCreateDatabaseTransition(
		UUID transitionId,
		TransitionType transitionType,
		UUID toStateId)
	{
		super(transitionId, transitionType, toStateId);
	}
	
	public MySqlCreateDatabaseTransition(
		UUID transitionId,
		TransitionType transitionType,
		UUID fromStateId,
		UUID toStateId)
	{
		super(transitionId, transitionType, fromStateId, toStateId);
	}

	@Override public void perform(ResourceInstance instance) throws TransitionFailedException
	{
		if (instance == null) { throw new IllegalArgumentException("instance"); }
		MySqlDatabaseResourceInstance db = ModelExtensions.As(instance, MySqlDatabaseResourceInstance.class);
		if (db == null) { throw new IllegalArgumentException("instance must be a MySqlDatabaseResourceInstance"); }

		if (MySqlDatabaseHelper.schemaExists(db, db.getSchemaName()))
		{
			throw new TransitionFailedException(
				this.getTransitionId(),
				String.format("database \"%s\" already exists",	db.getSchemaName()));
		}
		
		try
		{
			DatabaseHelper.execute(
				db.getInfoDataSource(),
				String.format("CREATE DATABASE `%s`;", db.getSchemaName()));
		}
		catch (SQLException e)
		{
			throw new TransitionFaultException(e);
		}
	}
}
