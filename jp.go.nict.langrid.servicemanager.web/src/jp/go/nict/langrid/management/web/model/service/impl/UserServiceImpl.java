/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2006-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.web.model.service.impl;

import java.net.URL;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.commons.lang.block.BlockR;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.UserSearchResult;
import jp.go.nict.langrid.dao.entity.EmbeddableStringValueClass;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserRole;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceLogic;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.logic.UserLogic;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.UserService;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class UserServiceImpl implements UserService {
	@Override
	public LangridList<UserModel> getListExcludeIds(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope, Set<String> excludes)
	throws ServiceManagerException
	{
		List<UserModel> list = getList(index, count, conditions, orders, scope);
		LangridList<UserModel> result = new LangridList<UserModel>();
		for(UserModel um : list) {
			if(excludes.contains(um.getUserId())) {
				continue;
			}
			result.add(um);
		}
		
		return result;
	}
	
	@Override
	public UserModel get(String id) throws ServiceManagerException {
		try{
			User u = new UserLogic().getUser(userGridId, id);
			if(u != null){
				return makeModel(u);
			}
			return null;
		} catch (UserNotFoundException e) {
			return null;
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass(),
					"ServiceManager couldn't get user.");
		}
	}

	@Override
	public LangridList<UserModel> getList(int index, int count,
			MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		LangridList<UserModel> list = new LangridList<UserModel>();
		try {
			list = new LangridList<UserModel>();
			UserSearchResult result = new UserLogic().searchUsers(index, count,
					userGridId, conditions, orders);
			if (result.getTotalCount() == 0) {
				return list;
			}
			for (User ue : result.getElements()) {
				list.add(makeModel(ue));
			}
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass(),
					"ServiceManager cannot get user list.");
		}
		return list;
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return new UserLogic().searchUsers(0, 1, userGridId, conditions,
					new Order[] {}).getTotalCount();
		} catch (Exception e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void add(UserModel obj) throws ServiceManagerException {
		try {
			new UserLogic().addUser(setProperty(new User(), obj));
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass(),
					"Cannot add user.");
		}
	}

	@Override
	public void edit(final UserModel obj) throws ServiceManagerException {
		try {
			new UserLogic().transactUpdate(userGridId, obj.getUserId(),
					new BlockP<User>() {
						@Override
						public void execute(User user) {
							setProperty(user, obj);
						}
					});
		} catch (Exception e) {
			throw new ServiceManagerException(e, this.getClass(),
					"ServiceManager cannot update user.");
		}
	}

	@Override
	public void changePassword(String userId, String password)
			throws ServiceManagerException {
		try {
			new UserLogic().setUserPassword(userGridId, userId, password);
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void delete(UserModel model) throws ServiceManagerException {
		try {
			String userId = model.getUserId();
			new ServiceLogic(MessageUtil.getActiveBpelUrl()).deleteServicesOfUser(userGridId, userId);
			new UserLogic().deleteUser(userGridId, userId);
		} catch (ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass(),
					"Cannot delete user.");
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass(),
					"Cannot delete user.");
		}
	}

	@Override
	public boolean authenticate(String userId, final String password)
	throws ServiceManagerException {
		try {
			UserLogic logic = new UserLogic();
			return logic.transactRead(userGridId, userId
					, new BlockPR<User, Boolean>() {
						@Override
						public Boolean execute(User entity) {
							return entity.getPassword().equals(
									MessageDigestUtil.digestBySHA512(password));
						}
					}, new BlockR<Boolean>(){
						@Override
						public Boolean execute() {
							return false;
						}
					});
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public boolean isCanCallServices(String userId)
			throws ServiceManagerException {
		return get(userId).isAbleToCallServices();
	}

	@Override
	public void setCanCallServices(String userId, final boolean isPermit)
			throws ServiceManagerException {
		try {
			new UserLogic().transactUpdate(userGridId, userId,
					new BlockP<User>() {
						public void execute(User user) {
							user.setAbleToCallServices(isPermit);
						}
					});
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public Set<jp.go.nict.langrid.management.web.model.enumeration.UserRole> getUserRoles(String userId)
	throws ServiceManagerException{
		Set<jp.go.nict.langrid.management.web.model.enumeration.UserRole> ret = EnumSet.noneOf(jp.go.nict.langrid.management.web.model.enumeration.UserRole.class);
		try{
			for(UserRole r : new UserLogic().getUser(userGridId, userId).getRoles()){
				if(r.getRoleName().equals("langridserviceuser")){
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEUSER);
				} else if(r.getRoleName().equals("langridserviceprovider")){
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEPROVIDER);
				} else if(r.getRoleName().equals("langriduser")){
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEUSER);
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEPROVIDER);
				} else if(r.getRoleName().equals("langridadministrator")){
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEUSER);
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.SERVICEPROVIDER);
					ret.add(jp.go.nict.langrid.management.web.model.enumeration.UserRole.ADMINISTRATOR);
				}
			}
		} catch(DaoException e){
			throw new ServiceManagerException(e, UserServiceImpl.class);
		}
		return ret;
	}

	@Override
	public boolean isShouldChangePassword(String userId, final int day)
			throws ServiceManagerException {
		try {
			return new UserLogic().transactRead(userGridId, userId,
					new BlockPR<User, Boolean>() {
						public Boolean execute(User user) {
							Calendar changed = user.getPasswordChangedDate();
							if (changed == null) {
								changed = user.getCreatedDateTime();
							}
							Calendar basingPoint = Calendar.getInstance();
							basingPoint.add(Calendar.DAY_OF_MONTH, -day);
							return changed.getTimeInMillis() < basingPoint
									.getTimeInMillis();
						}
					});
		} catch (DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId,
			String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;

	private UserModel makeModel(User entity) {
		UserModel um = new UserModel();
		um.setUserId(entity.getUserId());
		um.setOrganization(entity.getOrganization());
		um.setAbleToCallServices(entity.isAbleToCallServices());
		um.setAddress(entity.getAddress());
		um.setDefaultAppProvisionType(entity.getDefaultAppProvisionType());
		um.setDefaultUseType(entity.getDefaultUseType());
		um.setEmailAddress(entity.getEmailAddress());
		um.setGridId(entity.getGridId());
		if (entity.getHomepageUrl() != null) {
			um.setHomepageUrl(new EmbeddableStringValueClass<URL>(entity
					.getHomepageUrl()));
		}
		um.setPassword(entity.getPassword());
		um.setPasswordChangedDate(entity.getPasswordChangedDate());
		um.setCreatedDateTime(entity.getCreatedDateTime());
		um.setRepresentative(entity.getRepresentative());
		um.setUpdatedDateTime(entity.getUpdatedDateTime());
		return um;
	}

	private User setProperty(User user, UserModel model) {
		user.setUserId(model.getUserId());
		user.setAbleToCallServices(model.isAbleToCallServices());
		user.setAddress(model.getAddress());
		user.setCreatedDateTime(model.getCreatedDateTime());
		user.setDefaultAppProvisionType(model.getDefaultAppProvisionType());
		user.setDefaultUseType(model.getDefaultUseType());
		user.setEmailAddress(model.getEmailAddress());
		user.setGridId(model.getGridId());
		user.setHomepageUrl(model.getHomepageUrl().getValue());
		user.setOrganization(model.getOrganization());
		user.setPassword(model.getPassword());
		user.setPasswordChangedDate(model.getPasswordChangedDate());
		user.setRepresentative(model.getRepresentative());
		user.setUpdatedDateTime(model.getUpdatedDateTime());
		user.setVisible(user.isVisible());
		return user;
	}

	private static final long serialVersionUID = 1L;
}
