package jp.go.nict.langrid.wrapper.workflowsupport.analysis;

import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

public class SimpleCodeGenerator implements CodeGenerator {
	@Override
	public String generate(String orgWord, int index) {
		return StringUtil.generateCode(orgWord, index);
	}
}
