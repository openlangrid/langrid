package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface DomainService extends DataService<DomainModel> {
	/**
	 * 
	 * 
	 */
	public void delete(String id)
	throws ServiceManagerException;
	
   public LangridList<DomainModel> getListOnGrid(String gridId)
   throws ServiceManagerException;
   
   public LangridList<DomainModel> getAllList() throws ServiceManagerException;
}
