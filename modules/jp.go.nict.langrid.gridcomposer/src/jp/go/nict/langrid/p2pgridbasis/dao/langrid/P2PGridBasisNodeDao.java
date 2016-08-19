/*
 * $Id: P2PGridBasisNodeDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.NodeAlreadyExistsException;
import jp.go.nict.langrid.dao.NodeAndUserSearchResult;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.NodePK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NodeData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisNodeDao
extends AbstractP2PGridBasisDao<Node>
implements DataDao, NodeDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisNodeDao(NodeDao dao, DaoContext context) {
		super(context);
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[Node] : " + data.getId());
		if(data.getClass().equals(NodeData.class) == false) {
			throw new UnmatchedDataTypeException(NodeData.class.toString(), data.getClass().toString());
		}

		Node entity = null;
		try {
			entity = ((NodeData)data).getNode();
			if(entity.getGridId().equals(getSelfGridId())) return false;
			if(!isReachableTo(entity.getGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
	}

	@Override
	public void addNode(Node node) throws DaoException,
			NodeAlreadyExistsException {
		dao.addNode(node);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void deleteNode(String nodeGridId, String nodeId)
			throws NodeNotFoundException, DaoException {
		dao.deleteNode(nodeGridId, nodeId);
	}


	@Override
	public void deleteNodesOfGrid(String gridId) throws DaoException {
		dao.deleteNodesOfGrid(gridId);
	}

	@Override
	public void deleteNodesOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteNodesOfUser(userGridId, userId);
	}

	@Override
	public Node getNode(String nodeGridId, String nodeId)
			throws NodeNotFoundException, DaoException {
		return dao.getNode(nodeGridId, nodeId);
	}

	@Override
	public boolean isNodeExist(String nodeGridId, String nodeId)
			throws DaoException {
		return dao.isNodeExist(nodeGridId, nodeId);
	}

	@Override
	public List<Node> listAllNodes(String nodeGridId) throws DaoException {
		return dao.listAllNodes(nodeGridId);
	}

	@Override
	public List<Node> listNodesOfUser(String userGridId, String userId)
			throws DaoException {
		return dao.listNodesOfUser(userGridId, userId);
	}

	@Override
	public NodeSearchResult searchNodes(int startIndex, int maxCount,
			String nodeGridId, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return dao.searchNodes(startIndex, maxCount, nodeGridId, conditions, orders);
	}

	@Override
	public NodeAndUserSearchResult searchNodesAndUsers(int startIndex,
			int maxCount, String nodeGridId, MatchingCondition[] conditions,
			Order[] orders) throws DaoException {
		return dao.searchNodesAndUsers(startIndex, maxCount, nodeGridId, conditions, orders);
	}

	private NodeDao dao;
	private GenericHandler<Node> handler = new GenericHandler<Node>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new NodeData(
						getDaoContext().loadEntity(Node.class, id)
						));
				logger.info("published[Node(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			try{
				NodePK pk = (NodePK)id;
				getController().revoke(NodeData.getDataID(null, pk));
				logger.info("revoked[Node(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisNodeDao.class);
}
