package jp.go.nict.langrid.commons.lang.test;

import jp.go.nict.langrid.commons.lang.ObjectUtilTest;

public class Impl1Factory {
	public static Object create(){
		return new ObjectUtilTest.Intf1() {
			public void func(Number v) {
			}
		};
	}
}
