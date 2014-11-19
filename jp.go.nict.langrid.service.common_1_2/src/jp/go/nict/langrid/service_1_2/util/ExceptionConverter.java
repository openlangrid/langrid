/*
 * $Id: ExceptionConverter.java 220 2010-10-02 15:03:55Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.service_1_2.util;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 220 $
 */
public class ExceptionConverter {
	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		ParserConfigurationException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		ClassNotFoundException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		IllegalAccessException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		InvocationTargetException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		InstantiationException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
			UnsatisfiedLinkError e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
			RemoteException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e));
	}

	/**
	 * 
	 * 
	 */
	public static UnknownException convertException(
		Throwable e)
	{
		return new UnknownException(e);
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertException(
		SQLException e)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(e)
			);
	}

	/**
	 * 
	 * 
	 */
	public static ServiceConfigurationException convertToServiceConfigurationException(
			Throwable t)
	{
		return new ServiceConfigurationException(
				ExceptionUtil.getMessageWithStackTrace(t)
			);
	}

	/**
	 * 
	 * 
	 */
	public static UnknownException convertToUnknownException(
			Throwable t)
	{
		return new UnknownException(t);
	}
}
