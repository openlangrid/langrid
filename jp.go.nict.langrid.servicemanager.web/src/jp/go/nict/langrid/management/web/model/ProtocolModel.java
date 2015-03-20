package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ProtocolModel extends ServiceManagerModel {
	/**
	 * 
	 * 
	 */
	public String getProtocolId() {
		return protocolId;
	}

	/**
	 * 
	 * 
	 */
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	/**
	 * 
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserGridId() {
		return ownerUserGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserGridId(String ownerUserGridId) {
		this.ownerUserGridId = ownerUserGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getOwneruserid() {
		return ownerUserId;
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
	public String getProtocolName() {
		return protocolName;
	}

	/**
	 * 
	 * 
	 */
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	private String protocolId;
	private String description;
	private String ownerUserGridId;
	private String ownerUserId;
	private String protocolName;
	
	private static final long serialVersionUID = -2819998309562965932L;
}
