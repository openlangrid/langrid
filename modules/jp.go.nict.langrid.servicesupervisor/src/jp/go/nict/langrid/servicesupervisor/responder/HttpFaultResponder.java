package jp.go.nict.langrid.servicesupervisor.responder;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.util.LangridHttpUtil;
import jp.go.nict.langrid.servicesupervisor.ServiceNotFoundException;
import jp.go.nict.langrid.servicesupervisor.ServiceNotActiveException;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException;

public class HttpFaultResponder extends FaultResponder {
	public HttpFaultResponder(String hostName, String gridId, String serviceId){
		this.gridId = gridId;
		this.serviceId = serviceId;
	}

	public void setResponse(HttpServletResponse response){
		this.response = response;
	}

	protected void doRespond(ServiceNotActiveException exception) throws IOException{
		LangridHttpUtil.write403_ServiceNotActive(response, gridId, serviceId);
	}

	protected void doRespond(ServiceNotFoundException e) throws IOException{
		LangridHttpUtil.write404_ServiceNotFound(response, gridId, serviceId);
	}

	protected void doRespond(jp.go.nict.langrid.dao.ServiceNotFoundException e) throws IOException{
		LangridHttpUtil.write404_ServiceNotFound(response, gridId, serviceId);
	}

	protected void doRespond(AccessLimitExceededException e) throws IOException{
		LangridHttpUtil.write403_AccessLimitExceeded(response, gridId, serviceId, e.getMessage());
	}

	protected void doRespond(NoAccessPermissionException e) throws IOException{
		LangridHttpUtil.write403_NoAccessPermission(response, gridId, serviceId);
	}

	protected void doRespond(Throwable e) throws IOException{
		LangridHttpUtil.write500_Exception(response, gridId, serviceId, e);
	}

	private HttpServletResponse response;
	private String gridId;
	private String serviceId;
}
