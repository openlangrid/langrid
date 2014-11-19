package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import java.util.Calendar;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringLanguageServiceVerbosePage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
public class MonitoringUserListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public MonitoringUserListRowPanel(
			String componentId, ExecutionInformationStatisticsModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		Link link = new UserProfileLink("provider", entry.getUserGridId(), entry.getUserId(), uniqueId);
		link.add(new Label("labelProvider", entry.getUserOrganization()));
		add(link);
		add(new Label("accessCount", String.valueOf(entry.getAccessCount())));
		add(new Label("dataSize", String.valueOf(entry.getResponseBytes())));
		WebMarkupContainer vc = new WebMarkupContainer("visibleContainer");
		vc.add(new Link<ExecutionInformationStatisticsModel>("viewLog", new Model<ExecutionInformationStatisticsModel>(entry)){
			@Override
			public void onClick(){
				setResponsePage(getVerboseMonitoringPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		});
		vc.setVisible(isVerboseMonitoring());
		add(vc);
	}
	
	protected boolean isVerboseMonitoring(){
		return true;
	}
	
	protected Page getVerboseMonitoringPage(ExecutionInformationStatisticsModel uEntry){

//		Calendar start = CalendarUtil.createBeginningOfDay(Calendar.getInstance());
//		Calendar end = CalendarUtil.createEndingOfDay(Calendar.getInstance());
      Calendar start = Calendar.getInstance();
      Calendar end = CalendarUtil.createEndingOfDay(Calendar.getInstance());
      start.add(Calendar.MONTH, -1);
      start.set(Calendar.DAY_OF_MONTH, 1);
      start = CalendarUtil.createBeginningOfDay(start);
		return new MonitoringLanguageServiceVerbosePage(uEntry, start, end, 0);
	}
	
}
