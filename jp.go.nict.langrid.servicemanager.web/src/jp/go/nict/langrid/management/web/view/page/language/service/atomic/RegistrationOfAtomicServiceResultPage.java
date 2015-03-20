/*
 * $Id: RegistrationOfAtomicServiceResultPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic;

import java.util.Calendar;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.MultidirectionalLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.ServiceViewPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfAtomicServiceResultPage extends ServiceViewPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfAtomicServiceResultPage(AtomicServiceModel obj) {
		try {
			AtomicServiceService service = ServiceFactory.getInstance().getAtomicServiceService(obj.getGridId());
			String sourceUrl = obj.getSourceCodeUrl();
			add(new HyphenedLabel("sourceUrl", sourceUrl));
			add(new Label("labelHeadResourceName", obj.getResourceId()));
			add(new Label("serviceId", obj.getServiceId()));
			add(new Label("serviceName", obj.getServiceName()));
			add(new Label("serviceType", obj.getServiceType().getTypeId()));
			add(new Label("allowFederation"
            , obj.isFederatedUseAllowed()
               ? MessageManager.getMessage("Common.label.application.federal.Allow", getLocale())
               : MessageManager.getMessage("Common.label.application.federal.allow.Not", getLocale())));
			add(new HyphenedUrlExtractionLabel("description", obj.getServiceDescription()));
			try{
			   add(new MultidirectionalLanguagePathPanel(
			      "language", obj.getSupportedLanguagePathModel()).setRenderBodyOnly(true));
//				add(getLanguagePathComponent("language", obj).setRenderBodyOnly(true));
			}catch(InvalidLanguageTagException e){
				doErrorProcess(new ServiceManagerException(e));
			}
			StringBuilder useSb = new StringBuilder();
         int i = 1;
         Set<String> usages = obj.getAllowedUsage();
         for(String s : usages){
            useSb.append(MessageManager.getMessage(UseType.class.getSimpleName() + "." + s));
            if(i < usages.size()) {
               useSb.append(",&nbsp;");
            }
            i++;
         }
         add(new Label("useTypes", useSb.toString()).setRenderBodyOnly(true).setEscapeModelStrings(false));

         StringBuilder appSb = new StringBuilder();
         i = 1;
         Set<String> provisions = obj.getAllowedAppProvision();
         for(String s : provisions){
            appSb.append(MessageManager.getMessage(AppProvisionType.class.getSimpleName() + "." + s));
            if(i < provisions.size()) {
               appSb.append(",&nbsp;");
            }
            i++;
         }
         add(new Label("appTypes", appSb.toString()).setRenderBodyOnly(true).setEscapeModelStrings(false));

			add(new Label("registration", DateUtil.formatYMDWithSlash(
			   Calendar.getInstance().getTime())));

			ServiceEndpointModel ep = service.getEndpointList(obj.getServiceId()).get(0);
			add(new Label("url", ep.getUrl()));
			add(new Label("authName", ep.getAuthUserName()));
			add(new Label("password", StringUtil.makeMasked(ep.getAuthPassword())));
			
			add(new Label("membersOnlyLabel", obj.isMembersOnly()
            ? MessageManager.getMessage("ProvidingServices.language.service.label.accesible.MembersOnly", getLocale())
               : MessageManager.getMessage("ProvidingServices.language.service.label.accesible.AllUsers", getLocale())));
         WebMarkupContainer wmc = new WebMarkupContainer("membershipContainer");
         wmc.setVisible(obj.isMembersOnly());
         Label membership;
         wmc.add(membership = new Label("howToGetMembershipLabel", obj.getHowToGetMembershipInfo()));
         membership.setEscapeModelStrings(false);
         add(wmc);
			add(getStatusLabel(obj));
		}catch(ServiceManagerException e){
			doErrorProcess(e, "Can't create page.");
      }
	}
	
	private Label getStatusLabel(AtomicServiceModel obj) throws ServiceManagerException{
      ScheduleModel status = ServiceFactory.getInstance().getScheduleService(obj.getGridId()
      ).getNearestStatus(obj.getServiceId(), ScheduleTargetType.SERVICE);

      String statusLabel = "";
      if(status == null){
         if(obj.isApproved()){
            statusLabel = obj.isActive()
               ? MessageManager.getMessage("Common.label.status.Run", getLocale())
               : MessageManager.getMessage("Common.label.status.Suspended", getLocale());
         }else{
            statusLabel = MessageManager.getMessage("Common.label.status.Pending", getLocale());
         }           
      }else{
         statusLabel = StringUtil.toUppercaseHeadCharactor(status.getActionType().name());
         statusLabel += "<br/>("
            + DateUtil.formatYMDWithSlashLocale(status.getBookingDateTime().getTime())
            + ")";
      }
      Label l = new Label("status", statusLabel);
      l.setEscapeModelStrings(false);
      return l;
	}
}