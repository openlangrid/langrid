/*
 * $Id:DaoFactory.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public abstract class DaoFactory {
	/**
	 * 
	 * 
	 */
	public abstract DaoContext getDaoContext();

	/**
	 * 
	 * 
	 */
	public abstract SystemPropertyDao createSystemPropertyDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract GridDao createGridDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract FederationDao createFederationDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract UserDao createUserDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract TemporaryUserDao createTemporaryUserDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ServiceDao createServiceDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ServiceDeploymentDao createServiceDeploymentDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract NodeDao createNodeDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract AccessLimitDao createAccessLimitDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract AccessLogDao createAccessLogDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract AccessRightDao createAccessRightDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract AccessStatDao createAccessStateDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract OverUseLimitDao createOverUseLimitDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract OverUseStateDao createOverUseStateDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ResourceDao createResourceDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract AcceptableRemoteAddressDao createAcceptableRemoteAddressDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public static void addEntityClasses(Class<?>... additionalEntities)
	throws DaoException{
		DaoFactory.additionalEntities.addAll(Arrays.asList(additionalEntities));
	}

	/**
	 * 
	 * 
	 */
	public abstract ServiceActionScheduleDao createServiceActionScheduleDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract OperationRequestDao createOperationRequestDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract NewsDao createNewsDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract DomainDao createDomainDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ProtocolDao createProtocolDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ResourceTypeDao createResourceTypeDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract ServiceTypeDao createServiceTypeDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract InvocationDao createInvocationDao() throws DaoException;
	
	/**
	 * 
	 * 
	 */
	public static synchronized DaoFactory createInstance() throws DaoException{
		if(daoFactory == null){
			try{
				daoFactory = (DaoFactory)new XmlBeanFactory(new ClassPathResource("/dao.xml"))
						.getBean("dao.DaoFactory");
				daoFactory.initialize(additionalEntities);
			} catch(BeansException e){
				throw new DaoException(e);
			}
		}
		return daoFactory;
	}

	public abstract void initialize(List<Class<?>> additionalEntities) throws DaoException;

	private static List<Class<?>> additionalEntities = new ArrayList<Class<?>>();
	private static DaoFactory daoFactory;
}
