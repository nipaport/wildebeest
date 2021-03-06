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

package co.mv.wb.plugin.composite;

import co.mv.wb.MigrationType;
import co.mv.wb.ResourceType;
import co.mv.wb.Wildebeest;
import co.mv.wb.plugin.base.BaseMigration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Applies a migration in an external resource definition, and tracks both the external resource state and the composite
 * state.
 *
 * @since                                       4.0
 */
@MigrationType(
	pluginGroupUri = "co.mv.wb:Composite",
	uri = "co.mv.wb.composite:ExternalResourceMigration",
	description =
		"Applies a migration in another resource definition, and tracks both the external resource state and the " +
			"composite resource state",
	example =
		"<migration\n" +
		"    type=\"ExternalResourceMigration\"\n" +
		"    id=\"dd7a708f-bf6d-4e18-aaec-c1fdf3de965d\"\n" +
		"    fromStateId=\"d364f2ba-3972-4a76-944d-d9767cbfc2d7\"\n" +
		"    toStateId=\"f488f17b-71d3-4672-9216-afd1e73884cf\">\n" +
		"    <filename>AnotherResource.wbr</filename>\n" +
		"    <target>SomeExternalTarget</target>\n" +
		"</migration>"
)
public class ExternalResourceMigration extends BaseMigration
{
	public ExternalResourceMigration(
		UUID migrationId,
		Optional<UUID> fromStateId,
		Optional<UUID> toStateId,
		File baseDir,
		String fileName,
		Optional<String> target)
	{
		super(migrationId, fromStateId, toStateId);

		this.setBaseDir(baseDir);
		this.setFileName(fileName);
		this.setTarget(target);
	}

	// <editor-fold desc="BaseDir" defaultstate="collapsed">

	private File _baseDir = null;
	private boolean _baseDir_set = false;

	public File getBaseDir() {
		if(!_baseDir_set) {
			throw new IllegalStateException("baseDir not set.");
		}
		if(_baseDir == null) {
			throw new IllegalStateException("baseDir should not be null");
		}
		return _baseDir;
	}

	private void setBaseDir(
		File value) {
		if(value == null) {
			throw new IllegalArgumentException("baseDir cannot be null");
		}
		boolean changing = !_baseDir_set || _baseDir != value;
		if(changing) {
			_baseDir_set = true;
			_baseDir = value;
		}
	}

	private void clearBaseDir() {
		if(_baseDir_set) {
			_baseDir_set = true;
			_baseDir = null;
		}
	}

	private boolean hasBaseDir() {
		return _baseDir_set;
	}

	// </editor-fold>

	// <editor-fold desc="FileName" defaultstate="collapsed">

	private String _fileName = null;
	private boolean _fileName_set = false;

	public String getFileName() {
		if(!_fileName_set) {
			throw new IllegalStateException("fileName not set.");
		}
		if(_fileName == null) {
			throw new IllegalStateException("fileName should not be null");
		}
		return _fileName;
	}

	private void setFileName(
		String value) {
		if(value == null) {
			throw new IllegalArgumentException("fileName cannot be null");
		}
		boolean changing = !_fileName_set || !_fileName.equals(value);
		if(changing) {
			_fileName_set = true;
			_fileName = value;
		}
	}

	private void clearFileName() {
		if(_fileName_set) {
			_fileName_set = true;
			_fileName = null;
		}
	}

	private boolean hasFileName() {
		return _fileName_set;
	}

	// </editor-fold>

	// <editor-fold desc="Target" defaultstate="collapsed">

	private Optional<String> _target = null;
	private boolean _target_set = false;

	public Optional<String> getTarget() {
		if(!_target_set) {
			throw new IllegalStateException("target not set.");
		}
		if(_target == null) {
			throw new IllegalStateException("target should not be null");
		}
		return _target;
	}

	private void setTarget(
		Optional<String> value) {
		if(value == null) {
			throw new IllegalArgumentException("target cannot be null");
		}
		boolean changing = !_target_set || !_target.equals(value);
		if(changing) {
			_target_set = true;
			_target = value;
		}
	}

	private void clearTarget() {
		if(_target_set) {
			_target_set = true;
			_target = null;
		}
	}

	private boolean hasTarget() {
		return _target_set;
	}

	// </editor-fold>

	@Override public List<ResourceType> getApplicableTypes()
	{
		return Arrays.asList(
			Wildebeest.MySqlDatabase,
			Wildebeest.PostgreSqlDatabase,
			Wildebeest.SqlServerDatabase);
	}
}
