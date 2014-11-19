package jp.go.nict.langrid.testresource;

import java.io.FileOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import junit.framework.TestCase;

public class LERedResourceLoaderTest extends TestCase {
	public void test() throws Exception{
		StreamUtil.transfer(
				Services_1_2.ABSTRACTTRANSLATION.loadInstance()
				, new FileOutputStream("test.ler")
				);
	}
}
