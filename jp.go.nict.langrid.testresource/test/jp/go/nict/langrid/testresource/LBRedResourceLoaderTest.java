package jp.go.nict.langrid.testresource;

import java.io.FileOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import junit.framework.TestCase;

public class LBRedResourceLoaderTest extends TestCase {
	public void test2() throws Exception{
		System.out.println(getClass().getClassLoader().getResource(""));
	}

	public void test() throws Exception{
		StreamUtil.transfer(
//				Services_1_2.CLWTCLWT.loadInstance()
				Services_1_2.SPECIALIZEDTRANSLATION.loadInstance()
				, new FileOutputStream("test.zip")
				);
	}
}
