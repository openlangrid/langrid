package jp.go.nict.langrid.management.web.model;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class NodeModelForList extends ServiceGridModel {
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
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserOrganization() {
		return ownerUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	/**
	 * 
	 * 
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserOrganization(String ownerUserOrganization) {
		this.ownerUserOrganization = ownerUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * 
	 * 
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	private String nodeId;
	private String nodeName;
	// private String nodeType;
	private String url;
	// private String specialNotes;
	// private String cpu;
	// private String memory;
	// private String os;
	private String ownerUserId;
	private String ownerUserOrganization;
	// private Attribute[] attributes;
	private String organizationName;
	private boolean active;

}
