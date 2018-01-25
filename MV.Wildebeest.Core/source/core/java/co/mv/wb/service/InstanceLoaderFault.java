// Wildebeest Migration Framework
// Copyright © 2013 - 2015, Zen Digital Co Inc
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

package co.mv.wb.service;

/**
 * Indicates that an unexpected system error occurred while loading an Instance.
 * 
 * @author                                      Brendon Matheson
 * @since                                       1.0
 */
public class InstanceLoaderFault extends RuntimeException
{
	/**
	 * Creates a new InstanceLoaderFault with the supplied cause.
	 * 
	 * @param       cause                       the root cause of the fault.
	 * @since                                   1.0
	 */
	public InstanceLoaderFault(Exception cause)
	{
		super(cause);
	}
	
	/**
	 * Creates a new InstanceLoaderFault with the supplied message.
	 * 
	 * @param       message                     the message for this fault.
	 * @since                                   1.0
	 */
	public InstanceLoaderFault(String message)
	{
		super(message);
	}
}