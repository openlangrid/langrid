package jp.go.nict.langrid.servicesupervisor.advancedcontrol.filter;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceStatRanking;
import jp.go.nict.langrid.dao.ServiceStatRankingSearchResult;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.servicesupervisor.frontend.processors.pre.AccessRightCheck;

public class SelectionLogic {
	public enum Mode{
		MOST_POPULAR{
			@Override
			public Order[] createOrders() {
				return new Order[]{
						new Order("containerType", OrderDirection.ASCENDANT)
						, new Order("accessCount", OrderDirection.DESCENDANT)
				};
			}
		}, FASTEST{
			@Override
			public Order[] createOrders() {
				return new Order[]{
						new Order("containerType", OrderDirection.ASCENDANT)
						, new Order("responseMillisAve", OrderDirection.ASCENDANT)
				};
			}
		}
		;
		public abstract Order[] createOrders();
	}

	public SelectionLogic(DaoContext context, ServiceDao serviceDao, AccessRightDao rightDao){
		this.context = context;
		this.sdao = serviceDao;
		this.ardao = rightDao;
	}

	public String select(Mode mode,
			String gridId, String nodeId, String domainId, String serviceTypeId
			, String langQueryName, String langQueryValue
			, int sinceDays, String userGridId, String userId
			)
	throws DaoException{
		MatchingCondition[] conds = buildConditions(
				domainId, serviceTypeId, langQueryName, langQueryValue
				);
		String r = doGetSidFromRanking(
				gridId, nodeId, conds, sinceDays, mode.createOrders(), userGridId, userId
				);
		if(r == null){
			r = doGetSidFromServiceList(
					gridId, conds
					, new Order[]{new Order("serviceId", OrderDirection.ASCENDANT)}
					, userGridId, userId);
		}
		return r;
	}

	public static MatchingCondition[] buildConditions(String domainId, String serviceTypeId
			, String langQueryName, String langQueryValue){
		List<MatchingCondition> conds = new ArrayList<MatchingCondition>();
		conds.add(new MatchingCondition("active", true));
		if(domainId != null && domainId.length() > 0){
			conds.add(new MatchingCondition("serviceTypeDomainId", domainId));
		}
		if(serviceTypeId != null && serviceTypeId.length() > 0){
			conds.add(new MatchingCondition("serviceTypeId", serviceTypeId));
		}
		if(langQueryName != null && langQueryName.length() > 0
				&& langQueryValue != null&& langQueryValue.length() > 0){
			conds.add(new MatchingCondition(
					langQueryName
					, langQueryValue
					, MatchingMethod.LANGUAGEPATH));
		}
		return conds.toArray(new MatchingCondition[]{});
	}

	public static Order[] buildOrders(Mode mode){
		return mode.createOrders();
	}

	private String doGetSidFromRanking(
			String gridId, String nodeId, MatchingCondition[] conds, int sinceDays, Order[] orders
			, String userGridId, String userId)
	throws DaoException{
		context.beginTransaction();
		try{
			int startIndex = 0;
			while(true){
				ServiceStatRankingSearchResult result = sdao.searchServiceStatRanking(
						startIndex, 10, gridId, nodeId, false, conds, sinceDays, orders
						);
				if(result.getElements().length == 0) return null;
				for(ServiceStatRanking s : result.getElements()){
					if(!s.getOwnerUserId().equals(userId)){
						if(!AccessRightCheck.isAccessible(
								ardao, userGridId, userId
								, s.getGridId(), s.getServiceId()))
							continue;
					}
					return s.getServiceId();
				}
				if(startIndex + 10 >= result.getTotalCount()) return null;
			}
		} finally{
			context.commitTransaction();
		}
	}

	private String doGetSidFromServiceList(
			String gridId, MatchingCondition[] conds, Order[] orders
			, String userGridId, String userId)
	throws DaoException{
		context.beginTransaction();
		try{
			int startIndex = 0;
			while(true){
				ServiceSearchResult result = sdao.searchServices(
						startIndex, 10, gridId, false, conds, orders
						);
				if(result.getElements().length == 0) return null;
				for(Service s : result.getElements()){
					if(!s.getOwnerUserId().equals(userId)){
						if(!AccessRightCheck.isAccessible(
								ardao, userGridId, userId
								, s.getGridId(), s.getServiceId()))
							continue;
					}
					return s.getServiceId();
				}
				if(startIndex + 10 >= result.getTotalCount()) return null;
			}
		} finally{
			context.commitTransaction();
		}
	}

	private DaoContext context;
	private ServiceDao sdao;
	private AccessRightDao ardao;
}
