/*
 * $Id: ResourceManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language nodes.
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
package jp.go.nict.langrid.client.ws_1_2.management.impl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.ResourceManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceInstance;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import localhost.langrid_1_2.services.ResourceManagement.ResourceManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ResourceManagementClientImpl  
extends ServiceClientImpl
implements ResourceManagementClient
{
	public ResourceManagementClientImpl(URL resourceUrl) {
		super(resourceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ResourceManagementServiceLocator locator = new ResourceManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getResourceManagement(url);
	}

	@Override
	public void clear() throws LangridException {
		invoke();
	}

	@Override
	public void addResource(
			String resourceId, ResourceProfile profile, ResourceInstance instance, Attribute[] attributes)
	throws LangridException 
	{
		invoke(resourceId, profile, instance, attributes);
	}

	@Override
	public void addResourceAs(String ownerUserId, String resourceId
			, ResourceProfile profile, ResourceInstance instance, Attribute[] attributes)
	throws LangridException
	{
		invoke(ownerUserId, resourceId, profile, instance, attributes);
	}

	@Override
	public ResourceEntrySearchResult searchResources(int startIndex,
			int maxCount, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws LangridException
	{
		return (ResourceEntrySearchResult)invoke(
			startIndex, maxCount, conditions, orders, scope
		);
	}

	@Override
	public void deleteResource(String resourceId) throws LangridException {
		invoke(resourceId);
	}

	@Override
	public ResourceProfile getResourceProfile(String resourceId)
	throws LangridException
	{
		return (ResourceProfile)invoke(resourceId);
	}

	@Override
	public void setResourceProfile(String resourceId, ResourceProfile profile)
	throws LangridException 
	{
		invoke(resourceId, profile);
	}

	@Override
	public ResourceInstance getResourceInstance(String resourceId) throws LangridException {
		return (ResourceInstance)invoke(resourceId);
	}

	@Override
	public void setResourceInstance(String resourceId, ResourceInstance instance)
	throws LangridException 
	{
		invoke(resourceId, instance);
	}

	@Override
	public Attribute[] getResourceAttributes(String resourceId, String[] attributeNames)
	throws LangridException
	{
		return (Attribute[])invoke(resourceId, attributeNames);
	}

	@Override
	public void setResourceAttributes(String resourceId, Attribute[] attributes)
	throws LangridException
	{
		invoke(resourceId, attributes);
	}

	@Override
	public void activateResource(String resourceId) throws LangridException {
		invoke(resourceId);
	}

	@Override
	public void deactivateResource(String resourceId) throws LangridException {
		invoke(resourceId);
	}

	@Override
	public boolean isResourceActive(String resourceId) throws LangridException {
		return (Boolean)invoke(resourceId);
	}

	@Override
	public void authorizeResource(String resourceId) throws LangridException {
		invoke(resourceId);
	}

	@Override
	public void deauthorizeResource(String resourceId) throws LangridException {
		invoke(resourceId);
	}
	
	private static final long serialVersionUID = 9029586440504116417L;
}
