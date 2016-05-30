package jp.go.nict.langrid.management.web.model.service;

import java.util.List;

import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface OperationRequestService extends DataService<OperationRequestModel> {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public void deleteList(List<OperationRequestModel> list) throws ServiceManagerException;
	
	public void deleteByTargetId(String targetId, OperationType type) throws ServiceManagerException;
	
	public OperationRequestModel getByTargetId(String targetId, OperationType type) throws ServiceManagerException;
}
