package jp.go.nict.langrid.wrapper.workflowsupport.analysis;

import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

public class MarkingCodeGenerator
extends SimpleCodeGenerator
implements CodeGenerator{
	@Override
	public String generate(String orgWord, int index) {
		return StringUtil.markingWord(super.generate(orgWord, index), markingCount++);
	}
	private int markingCount = 1;
}
