package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.AccessRightControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface AccessRightControlService extends DataService<AccessRightControlModel> {

	/**
	 * 
	 * 
	 */
	public LangridList<AccessRightControlModel> getList(
			int index, int count, String serviceOwnerUserId, String serviceId
			, String userGridId, Order[] orders)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public int getTotalCount(
	   String serviceGridId, String serviceOwnerUserId, String serviceId, String userGridId, String userId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
   void setDefault(String serviceId, String serviceOwnerUserId, boolean accessRight)
   throws ServiceManagerException;
}
