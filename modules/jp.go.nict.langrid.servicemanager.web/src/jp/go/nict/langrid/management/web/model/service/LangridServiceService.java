package jp.go.nict.langrid.management.web.model.service;

import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface LangridServiceService<T> extends DataService<T> {
	public void deleteById(String serviceId) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void activate(String serviceId) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void deactivate(String serviceId) throws ServiceManagerException;

	public void approveService(String serviceId) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public boolean isActive(String serviceId) throws ServiceManagerException;

	public boolean isExternalService(String serviceId) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	//	public byte[] getWsdl(String serviceId) throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public void addEndpoint(ServiceEndpointModel endpoint) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public List<ServiceEndpointModel> getEndpointList(String serviceId)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void editEndpoint(
		String serviceId, String beforeUrl, String beforeUserName, ServiceEndpointModel endpoint)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public void deleteEndpoint(ServiceEndpointModel obj) throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public LangridList<ServiceModel> getOtherList(String serviceGridId, Order order)
	throws ServiceManagerException;
	
	
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
//	public byte[] getInstanceBody(String serviceId) throws ServiceManagerException;
}
