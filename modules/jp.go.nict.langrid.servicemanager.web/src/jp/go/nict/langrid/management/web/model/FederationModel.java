package jp.go.nict.langrid.management.web.model;

import java.io.Serializable;
import java.net.URL;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FederationModel implements Serializable {
	/**
	 * 
	 * 
	 */
	public String getSourceGridId() {
		return sourceGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getSourceGridName() {
		return sourceGridName;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridAccessToken() {
		return targetGridAccessToken;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridId() {
		return targetGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridName() {
		return targetGridName;
	}

	/**
	 * 
	 * 
	 */
	public URL getTargetGridUserHomepage() {
		return targetGridUserHomepage;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridUserId() {
		return targetGridUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetGridUserOrganization() {
		return targetGridUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public boolean isRequesting() {
		return requesting;
	}

	/**
	 * 
	 * 
	 */
	public void setRequesting(boolean requesting) {
		this.requesting = requesting;
	}

	/**
	 * 
	 * 
	 */
	public void setSourceGridId(String gridId) {
		this.sourceGridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setSourceGridName(String sourceGridName) {
		this.sourceGridName = sourceGridName;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridAccessToken(String targetGridAccessToken) {
		this.targetGridAccessToken = targetGridAccessToken;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridId(String gridId) {
		this.targetGridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridName(String targetGridName) {
		this.targetGridName = targetGridName;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridUserHomepage(URL targetGridUserHomepage) {
		this.targetGridUserHomepage = targetGridUserHomepage;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridUserId(String targetGridUserId) {
		this.targetGridUserId = targetGridUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetGridUserOrganization(String targetGridUserOrganization) {
		this.targetGridUserOrganization = targetGridUserOrganization;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean disconnected) {
		this.connected = disconnected;
	}

	public boolean isSymmetricRelationEnabled() {
		return symmetricRelationEnabled;
	}
	
	public void setSymmetricRelationEnabled(boolean symmetricRelationEnabled) {
		this.symmetricRelationEnabled = symmetricRelationEnabled;
	}
	
	public boolean isTransitiveRelationEnabled() {
		return transitiveRelationEnabled;
	}
	
	public void setTransitiveRelationEnabled(boolean transitiveRelationEnabled) {
		this.transitiveRelationEnabled = transitiveRelationEnabled;
	}

	private boolean requesting;
	private String sourceGridId;
	private String sourceGridName;
	private String targetGridAccessToken;
	private String targetGridId;
	private String targetGridName;
	private URL targetGridUserHomepage;
	private String targetGridUserId;
	private String targetGridUserOrganization;
	private boolean connected;
	private boolean symmetricRelationEnabled;
	private boolean transitiveRelationEnabled;
	private static final long serialVersionUID = 5175155697194974301L;
}
