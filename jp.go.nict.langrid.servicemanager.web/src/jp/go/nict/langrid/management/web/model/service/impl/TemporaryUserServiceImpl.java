package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.TemporaryUserSearchResult;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.TemporaryUserLogic;
import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.TemporaryUserService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class TemporaryUserServiceImpl implements TemporaryUserService {
	public TemporaryUserServiceImpl() throws ServiceManagerException {
	}

	@Override
	public TemporaryUserModel get(String id) throws ServiceManagerException {
		List<TemporaryUserModel> list = getList(0, 1, new MatchingCondition[]{
			new MatchingCondition("gridId", userGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("userId", id, MatchingMethod.COMPLETE)
		}, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public LangridList<TemporaryUserModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		try {
			TemporaryUserSearchResult result = new TemporaryUserLogic()
				.searchTemporaryUsers(index, count, userGridId, userId, conditions,
					orders);
			LangridList<TemporaryUserModel> list = new LangridList<TemporaryUserModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(TemporaryUser tu : result.getElements()) {
				list.add(makeModel(tu));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		try {
			return new TemporaryUserLogic().searchTemporaryUsers(0, 1, userGridId,
				userId, conditions, new Order[]{}
				).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void add(TemporaryUserModel model) throws ServiceManagerException {
		TemporaryUser tu = new TemporaryUser();
		try {
			new TemporaryUserLogic().addUser(setProperty(model, tu));
		} catch(UserAlreadyExistsException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void edit(final TemporaryUserModel model) throws ServiceManagerException {
		try {
			new TemporaryUserLogic().transactUpdate(userGridId, model.getUserId(),
				new BlockP<TemporaryUser>() {
					@Override
					public void execute(TemporaryUser arg0) {
						setProperty(model, arg0);
					}
				});
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void delete(TemporaryUserModel model) throws ServiceManagerException {
		try {
			new TemporaryUserLogic().deleteUser(userGridId, model.getUserId());
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void clearExpiredUsers() throws ServiceManagerException {
		try {
			new TemporaryUserLogic().clearExpiredUsers();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private TemporaryUserModel makeModel(TemporaryUser user) {
		TemporaryUserModel model = new TemporaryUserModel();
		model.setGridId(user.getGridId());
		model.setUpdatedDateTime(user.getUpdatedDateTime());
		model.setUserId(user.getUserId());
		model.setParentUserId(user.getParentUserId());
		model.setBeginAvailableDateTime(user.getBeginAvailableDateTime());
		model.setEndAvailableDateTime(user.getEndAvailableDateTime());
		model.setPassword(user.getPassword());
		model.setCreatedDateTime(user.getCreatedDateTime());
		model.setUpdatedDateTime(user.getUpdatedDateTime());
		return model;
	}

	private TemporaryUser setProperty(TemporaryUserModel model, TemporaryUser user) {
		user.setGridId(model.getGridId());
		user.setUserId(model.getUserId());
		user.setParentUserId(model.getParentUserId());
		user.setPassword(model.getPassword());
		user.setBeginAvailableDateTime(model.getBeginAvailableDateTime());
		user.setEndAvailableDateTime(model.getEndAvailableDateTime());
		user.setCreatedDateTime(model.getCreatedDateTime());
		user.setUpdatedDateTime(model.getUpdatedDateTime());
		return user;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
	private static final long serialVersionUID = 1L;
}
