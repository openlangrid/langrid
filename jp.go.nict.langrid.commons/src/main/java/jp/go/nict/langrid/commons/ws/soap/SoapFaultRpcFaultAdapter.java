package jp.go.nict.langrid.commons.ws.soap;

import javax.xml.soap.SOAPFault;

import jp.go.nict.langrid.commons.rpc.RpcFault;

public class SoapFaultRpcFaultAdapter extends RpcFault{
	public SoapFaultRpcFaultAdapter(SOAPFault f) {
		super(f.getFaultCode(), f.getFaultString(), f.getDetail().getTextContent());
	}
}
