package jp.go.nict.langrid.servicecontainer.executor.javawithfe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServiceContextWrapper;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

public class JavaServiceContext extends ServiceContextWrapper{
	public JavaServiceContext(ServiceContext wrapped
			, Map<String, Object> mimeHeaders
			, List<RpcHeader> headers){
		super(wrapped);
		this.mimeHeaders = mimeHeaders;
		this.headers = headers;
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		return MimeHeadersUtil.fromStringObjectMap(mimeHeaders);
	}
	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders() {
		return headers;
	}

	private Map<String, Object> mimeHeaders = new HashMap<String, Object>();
	private List<RpcHeader> headers = new ArrayList<RpcHeader>();
}
