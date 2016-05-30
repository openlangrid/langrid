package jp.go.nict.langrid.management.web.model.service;

import java.util.List;

import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface AtomicServiceService extends LangridServiceService<AtomicServiceModel> {
	/**
	 * 
	 * 
	 */
	public List<String> getRelatedResourceIdList(String serviceId);

	public List<AtomicServiceModel> getListByRelatedId(String resourceId)
	throws ServiceManagerException;
}
