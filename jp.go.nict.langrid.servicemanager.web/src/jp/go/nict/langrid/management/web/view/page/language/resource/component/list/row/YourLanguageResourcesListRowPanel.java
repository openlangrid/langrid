package jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row;

import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;
import jp.go.nict.langrid.management.web.view.page.language.resource.UnregistrationOfLanguageResourcesPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.YourLanguageResourcesEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class YourLanguageResourcesListRowPanel extends LanguageResourcesListRowPanel {
	/**
	 * 
	 * 
	 */
	public YourLanguageResourcesListRowPanel(
		String gridId, String componentId, ResourceModel resource, String uniqueId)
	throws ServiceManagerException
	{
		super(gridId, componentId, resource, uniqueId);
		this.lResource = resource;
		add(new Link<ResourceModel>("edit", new Model<ResourceModel>(resource)) {
			@Override
			public void onClick() {
				setResponsePage(getDoEditPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		});
		ScheduleService service = ServiceFactory.getInstance().getScheduleService(
			resource.getGridId());
		boolean canUnregister = service.canSchedule(resource.getResourceId()
			, ScheduleTargetType.RESOURCE, ScheduleActionType.UNREGISTRATION);
		boolean hasSchedule = service.getNearestStatus(
			resource.getResourceId(), ScheduleTargetType.RESOURCE) != null;
		add(new Link<ResourceModel>("unregister", new Model<ResourceModel>(resource)) {
			@Override
			public void onClick() {
				if(!getModelObject().isApproved()) {
					doUnregisterProcess(getModelObject());
					return;
				}
				setResponsePage(getDoUnregisterPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(canUnregister && lResource.isApproved()));
		add(new Button("unregister-now") {
			@Override
			public void onSubmit() {
				doUnregisterProcess(lResource);
			}

			@Override
			protected String getOnClickScript() {
				return "return confirm('" + MessageManager.getMessage(
					"ProvidingServices.language.resource.message.unregister.Confirm")
					+ "')";
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(!lResource.isApproved()));
		add(new Link("unregister-off") {
			@Override
			public void onClick() {
			}

			private static final long serialVersionUID = 1L;
		}.setEnabled(false).setVisible(!canUnregister));
		add(new ConfirmLink<ResourceModel>("cancel"
			, new Model<ResourceModel>(lResource)
			, MessageManager.getMessage(
				"ProvidingServices.language.resource.message.unrigester.cancel.Confirm",
				getLocale()))
		{
			@Override
			public void onClick() {
				doCancelProcess(getModelObject());
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(!canUnregister && hasSchedule));
	}

	protected Page getDoUnregisterPage(ResourceModel resource) {
		return new UnregistrationOfLanguageResourcesPage(resource);
	}

	protected Page getDoEditPage(ResourceModel resource) {
		return new YourLanguageResourcesEditPage(resource.getResourceId());
	}

	protected void doCancelProcess(ResourceModel resource) {
		// noop
	}

	protected void doUnregisterProcess(ResourceModel model) {
		// noop
	}

	private ResourceModel lResource;
	private static final long serialVersionUID = 1L;
}
