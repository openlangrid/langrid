package jp.go.nict.langrid.management.web.view.model.provider;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceMetaAttributeService;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ServiceMetaAttributeSortableDataProvider
extends LangridSortableDataProvider<ServiceMetaAttributeModel>
{
   /**
    * 
    * 
    */
   public ServiceMetaAttributeSortableDataProvider(String gridId, String userId, String domainId)
	throws ServiceManagerException
	{
		this.domainId = domainId;
		this.gridId = gridId;
		this.userId = userId;
	}
   
   @Override
   protected LangridList<ServiceMetaAttributeModel> getList(int first, int count)
   throws ServiceManagerException {
      return ((ServiceMetaAttributeService)getService()).getListOnDomain(first, count, domainId);
   }
   
   @Override
   protected int getTotalCount() throws ServiceManagerException {
      return ((ServiceMetaAttributeService)getService()).getAllListOnDomain(domainId).size();
   }
   
   @Override
	protected DataService<ServiceMetaAttributeModel> getService()
	throws ServiceManagerException {
		return ServiceFactory.getInstance().getServiceMetaAttributeService(gridId, gridId, userId);
	}
   
   private String gridId;
   private String userId;
   private String domainId;
}
