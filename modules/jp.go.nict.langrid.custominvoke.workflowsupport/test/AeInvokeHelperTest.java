import jp.go.nict.langrid.custominvoke.workflowsupport.AeInvokeHelper;
import junit.framework.TestCase;


public class AeInvokeHelperTest extends TestCase {

	public AeInvokeHelperTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSanitize() {
		String str = "%amp;&&&&%amp;";
		System.out.println(AeInvokeHelper.sanitize(str));
	}

}
