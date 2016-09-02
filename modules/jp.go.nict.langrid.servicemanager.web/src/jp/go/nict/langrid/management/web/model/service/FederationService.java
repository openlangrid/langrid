package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public interface FederationService extends DataService<FederationModel> {
	public LangridList<String> getReachableTargetGridIdListFrom(String sourceGridId)
	throws ServiceManagerException;

	public LangridList<String> getReachableTargetGridIdListTo(String targetGridId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<String> getConnectedTargetGridIdList(String gridId, Order order)
	throws ServiceManagerException;

	//	/**
	//	 * 
	//	 * 
	//	 */
	//	public LangridList<String> getAllTargetGridIdList(String gridId) throws ServiceManagerException;
	/**
	 * 
	 * 
	 */
	public LangridList<FederationModel> getAllRelatedTargetGridList(
		String gridId, int first, int count)
	throws ServiceManagerException;

	public int getAllRelatedTargetGridListTotalCount(String gridId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public LangridList<String> getConnectedSourceGridIdList(String gridId, Order order)
	throws ServiceManagerException;

	//	/**
	//	 * 
	//	 * 
	//	 */
	//	public LangridList<String> getAllConnectedSourceGridIdList(String gridId) throws ServiceManagerException; 
	/**
	 * 
	 * 
	 */
	public LangridList<FederationModel> getAllRelatedSourceGridList(
		String gridId, int first, int count)
	throws ServiceManagerException;

	public int getAllRelatedSourceGridListTotalCount(String gridId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public FederationModel get(String sourceGridId, String targetGridId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void setRequesting(
		String sourceGridId, String targetGridId, boolean isRequesting)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void setConnected(String sourceGridId, String targetGridId, boolean isConnected)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void deleteById(String sourceGridId, String targetGridId)
	throws ServiceManagerException;
}
