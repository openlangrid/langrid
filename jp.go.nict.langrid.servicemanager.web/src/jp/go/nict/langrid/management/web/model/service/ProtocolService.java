package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface ProtocolService extends DataService<ProtocolModel> {
   public void deleteById(String id) throws ServiceManagerException;
   
   public LangridList<ProtocolModel> getAllList() throws ServiceManagerException;
}
