package jp.go.nict.langrid.management.web.model.service;

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public interface ServiceInformationService extends Serializable {
	/**
	 * 
	 * 
	 */
	public LangridList<ExecutionInformationStatisticsModel> getStatisticsList(
			int index, int count, String serviceId, Calendar start, Calendar end, Period period, Order[] orders)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public int getStatisticsTotalCount(String serviceId, Calendar start, Calendar end, Period period)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public LangridList<IndividualExecutionInformationModel> getVerboseList(
			int index, int count, String userId, String serviceId, Calendar start, Calendar end
			, MatchingCondition[] conditions, Order[] orders)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public int getVerboseTotalCount(int index, int count, String userId, String serviceId, Calendar start, Calendar end
	   , MatchingCondition[] conditions)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<IndividualExecutionInformationModel> getLimitOverVerboseList(
		int index, int count, String userId, String serviceId, Calendar start, Calendar end
		, MatchingCondition[] conditions, Order[] orders, int limitCount)
		throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	public int getLimitOverVerboseTotalCount(
		int index, int count, String userId, String serviceId, Calendar start, Calendar end
		, MatchingCondition[] conditions, int limitCount)
	throws ServiceManagerException;
	
	public void setScopeParameter(String serviceGridId, String userGridId, String userId);
}
