/*
 * $Id: RegistrationOfJavaCompositeServiceResultPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite;

import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.MultidirectionalLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.ServiceViewPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.panel.InUseListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel.ServiceInUseListPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfJavaCompositeServiceResultPage extends
ServiceViewPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfJavaCompositeServiceResultPage(String serviceId) {
		try {
			CompositeServiceService service = ServiceFactory.getInstance()
				.getCompositeServiceService(getSelfGridId());
			JavaCompositeServiceModel model = (JavaCompositeServiceModel)service
				.get(serviceId);
			if(model == null) {
				throw new ServiceManagerException("\"" + serviceId + "\" is not found");
			}
			add(new Label("registration", DateUtil.formatYMDWithSlash(
					model.getCreatedDateTime().getTime())));
			add(new Label("serviceId", model.getServiceId()));
			add(new Label("serviceName", model.getServiceName()));
			add(new Label("serviceType", model.getServiceType().getTypeId()));
			String des = model.getServiceDescription().replaceAll("\\n", "<br/>");
			add(new Label("discription", des));
			try {
				add(new MultidirectionalLanguagePathPanel("language",
					model.getSupportedLanguagePathModel()));
			} catch(InvalidLanguageTagException e) {
				new ServiceManagerException(e);
			}
			StringBuilder useSb = new StringBuilder();
			int i = 1;
			Set<String> usages = model.getAllowedUsage();
			for(String s : usages) {
				useSb.append(MessageManager.getMessage(UseType.class.getSimpleName()
					+ "." + s));
				if(i < usages.size()) {
					useSb.append(",&nbsp;");
				}
				i++;
			}
			add(new Label("useTypes", useSb.toString()).setRenderBodyOnly(true)
				.setEscapeModelStrings(false));
			StringBuilder appSb = new StringBuilder();
			i = 1;
			Set<String> provisions = model.getAllowedAppProvision();
			for(String s : provisions) {
				appSb.append(MessageManager.getMessage(AppProvisionType.class
					.getSimpleName() + "." + s));
				if(i < provisions.size()) {
					appSb.append(",&nbsp;");
				}
				i++;
			}
			add(new Label("appTypes", appSb.toString()).setRenderBodyOnly(true)
				.setEscapeModelStrings(false));
			add(new Label("allowFederation"
				, model.isFederatedUseAllowed()
					? MessageManager.getMessage("Common.label.application.federal.Allow",
						getLocale())
					: MessageManager.getMessage(
						"Common.label.application.federal.allow.Not", getLocale())));
			String gridId = getSelfGridId();
			InUseListPanel iulp = new ServiceInUseListPanel(gridId,
				model.getInvocations());
			iulp.makeList();
			add(iulp);
			add(new Label("membersOnlyLabel", model.isMembersOnly()
				? MessageManager.getMessage(
					"ProvidingServices.language.service.label.accesible.MembersOnly",
					getLocale())
				: MessageManager.getMessage(
					"ProvidingServices.language.service.label.accesible.AllUsers",
					getLocale())));
			WebMarkupContainer membersCotainer = new WebMarkupContainer(
				"membershipContainer");
			membersCotainer.setVisible(model.isMembersOnly());
			Label membership;
			membersCotainer.add(membership = new Label("howToGetMembershipLabel", model
				.getHowToGetMembershipInfo()));
			membership.setEscapeModelStrings(false);
			add(membersCotainer);
			List<ServiceEndpointModel> epList = service.getEndpointList(serviceId);
			RepeatingView rv = new RepeatingView("repeater");
			for(ServiceEndpointModel ep : epList) {
				WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
				wmc.add(new HyphenedLabel("url", ep.getUrl()));
				wmc.add(new HyphenedLabel("userName"
					, ep.getAuthUserName()));
				wmc.add(new HyphenedLabel("password"
					, StringUtil.makeMasked(ep.getAuthPassword())));
				rv.add(wmc);
			}
			add(rv);
			add(new HyphenedLabel("sourceUrl", model.getSourceCodeUrl()));
			add(getStatusLabel(model));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	private Label getStatusLabel(CompositeServiceModel obj)
	throws ServiceManagerException {
		ScheduleModel status = ServiceFactory.getInstance()
			.getScheduleService(obj.getGridId()
			).getNearestStatus(obj.getServiceId(), ScheduleTargetType.SERVICE);
		String statusLabel = "";
		if(status == null) {
			if(obj.isApproved()) {
				statusLabel = obj.isActive()
					? MessageManager.getMessage("Common.label.status.Run", getLocale())
					: MessageManager.getMessage("Common.label.status.Suspended",
						getLocale());
			} else {
				statusLabel = MessageManager.getMessage("Common.label.status.Pending",
					getLocale());
			}
		} else {
			statusLabel = StringUtil.toUppercaseHeadCharactor(status.getActionType()
				.name());
			statusLabel += "<br/>("
				+ DateUtil
					.formatYMDWithSlashLocale(status.getBookingDateTime().getTime())
				+ ")";
		}
		Label l = new Label("status", statusLabel);
		l.setEscapeModelStrings(false);
		return l;
	}
}
