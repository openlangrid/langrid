package jp.go.nict.langrid.management.web.model;

import jp.go.nict.langrid.service_1_2.foundation.Attribute;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NodeModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getCpu() {
		return cpu;
	}

	/**
	 * 
	 * 
	 */
	public String getMemory() {
		return memory;
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
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 
	 * 
	 */
	public String getOs() {
		return os;
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
	public String getSpecialNotes() {
		return specialNotes;
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
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	/**
	 * 
	 * 
	 */
	public void setMemory(String memory) {
		this.memory = memory;
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
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * 
	 * 
	 */
	public void setOs(String os) {
		this.os = os;
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
	public void setSpecialNotes(String specialNotes) {
		this.specialNotes = specialNotes;
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
	protected Attribute[] getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * 
	 */
	protected void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	private String nodeId;
	private String nodeName;
	private String nodeType;
	private String url;
	private String specialNotes;
	private String cpu;
	private String memory;
	private String os;
	private String ownerUserId;
	private String ownerUserOrganization;
	private Attribute[] attributes;
	private boolean active;

}
