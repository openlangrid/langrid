package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.Calendar;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.OverUseState;
import jp.go.nict.langrid.dao.OverUseStateSearchResult;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.logic.OverUseLimitLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.OverUseStateModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.OveruseLimitControlService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class OveruseLimitControlServiceImpl implements OveruseLimitControlService {
	public OveruseLimitControlServiceImpl() {
	}

	@Override
	public void add(AccessLimitControlModel obj) throws ServiceManagerException {
		try {
			new OverUseLimitLogic().setOverUseLimit(serviceGridId, obj.getPeriod(),
				obj.getLimitType(), obj.getLimitCount());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void clearAll() throws ServiceManagerException {
		try {
			new OverUseLimitLogic().clearOverUseLimits();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void delete(AccessLimitControlModel condition) throws ServiceManagerException {
		try {
			new OverUseLimitLogic().deleteOverUseLimit(
				condition.getServiceGridId(), condition.getPeriod(),
				condition.getLimitType());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void edit(AccessLimitControlModel obj) throws ServiceManagerException {
	}

	@Override
	public AccessLimitControlModel get(String id) throws ServiceManagerException {
		return null;
	}

	@Override
	public LangridList<AccessLimitControlModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		return new LangridList<AccessLimitControlModel>();
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		return 0;
	}

	@Override
	public void setScopeParametar(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
	}

	private AccessLimitControlModel makeModel(OverUseLimit entity) {
		AccessLimitControlModel model = new AccessLimitControlModel();
		model.setLimitCount(entity.getLimitCount());
		model.setLimitType(entity.getLimitType());
		model.setPeriod(entity.getPeriod());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setServiceGridId(entity.getGridId());
		model.setUserGridId(entity.getGridId());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		return model;
	}

	private String serviceGridId;
	private String userGridId;

	@Override
	public LangridList<AccessLimitControlModel> getAll() throws ServiceManagerException {
		LangridList<AccessLimitControlModel> list = new LangridList<AccessLimitControlModel>();
		try {
			for(OverUseLimit ol : new OverUseLimitLogic().listOverUseLimits(serviceGridId
				, new Order[]{
					new Order("limitType", OrderDirection.ASCENDANT)
					, new Order("period", OrderDirection.ASCENDANT)})) {
				list.add(makeModel(ol));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<OverUseStateModel> getAllStatList(Calendar cal, Calendar cal2,
		Order[] orders, Period period)
	throws ServiceManagerException {
		int index = 0;
		LangridList<OverUseStateModel> list = new LangridList<OverUseStateModel>();
		try {
			while(true) {
				OverUseStateSearchResult result = new OverUseLimitLogic()
					.searchOverUseStateWithPeriod(
						index, 1000, serviceGridId, cal, cal2, orders, period);
				if(result.getTotalCount() == 0) {
					break;
				}
				for(OverUseState state : result.getElements()) {
					list.add(makeStateModel(state));
				}
				if(result.getElements().length < 50) {
					break;
				}
				index += 50;
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<OverUseStateModel> getStatList(
		int first, int count, Calendar cal, Calendar cal2, Order[] orders, Period period)
	throws ServiceManagerException {
		LangridList<OverUseStateModel> list = new LangridList<OverUseStateModel>();
		try {
			OverUseStateSearchResult result = new OverUseLimitLogic()
				.searchOverUseStateWithPeriod(
					first, count, serviceGridId, cal, cal2, orders, period);
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(OverUseState state : result.getElements()) {
				list.add(makeStateModel(state));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getStatTotalCount(Calendar cal, Calendar cal2, Order[] orders,
		Period period)
	throws ServiceManagerException {
		try {
			return new OverUseLimitLogic().searchOverUseStateWithPeriod(
				0, 1, serviceGridId, cal, cal2, orders, period).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private OverUseStateModel makeStateModel(OverUseState entity) {
		OverUseStateModel model = new OverUseStateModel();
		model.setBaseDateTime(entity.getBaseDateTime());
		model.setCurrentCount(entity.getCurrentCount());
		model.setLastAccessDateTime(entity.getLastAccessDateTime());
		model.setLimitCount(entity.getLimitCount());
		model.setLimitType(entity.getLimitType());
		model.setPeriod(entity.getPeriod());
		model.setServiceGridId(entity.getServiceGridId());
		model.setServiceId(entity.getServiceId());
		model.setUserGridId(entity.getUserGridId());
		model.setUserId(entity.getUserId());
		return model;
	}
}
