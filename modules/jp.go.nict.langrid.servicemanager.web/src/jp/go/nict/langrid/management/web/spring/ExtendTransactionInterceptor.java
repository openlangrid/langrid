package jp.go.nict.langrid.management.web.spring;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ExtendTransactionInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DaoContext dc = DaoFactory.createInstance().getDaoContext();
		try {
			dc.beginTransaction();
			Object ret = invocation.proceed();
			dc.commitTransaction();
			return ret;
		} catch(Throwable e) {
			if ( 0 < dc.getTransactionNestCount()) {
				dc.rollbackTransaction();
				LogWriter.writeWarn("Spring Transaction", "Spring Service Transaction is rollbacked", this.getClass());
			}
			if( ! (e instanceof ServiceManagerException)){
				if(e instanceof Exception) {
					LogWriter.writeError("Spring Transaction", (Exception)e, this.getClass());
				} else {
					e.printStackTrace();
				}
			}
			throw e;
		}
	}
}
