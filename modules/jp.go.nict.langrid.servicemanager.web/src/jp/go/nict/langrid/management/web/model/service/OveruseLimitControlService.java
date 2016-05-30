package jp.go.nict.langrid.management.web.model.service;

import java.util.Calendar;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.OverUseStateModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public interface OveruseLimitControlService extends DataService<AccessLimitControlModel> {
	/**
	 * 
	 * 
	 */
	public void clearAll() throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<AccessLimitControlModel> getAll() throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<OverUseStateModel> getAllStatList(Calendar cal, Calendar cal2,
		Order[] orders, Period period)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<OverUseStateModel> getStatList(int first, int count, Calendar cal,
		Calendar cal2, Order[] orders, Period period)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public int getStatTotalCount(Calendar cal, Calendar cal2, Order[] orders,
		Period period)
	throws ServiceManagerException;
}
