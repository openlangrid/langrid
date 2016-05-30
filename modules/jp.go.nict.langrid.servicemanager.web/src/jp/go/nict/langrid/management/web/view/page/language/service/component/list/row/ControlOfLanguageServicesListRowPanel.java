package jp.go.nict.langrid.management.web.view.page.language.service.component.list.row;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ControlOfLanguageServicesListRowPanel
extends LanguageServicesListRowPanel
{
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesListRowPanel(
			String componentId, ServiceModel model, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId, model, uniqueId);

		add(new Link<String>("control", new Model<String>(model.getServiceId())){
			@Override
			public void onClick(){
				setResponsePage(getDoControlPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		});
	}
	
	protected Page getDoControlPage(String serviceId){
		return new ControlOfLanguageServicesPage(serviceId);
	}
}
