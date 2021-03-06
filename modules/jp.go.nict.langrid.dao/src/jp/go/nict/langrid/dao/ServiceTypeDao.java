/*
 * $Id: ServiceTypeDao.java 1514 2015-03-08 12:43:46Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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

import java.util.List;

import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision: 1514 $
 */
public interface ServiceTypeDao{
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	public List<ServiceType> listAllServiceTypes()
	throws DaoException;

	public List<ServiceType> listAllServiceTypes(String domainId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addServiceType(ServiceType serviceType)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServiceType(String domainId, String serviceTypeId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServiceType(String domainId) throws DaoException;

	/**
	 * 
	 * 
	 */
	ServiceType getServiceType(String domainId, String serviceTypeId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isServiceTypeExist(String domainId, String serviceTypeId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes()
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes(String domainId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addServiceMetaAttribute(ServiceMetaAttribute metaAttribute)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServiceMetaAttribute(String domainId, String attributeName)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteServiceMetaAttribute(String domainId) throws DaoException;
	
	/**
	 * 
	 * 
	 */
	ServiceMetaAttribute getServiceMetaAttribute(String domainId, String attributeName)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isServiceMetaAttributeExist(String domainId, String serviceMetaAttributeId)
	throws DaoException;

	void mergeServiceType(ServiceType st) throws DaoException;
}
