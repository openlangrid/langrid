package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface ResourceService extends DataService<ResourceModel> {
   public void deleteById(String resourceId) throws ServiceManagerException;
   
   public void approveResource(String resourceId) throws ServiceManagerException;
}
