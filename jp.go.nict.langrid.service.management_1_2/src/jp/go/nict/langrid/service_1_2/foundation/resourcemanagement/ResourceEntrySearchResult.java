package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceEntrySearchResult 
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public ResourceEntrySearchResult() {
	}

	/**
	 * 
	 * 
	 */
	public ResourceEntrySearchResult(
			ResourceEntry[] elements, int totaCount, boolean totalCountFixed)
	{
		super(totaCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * 
	 */
	public ResourceEntry[] getElements() {
		return elements;
	}
	
	/**
	 * 
	 * 
	 */
	public void setElements(ResourceEntry[] elements) {
		this.elements = elements;
	}
	
	private ResourceEntry[] elements = new ResourceEntry[]{};
	
	private static final long serialVersionUID = 6796678588903986026L;
	
}
