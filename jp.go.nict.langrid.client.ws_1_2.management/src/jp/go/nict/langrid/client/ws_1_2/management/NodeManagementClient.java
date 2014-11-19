/*
 * $Id: NodeManagementClient.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeProfile;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public interface NodeManagementClient extends ServiceClient{
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
	NodeEntrySearchResult searchNodes(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, Scope scope)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void addNode(String nodeId, NodeProfile profile, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void addNodeAs(String ownerUserId, String nodeId, NodeProfile profile, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void deleteNode(String nodeId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	NodeProfile getNodeProfile(String nodeId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setNodeProfile(String nodeId, NodeProfile profile)
	throws LangridException;

	/**
	 * ノードのBasic認証ユーザ名を取得する。
	 * @param nodeId ノードID
	 * @return Basic認証ユーザ名
	 * @throws LangridException 処理に失敗した
	 */
	String getBasicAuthUserName(String nodeId)
	throws LangridException;

	/**
	 * ノードのBasic認証ユーザ 名とパスワードを設定する。
	 * @param nodeId ノードID
	 * @param basicAuthUserName Basic認証ユーザ名
	 * @param basicAuthPassword Basic認証パスワード
	 * @throws LangridException 処理に失敗した
	 */
	void setBasicAuthUserNameAndPassword(String nodeId
			, String basicAuthUserName, String basicAuthPassword)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	Attribute[] getNodeAttributes(String nodeId, String[] attributeNames)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setNodeAttributes(String nodeId, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void activateNode(String nodeId)
		throws LangridException;

	/**
	 * 
	 * 
	 */
	void deactivateNode(String nodeId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	boolean isNodeActive(String nodeId)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	void setUpSelfNode(String ownerUserId, String nodeId
			, NodeProfile profile, Attribute[] attributes)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	String getSelfNodeId()
	throws LangridException;
}
