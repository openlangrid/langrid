package jp.go.nict.langrid.dao;

import jp.go.nict.langrid.dao.entity.Service;

public class ServiceSearchResultForServiceList extends SearchResult {
	/**
	 * 
	 * 
	 */
	public ServiceSearchResultForServiceList(
			ServiceForList[] objects
			, int totalCount
			, boolean totalCountFixed
			)
	{
		super(totalCount, totalCountFixed);
		this.elements = objects;
	}

	/**
	 * 
	 * 
	 */
	public ServiceForList[] getElements(){
		return elements;
	}

	private ServiceForList[] elements;
}
