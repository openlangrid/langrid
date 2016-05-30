package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class NewsModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * 
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 
	 * 
	 */
	public String getContents() {
		return contents;
	}
	/**
	 * 
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	/**
	 * 
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	private int id;
	private String nodeId;
	private String contents;
	
	private static final long serialVersionUID = 7550842110179512309L;	
}
