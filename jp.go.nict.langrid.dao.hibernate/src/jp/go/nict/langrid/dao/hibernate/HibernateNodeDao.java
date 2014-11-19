/*
 * $Id:HibernateUserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.NodeAlreadyExistsException;
import jp.go.nict.langrid.dao.NodeAndUserSearchResult;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.NodePK;
import jp.go.nict.langrid.dao.entity.User;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateNodeDao
extends HibernateCRUDDao<Node>
implements NodeDao
{
	/**
	 * 
	 * 
	 */
	public HibernateNodeDao(HibernateDaoContext context){
		super(context, Node.class);
	}

	public void clear() throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Object o : session.createCriteria(Node.class).list()){
				session.delete(o);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Node> listAllNodes(String nodeGridId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Node> list = session
					.createCriteria(Node.class)
					.add(Property.forName("gridId").eq(nodeGridId))
					.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Node> listNodesOfUser(String userGridId, String userId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Node> list = (List<Node>)
					session.createCriteria(Node.class)
					.add(Property.forName("gridId").eq(userGridId))
					.add(Property.forName("ownerUserId").eq(userId))
					.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public NodeSearchResult searchNodes(
			int startIndex, int maxCount, String nodeGridId
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		conditions = ArrayUtil.append(conditions, new MatchingCondition("gridId", nodeGridId));
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Query query = QueryUtil.buildSearchQuery(
					session, Node.class, nodeFields
					, conditions, orders);
			List<Node> nodes = query
				.setFirstResult(startIndex)
				.setMaxResults(maxCount)
				.list();
			long totalCount = 0;
			if(nodes.size() < maxCount){
				totalCount = nodes.size() + startIndex;
			} else{
				totalCount = (Long)QueryUtil.buildRowCountQuery(
						session, Node.class, nodeFields
						, conditions).uniqueResult();
			}
			getContext().commitTransaction();
			return new NodeSearchResult(
					nodes.toArray(new Node[]{})
					, (int)totalCount, true
					);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public NodeAndUserSearchResult searchNodesAndUsers(
			int startIndex, int maxCount, String nodeGridId
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		conditions = ArrayUtil.append(conditions, new MatchingCondition("gridId", nodeGridId));
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Query query = QueryUtil.buildJoinSearchQuery(
					session, Node.class, User.class
					, "ownerUserId", "userId"
					, nodeFields
					, conditions, orders);
			List<Node> nodes = (List<Node>)query
				.setFirstResult(startIndex)
				.setMaxResults(maxCount)
				.list();
			long totalCount = 0;
			if(nodes.size() < maxCount){
				totalCount = nodes.size() + startIndex;
			} else{
				totalCount = (Long)QueryUtil.buildRowCountQuery(
						session, Node.class, nodeFields
						, conditions).uniqueResult();
			}
			getContext().commitTransaction();
			Pair<Node, User>[] elements = (Pair<Node, User>[])ArrayUtil.collect(
					nodes.toArray(), Pair.class, new Transformer<Object, Pair>(){
						public Pair<Node, User> transform(Object value)
								throws TransformationException {
							Object[] values = (Object[])value;
							return Pair.create((Node)values[0], (User)values[1]);
						}
					});
			return new NodeAndUserSearchResult(
					elements
					, (int)totalCount, true
					);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void addNode(Node node)
	throws DaoException, NodeAlreadyExistsException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = node.getGridId();
			String nid = node.getNodeId();
			Node current = (Node)session.get(
					Node.class, new NodePK(gid, nid)
					);
			if(current != null){
				getContext().commitTransaction();
				throw new NodeAlreadyExistsException(gid, nid);
			} else{
				session.save(node);
				getContext().commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Node current = (Node)session.get(Node.class, new NodePK(nodeGridId, nodeId));
			if(current == null){
				getContext().commitTransaction();
				throw new NodeNotFoundException(nodeGridId, nodeId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteNodesOfGrid(final String userGridId) throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				for(Node n : listAllNodes(userGridId)){
					delete(n);
				}
			}
		});
	}

	public void deleteNodesOfUser(String userGridId, String userId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Node s : listNodesOfUser(userGridId, userId)){
				session.delete(s);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Node getNode(String nodeGridId, String nodeId)
	throws NodeNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Node current = (Node)session.get(
					Node.class, new NodePK(nodeGridId, nodeId)
					);
			getContext().commitTransaction();
			if(current == null){
				throw new NodeNotFoundException(nodeGridId, nodeId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public boolean isNodeExist(String nodeGridId, String nodeId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			boolean b = session.get(Node.class, new NodePK(nodeGridId, nodeId)) != null;
			getContext().commitTransaction();
			return b;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private static Map<String, Class<?>> nodeFields
			= new HashMap<String, Class<?>>();
	static{
		for(Field f : Node.class.getDeclaredFields()){
			nodeFields.put(f.getName(), f.getType());
		}
	}
}
