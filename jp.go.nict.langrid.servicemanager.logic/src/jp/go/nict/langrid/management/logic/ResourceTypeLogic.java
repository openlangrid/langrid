/*
 * $Id: ResourceTypeLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ResourceTypeLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ResourceTypeLogic() throws DaoException {
	}

	@DaoTransaction
	public void clear() throws DaoException {
		getResourceTypeDao().clear();
	}

	@DaoTransaction
	public List<ResourceType> listAllResourceType() throws DaoException {
		return getResourceTypeDao().listAllResourceTypes();
	}

	@DaoTransaction
	public List<ResourceType> listResourceType(String domainId) throws DaoException {
		return getResourceTypeDao().listAllResourceTypes(domainId);
	}

	@DaoTransaction
	public void addResourceType(ResourceType data) throws DaoException {
		getResourceTypeDao().addResourceType(data);
	}

	@DaoTransaction
	public void deleteResourceType(String domainId, String resourceTypeId) throws DaoException {
		getResourceTypeDao().deleteResourceType(domainId, resourceTypeId);
	}

	@DaoTransaction
	public ResourceType getResourceType(String domainId, String resourceTypeId) throws DaoException {
		return getResourceTypeDao().getResourceType(domainId, resourceTypeId);
	}

	@DaoTransaction
	public void transactUpdate(String domainId, String resourceTypeId, BlockP<ResourceType> block) throws DaoException{
		ResourceType d = getResourceTypeDao().getResourceType(domainId, resourceTypeId);
		block.execute(d);
		d.touchUpdatedDateTime();
	}

	@DaoTransaction
	public List<ResourceMetaAttribute> listAllResourceMetaAttributes() throws DaoException {
		return getResourceTypeDao().listAllResourceMetaAttributes();
	}

	@DaoTransaction
	public List<ResourceMetaAttribute> listResourceMetaAttributes(String domainId) throws DaoException {
		return getResourceTypeDao().listAllResourceMetaAttributes(domainId);
	}

	@DaoTransaction
	public void addResourceMetaAttribute(ResourceMetaAttribute data) throws DaoException {
		getResourceTypeDao().addResourceMetaAttribute(data);
	}

	@DaoTransaction
	public void deleteResourceMetaAttribute(String domainId, String attributeId) throws DaoException {
		getResourceTypeDao().deleteResourceMetaAttribute(domainId, attributeId);
	}

	@DaoTransaction
	public ResourceMetaAttribute getResourceMetaAttribute(String domainId, String attributeId) throws DaoException {
		return getResourceTypeDao().getResourceMetaAttribute(domainId, attributeId);
	}

	@DaoTransaction
	public void transactAttributeUpdate(String domainId, String attributeId, BlockP<ResourceMetaAttribute> block) throws DaoException{
		ResourceMetaAttribute d = getResourceTypeDao().getResourceMetaAttribute(domainId, attributeId);
		block.execute(d);
	}

	private static Logger logger = Logger.getLogger(ResourceTypeLogic.class.getName());
}
