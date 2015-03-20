/*
 * $Id: ServiceManagementClientImpl.java 432 2011-12-20 07:27:24Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management.impl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.ServiceManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.Invocation;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceInstance;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import localhost.langrid_2_0.services.ServiceManagement.ServiceManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 432 $
 */
public class ServiceManagementClientImpl  
extends ServiceClientImpl
implements ServiceManagementClient
{
	/**
	 * 
	 * 
	 */
	public ServiceManagementClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ServiceManagementServiceLocator locator = new ServiceManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getServiceManagement(url);
	}

	public void clear() throws LangridException{
		invoke();
	}

	public ServiceEntrySearchResult searchServices(
			int startIndex, int maxCount
			, MatchingCondition[] conditions, Order[] orders
			, Scope scope
			) throws LangridException
	{
		return (ServiceEntrySearchResult)invoke(
				startIndex, maxCount, conditions, orders, scope
				);
	}

	@Override
	public ServiceEntryWithCompactLanguageExpressionSearchResult searchServicesWithCompactLanguageExpression(
			int startIndex, int maxCount, MatchingCondition[] conditions,
			Order[] orders, Scope scope) throws LangridException {
		return (ServiceEntryWithCompactLanguageExpressionSearchResult)invoke(
				startIndex, maxCount, conditions, orders, scope
				);
	}

	public CompositeServiceEntrySearchResult searchCompositeServices(
			int startIndex, int maxCount
			, MatchingCondition[] conditions, Order[] orders
			, Scope scope
			) throws LangridException
	{
		return (CompositeServiceEntrySearchResult)invoke(
				startIndex, maxCount, conditions, orders, scope
				);
	}

	public void addService(
			String serviceId, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
		throws LangridException
	{
		invoke(serviceId, profile, instance, attributes);
	}

	public void addServiceAs(
			String ownerUserId, String serviceId, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
		throws LangridException
	{
		invoke(ownerUserId, serviceId, profile, instance, attributes);
	}

	public void deleteService(String serviceId) throws LangridException{
		invoke(serviceId);
	}

	public ServiceProfile getServiceProfile(String serviceId)
		throws LangridException
	{
		return (ServiceProfile)invoke(serviceId);
	}

	public void setServiceProfile(String serviceId, ServiceProfile profile)
		throws LangridException
	{
		invoke(serviceId, profile);
	}

	public ServiceInstance getServiceInstance(String serviceId)
		throws LangridException
	{
		return (ServiceInstance)invoke(serviceId);
	}

	public void setServiceInstance(String serviceId, ServiceInstance instance)
		throws LangridException
	{
		invoke(serviceId, instance);
	}

	public Attribute[] getServiceAttributes(
			String serviceId, String[] attributeNames)
		throws LangridException
	{
		return (Attribute[])invoke(serviceId, attributeNames);
	}

	public void setServiceAttributes(
			String serviceId, Attribute[] attributes)
		throws LangridException
	{
		invoke(serviceId, attributes);
	}

	public byte[] getServiceWsdl(String serviceId) throws LangridException {
		return (byte[])invoke(serviceId);
	}

	public void activateService(String serviceId) throws LangridException {
		invoke(serviceId);
	}

	public void deactivateService(String serviceId) throws LangridException {
		invoke(serviceId);
	}

	public boolean isServiceActive(String serviceId) throws LangridException {
		return (Boolean)invoke(serviceId);
	}

	public Invocation[] getExternalInvocations(String serviceId)
			throws LangridException {
		return (Invocation[])invoke(serviceId);
	}

	private static final long serialVersionUID = -5363298722912698594L;
}
