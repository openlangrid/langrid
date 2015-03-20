package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class DomainModel extends ServiceDomainModel {
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
	public String getDomainName() {
		return domainName;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserGridId() {
		return ownerusergridid;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserId() {
		return owneruserid;
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
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserGridId(String ownerusergridid) {
		this.ownerusergridid = ownerusergridid;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserId(String owneruserid) {
		this.owneruserid = owneruserid;
	}

	private String domainName;
	private String description;
	private String ownerusergridid;
	private String owneruserid;

	private static final long serialVersionUID = 620293376010106741L;
}
