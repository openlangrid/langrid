package jp.go.nict.langrid.serviceexecutor.db.bilingualdictionary;

import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

public class DictionaryQuery {
	public DictionaryQuery(){
	}

	public DictionaryQuery(
			String headWord, MatchingMethod matchingMethod
			, boolean order) {
		this.headWord = headWord;
		this.matchingMethod = matchingMethod;
		this.order = order;
	}

	public String getHeadWord() {
		return headWord;
	}

	public void setHeadWord(String headWord) {
		this.headWord = headWord;
	}

	public MatchingMethod getMatchingMethod() {
		return matchingMethod;
	}

	public void setMatchingMethod(MatchingMethod matchingMethod) {
		this.matchingMethod = matchingMethod;
	}

	public boolean getOrder(){
		return order;
	}

	public void setOrder(boolean order){
		this.order = order;
	}

	private String headWord;
	private MatchingMethod matchingMethod;
	private boolean order;
}
