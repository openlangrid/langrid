package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface TemporaryUserService extends DataService<TemporaryUserModel> {
	/**
	 * 
	 * 
	 */
	public void clearExpiredUsers() throws ServiceManagerException;
}
