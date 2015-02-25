package jp.go.nict.langrid.management.web.model.service;

import java.io.Serializable;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public interface DataService<T> extends Serializable {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public void add(T obj) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public T get(String id) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public LangridList<T> getList(
	      int index, int count
			, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException;


	/**
	 * 
	 * 
	 */
	public void edit(T obj) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public void delete(T condition) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public void setScopeParameter(String serviceGridId, String userGridId, String userId);
	
	public boolean isExist(String dataId) throws ServiceManagerException;
}
