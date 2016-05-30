/*
 * $Id: AccessLimitEachTimeCheck.java 247 2010-10-03 02:07:33Z t-nakaguchi $
 *
 * langridライセンス条項
 */
package jp.go.nict.langrid.servicesupervisor.frontend.accesslimitcheck.post;

import static jp.go.nict.langrid.dao.entity.LimitType.CAPACITY;
import static jp.go.nict.langrid.dao.entity.Period.EACHTIME;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.Postprocess;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5456 $
 */
public class AccessLimitEachTimeCheck implements Postprocess{
	public void process(
			ProcessContext context, int responseBytes
			)
	throws AccessLimitExceededException, SystemErrorException{
		User u = context.getCallerUser();
		Service s = context.getTargetService();
		if(u.getGridId().equals(s.getGridId())){
			if(u.isAdminUser()) return;
			if(u.getUserId().equals(s.getOwnerUserId())) return;
		}

		try{
			checkLimitation(context.getAccessLimitDao()
					, u.getGridId(), u.getUserId()
					, s.getGridId(), s.getServiceId()
					, responseBytes);
		} catch(DaoException e){
			logger.log(Level.SEVERE, "exception when checking access limit.", e);
			throw new SystemErrorException(
					ExceptionUtil.getMessageWithStackTrace(e)
					);
			}
	}

	public static void checkLimitation(
			AccessLimitDao dao, String userGridId, String userId
			, String serviceGridId, String serviceId, int responseBytes)
	throws AccessLimitExceededException, DaoException{
		try{
			AccessLimit al = dao.getAccessLimit(
					userGridId, userId, serviceGridId, serviceId
					, EACHTIME, CAPACITY);
			if(responseBytes >= al.getLimitCount()){
					throw new AccessLimitExceededException(
							"Transfer capacity(" + al.getLimitCount()
							+ "bytes/access) exceeded."
					);
			}
		} catch(AccessLimitNotFoundException e){
		}
	}

	private static Logger logger = Logger.getLogger(
			AccessLimitEachTimeCheck.class.getName());
}
