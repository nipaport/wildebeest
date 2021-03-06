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

package co.mv.wb.plugin.base.dom;

import co.mv.wb.Instance;
import co.mv.wb.InstanceBuilder;
import co.mv.wb.InstanceLoader;
import co.mv.wb.LoaderFault;
import co.mv.wb.Messages;
import co.mv.wb.PluginBuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * An {@link InstanceBuilder} deserializes {@link Instance} descriptors from XML.
 * 
 * @since                                       1.0
 */
public class DomInstanceLoader implements InstanceLoader
{
	private static final String ELT_INSTANCE = "instance";
	private static final String ATT_INSTANCE_TYPE = "type";
	
	private static final String ELT_HOST_NAME = "hostName";
	private static final String ELT_PORT = "port";
	private static final String ELT_ADMIN_USERNAME = "adminUsername";
	private static final String ELT_ADMIN_PASSWORD = "adminPassword";
	private static final String ELT_SCHEMA_NAME = "schemaName";
	
	/**
	 * Creates a new DomInstanceLoader.
	 * 
	 * @param       instanceBuilders            the set of available {@link InstanceBuilder}s.
	 * @param       instanceXml                 the XML representation of the {@link Instance} to be loaded.
	 * @since                                   1.0
	 */
	public DomInstanceLoader(
		Map<String, InstanceBuilder> instanceBuilders,
		String instanceXml)
	{
		this.setInstanceBuilders(instanceBuilders);
		this.setInstanceXml(instanceXml);
	}

	// <editor-fold desc="InstanceBuilders" defaultstate="collapsed">

	private Map<String, InstanceBuilder> _instanceBuilders = null;
	private boolean _instanceBuilders_set = false;

	private Map<String, InstanceBuilder> getInstanceBuilders() {
		if(!_instanceBuilders_set) {
			throw new IllegalStateException("instanceBuilders not set.  Use the HasInstanceBuilders() method to check its state before accessing it.");
		}
		return _instanceBuilders;
	}

	private void setInstanceBuilders(Map<String, InstanceBuilder> value) {
		if(value == null) {
			throw new IllegalArgumentException("instanceBuilders cannot be null");
		}
		boolean changing = !_instanceBuilders_set || _instanceBuilders != value;
		if(changing) {
			_instanceBuilders_set = true;
			_instanceBuilders = value;
		}
	}

	private void clearInstanceBuilders() {
		if(_instanceBuilders_set) {
			_instanceBuilders_set = true;
			_instanceBuilders = null;
		}
	}

	private boolean hasInstanceBuilders() {
		return _instanceBuilders_set;
	}

	// </editor-fold>

	// <editor-fold desc="InstanceXml" defaultstate="collapsed">

	private String _instanceXml = null;
	private boolean _instanceXml_set = false;

	private String getInstanceXml() {
		if(!_instanceXml_set) {
			throw new IllegalStateException("instanceXml not set.  Use the HasInstanceXml() method to check its state before accessing it.");
		}
		return _instanceXml;
	}

	private void setInstanceXml(
		String value) {
		if(value == null) {
			throw new IllegalArgumentException("instanceXml cannot be null");
		}
		boolean changing = !_instanceXml_set || !_instanceXml.equals(value);
		if(changing) {
			_instanceXml_set = true;
			_instanceXml = value;
		}
	}

	private void clearInstanceXml() {
		if(_instanceXml_set) {
			_instanceXml_set = true;
			_instanceXml = null;
		}
	}

	private boolean hasInstanceXml() {
		return _instanceXml_set;
	}

	// </editor-fold>

	@Override public Instance load() throws
		LoaderFault,
		PluginBuildException
	{
		InputSource inputSource = new InputSource(new StringReader(this.getInstanceXml()));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db;
		try
		{
			db = dbf.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new LoaderFault(e);
		}
		
		Document resourceXd;
		try
		{
			resourceXd = db.parse(inputSource);
		}
		catch (IOException | SAXException e)
		{
			throw new LoaderFault(e);
		}
		
		Element instanceXe = resourceXd.getDocumentElement();
		Instance instance = null;

		if (ELT_INSTANCE.equals(instanceXe.getTagName()))
		{
			instance = buildInstance(
				this.getInstanceBuilders(),
				instanceXe);
		}
		
		return instance;
	}
	
	private static Instance buildInstance(
		Map<String, InstanceBuilder> instanceBuilders,
		Element instanceXe) throws
			LoaderFault,
			PluginBuildException
	{
		if (instanceBuilders == null) { throw new IllegalArgumentException("instanceBuilders"); }
		if (instanceXe == null) { throw new IllegalArgumentException("instanceXe"); }
		
		String type = instanceXe.getAttribute(ATT_INSTANCE_TYPE);
		
		InstanceBuilder builder = instanceBuilders.get(type);
			
		if (builder == null)
		{
			Messages messages = new Messages();
			messages.addMessage(String.format(
				"instance builder of type %s not found",
				type));
			throw new PluginBuildException(messages);
		}
		
		builder.reset();
		((DomBuilder)builder).setElement(instanceXe);
		return builder.build();
	}
}
