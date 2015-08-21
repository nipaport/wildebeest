// Wildebeest Migration Framework
// Copyright 2013 - 2014, Zen Digital Co Inc
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

package co.zd.wb.service.dom.sqlserver;

import co.zd.wb.Assertion;
import co.zd.wb.plugin.sqlserver.SqlServerTableExistsAssertion;
import co.zd.wb.service.Messages;
import co.zd.wb.service.MessagesException;
import co.zd.wb.service.V;
import co.zd.wb.service.dom.BaseDomAssertionBuilder;
import co.zd.wb.service.dom.TryGetResult;
import java.util.UUID;

/**
 * An {@link AssertionBuilder} that builds a {@link SqlServerTableExistsAssertion} from a DOM
 * {@link org.w3c.dom.Element}.
 * 
 * @author                                      Brendon Matheson
 * @since                                       2.0
 */
public class SqlServerTableExistsDomAssertionBuilder extends BaseDomAssertionBuilder
{
	@Override public Assertion build(
		UUID assertionId,
		int seqNum) throws MessagesException
	{
		TryGetResult<String> schemaName = this.tryGetString("schemaName");
		TryGetResult<String> tableName = this.tryGetString("tableName");

		// Validation
		Messages messages = new Messages();
		if (!schemaName.hasValue()) { V.elementMissing(messages, assertionId, "schemaName", SqlServerTableExistsAssertion.class); }
		if (!tableName.hasValue()) { V.elementMissing(messages, assertionId, "tableName", SqlServerTableExistsAssertion.class); }
		
		if (messages.size() > 0)
		{
			throw new MessagesException(messages);
		}
		
		Assertion result = new SqlServerTableExistsAssertion(
			assertionId,
			seqNum,
			schemaName.getValue(),
			tableName.getValue());
		
		return result;
	}
}