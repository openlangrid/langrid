package jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row;

import java.util.Collection;
import java.util.HashSet;

import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.LimitedLabel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.link.LanguageResourceProfileLink;
import jp.go.nict.langrid.management.web.view.page.user.component.label.OrganizationLabel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LanguageResourcesListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public LanguageResourcesListRowPanel(String gridId,
			String componentId, ResourceModel resource, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new LimitedLabel("resourceType", resource.getResourceType().getResourceTypeName(), 20));

		// ad hoc
		Collection<String> set = resource.getSupportedLanguagePathModel().getAllKeySet();
		if(ResourceModelUtil.isSingleLanguagePathTypel(resource.getResourceType().getResourceTypeId())) {
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
		for(String key : set){
		   if(MetaAttribute.containName(key.toUpperCase())){
		      sb.append(
		         jp.go.nict.langrid.management.web.utility.LanguagePathUtil.makeMargedLanguagePathCodeString(
		            resource.getSupportedLanguagePathModel().getCollectedPath(key)
		            , MetaAttribute.valueOf(key.toUpperCase()), getLocale()));
//         sb.append(LanguagePathUtil.encodeToSimplifiedExpressionByCode(paths));
		   }else{
		      sb.append(resource.getSupportedLanguagePathModel().getOtherLanguage(key));
		   }
      }
		add(new Label("languages", StringUtil.nullTohyphen(StringUtil.shortenString(sb.toString(), 40))));

		
		add(new Label("status", getStatusString(resource)).setEscapeModelStrings(false));
		LanguageResourceProfileLink link = new LanguageResourceProfileLink(
				"resourceName", resource.getGridId(), resource.getResourceId(), uniqueId);
		link.add(new Label("labelResourceName", resource.getResourceName())
				.add(new AttributeAppender("style", new Model<String>(StringUtil
						.getLimitStyle(resource.getResourceName(), 16)), "")));
		add(link);
		Link link2 = new UserProfileLink("provider", resource.getGridId(), resource.getOwnerUserId()
				, uniqueId);
		link2.add(new OrganizationLabel("labelProvider", getOrganizationName(gridId, resource)));
		add(link2);
	}
	
	protected String getOrganizationName(String gridId, ResourceModel resource)
	throws ServiceManagerException
	{
		UserModel ue = ServiceFactory.getInstance().getUserService(gridId).get(resource.getOwnerUserId());
		return ue == null ? "" : ue.getOrganization();
	}

	private String getStatusString(ResourceModel resource) throws ServiceManagerException{
		ScheduleModel sm = ServiceFactory.getInstance().getScheduleService(resource.getGridId()).getNearestStatus(
		   resource.getResourceId(), ScheduleTargetType.RESOURCE);
		if(sm == null){
			if(resource.isApproved()){
				return resource.isActive()
						? MessageManager.getMessage("Common.label.status.Run", getLocale())
						: MessageManager.getMessage("Common.label.status.Suspended", getLocale());
			}else{
				return MessageManager.getMessage("Common.label.status.Pending", getLocale());
			}
		}else{
		   String status = StringUtil.toUppercaseHeadCharactor(sm.getActionType().name());
			status += "<br/>("
					+ DateUtil.formatYMDWithSlashLocale(sm.getBookingDateTime().getTime()) + ")";
			return status;
		}
	}
	
	private static final long serialVersionUID = 1L;
}
