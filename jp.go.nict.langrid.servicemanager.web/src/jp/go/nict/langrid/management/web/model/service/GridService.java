package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public interface GridService extends DataService<GridModel> {
   /**
    * 
    * 
    */
	public String getSelfGridId() throws ServiceManagerException;

   /**
    * 
    * 
    */
	void setSelfGridId(String gridId);

	/**
	 * 
	 * 
	 */
	public int getPasswordExpiredDay() throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void setPasswordExpiredDay(int day) throws ServiceManagerException;

	public boolean isCommercialUse() throws ServiceManagerException;

	public void setCommercialUse(boolean isCommercialUse) throws ServiceManagerException;

	public boolean isAutoApproveEnabled() throws ServiceManagerException;
	public void setAutoApproveEnabled(boolean autoApproveEnabled) throws ServiceManagerException;
	
	public LangridList<GridModel> getAll() throws ServiceManagerException;
}
