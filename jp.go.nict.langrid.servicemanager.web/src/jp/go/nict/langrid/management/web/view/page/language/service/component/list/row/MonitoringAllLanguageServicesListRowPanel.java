package jp.go.nict.langrid.management.web.view.page.language.service.component.list.row;

import java.util.Calendar;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringAllLanguageServiceStatisticPage;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MonitoringAllLanguageServicesListRowPanel extends LanguageServicesListRowPanel
{
	/**
	 * 
	 * 
	 */
	public MonitoringAllLanguageServicesListRowPanel(
			String componentId, ServiceModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId, model, uniqueId);
		final String serviceName = model.getServiceName();
		final String serviceId = model.getServiceId();
		final String serviceGridId = model.getGridId();
		Link<String> link = new Link<String>("monitor", new Model<String>())
		{
			@Override
			public void onClick(){
				Calendar start = Calendar.getInstance();
				Calendar end = CalendarUtil.createEndingOfDay(Calendar.getInstance());
				start.add(Calendar.MONTH, -1);
	         start.set(Calendar.DAY_OF_MONTH, 1);
	         start = CalendarUtil.createBeginningOfDay(start);
				setResponsePage(new MonitoringAllLanguageServiceStatisticPage(
				   serviceGridId, serviceId, serviceName, start, end));
			}

			private static final long serialVersionUID = 1L;
		};
		add(link);
	}

	private static final long serialVersionUID = 1L;
}
