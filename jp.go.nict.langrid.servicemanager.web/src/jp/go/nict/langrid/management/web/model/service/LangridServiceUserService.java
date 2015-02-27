package jp.go.nict.langrid.management.web.model.service;

import java.util.Set;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * @author Trang
 *
 */
public interface LangridServiceUserService extends DataService<UserModel> {
	/**
	 * 
	 */
	public LangridList<UserModel> getListExcludeIds(
	      int index, int count, MatchingCondition[] conditions
	      , Order[] orders, Scope scope, Set<String> excludes)
	throws ServiceManagerException;
	
	public boolean authenticate(String userId, String password) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public boolean isAdministrator(String userId) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public void changePassword(String changedUserId, String password)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public boolean isCanCallServices(String userId) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public void setCanCallServices(String userId, boolean isPermit)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
   boolean isShouldChangePassword(String userId, int day)
   throws ServiceManagerException;
}
