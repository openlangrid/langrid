package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel;

import java.util.Set;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.label.CalendarLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.MultidirectionalLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link.ServiceTypeProfileLink;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.link.BpelLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class CompositeServiceProfilePanel extends Panel {
	public CompositeServiceProfilePanel(
		String panelId, String gridId, CompositeServiceModel model)
	throws ServiceManagerException, InvalidLanguageTagException
	{
		super(panelId);
		String serviceId = model.getServiceId();
		UserProfileLink upl = new UserProfileLink("providerLink", model.getGridId()
			, model.getOwnerUserId(), model.getOwnerUserId());
		UserModel ue = ServiceFactory.getInstance().getUserService(gridId).get(model.getOwnerUserId());
		String organization = ue == null ? "" : ue.getOrganization();
		upl.add(new Label("providerLinkLabel", organization));
		add(upl);
		add(new Label("idLabel", model.getServiceId()));
		add(new Label("nameLabel", model.getServiceName()));

		// add(new Label("typeLabel", model.getServiceType().getTypeName()));

		ServiceTypeModel stm = model.getServiceType();
		ServiceTypeProfileLink link = new ServiceTypeProfileLink("typeLink"
				, stm.getDomainId(), stm.getTypeId(), stm.getDomainId() + ":" + stm.getTypeId());
		link.add(new Label("typeLinkLabel", stm.getTypeName()));
		add(link);

		add(new Label("allowFederation"
			, model.isFederatedUseAllowed()
				? MessageManager.getMessage("Common.label.application.federal.Allow", getLocale())
				: MessageManager.getMessage("Common.label.application.federal.allow.Not", getLocale())));
		add(new HyphenedUrlExtractionLabel("descriptionLabel", model.getServiceDescription()));
		LanguagePathModel lpm = model.getSupportedLanguagePathModel();
		if(ResourceModelUtil.isSingleLanguagePathTypel(model.getServiceType().getTypeId())) {
			lpm = new LanguagePathModel();
			lpm.addPathArray("supportedLanguages",
				model.getSupportedLanguagePathModel().getPath("supportedLanguages"));
		}
		add(new MultidirectionalLanguagePathPanel("languagePath", lpm).setRenderBodyOnly(true));
		//		add(new MultidirectionalLanguagePathPanel("languagePath", model.getSupportedLanguagePathModel()).setRenderBodyOnly(true));
		//		add(getLanguagePathComponent("languagePath", model).setRenderBodyOnly(true));
		StringBuilder useSb = new StringBuilder();
		int i = 1;
		Set<String> usages = model.getAllowedUsage();
		for(String s : usages) {
			useSb.append(MessageManager.getMessage(UseType.class.getSimpleName() + "."
				+ s));
			if(i < usages.size()) {
				useSb.append(",&nbsp;");
			}
			i++;
		}
		add(new Label("useTypes", useSb.toString()).setRenderBodyOnly(true).setEscapeModelStrings(false));
		StringBuilder appSb = new StringBuilder();
		i = 1;
		Set<String> provisions = model.getAllowedAppProvision();
		for(String s : provisions) {
			appSb.append(MessageManager.getMessage(AppProvisionType.class.getSimpleName() + "." + s));
			if(i < provisions.size()) {
				appSb.append(",&nbsp;");
			}
			i++;
		}
		add(new Label("appTypes", appSb.toString()).setRenderBodyOnly(true).setEscapeModelStrings(false));
		String url = MessageUtil.getCoreNodeUrl();
		if(!url.endsWith("/")) {
			url += "/";
		}
		url += "wsdl/" + model.getGridId() + ":" + model.getServiceId();
		add(new SmartLinkLabel("wsdlLink", new Model<String>(url)));
		// for composite service
		Link bpelLink = makeBpelLink(model);
		bpelLink.setPopupSettings(new PopupSettings(PopupSettings.SCROLLBARS | PopupSettings.RESIZABLE));
		bpelLink.add(new Label("bpelLinkLabel", serviceId + ".bpel"));
		add(bpelLink);
		ServiceInUseListPanel iulp = new ServiceInUseListPanel(gridId, model.getInvocations());
		iulp.makeList();
		add(iulp);
		add(new CalendarLabel("registreredDateLabel"
			, DateUtil.STR_YMD_SLASH, model.getCreatedDateTime()));
		add(new CalendarLabel("updatedDateLabel"
			, DateUtil.STR_YMD_SLASH, model.getUpdatedDateTime()));
		ScheduleModel sm = ServiceFactory.getInstance().getScheduleService(gridId).getNearestStatus(
				model.getServiceId(), ScheduleTargetType.SERVICE);
		String statusLabel = "";
		if(sm == null) {
			if(model.isApproved()) {
				statusLabel = model.isActive()
					? MessageManager.getMessage("Common.label.status.Run", getLocale())
					: MessageManager.getMessage("Common.label.status.Suspended",
						getLocale());
			} else {
				statusLabel = MessageManager.getMessage("Common.label.status.Pending", getLocale());
			}
		} else {
			statusLabel = StringUtil.toUppercaseHeadCharactor(sm.getActionType().name());
			statusLabel += " ("
				+ DateUtil.formatYMDWithSlashLocale(sm.getBookingDateTime().getTime())
				+ ")";
		}
		add(new Label("statusLabel", statusLabel).setEscapeModelStrings(false));
		add(new Label("membersOnlyLabel", model.isMembersOnly()
			? MessageManager.getMessage(
				"ProvidingServices.language.service.label.accesible.MembersOnly", getLocale())
			: MessageManager.getMessage(
				"ProvidingServices.language.service.label.accesible.AllUsers", getLocale())));
		WebMarkupContainer wmc = new WebMarkupContainer("membershipContainer");
		wmc.setVisible(model.isMembersOnly());
		add(wmc);
		Label membership = new Label("howToGetMembershipLabel", model.getHowToGetMembershipInfo());
		membership.setEscapeModelStrings(false);
		wmc.add(membership);
	}

	private Link makeBpelLink(CompositeServiceModel model) {
		final InstanceType instancetype = model.getInstanceType();
		return new BpelLink("bpelLink", model.getGridId(), model.getServiceId()){
			@Override
			public boolean isVisible() {
				return InstanceType.BPEL.equals(instancetype);
			}
		};
	}
}
