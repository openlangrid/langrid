package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.management.logic.NodeLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.NodeModelUtil;
import jp.go.nict.langrid.management.web.model.service.NodeService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NodeServiceImpl implements NodeService {
	public NodeServiceImpl() {
	}

	/**
	 * 
	 * 
	 */
	@Deprecated
	public void add(NodeModel obj) throws ServiceManagerException {
		// noop
	}

	public void edit(final NodeModel obj) throws ServiceManagerException {
		try {
			NodeLogic nl = new NodeLogic();
			nl.transactUpdate(serviceGridId, obj.getNodeId(), new BlockP<Node>() {
				@Override
				public void execute(Node n) {
					try {
						NodeModelUtil.setProperty(obj, n);
					} catch(ServiceManagerException e) {
						e.printStackTrace();
					}
				}
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	@Deprecated
	public void delete(NodeModel condition) throws ServiceManagerException {
		// noop
	}

	public NodeModel get(String id) throws ServiceManagerException {
		List<NodeModel> list = getList(0, 1, new MatchingCondition[]{
			new MatchingCondition("gridId", userGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("nodeId", id, MatchingMethod.COMPLETE)
		}, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public LangridList<NodeModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		try {
			NodeSearchResult result = new NodeLogic().searchNodes(
				index, count, serviceGridId, conditions, orders
				);
			if(result == null || result.getTotalCount() == 0) {
				return new LangridList<NodeModel>();
			}
			LangridList<NodeModel> list = new LangridList<NodeModel>();
			for(Node resource : result.getElements()) {
				list.add(NodeModelUtil.makeModel(resource));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return new NodeLogic().searchNodes(0, 1, serviceGridId, conditions,
				new Order[]{}).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	private String userId;
	private String serviceGridId;
	private String userGridId;
	private static final long serialVersionUID = 1L;
}
