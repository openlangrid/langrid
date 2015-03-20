package jp.go.nict.langrid.dao;

import java.util.List;

public class NodeSearchResultForNodesList extends SearchResult {

	public NodeSearchResultForNodesList(NodeForList [] elements, int totalCount, boolean totalCountFixed) {
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}
	
	/**
	 * 
	 * 
	 */
	public NodeForList[] getElements(){
		return elements;
	}
	
	private NodeForList[] elements;
}