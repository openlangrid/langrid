/*
 * $Id: ServiceFactory.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class ServiceFactory {
   public static ServiceFactory getInstance() {
      if(factory == null){
         factory = new ServiceFactory();
      }
      return factory;
   }

   public AccessLimitControlService getAccessLimitControlService(String gridId)
	throws ServiceManagerException
	{
	   return getAccessLimitControlService(gridId, gridId, "");
	}
   public AccessLimitControlService getAccessLimitControlService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   accessLimitControlService.setScopeParameter(serviceGridId, userGridId, userId);
	   return accessLimitControlService;
	}
   public AccessRightControlService getAccessRightControlService(String gridId)
	throws ServiceManagerException
	{
	   return getAccessRightControlService(gridId, gridId, "");
	}
   public AccessRightControlService getAccessRightControlService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   accessRightControlService.setScopeParameter(serviceGridId, userGridId, userId);
	   return accessRightControlService;
	}
   public AtomicServiceService getAtomicServiceService(String gridId)
	throws ServiceManagerException
	{
	   return getAtomicServiceService(gridId, gridId, "");
	}
   public AtomicServiceService getAtomicServiceService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   atomicServiceService.setScopeParameter(serviceGridId, userGridId, userId);
	   return atomicServiceService;
	}
   public CompositeServiceService getCompositeServiceService(String gridId)
	throws ServiceManagerException
	{
	   return getCompositeServiceService(gridId, gridId, "");
	}
   public CompositeServiceService getCompositeServiceService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   compositeServiceService.setScopeParameter(serviceGridId, userGridId, userId);
	   return compositeServiceService;
	}

   public DomainService getDomainService(String gridId) throws ServiceManagerException  {
		return getDomainService(gridId, gridId, "");
	}

	public DomainService getDomainService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException  {
		domainService.setScopeParameter(serviceGridId, userGridId, userId);
		return domainService;
	}

   public FederationService getFederationService(String gridId) {
	   return getFederationService(gridId, gridId, "");
	}
   public FederationService getFederationService(String serviceGridId, String userGridId, String userId) {
	   return federationService;
	}
   public GridService getGridService() {
	   return gridService;
	}
   public <T> LangridServiceService<T> getLangridServiceService(String gridId) {
	   return getLangridServiceService(gridId, gridId, "");
	}
   @SuppressWarnings("unchecked")
   public <T> LangridServiceService<T> getLangridServiceService(String serviceGridId, String userGridId, String userId) {
      langridServiceService.setScopeParameter(serviceGridId, userGridId, userId);
	   return langridServiceService;
	}
   public NewsService getNewsService(String gridId)
	throws ServiceManagerException
	{
	   return getNewsService(gridId, gridId, "");
	}
   public NewsService getNewsService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   newsService.setScopeParameter(serviceGridId, userGridId, userId);
	   return newsService;
	}

   public NodeService getNodeService(String gridId) throws ServiceManagerException {
	   return getNodeService(gridId, gridId, "");
	}

	public NodeService getNodeService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException {
	   nodeService.setScopeParameter(serviceGridId, userGridId, userId);
	   return nodeService;
	}

	public OperationRequestService getOperationService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   operationRequestService.setScopeParameter(serviceGridId, userGridId, userId);
	   return operationRequestService;
	}

	public OveruseLimitControlService getOveruseLimitControlService(String serviceGridId, String userGridId) {
	   overuseLimitControlService.setScopeParameter(serviceGridId, userGridId, "");
	   return overuseLimitControlService;
	}

	public ResourceService getResourceService(String gridId)
	throws ServiceManagerException
	{
	   return getResourceService(gridId, gridId, "");
	}

	public ResourceService getResourceService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   resourceService.setScopeParameter(serviceGridId, userGridId, userId);
	   return resourceService;
	}

	public ResourceMetaAttributeService getResourceMetaAttributeService(String gridId)
	throws ServiceManagerException
	{
	   return getResourceMetaAttributeService(gridId, gridId, "");
	}

	public ResourceMetaAttributeService getResourceMetaAttributeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   resourceMetaAttributeService.setScopeParameter(serviceGridId, userGridId, userId);
	   return resourceMetaAttributeService;
	}

	public ResourceTypeService getResourceTypeService(String gridId)
	throws ServiceManagerException
	{
	   return getResourceTypeService(gridId, gridId, "");
	}

	public ResourceTypeService getResourceTypeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   resourceTypeService.setScopeParameter(serviceGridId, userGridId, userId);
	   return resourceTypeService;
	}

	public ScheduleService getScheduleService(String gridId)
	throws ServiceManagerException
	{
	   return getScheduleService(gridId, gridId, "");
	}

	public ScheduleService getScheduleService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   scheduleService.setScopeParameter(serviceGridId, userGridId, userId);
	   return scheduleService;
	}

	public ServiceInformationService getServiceInformationService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   serviceInformationService.setScopeParameter(serviceGridId, userGridId, userId);
	   return serviceInformationService;
	}

	public TemporaryUserService getTemporaryUserService(String gridId)
	throws ServiceManagerException
	{
	   return getTemporaryUserService(gridId, gridId, "");
	}

	public TemporaryUserService getTemporaryUserService(String gridId, String userId)
	throws ServiceManagerException
	{
	   return getTemporaryUserService(gridId, gridId, userId);
	}

	public TemporaryUserService getTemporaryUserService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   temporaryUserService.setScopeParameter(serviceGridId, userGridId, userId);
	   return temporaryUserService;
	}

	public UserService getUserService(String gridId) throws ServiceManagerException  {
	   return getUserService(gridId, gridId, "");
	}

	public UserService getUserService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException  {
		userService.setScopeParameter(serviceGridId, userGridId, userId);
	   return userService;
	}
	
	public ProtocolService getProtocolService(String gridId) throws ServiceManagerException {
	   return getProtocolService(gridId,  gridId, "");
	}

	public ProtocolService getProtocolService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   protocolService.setScopeParameter(serviceGridId, userGridId, userId);
	   return protocolService;
	}

	public ServiceMetaAttributeService getServiceMetaAttributeService(String gridId)
	throws ServiceManagerException {
	   return getServiceMetaAttributeService(gridId, gridId, "");
	}

	public ServiceMetaAttributeService getServiceMetaAttributeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException {
	   serviceMetaAttributeService.setScopeParameter(serviceGridId, userGridId, userId);
	   return serviceMetaAttributeService;
	}

	public ServiceTypeService getServiceTypeService(String gridId)
	throws ServiceManagerException {
	   return getServiceTypeService(gridId, gridId, "");
	}

	public ServiceTypeService getServiceTypeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException {
	   serviceTypeService.setScopeParameter(serviceGridId, userGridId, userId);
	   return serviceTypeService;
	}

   private ServiceFactory() {
      InjectorHolder.getInjector().inject(this);
   }

	@SpringBean
   private AccessLimitControlService accessLimitControlService;

	@SpringBean
   private AccessRightControlService accessRightControlService;

	@SpringBean
   private AtomicServiceService atomicServiceService;

	@SpringBean
   private CompositeServiceService compositeServiceService;

	@SpringBean
   private DomainService domainService;

	@SpringBean
   private FederationService federationService;

	@SpringBean
   private GridService gridService;

	@SpringBean(name="langridServiceService")
   private LangridServiceService langridServiceService;

	@SpringBean(name="langridServiceService")
	private ServiceInformationService serviceInformationService;

	@SpringBean
   private NewsService newsService;

	@SpringBean
   private NodeService nodeService;

	@SpringBean
   private OperationRequestService operationRequestService;

	@SpringBean
   private OveruseLimitControlService overuseLimitControlService;

	@SpringBean
   private ResourceService resourceService;

	@SpringBean
	private ResourceMetaAttributeService resourceMetaAttributeService;

	@SpringBean
	private ResourceTypeService resourceTypeService;

	@SpringBean
   private ScheduleService scheduleService;

	@SpringBean
   private TemporaryUserService temporaryUserService;

	@SpringBean
   private UserService userService;
	
	@SpringBean
	private ProtocolService protocolService;

	@SpringBean
	private ServiceMetaAttributeService serviceMetaAttributeService;

	@SpringBean
	private ServiceTypeService serviceTypeService;

	private static ServiceFactory factory;

}
