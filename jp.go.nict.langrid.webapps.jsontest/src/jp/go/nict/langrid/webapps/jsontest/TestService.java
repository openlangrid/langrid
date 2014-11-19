package jp.go.nict.langrid.webapps.jsontest;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;

public interface TestService {
	void func1(
			@Parameter(name="text")
			String text);
	void func2(
			@Parameter(name="bytes")
			byte[] bytes);
}
