package jp.go.nict.langrid.webapps.jsontest;

import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.rpc.intf.Parameter;

public class Test implements TestService{
	@Override
	public void func1(
			@Parameter(sample="hello+'&%$#")
			String text) {
		System.out.println("func1(\"" + text + "\")");
	}

	public void func2(byte[] bytes) {
		System.out.println("func2(\"" +
				new String(bytes, CharsetUtil.UTF_8) +
				"\")");
	};
}
