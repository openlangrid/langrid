package jp.go.nict.langrid.management.web.view.page.language.service.component.list.row;

import java.util.Collection;
import java.util.HashSet;

import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.LimitedLabel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link.ServiceTypeProfileLink;
import jp.go.nict.langrid.management.web.view.page.language.service.component.link.ServiceProfileLink;
import jp.go.nict.langrid.management.web.view.page.user.component.label.OrganizationLabel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LanguageServicesListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public LanguageServicesListRowPanel(
			String componentId, ServiceModel obj, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		ServiceTypeProfileLink typeLink = new ServiceTypeProfileLink("serviceType", obj.getServiceType().getDomainId()
         , obj.getServiceType().getTypeId(), uniqueId);
		add(typeLink);
		typeLink.add(new LimitedLabel("labelServiceType"
		      , obj.getServiceType().getTypeName(), 20
		   ).setEscapeModelStrings(false));
		typeLink.setEnabled( ! obj.getServiceType().getTypeId().equals("Other"));
	// ad hoc
      Collection<String> set = obj.getSupportedLanguagePathModel().getAllKeySet();
      if(ResourceModelUtil.isSingleLanguagePathTypel(obj.getServiceType().getTypeId())) {
         Collection<String> keySet = new HashSet<String>();
         for(String s : set){
            if(s.equals(MetaAttribute.SUPPORTEDLANGUAGES.getName())){
               keySet.add(s);
               break;
            }
         }
         set = keySet;
      }
      //
      StringBuilder sb = new StringBuilder();
		for(String key: set){
		   if(MetaAttribute.containName(key.toUpperCase())){
		      sb.append(
		         jp.go.nict.langrid.management.web.utility.LanguagePathUtil.makeMargedLanguagePathCodeString(
		            obj.getSupportedLanguagePathModel().getCollectedPath(key), MetaAttribute.valueOf(key.toUpperCase()), getLocale()));
		   }else{
		      sb.append(obj.getSupportedLanguagePathModel().getOtherLanguage(key));
		   }
		}
		add(new Label("languages", StringUtil.nullTohyphen(StringUtil.shortenString(sb.toString(), 40))));
//		add(new Label("languages", StringUtil.shortenString(
//		   LanguagePathUtil.encodeToSimplifiedExpressionByCode(
//		      obj.getSupportedLanguages()), 40)));
		
		ServiceProfileLink link = new ServiceProfileLink("serviceName"
				, obj.getGridId(), obj.getServiceId(), uniqueId);
		link.add(new Label("labelServiceName", obj.getServiceName())
				.add(new AttributeAppender("style", new Model<String>(
						StringUtil.getLimitStyle(obj.getServiceName(), 16)), "")));
		add(link);

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
		add(new Label("status", statusLabel).setEscapeModelStrings(false));

		UserProfileLink userLink = new UserProfileLink("provider"
				, obj.getGridId(), obj.getOwnerUserId(), uniqueId);
		userLink.add(new OrganizationLabel("labelProvider", getOrganizationName(obj)));
		add(userLink);
	}
	
	protected String getOrganizationName(ServiceModel obj) throws ServiceManagerException{
		return "";
	}
}
