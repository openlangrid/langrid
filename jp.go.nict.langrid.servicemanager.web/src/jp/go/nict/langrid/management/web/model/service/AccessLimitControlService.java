package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public interface AccessLimitControlService extends DataService<AccessLimitControlModel> {
	/**
	 * 
	 * 
	 */
	public LangridList<AccessLimitControlModel> getList(
			int start, int max, String userGridId, String serviceId, Order[] orders)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public LangridList<AccessLimitControlModel> getList(
		String userGridId, String userId, String serviceGridId, String serviceId)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public int getTotalCount(String serviceId, String userGridId)
	throws ServiceManagerException;
}
