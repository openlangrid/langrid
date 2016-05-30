/*
 * $Id:NodeDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import jp.go.nict.langrid.dao.entity.Node;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision&
 */
public interface NodeDao{
	/**
	 * 
	 * 
	 */
	void clear()
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Node> listAllNodes(String nodeGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<Node> listNodesOfUser(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	NodeSearchResult searchNodes(
			int startIndex, int maxCount
			, String nodeGridId
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	NodeAndUserSearchResult searchNodesAndUsers(
			int startIndex, int maxCount
			, String nodeGridId
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addNode(Node node)
	throws DaoException, NodeAlreadyExistsException;

	/**
	 * 
	 * 
	 */
	void deleteNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	void deleteNodesOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void deleteNodesOfUser(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	Node getNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException;

	/**
	 * 
	 * 
	 */
	boolean isNodeExist(String nodeGridId, String nodeId)
		throws DaoException;
}
