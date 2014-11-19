/*
 * $Id: YourLanguageResourcesPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.NewsService;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.model.provider.ResourceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.ResourceListPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.YourLanguageResourcesListRowPanel;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class YourLanguageResourcesPage extends ServiceManagerPage {
   /**
    * 
    * 
    */
   public YourLanguageResourcesPage() {
      try {
         Form form = new Form("form");
         form.add(getListPanel("resourceList", getProvider()));
         add(form);
         add(new Label("headline", MessageManager.getMessage(
               "ProvidingServices.language.resource.headline.YourLanguageResources"
               , getLocale())).setRenderBodyOnly(true));
         add(new Link("register") {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
               setResponsePage(new RegistrationOfLanguageResourcePage());
            }
         });
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
   }

   protected Panel getListPanel(String listId, SortableDataProvider<ResourceModel> dp) {
      return new ResourceListPanel(getSelfGridId(), listId, dp) {
		/**
		 * 
		 * 
		 */
		
		private static final long serialVersionUID = 1L;

		@Override
         protected Panel getRowPanel(String gridId, Item<ResourceModel> item,
            String uniqueId)
         throws ServiceManagerException {
            return new YourLanguageResourcesListRowPanel(
               gridId, "row", item.getModelObject(), uniqueId)
            {
               /**
                * 
                * 
                */
               @Override
               protected void doCancelProcess(ResourceModel resource) {
                  try {
                     String gridId = getSelfGridId();
                     ScheduleService service = ServiceFactory.getInstance().getScheduleService(gridId);
                     NewsService nService = ServiceFactory.getInstance().getNewsService(gridId);
                     
                     ScheduleModel sm = service.getByConditions(
                           resource.getGridId(), resource.getResourceId()
                           , ScheduleActionType.UNREGISTRATION
                           , ScheduleTargetType.RESOURCE
                           , false);
                     service.delete(sm);
                     
                     NewsModel nm = new NewsModel();
                     nm.setGridId(gridId);
                     Map<String, String> param = new HashMap<String, String>();
                     param.put("id", sm.getTargetId());
                     param.put("name", resource.getResourceName());
                     param.put("date", DateUtil.formatYMDWithSlashLocale(
                        sm.getBookingDateTime().getTime()));
                     nm.setContents(MessageManager.getMessage(
                        "news.resource.language.unregistration.Cancel", param));
                     nService.add(nm);
                     
                     LogWriter.writeInfo(resource.getOwnerUserId()
                           , "\"" + resource.getResourceId()
                           + "\" of language resource has been canceled to transaction."
                           , getPage().getPageClass());

//                     // 関連する原子サービスの予約を削除
//                     List<AtomicServiceModel> list = ServiceFactory.getInstance().getAtomicServiceService(
//                        gridId).getListByRelatedId(resource.getResourceId());
//
//                     for(AtomicServiceModel asm : list){
//                        ScheduleModel rsm = service.getByConditions(asm.getGridId(), asm.getServiceId()
//                           , ScheduleActionType.UNREGISTRATION, ScheduleTargetType.SERVICE
//                           , true);
//                        if(rsm != null) {
//                           service.delete(rsm);
//                        } 
//                        Map<String, String> param2 = new HashMap<String, String>();
//                        param2.put("id", asm.getServiceId());
//                        param2.put("name", asm.getServiceName());
//                        param.put("resourceId", resource.getResourceId());
//                        param.put("resourceName", resource.getResourceName());
//                        param2.put("date", DateUtil.formatYMDWithSlashLocale(
//                           sm.getBookingDateTime().getTime()));
//                        NewsModel nm2 = new NewsModel();
//                        nm2.setContents(MessageManager.getMessage(
//                              "news.service.atomic.unregistration.cancel.ByResource"
//                           , param2));
//                        nm2.setGridId(asm.getGridId());
//                        nService.add(nm2);
//                      }
                     setResponsePage(new UnregistrationOfLanguageResourcesCancelResultPage(resource));
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
               }

               @Override
               protected void doUnregisterProcess(ResourceModel model) {
                  try {
                     ServiceFactory factory = ServiceFactory.getInstance();
                     factory.getResourceService(model.getGridId()).delete(model);
                     factory.getOperationService(model.getGridId(), model.getGridId(), "").deleteByTargetId(
                        model.getResourceId(), OperationType.RESOURCE);
                     
                     setResponsePage(new UnregistrationOfLanguageResourceNowResultPage(model));
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
               }
               
               @Override
               protected String getOrganizationName(String gridId, ResourceModel resource)
               throws ServiceManagerException {
                  return getOrganization(gridId, resource);
               }

               private static final long serialVersionUID = 1L;
            };
         }
      };
   }

   protected SortableDataProvider<ResourceModel> getProvider()
   throws ServiceManagerException
   {
      ResourceSortableDataProvider provider = new ResourceSortableDataProvider(
            getSelfGridId(), getSessionUserId());
      provider.setConditions(new MatchingCondition[]{}
      , new Order[]{
         new Order("resourceName", OrderDirection.ASCENDANT)
      }, Scope.MINE);
      return provider;
   }
}
