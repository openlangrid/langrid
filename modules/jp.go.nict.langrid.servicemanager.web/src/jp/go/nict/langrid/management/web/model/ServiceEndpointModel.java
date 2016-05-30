package jp.go.nict.langrid.management.web.model;

import java.util.Calendar;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceEndpointModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 
	 */
	public String getAuthUserName() {
		return authUserName;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}
	
	/**
	 * 
	 * 
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	/**
	 * 
	 * 
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * 
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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
	

	public long getAverage() {
		return average;
	}

	public void setAverage(long average) {
		this.average = average;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public Calendar getDisabledByErrorDate() {
		return disabledByErrorDate;
	}

	public void setDisabledByErrorDate(Calendar disabledByErrorDate) {
		this.disabledByErrorDate = disabledByErrorDate;
	}

	public String getDisableReason() {
		return disableReason;
	}

	public void setDisableReason(String disableReason) {
		this.disableReason = disableReason;
	}

	public long getExperience() {
		return experience;
	}

	public void setExperience(long experience) {
		this.experience = experience;
	}

	private String gridId;
	private String serviceId;
	private String url;
	private String authUserName = "";
	private String authPassword = "";
	private String protocolId = "";
	private boolean enabled;
	private long average = 0;

	private Calendar disabledByErrorDate;
	private String disableReason;
	private long experience = 0;
}
