// Wildebeest Migration Framework
// Copyright © 2013 - 2018, Matheson Ventures Pte Ltd
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

package co.mv.wb.cli;

import co.mv.wb.*;
import co.mv.wb.fixture.Fixtures;
import co.mv.wb.fixture.TestContext_WildebeestCommandUnit;
import co.mv.wb.framework.PredicateMatcher;
import co.mv.wb.plugin.fake.FakeConstants;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.PrintStream;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for WildebeestCommand.
 *
 * @since                                       1.0
 */
public class WildebeestCommandUnitTests
{
	@Test public void noCommand_noOperationsCalled()
	{
		// Setup
		TestContext_WildebeestCommandUnit context = TestContext_WildebeestCommandUnit.get();

		String[] args = new String[] { };

		// Execute
		context.wildebeestCommand.run(args);

		// Verify
		verifyZeroInteractions(context.wildebeestApi);
	}

	@Test public void about_validRequest_noOperationsCalled()
	{
		// Setup
		TestContext_WildebeestCommandUnit context = TestContext_WildebeestCommandUnit.get();

		String[] args = new String[]
		{
			"about"
		};

		// Execute
		context.wildebeestCommand.run(args);

		// Verify
		verifyZeroInteractions(context.wildebeestApi);
	}
	
	@Test public void migrate_validRequest_migrateOperationCalled() throws
		AssertionFailedException,
		FileLoadException,
		IndeterminateStateException,
		InvalidStateSpecifiedException,
		LoaderFault,
		MigrationFailedException,
		MigrationNotPossibleException,
		PluginBuildException,
		TargetNotSpecifiedException,
		UnknownStateSpecifiedException,
		XmlValidationException
	{
		// Setup
		TestContext_WildebeestCommandUnit context = TestContext_WildebeestCommandUnit.get();

		String[] args = new String[]
		{
			"migrate",
			"--resource:MySqlDatabase/database.wbresource.xml",
			"--instance:MySqlDatabase/staging_db.wbinstance.xml",
			"--targetState:Core Schema Loaded"
		};

        // Execute
		context.wildebeestCommand.run(args);

		// Verify
		verify(context.wildebeestApi).loadResource(any());

		verify(context.wildebeestApi).loadInstance(any());

		verify(context.wildebeestApi).migrate(
			Matchers.argThat(new PredicateMatcher<>(
				resource -> Asserts.verifyFakeResource(
					context.fakeResource.getResourceId(),
					FakeConstants.Fake,
					context.fakeResource.getName(),
					resource))),
			Matchers.argThat(new PredicateMatcher<>(
				instance -> Asserts.verifyFakeInstance(
					instance))),
			eq(Optional.of("Core Schema Loaded")));

		verifyNoMoreInteractions(context.wildebeestApi);
	}

	@Test public void migrate_missingInstanceArg_fails()
	{
		// Setup
		PrintStream output = System.out;

		WildebeestApi wildebeestApi = Fixtures
			.wildebeestApi()
			.get();

		WildebeestCommand wb = new WildebeestCommand(
			output,
			wildebeestApi);

		// Execute
		wb.run(new String[]
			{
				"migrate",
				"--resource:MySqlDatabase/database.wbresource.xml",
				"--targetState:Core Schema Loaded"
			});

		// Verify
		throw new RuntimeException("verification required");
	}

	@Test public void migrate_missingResourceArg_fails()
	{
		// Setup
		PrintStream output = System.out;

		WildebeestApi wildebeestApi = Fixtures
			.wildebeestApi()
			.get();

		WildebeestCommand wb = new WildebeestCommand(
			output,
			wildebeestApi);

		// Execute
		wb.run(new String[]
			{
				"migrate",
				"--instance:MySqlDatabase/staging_db.wbinstance.xml",
				"--targetState:Core Schema Loaded"
			});

		// Verify
		throw new RuntimeException("verification required");
	}

	@Test public void plugins_succeeds()
	{
		// Setup
		PrintStream output = System.out;

		WildebeestApi wildebeestApi = Wildebeest
			.wildebeestApi(output)
			.withFactoryResourcePlugins()
			.withFactoryPluginManager()
			.get();

		WildebeestCommand wb = new WildebeestCommand(
			output,
			wildebeestApi);

		// Execute
		wb.run(new String[]
			{
				"plugins"
			});

		// Verify

		// (none)
	}
}
