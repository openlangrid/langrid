package jp.go.nict.langrid.management.web.model.service;

import java.io.File;
import java.util.List;

import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface CompositeServiceService extends LangridServiceService<CompositeServiceModel> {
	/**
	 * 
	 * 
	 */
	public Invocation[] getInvocations(String serviceId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public byte[] getBpel(String serviceId) throws ServiceManagerException;

	public List<String> getWsdlNames(String serviceId) throws ServiceManagerException;

	public File getDefinisionFileListZip(String serviceId) throws ServiceManagerException;

	public void deleteInvocation(String serviceId, List<Invocation> list) throws ServiceManagerException;

}

