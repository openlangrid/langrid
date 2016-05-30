/*
 * $Id: NodeManagementService.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.nodemanagement;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.NodeNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.UnsupportedMatchingMethodException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface NodeManagementService {
	/**
	 * 
	 * 
	 */
	void clear()
		throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * 
	 */
	NodeEntrySearchResult searchNodes(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, String scope)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UnsupportedMatchingMethodException;

	/**
	 * 
	 * 
	 */
	void addNode(String nodeId, NodeProfile profile
			, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void addNodeAs(String ownerUserId, String nodeId, NodeProfile profile
			, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void deleteNode(String nodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, NodeNotInactiveException
		, UnknownException
		;

	/**
	 * 
	 * 
	 */
	NodeProfile getNodeProfile(String nodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void setNodeProfile(String nodeId, NodeProfile profile)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	Attribute[] getNodeAttributes(String nodeId, String[] attributeNames)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void setNodeAttributes(String nodeId, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void activateNode(String nodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void deactivateNode(String nodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	boolean isNodeActive(String nodeId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	String getSelfNodeId()
	throws AccessLimitExceededException, NoAccessPermissionException
	, ServiceConfigurationException, UnknownException
	;
}
