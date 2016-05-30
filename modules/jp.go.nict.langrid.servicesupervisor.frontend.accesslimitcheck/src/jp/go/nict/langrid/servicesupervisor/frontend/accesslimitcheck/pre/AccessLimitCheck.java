/*
 * $Id: AccessLimitCheck.java 407 2011-08-25 02:21:46Z t-nakaguchi $
 *
 * langridライセンス条項
 */
package jp.go.nict.langrid.servicesupervisor.frontend.accesslimitcheck.pre;

import static jp.go.nict.langrid.dao.entity.Period.EACHTIME;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException;
import jp.go.nict.langrid.servicesupervisor.frontend.Preprocess;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class AccessLimitCheck implements Preprocess {
	public void process(ProcessContext context, MimeHeaders requestMimeHeaders)
	throws AccessLimitExceededException, NoAccessPermissionException, SystemErrorException{
		User u = context.getCallerUser();
		Service s = context.getTargetService();
		if(u.getGridId().equals(s.getGridId())){
			if(u.isAdminUser()) return;
			if(u.getUserId().equals(s.getOwnerUserId())) return;
		}
		try{
			checkLimitation(
					context.getAccessLimitDao(), context.getAccessStateDao()
					, u.getGridId(), u.getUserId()
					, s.getGridId(), s.getServiceId()
					);
		} catch(DaoException e){
			logger.log(Level.SEVERE, "exception when checking access limit.", e);
			throw new SystemErrorException(
					ExceptionUtil.getMessageWithStackTrace(e)
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void checkLimitation(
			AccessLimitDao ldao, AccessStatDao sdao
			, String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws AccessLimitExceededException, DaoException
	{
		List<AccessLimit> limits = ldao.getAccessLimits(
				userGridId, userId, serviceGridId, serviceId);
		if(limits.size() == 0){
			return;
		}
		Map<Period, AccessStat> states = new HashMap<Period, AccessStat>();
		for(AccessStat s : sdao.getAccessStats(
				userGridId, userId, serviceGridId, serviceId, Calendar.getInstance())){
			states.put(s.getPeriod(), s);
		}
		if(states.size() == 0){
			return;
		}
		for(AccessLimit l : limits){
			if(l.getPeriod().equals(EACHTIME)) continue;
			AccessStat s = states.get(l.getPeriod());
			if(s == null) continue;

			switch(l.getLimitType()){
				case FREQUENCY:
					if(s.getAccessCount() >= l.getLimitCount()){
						throw new AccessLimitExceededException(
								"Access count limit(" + l.getLimitCount()
								+ "/" + l.getPeriod().name() + ") exceeded."
						);
					}
					break;
				case CAPACITY:
					if(s.getResponseBytes() >= l.getLimitCount()){
						throw new AccessLimitExceededException(
								"Transfer capacity(" + l.getLimitCount()
								+ "/" + l.getPeriod().name() + ") exceeded."
						);
					}
					break;
			}
		}
	}

	private static Logger logger = Logger.getLogger(
			AccessLimitCheck.class.getName());
}
