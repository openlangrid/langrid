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
			for(String h : removeHeadersForAtomicServices){
				headers.remove(h);
			}
		}
	}

	private static String[] removeHeadersForAtomicServices = {
			LangridConstants.HTTPHEADER_FROMADDRESS,
			LangridConstants.HTTPHEADER_CALLNEST,
			LangridConstants.HTTPHEADER_TYPEOFUSE,
			LangridConstants.HTTPHEADER_TYPEOFAPPPROVISION,
			LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION,
			LangridConstants.HTTPHEADER_FEDERATEDCALL_CREATESHORTCUT,
			LangridConstants.HTTPHEADER_FEDERATEDCALL_REMOVESHORTCUT,
			LangridConstants.HTTPHEADER_FEDERATEDCALL_ROUTE,
			LangridConstants.HTTPHEADER_FEDERATEDCALL_VISITED,
	};
}
