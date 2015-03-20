package jp.go.nict.langrid.service_1_3.foundation.federation;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

public class GridEntrySearchResult extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public GridEntrySearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Array of user entries
	 * @param totalCount Total number of user entry data
	 * @param totalCountFixed Whether or not we fixed the total number
	 * 
	 */
	public GridEntrySearchResult(
			GridEntry[] elements, int totalCount
			, boolean totalCountFixed)
	{
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Sets user entry array.
	 * @param elements User entry array specified
	 * 
	 */
	public void setElements(GridEntry[] elements){
		this.elements = elements;
	}

	/**
	 * 
	 * Returns user entry array.
	 * @return Array of user entries
	 * 
	 */
	public GridEntry[] getElements(){
		return elements;
	}

	private GridEntry[] elements = new GridEntry[]{};	

	private static final long serialVersionUID = -8177480506813972668L;
}
