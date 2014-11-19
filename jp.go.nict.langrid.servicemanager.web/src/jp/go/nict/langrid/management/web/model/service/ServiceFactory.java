/*
 * $Id: ServiceFactory.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
 * @version $Revision: 406 $
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
	   accessLimitControlService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   accessRightControlService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   atomicServiceService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   compositeServiceService.setScopeParametar(serviceGridId, userGridId, userId);
	   return compositeServiceService;
	}

   public DomainService getDomainService(String gridId) throws ServiceManagerException  {
		return getDomainService(gridId, gridId, "");
	}

	public DomainService getDomainService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException  {
		domainService.setScopeParametar(serviceGridId, userGridId, userId);
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
      langridServiceService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   newsService.setScopeParametar(serviceGridId, userGridId, userId);
	   return newsService;
	}

   public NodeService getNodeService(String gridId) throws ServiceManagerException {
	   return getNodeService(gridId, gridId, "");
	}

	public NodeService getNodeService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException {
	   nodeService.setScopeParametar(serviceGridId, userGridId, userId);
	   return nodeService;
	}

	public OperationRequestService getOperationService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   operationRequestService.setScopeParametar(serviceGridId, userGridId, userId);
	   return operationRequestService;
	}

	public OveruseLimitControlService getOveruseLimitControlService(String serviceGridId, String userGridId) {
	   overuseLimitControlService.setScopeParametar(serviceGridId, userGridId, "");
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
	   resourceService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   resourceMetaAttributeService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   resourceTypeService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   scheduleService.setScopeParametar(serviceGridId, userGridId, userId);
	   return scheduleService;
	}

	public ServiceInformationService getServiceInformationService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   serviceInformationService.setScopeParametar(serviceGridId, userGridId, userId);
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
	   temporaryUserService.setScopeParametar(serviceGridId, userGridId, userId);
	   return temporaryUserService;
	}

	public UserService getUserService(String gridId) throws ServiceManagerException  {
	   return getUserService(gridId, gridId, "");
	}

	public UserService getUserService(String serviceGridId, String userGridId, String userId) throws ServiceManagerException  {
		userService.setScopeParametar(serviceGridId, userGridId, userId);
	   return userService;
	}

	public ProtocolService getProtocolService(String gridId) throws ServiceManagerException {
	   return getProtocolService(gridId,  gridId, "");
	}

	public ProtocolService getProtocolService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException
	{
	   protocolService.setScopeParametar(serviceGridId, userGridId, userId);
	   return protocolService;
	}

	public ServiceMetaAttributeService getServiceMetaAttributeService(String gridId)
	throws ServiceManagerException {
	   return getServiceMetaAttributeService(gridId, gridId, "");
	}

	public ServiceMetaAttributeService getServiceMetaAttributeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException {
	   serviceMetaAttributeService.setScopeParametar(serviceGridId, userGridId, userId);
	   return serviceMetaAttributeService;
	}

	public ServiceTypeService getServiceTypeService(String gridId)
	throws ServiceManagerException {
	   return getServiceTypeService(gridId, gridId, "");
	}

	public ServiceTypeService getServiceTypeService(String serviceGridId, String userGridId, String userId)
	throws ServiceManagerException {
	   serviceTypeService.setScopeParametar(serviceGridId, userGridId, userId);
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
