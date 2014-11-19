/*
 * $Id: ServiceTypeLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic;

import java.util.List;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.OldServiceType;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ServiceTypeLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ServiceTypeLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getServiceTypeDao().clear();
	}

	@DaoTransaction
	public List<ServiceType> listServiecType(String domainId) throws DaoException
	{
	   return getServiceTypeDao().listAllServiceTypes(domainId);
	}

	@DaoTransaction
	public List<ServiceType> listAllServiceType() throws DaoException
	{
	   return getServiceTypeDao().listAllServiceTypes();
	}

	@DaoTransaction
	public void addServiceType(ServiceType type) throws DaoException {
	   getServiceTypeDao().addServiceType(type);
	}

	@DaoTransaction
	public void deleteServiceType(String domainId, String serviceTypeId) throws DaoException {
	   getServiceTypeDao().deleteServiceType(domainId, serviceTypeId);
	}

	@DaoTransaction
	public ServiceType getServiceType(String domainId, String typeId) throws DaoException {
	   return getServiceTypeDao().getServiceType(domainId, typeId);
	}

  @DaoTransaction
   public void transactUpdate(String domainId, String serviceTypeId, BlockP<ServiceType> block)
   throws DaoException{
      ServiceType p = getServiceTypeDao().getServiceType(domainId, serviceTypeId);
      block.execute(p);
      p.touchUpdatedDateTime();
   }

	@DaoTransaction
	public void clearMetaAttribute() throws DaoException{
		getServiceTypeDao().clear();
	}

	@DaoTransaction
	public List<ServiceMetaAttribute> listServiecMetaAttribute(String domainId) throws DaoException
	{
	   return getServiceTypeDao().listAllServiceMetaAttributes(domainId);
	}

	@DaoTransaction
	public List<ServiceMetaAttribute> listAllServiceMetaAttribute() throws DaoException
	{
	   return getServiceTypeDao().listAllServiceMetaAttributes();
	}
	
	public ServiceMetaAttribute getServiceMetaAttribute(String domainId, String attributeId) throws DaoException {
	   return getServiceTypeDao().getServiceMetaAttribute(domainId, attributeId);
	}

	@DaoTransaction
	public void addServiceMetaAttribute(ServiceMetaAttribute type) throws DaoException {
	   getServiceTypeDao().addServiceMetaAttribute(type);
	}

	@DaoTransaction
	public void deleteServiceMetaAttribute(String domainId, String serviceTypeId) throws DaoException {
	   getServiceTypeDao().deleteServiceMetaAttribute(domainId, serviceTypeId);
	}

  @DaoTransaction
   public void transactUpdateMetaAttribute(String domainId, String serviceTypeId, BlockP<ServiceMetaAttribute> block)
   throws DaoException{
      ServiceMetaAttribute p = getServiceTypeDao().getServiceMetaAttribute(domainId, serviceTypeId);
      block.execute(p);
      p.touchUpdatedDateTime();
   }

	private static Logger logger = Logger.getLogger(
			ServiceTypeLogic.class.getName());
}
