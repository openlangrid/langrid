package jp.go.nict.langrid.dao;

public class AccessLogSearchResultForList extends SearchResult {

	/**
	 * 
	 * 
	 */
	public AccessLogSearchResultForList(AccessLogForList[] elements
			, int totalCount, boolean totalCountFixed){
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * 
	 */
	public AccessLogForList[] getElements() {
		return elements;
	}

	private AccessLogForList[] elements;

}
