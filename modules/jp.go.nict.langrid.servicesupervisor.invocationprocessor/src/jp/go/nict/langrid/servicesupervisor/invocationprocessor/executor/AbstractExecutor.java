package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.util.Map;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;

public class AbstractExecutor {
	protected void adjustHeaders(Service service, Map<String, String> headers){
		ServiceContainerType ct = service.getContainerType();
		if(
				(ct != null && !ct.equals(ServiceContainerType.COMPOSITE))
				|| (ct == null && service.getInstanceType().equals(InstanceType.EXTERNAL))
				){
			headers.remove(LangridConstants.HTTPHEADER_FROMADDRESS);
			headers.remove(LangridConstants.HTTPHEADER_CALLNEST);
			headers.remove(LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION);
		}
	}
}
