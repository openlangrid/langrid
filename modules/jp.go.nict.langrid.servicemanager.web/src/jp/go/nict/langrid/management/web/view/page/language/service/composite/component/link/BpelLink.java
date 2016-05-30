package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.link;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.StringResourceStream;

public class BpelLink<T> extends Link<T> {
	public BpelLink(String linkId, String serviceGridId, String compositeServiceId) {
		super(linkId);
		gridId = serviceGridId;
		serviceId = compositeServiceId;
	}
	
	@Override
	public void onClick() {
		try {
			byte[] bpel = ServiceFactory.getInstance()
			.getCompositeServiceService(gridId)
			.getBpel(serviceId);
			StringBuffer buf = new StringBuffer();

			buf.append(new String(bpel));
			buf.append("\n");
			
			String xml = buf.toString();
			ClientProperties properties =
				((WebClientInfo)getRequestCycle().getClientInfo())
				.getProperties();
			StringResourceStream srs = null;
			if(properties.isBrowserSafari()) {
				srs = new StringResourceStream(xml, "text/plain");
			} else {
				srs = new StringResourceStream(xml, "text/xml");
			}
			ResourceStreamRequestTarget rsrt = new ResourceStreamRequestTarget(
				srs);
			getRequestCycle().setRequestTarget(rsrt);
		} catch(ServiceManagerException e) {
			e.printStackTrace();
		}
	}
	
	private String gridId;
	private String serviceId;
}
