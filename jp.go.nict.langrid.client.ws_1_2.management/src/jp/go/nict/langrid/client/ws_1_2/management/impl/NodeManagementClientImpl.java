/*
 * $Id: NodeManagementClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language nodes.
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
package jp.go.nict.langrid.client.ws_1_2.management.impl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.NodeManagementClient;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import localhost.langrid_1_2.services.NodeManagement.NodeManagementServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class NodeManagementClientImpl  
extends ServiceClientImpl
implements NodeManagementClient
{
	/**
	 * 
	 * 
	 */
	public NodeManagementClientImpl(URL nodeUrl){
		super(nodeUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		NodeManagementServiceLocator locator = new NodeManagementServiceLocator();
		setUpService(locator);
		return (Stub)locator.getNodeManagement(url);
	}

	public void clear() throws LangridException{
		invoke();
	}

	public NodeEntrySearchResult searchNodes(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope)
		throws LangridException
	{
		return (NodeEntrySearchResult)invoke(
				startIndex, maxCount, conditions, orders, scope
				);
	}

	public void addNode(String nodeId, NodeProfile profile,
			Attribute[] attributes) throws LangridException {
		invoke(nodeId, profile, attributes);
	}

	public void addNodeAs(
			String ownerUserId, String nodeId, NodeProfile profile
			, Attribute[] attributes)
		throws LangridException
	{
		invoke(ownerUserId, nodeId, profile, attributes);
	}

	public void deleteNode(String nodeId) throws LangridException{
		invoke(nodeId);
	}

	public NodeProfile getNodeProfile(String nodeId)
		throws LangridException
	{
		return (NodeProfile)invoke(nodeId);
	}

	public void setNodeProfile(String nodeId, NodeProfile profile)
		throws LangridException
	{
		invoke(nodeId, profile);
	}

	public String getBasicAuthUserName(String nodeId)
	throws LangridException
	{
		return (String)invoke(nodeId);
	}

	public void setBasicAuthUserNameAndPassword(String nodeId
			, String basicAuthUserName, String basicAuthPassword)
	throws LangridException
	{
		invoke(nodeId, basicAuthUserName, basicAuthPassword);
	}


	public Attribute[] getNodeAttributes(
			String nodeId, String[] attributeNames)
		throws LangridException
	{
		return (Attribute[])invoke(nodeId, attributeNames);
	}

	public void setNodeAttributes(
			String nodeId, Attribute[] attributes)
		throws LangridException
	{
		invoke(nodeId, attributes);
	}

	public void activateNode(String nodeId) throws LangridException {
		invoke(nodeId);
	}

	public void deactivateNode(String nodeId) throws LangridException {
		invoke(nodeId);
	}

	public boolean isNodeActive(String nodeId) throws LangridException {
		return (Boolean)invoke(nodeId);
	}

	public void setUpSelfNode(String ownerUserId, String nodeId,
			NodeProfile profile, Attribute[] attributes)
	throws LangridException {
		invoke(ownerUserId, nodeId, profile, attributes);
	}

	public String getSelfNodeId() throws LangridException {
		return (String)invoke();
	}

	private static final long serialVersionUID = 8750321595733879382L;
}
