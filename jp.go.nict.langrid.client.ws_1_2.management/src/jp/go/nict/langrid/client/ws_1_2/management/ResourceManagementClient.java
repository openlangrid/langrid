/*
 * $Id: ResourceManagementClient.java 370 2011-08-19 10:10:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Resource. This combines multiple language resources and provides composite language services.
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
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceInstance;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public interface ResourceManagementClient extends ServiceClient{
	/**
	 * 
	 * 
	 */
	void clear()
		throws LangridException;

	/**
	 * 
	 * 
	 */
	ResourceEntrySearchResult searchResources(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void addResource(String resourceId, ResourceProfile profile, ResourceInstance instance
			, Attribute[] attributes)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void addResourceAs(String ownerUserId, String resourceId, ResourceProfile profile
			, ResourceInstance instance, Attribute[] attributes)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void deleteResource(String resourceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	ResourceProfile getResourceProfile(String resourceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void setResourceProfile(String resourceId, ResourceProfile profile)
		throws LangridException;
	
	/**
	 * 
	 * 
	 */
	ResourceInstance getResourceInstance(String resourceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void setResourceInstance(String resourceId, ResourceInstance instance)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	Attribute[] getResourceAttributes(String resourceId, String[] attributeNames)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void setResourceAttributes(String resourceId, Attribute[] attributes)
		throws LangridException;

	/**
	 * 
	 * s
	 */
	void activateResource(String resourceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void deactivateResource(String resourceId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	boolean isResourceActive(String resourceId)
		throws LangridException;
	
	/**
	 * 
	 * 
	 */
	void authorizeResource(String resourceId)
		throws LangridException;
	
	/**
	 * 
	 * 
	 */
	void deauthorizeResource(String resourceId)
	throws LangridException;
}
