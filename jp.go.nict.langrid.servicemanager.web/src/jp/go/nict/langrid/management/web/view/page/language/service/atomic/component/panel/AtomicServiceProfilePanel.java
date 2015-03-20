package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.panel;

import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.label.CalendarLabel;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.language.component.panel.MultidirectionalLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link.ServiceTypeProfileLink;
import jp.go.nict.langrid.management.web.view.page.language.service.component.panel.InUseListPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class AtomicServiceProfilePanel extends Panel {
	public AtomicServiceProfilePanel(String panelId, String gridId, AtomicServiceModel model)
	throws ServiceManagerException, InvalidLanguageTagException
	{
		super(panelId);

		String serviceId = model.getServiceId();

		UserProfileLink upl = new UserProfileLink("providerLink"
				, model.getGridId(), model.getOwnerUserId(), model.getOwnerUserId());
		String organization;
		UserModel ue = ServiceFactory.getInstance().getUserService(gridId).get(model.getOwnerUserId());
		organization = ue == null ? "" : ue.getOrganization();
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
       if(ResourceModelUtil.isSingleLanguagePathTypel(model.getServiceType().getTypeId())){
          lpm = new LanguagePathModel();
          lpm.addPathArray(
             "supportedLanguages", model.getSupportedLanguagePathModel().getPath("supportedLanguages"));
       }
		add(new MultidirectionalLanguagePathPanel("languagePath", lpm).setRenderBodyOnly(true));
		
		
//		add(getLanguagePathComponent("languagePath", model).setRenderBodyOnly(true));
		
      StringBuilder useSb = new StringBuilder();
      int i = 1;
      Set<String> usages = model.getAllowedUsage();
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
      Set<String> provisions = model.getAllowedAppProvision();
      for(String s : provisions){
         appSb.append(MessageManager.getMessage(AppProvisionType.class.getSimpleName() + "." + s));
         if(i < provisions.size()) {
            appSb.append(",&nbsp;");
         }
         i++;
      }
      add(new Label("appTypes", appSb.toString()).setRenderBodyOnly(true).setEscapeModelStrings(false));

//		Link wsdlLink = makeWsdlLink(model.getGridId(), model.getServiceId());
//		wsdlLink.setPopupSettings(new PopupSettings(PopupSettings.SCROLLBARS | PopupSettings.RESIZABLE));
//		wsdlLink.add(new Label("wsdlLinkLabel", wsdlLink.isEnabled() ? serviceId + ".wsdl" : "-"));
//		add(wsdlLink);
		String url = MessageUtil.getCoreNodeUrl();
		if( ! url.endsWith("/")) {
		   url += "/";
		}
		url += "wsdl/" + model.getGridId() + ":" + model.getServiceId();
	   add(new SmartLinkLabel("wsdlLink", new Model<String>(url)));
	   
		List<ResourceModel> list = new LangridList<ResourceModel>();

		if(model.getResourceId() != null){
			ResourceService rService = ServiceFactory.getInstance().getResourceService(gridId);
			ResourceModel rModel = rService.get(model.getResourceId());
			if(rModel != null){
			   list.add(rModel);
			}
		}
		InUseListPanel iulp = new ResourceInUseListPanel(list);
		iulp.makeList();
		add(iulp);

		String sourceUrl = StringUtil.nullTohyphen(model.getSourceCodeUrl());
		ExternalHomePageLink sourceLink = new ExternalHomePageLink("sourceLink", sourceUrl, serviceId);
		sourceLink.setEnabled(!sourceUrl.equals("-"));
		add(sourceLink);

		add(new CalendarLabel("registreredDateLabel"
			, DateUtil.STR_YMD_SLASH, model.getCreatedDateTime()));
		add(new CalendarLabel("updatedDateLabel"
			, DateUtil.STR_YMD_SLASH, model.getUpdatedDateTime()));

		ScheduleModel sm= ServiceFactory.getInstance().getScheduleService(model.getGridId()).getNearestStatus(
				model.getServiceId(), ScheduleTargetType.SERVICE);

      String statusLabel = "";
      if(sm == null){
         if(model.isApproved()){
            statusLabel = model.isActive()
               ? MessageManager.getMessage("Common.label.status.Run", getLocale())
               : MessageManager.getMessage("Common.label.status.Suspended", getLocale());
         }else{
            statusLabel = MessageManager.getMessage("Common.label.status.Pending", getLocale());
         }
      }else{
         statusLabel = StringUtil.toUppercaseHeadCharactor(sm.getActionType().name());
         statusLabel += " ("
            + DateUtil.formatYMDWithSlashLocale(sm.getBookingDateTime().getTime())
            + ")";
      }
      add(new Label("statusLabel", statusLabel).setEscapeModelStrings(false));
      
      add(new Label("membersOnlyLabel", model.isMembersOnly()
         ? MessageManager.getMessage("ProvidingServices.language.service.label.accesible.MembersOnly", getLocale())
            : MessageManager.getMessage("ProvidingServices.language.service.label.accesible.AllUsers", getLocale())));
      WebMarkupContainer wmc = new WebMarkupContainer("membershipContainer");
      wmc.setVisible(model.isMembersOnly());
      Label membership;
      wmc.add(membership = new Label("howToGetMembershipLabel", model.getHowToGetMembershipInfo()));
      membership.setEscapeModelStrings(false);
      add(wmc);
	}
	
	protected String replaceURLStringToLinkTags(String string){
      String[] urls = StringUtil.extrateUrl(string);
      String result = string;
      for(String url : urls){
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

//	private Component getLanguagePathComponent(String componentId, ServiceModel model)
//	throws InvalidLanguageTagException
//	{
//		if(model.getServiceType().equals(ServiceType.TRANSLATION.name())
//				|| model.getServiceType().equals(ServiceType.TRANSLATIONWITHTEMPORALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARYHEADWORDSEXTRACTION.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARYWITHLONGESTMATCHSEARCH.name())
//				|| model.getServiceType().equals(ServiceType.EDITABLEBILINGUALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXT.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXTWITHEMBEDDEDMETADATA.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXTWITHEXTERNALMETADATA.name())
//				|| model.getServiceType().equals(ServiceType.METADATAFORPARALLELTEXT.name()))
//		{
//			return new MultidirectionalLanguagePathPanel(componentId, model.getSupportedLanguages());
//		}
//
//		return new LanguagePathLabel(componentId, model.getSupportedLanguages());
//	}

//	private Link makeWsdlLink(final String gridId, final String serviceId) throws ServiceManagerException {
//      try{
//         String xml = new String(ServiceFactory.getInstance().getLangridServiceService(
//            gridId).getWsdl(serviceId));
//         Link<String> wsdlLink = new Link<String>("wsdlLink", new  Model<String>(xml)){
//            @Override
//            public void onClick(){
//               StringResourceStream srs = new StringResourceStream(getModelObject(), "text/xml");
//               ResourceStreamRequestTarget rsrt = new ResourceStreamRequestTarget(srs, serviceId + ".wsdl");
//               getRequestCycle().setRequestTarget(rsrt);
//               
//            }
//            private static final long serialVersionUID = 1L;
//         };
//         wsdlLink.setEnabled( ! xml.isEmpty());
//         return wsdlLink;
//      } catch (ServiceManagerException e) {
//         throw e;
//      }
//	}
}
