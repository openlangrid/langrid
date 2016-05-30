/*
 * $Id: NodeLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.NodeAlreadyExistsException;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Node;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class NodeLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public NodeLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getNodeDao().clear();
	}

	@DaoTransaction
	public NodeSearchResult searchNodes(
			int startIndex, int maxCount, String nodeGridId
			, MatchingCondition[] conditions, Order[] orders
	) throws DaoException{
		return getNodeDao().searchNodes(
				startIndex, maxCount, nodeGridId, conditions, orders
				);
	}

	@DaoTransaction
	public void addNode(Node node) throws NodeAlreadyExistsException, DaoException{
		String ngid = node.getGridId();
		String nid = node.getNodeId();
		if(getNodeDao().isNodeExist(ngid, nid)){
			throw new NodeAlreadyExistsException(ngid, nid);
		}
		getNodeDao().addNode(node);
	}

	@DaoTransaction
	public void deleteNode(String nodeGridId, String nodeId)
	throws DaoException{
		getNodeDao().deleteNode(nodeGridId, nodeId);
	}

	@DaoTransaction
	public <T> T transactRead(String nodeGridId, String nodeId, BlockPR<Node, T> block)
	throws UserNotFoundException, DaoException{
		return block.execute(getNodeDao().getNode(nodeGridId, nodeId));
	}

	@DaoTransaction
	public void transactUpdate(String nodeGridId, String nodeId, BlockP<Node> block)
	throws UserNotFoundException, DaoException{
		Node u = getNodeDao().getNode(nodeGridId, nodeId);
		block.execute(u);
		u.touchUpdatedDateTime();
	}
}
