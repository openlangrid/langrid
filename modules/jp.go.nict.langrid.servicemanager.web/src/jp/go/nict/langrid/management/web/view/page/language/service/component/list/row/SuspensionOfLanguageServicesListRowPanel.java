package jp.go.nict.langrid.management.web.view.page.language.service.component.list.row;

import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.SuspensionOfLanguageServicePage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SuspensionOfLanguageServicesListRowPanel extends LanguageServicesListRowPanel{
	/**
	 * 
	 * 
	 */
	public SuspensionOfLanguageServicesListRowPanel(
			String componentId, ServiceModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId, model, uniqueId);
		ScheduleService service = ServiceFactory.getInstance().getScheduleService(model.getGridId());
		ScheduleModel sm = service.getNearestStatus(model.getServiceId(), ScheduleTargetType.SERVICE);
		
		boolean canSuspend = ServiceFactory.getInstance().getScheduleService(model.getGridId()
		   ).canSchedule(model.getServiceId(), ScheduleTargetType.SERVICE
         , ScheduleActionType.SUSPENSION) && model.isActive();
		boolean canCancel = ! canSuspend && sm != null
		   && sm.getActionType().equals(ScheduleActionType.SUSPENSION);
		boolean canRestart = ! model.isActive();
		
		add(new Link<String>("suspend", new Model<String>(model.getServiceId())){
			@Override
			public void onClick(){
				setResponsePage(getDoSuspendPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(canSuspend));
		
		final String serviceId = model.getServiceId();
		add(new Button("cancel"){
			@Override
			public void onSubmit(){
				doCancelProcess(serviceId);
			}
			@Override
			protected String getOnClickScript(){
				return "return confirm('"+
				MessageManager.getMessage("ProvidingServices.language.service.message.suspension.cancel.Confirm", getLocale())
				+"')";
			}
			
			private static final long serialVersionUID = 1L;
		}.setVisible(canCancel));
		
		Button b;
		add(b = new Button("restart-on"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(){
				doRestartProcess(serviceId);
			}
			@Override
			protected String getOnClickScript(){
				return "return confirm('"+
				MessageManager.getMessage("ProvidingServices.language.service.message.restart.Confirm", getLocale())
				+"')";
			}
		});
		b.setVisible(canRestart).setEnabled(sm == null);
		if( ! b.isEnabled()){
		   b.add(new AttributeModifier("class", new Model<String>("btn-off")));
		}
	}
	
	protected void doCancelProcess(String serviceId){
		// noop
	}
	
	protected void doRestartProcess(String serviceId){
		// noop
	}
	
	protected Page getDoSuspendPage(String serviceId){
		return new SuspensionOfLanguageServicePage(serviceId);
	}

	private static final long serialVersionUID = 1L;
}
