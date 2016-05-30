package jp.go.nict.langrid.management.web.view.page.language.service.component.list.row;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.service.UnregistrationOfLanguageServicesPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.AtomicServiceEditPage;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.BpelCompositeServiceEditPage;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.JavaCompositeServiceEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class YourLanguageServicesListRowPanel extends LanguageServicesListRowPanel{
	/**
	 * 
	 * 
	 */
	public YourLanguageServicesListRowPanel(String componentId, ServiceModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId, model, uniqueId);
      ScheduleModel sm = ServiceFactory.getInstance().getScheduleService(model.getGridId()
         ).getNearestStatus(model.getServiceId(), ScheduleTargetType.SERVICE);

      boolean isRelated = sm != null && sm.isRelated();
      boolean canUnregister = ServiceFactory.getInstance().getScheduleService(model.getGridId()
         ).canSchedule(model.getServiceId()
            , ScheduleTargetType.SERVICE, ScheduleActionType.UNREGISTRATION);

      final String serviceId = model.getServiceId();
      add(new Link<InstanceType>("edit", new Model<InstanceType>(model.getInstanceType())){
			@Override
			public void onClick(){
				if(getModelObject().equals(InstanceType.BPEL)){
					setResponsePage(getEditCompositeServicePage(serviceId));
				} else if (getModelObject().equals(InstanceType.Java)){
					setResponsePage(getEditJavaCompositeServicePage(serviceId));
				} else {
					setResponsePage(getEditAtomicServicePage(serviceId));
				}
			}
			private static final long serialVersionUID = 1L;
		});

		add(new Link<String>("unregister", new Model<String>(model.getServiceId())) {
			@Override
			public void onClick(){
				setResponsePage(getUnregisterServicePage(getModelObject()));
			}
			private static final long serialVersionUID = 1L;
		}.setVisible(canUnregister && model.isApproved()));

		add(new ConfirmLink<String>("unregister-now", new Model<String>(model.getServiceId())
		   , MessageManager.getMessage("ProvidingServices.language.service.message.unregister.Confirm"))
		{
		   @Override
		   public void onClick(){
		      doUnregistUnapprovedService(getModelObject());
		   }
		}.setVisible( ! model.isApproved()));

		add(new Link("unregister-off"){
	      @Override
	      public void onClick(){}
	   	private static final long serialVersionUID = 1L;
	   }.setEnabled(false).setVisible( ! canUnregister && model.isApproved()));


		Link<String> cancelLink = new ConfirmLink<String>("cancel"
				, new Model<String>(model.getServiceId())
				, MessageManager.getMessage("ProvidingServices.language.service.message.unregister.cancel.Confirm", getLocale()))
		{
			@Override
			public void onClick(){
				doCancelProcess(getModelObject());
			}
			private static final long serialVersionUID = 1L;
		};

		cancelLink.setVisible( ! canUnregister && ! isRelated && ! model.isActive());
		add(cancelLink);
	}

	protected Page getEditAtomicServicePage(String serviceId){
		return new AtomicServiceEditPage(serviceId);
	}

	protected Page getEditCompositeServicePage(String serviceId){
		return new BpelCompositeServiceEditPage(serviceId);
	}

	protected Page getEditJavaCompositeServicePage(String serviceId){
		return new JavaCompositeServiceEditPage(serviceId);
	}

	protected Page getUnregisterServicePage(String serviceId){
		return new UnregistrationOfLanguageServicesPage(serviceId);
	}

	protected void doCancelProcess(String serviceId){
	}

	protected void doUnregistUnapprovedService(String serviceId){
      // noop
   }

	private static final long serialVersionUID = 1L;
}
