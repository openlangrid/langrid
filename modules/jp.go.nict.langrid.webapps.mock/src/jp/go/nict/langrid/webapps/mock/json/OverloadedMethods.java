package jp.go.nict.langrid.webapps.mock.json;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;

public class OverloadedMethods implements Intf1, Intf2{
	@Override
	public String func() {
		return "hello";
	}
	@Override
	public String func(
			@Parameter(name="arg", sample="10") int arg) {
		return "hello" + arg;
	}
}
