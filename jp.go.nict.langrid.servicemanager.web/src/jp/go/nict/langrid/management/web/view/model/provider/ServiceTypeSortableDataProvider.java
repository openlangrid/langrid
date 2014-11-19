package jp.go.nict.langrid.management.web.view.model.provider;

import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ServiceTypeSortableDataProvider
extends LangridSortableDataProvider<ServiceTypeModel> 
{
   /**
    * 
    * 
    */
   public ServiceTypeSortableDataProvider(String gridId, String domainId, String userId)
	throws ServiceManagerException
	{
		this.gridId = gridId;
		this.userId = userId;
		this.domainId = domainId;
	}
   
   @Override
   protected LangridList<ServiceTypeModel> getList(int first, int count)
   throws ServiceManagerException {
      LangridList<ServiceTypeModel> list = new LangridList<ServiceTypeModel>();
      for(ServiceTypeModel model : ServiceFactory.getInstance().getServiceTypeService(
         gridId).getAllList(domainId).subList(first, first + count))
      {
         list.add(model);
      }
      return list;
   }
   
   @Override
   protected int getTotalCount() throws ServiceManagerException {
      return ServiceFactory.getInstance().getServiceTypeService(
         gridId).getAllList(domainId).size();
   }
   
   @Override
	protected DataService<ServiceTypeModel> getService() throws ServiceManagerException {
		return ServiceFactory.getInstance().getServiceTypeService(gridId, gridId, userId);
	}
   
   private String gridId;
   private String userId;
   private String domainId;
}
