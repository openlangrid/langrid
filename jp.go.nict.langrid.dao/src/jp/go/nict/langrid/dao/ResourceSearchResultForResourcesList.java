package jp.go.nict.langrid.dao;

import java.util.List;

public class ResourceSearchResultForResourcesList extends SearchResult {

	public ResourceSearchResultForResourcesList(ResourceForList[] elements,
			int totalCount,
			boolean totalCountFixed,
			List<String> usedResourceIdList) {
		super(totalCount, totalCountFixed);
		this.elements = elements;
		this.usedResourceIdList = usedResourceIdList;
	}
	
	/**
	 * 
	 * 
	 */
	public ResourceForList[] getElements(){
		return elements;
	}
	
	public List<String> getUsedResourceIdList() {
		return usedResourceIdList;
	}

	private ResourceForList[] elements;
	private List<String> usedResourceIdList;
}
