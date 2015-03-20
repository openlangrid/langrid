/*
 * $Id: ServiceManagementClient.java 432 2011-12-20 07:27:24Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
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

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 432 $
 */
public interface ServiceManagementClient extends ServiceClient{
	/**
	 * 
	 * 
	 */
	void clear() throws LangridException;

	/**
	 * 
	 * 
	 */
	ServiceEntrySearchResult searchServices(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope
			)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	ServiceEntryWithCompactLanguageExpressionSearchResult searchServicesWithCompactLanguageExpression(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope
			)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	CompositeServiceEntrySearchResult searchCompositeServices(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void addService(String serviceId, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void addServiceAs(String ownerUserId, String serviceId
			, ServiceProfile profile
			, ServiceInstance instance, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void deleteService(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	ServiceProfile getServiceProfile(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setServiceProfile(String serviceId, ServiceProfile profile)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	Attribute[] getServiceAttributes(String serviceId, String[] attributeNames)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setServiceAttributes(String serviceId, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	ServiceInstance getServiceInstance(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setServiceInstance(String serviceId, ServiceInstance instance)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	byte[] getServiceWsdl(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void activateService(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void deactivateService(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	boolean isServiceActive(String serviceId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	Invocation[] getExternalInvocations(String serviceId)
	throws LangridException;
}
