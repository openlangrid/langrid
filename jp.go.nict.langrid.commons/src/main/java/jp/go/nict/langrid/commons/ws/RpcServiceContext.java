package jp.go.nict.langrid.commons.ws;

import java.util.Arrays;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

public class RpcServiceContext extends ServiceContextWrapper{
	public RpcServiceContext(ServiceContext original, RpcHeader[] headers){
		super(original);
		this.headers = headers;
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders() {
		return Arrays.asList(headers);
	}

	private RpcHeader[] headers;
}
