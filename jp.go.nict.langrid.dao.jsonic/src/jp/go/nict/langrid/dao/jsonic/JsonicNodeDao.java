/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.jsonic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.NodeAlreadyExistsException;
import jp.go.nict.langrid.dao.NodeAndUserSearchResult;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Node;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicNodeDao implements NodeDao {
	public JsonicNodeDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Node> listAllNodes(String nodeGridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Node> listNodesOfUser(String userGridId, String userId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSearchResult searchNodes(int startIndex, int maxCount,
			String nodeGridId, MatchingCondition[] conditions, Order[] orders)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeAndUserSearchResult searchNodesAndUsers(int startIndex,
			int maxCount, String nodeGridId, MatchingCondition[] conditions,
			Order[] orders)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addNode(Node node)
	throws DaoException, NodeAlreadyExistsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteNodesOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteNodesOfUser(String userGridId, String userId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node getNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException {
		try{
			return JsonicUtil.decode(getFile(nodeGridId, nodeId), Node.class);
		} catch(FileNotFoundException e){
			throw new NodeNotFoundException(nodeGridId, nodeId);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isNodeExist(String nodeGridId, String nodeId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	private File getFile(String gridId, String nodeId){
		return new File(new File(new File(getBaseDir(), gridId), "nodes"), nodeId + ".json");
	}
	
	private File getBaseDir(){
		return context.getBaseDir();
	}

	private JsonicDaoContext context;
}
