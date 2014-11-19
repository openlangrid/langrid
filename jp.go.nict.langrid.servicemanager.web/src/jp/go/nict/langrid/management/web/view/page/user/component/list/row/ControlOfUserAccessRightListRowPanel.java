package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AccessRightControlModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesChangeLimitPage;
import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesChangeRightConfirmPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.Page;
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
public class ControlOfUserAccessRightListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public ControlOfUserAccessRightListRowPanel(
			String componentId, final String serviceGridId, final String serviceId
			, AccessRightControlModel rightModel, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		arModel = rightModel;
		uModel = ServiceFactory.getInstance().getUserService(arModel.getUserGridId()).get(arModel.getUserId());

		UserProfileLink userLink = new UserProfileLink("provider", arModel.getUserGridId(), arModel.getUserId(), uniqueId);
		userLink.add(new Label("labelProvider", uModel.getOrganization()));
		add(userLink);
		add(new Label("homePage", StringUtil.shortenString(uModel.getHomepageUrl() == null ? "" : uModel.getHomepageUrl() == null ? "" : uModel.getHomepageUrl().getValue().toString(), 24)));
		add(new Label("accessRight", rightModel.isPermitted() ? new Model<String>("permitted")
						: new Model<String>("prohibit")));
		
		Link<String> permitLink = new Link<String>("permit", new Model<String>()){
			@Override
			public void onClick(){
            try {
               LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(arModel.getServiceGridId());
               ServiceModel model = service.get(serviceId);
               setResponsePage(getDoChangeRightConfirmPage(model, true, uModel));
            } catch(ServiceManagerException e) {
               LogWriter.writeError("", e, getClass());
               setResponsePage(RequestResponseUtil.getPageForErrorRequest(e));
            }
			}
			private static final long serialVersionUID = 1L;
		};
		Link<String> prohibitLink = new Link<String>("prohibit"){
			@Override
			public void onClick(){
			   try{
   			   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(serviceGridId);
   			   ServiceModel model = service.get(serviceId);
   				setResponsePage(getDoChangeRightConfirmPage(model, false, uModel));
   			} catch(ServiceManagerException e) {
   			   LogWriter.writeError("", e, getClass());
   			   setResponsePage(RequestResponseUtil.getPageForErrorRequest(e));
   			}
			}
			private static final long serialVersionUID = 1L;
		};
		Link<String> limitLink = new Link<String>("limit"){
			@Override
			public void onClick(){
			   try {
   			   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(serviceGridId);
   			   ServiceModel model = service.get(serviceId);
   				setResponsePage(getDoChangeLimitPage(model, true, arModel.getUserGridId(), arModel.getUserId(), uModel));
   			} catch(ServiceManagerException e) {
   			   LogWriter.writeError("", e, getClass());
   			   setResponsePage(RequestResponseUtil.getPageForErrorRequest(e));
   			}
			}
			private static final long serialVersionUID = 1L;
		};
		
		add(permitLink.setVisible(!rightModel.isPermitted()));
		add(prohibitLink.setVisible(rightModel.isPermitted()));
		add(limitLink.setVisible(rightModel.isPermitted()));
	}
	
	protected Page getDoChangeLimitPage(
			ServiceModel model, boolean permit, String userGridId, String userId, UserModel user)
	{
		return new ControlOfLanguageServicesChangeLimitPage(model, permit, userGridId, userId, user);
	}

	protected Page getDoChangeRightConfirmPage(
			ServiceModel model, boolean permit, UserModel user)
	{
		return new ControlOfLanguageServicesChangeRightConfirmPage(
				model.getServiceId(), permit, user);
	}
	
	private AccessRightControlModel arModel;
//	private ServiceModel sModel;
	private UserModel uModel;
}
