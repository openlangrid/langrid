package jp.go.nict.langrid.servicecontainer.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor.HeaderMessageHandler;

public class RIProcessorContext {
	public RIProcessorContext() {
	}

	public RIProcessorContext(ServiceContext context, long processId,
			HeaderMessageHandler headerMessageHandler) {
		this.context = context;
		this.processId = processId;
		this.headerMessageHandler = headerMessageHandler;
		this.serviceLoader = new ServiceLoader(context);
	}

	public ServiceContext getContext() {
		return context;
	}

	public void setContext(ServiceContext context) {
		this.context = context;
	}

	public HeaderMessageHandler getHeaderMessageHandler() {
		return headerMessageHandler;
	}

	public void setHeaderMessageHandler(
			HeaderMessageHandler headerMessageHandler) {
		this.headerMessageHandler = headerMessageHandler;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public List<RIProcessorContext> getSubContexts() {
		return subContexts;
	}

	public ServiceLoader getServiceLoader(){
		return serviceLoader;
	}

	public Object getProperty(String key) {
		return headerMessageHandler.getProperties(processId).get(key);
	}

	public void setProperty(String key, Object value) {
		headerMessageHandler.getProperties(processId).put(key, value);
	}

	@SuppressWarnings("unchecked")
	public void setAdditionalMimeHeader(String name, String value){
		((List<Pair<String, String>>)headerMessageHandler.getProperties(processId).get("additionalMimeHeaders")).add(
				Pair.create(name, value));
	}

	@SuppressWarnings("unchecked")
	public void addResponseHeader(String namespace, String name, String value){
		((Collection<RpcHeader>)headerMessageHandler.getProperties(processId).get("responseHeaders")).add(
				new RpcHeader(namespace, name, value)
				);
	}

	private ServiceContext context;
	private long processId;
	private HeaderMessageHandler headerMessageHandler;
	private List<RIProcessorContext> subContexts = new ArrayList<RIProcessorContext>();
	private ServiceLoader serviceLoader;
}
