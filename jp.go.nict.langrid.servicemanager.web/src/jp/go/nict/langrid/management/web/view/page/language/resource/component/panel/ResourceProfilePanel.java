package jp.go.nict.langrid.management.web.view.page.language.resource.component.panel;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.CalendarLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.MultidirectionalLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class ResourceProfilePanel extends Panel {
	public ResourceProfilePanel(String panelId, String gridId, ResourceModel resource)
	throws ServiceManagerException
	{
		super(panelId);
		add(new Label("idLabel", resource.getResourceId()));
		add(new HyphenedLabel("nameLabel", resource.getResourceName()));
		add(new HyphenedUrlExtractionLabel("descriptionLabel",
			resource.getResourceDescription()));
		add(new HyphenedLabel("typeLabel", resource.getResourceType()
			.getResourceTypeName()));
		add(new HyphenedLabel("copyrightLabel",
			replaceURLStringToLinkTags(resource.getCopyrightInfo()))
			.setEscapeModelStrings(false));
		add(new HyphenedLabel("licenseLabel",
			replaceURLStringToLinkTags(resource.getLicenseInfo()))
			.setEscapeModelStrings(false));
		UserModel user = ServiceFactory.getInstance().getUserService(gridId)
			.get(resource.getOwnerUserId());
		Label labelProvider;
		if(user == null) {
			labelProvider = new Label("providerLinkLabel", "");
		} else {
			labelProvider = new Label("providerLinkLabel", user.getOrganization());
		}
		Label noProvider = new Label("noProviderLabel", "");
		noProvider.setVisible(user == null);
		labelProvider.setVisible(user != null);
		add(noProvider);
		add(new UserProfileLink("providerLink", resource.getGridId()
			, resource.getOwnerUserId(), resource.getOwnerUserId()).add(labelProvider));
		add(new CalendarLabel("registreredDateLabel", DateUtil.STR_YMD_SLASH,
			resource.getCreatedDateTime()));
		add(new CalendarLabel("updatedDateLabel", DateUtil.STR_YMD_SLASH,
			resource.getUpdatedDateTime()));
		try {
			LanguagePathModel lpm = resource.getSupportedLanguagePathModel();
			if(ResourceModelUtil.isSingleLanguagePathTypel(resource.getResourceType()
				.getResourceTypeId())) {
				lpm = new LanguagePathModel();
				lpm.addPathArray(
					"supportedLanguages", resource.getSupportedLanguagePathModel()
						.getPath("supportedLanguages"));
			}
			add(new MultidirectionalLanguagePathPanel("languagePathLabel", lpm));
		} catch(InvalidLanguageTagException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
		add(new Label("statusLabel", resource.isApproved()
			? MessageManager.getMessage("Common.label.status.Run")
			: MessageManager.getMessage("Common.label.status.Pending")));
	}

	protected String replaceURLStringToLinkTags(String string) {
		String[] urls = StringUtil.extrateUrl(string);
		String result = string;
		for(String url : urls) {
			StringBuilder sb = new StringBuilder();
			sb.append("<a target=\"_blank\" href=\"");
			sb.append(url);
			sb.append("\">");
			sb.append(url);
			sb.append("</a>");
			result = result.replace(url, sb.toString());
		}
		return result;
	}
}
