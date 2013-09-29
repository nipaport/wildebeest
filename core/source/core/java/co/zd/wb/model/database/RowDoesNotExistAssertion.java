// Wildebeest Migration Framework
// Copyright 2013, Zen Digital Co Inc
//
// This file is part of Wildebeest
//
// Wildebeest is free software: you can redistribute it and/or modify it under
// the terms of the GNU General Public License v2 as published by the Free
// Software Foundation.
//
// Wildebeest is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with
// Wildebeest.  If not, see http://www.gnu.org/licenses/gpl-2.0.html

package co.zd.wb.model.database;

import co.zd.wb.model.base.BaseAssertion;
import co.zd.wb.model.Assertion;
import co.zd.wb.model.AssertionFaultException;
import co.zd.wb.model.AssertionResponse;
import co.zd.wb.model.ModelExtensions;
import co.zd.wb.model.Instance;
import co.zd.wb.model.base.ImmutableAssertionResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

public class RowDoesNotExistAssertion extends BaseAssertion implements Assertion
{
	public RowDoesNotExistAssertion(
		UUID assertionId,
		String name,
		int seqNum,
		String sql)
	{
		super(assertionId, name, seqNum);
		this.setSql(sql);
	}
	
	// <editor-fold desc="Sql" defaultstate="collapsed">

	private String m_sql = null;
	private boolean m_sql_set = false;

	public String getSql() {
		if(!m_sql_set) {
			throw new IllegalStateException("sql not set.  Use the HasSql() method to check its state before accessing it.");
		}
		return m_sql;
	}

	private void setSql(
		String value) {
		if(value == null) {
			throw new IllegalArgumentException("sql cannot be null");
		}
		boolean changing = !m_sql_set || m_sql != value;
		if(changing) {
			m_sql_set = true;
			m_sql = value;
		}
	}

	private void clearSql() {
		if(m_sql_set) {
			m_sql_set = true;
			m_sql = null;
		}
	}

	private boolean hasSql() {
		return m_sql_set;
	}

	// </editor-fold>
	
	@Override public AssertionResponse apply(Instance instance)
	{
		if (instance == null) { throw new IllegalArgumentException("instance cannot be null"); }
		DatabaseInstance db = ModelExtensions.As(instance, DatabaseInstance.class);
		if (db == null) { throw new IllegalArgumentException("instance must be a DatabaseInstance"); }

		AssertionResponse result = null;
		
		DataSource ds = db.getAppDataSource();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			try
			{
				conn = ds.getConnection();
				ps = conn.prepareStatement(this.getSql());
				rs = ps.executeQuery();

				int rowCount = 0;
				while(rs.next())
				{
					rowCount ++;
				}

				if (rowCount == 0)
				{
					result = new ImmutableAssertionResponse(true, "Row does not exist, as expected");
				}
				else
				{
					result = new ImmutableAssertionResponse(
						false,
						String.format("Expected to find no rows but found %d", rowCount));
				}
			}
			finally
			{
				DatabaseHelper.release(rs);
				DatabaseHelper.release(ps);
				DatabaseHelper.release(conn);
			}
		}
		catch(SQLException e)
		{
			throw new AssertionFaultException(this.getAssertionId(), e);
		}
				
		return result;
	}
}