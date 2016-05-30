package jp.go.nict.langrid.management.web.model;

import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.EmbeddableStringValueClass;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.dao.entity.UserAttribute;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class UserModel extends ServiceGridModel  {
	/**
	 * 
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, UserAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * 
	 */
	public String getDefaultAppProvisionType() {
		return defaultAppProvisionType;
	}
	
	/**
	 * 
	 * 
	 */
	public String getDefaultUseType() {
		return defaultUseType;
	}

	/**
	 * 
	 * 
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * 
	 * 
	 */
	public EmbeddableStringValueClass<URL> getHomepageUrl() {
		return homepageUrl;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getLastAuthenticatedDate() {
		return lastAuthenticatedDate;
	}

	/**
	 * 
	 * 
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * 
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getPasswordChangedDate() {
		return passwordChangedDate;
	}

	/**
	 * 
	 * 
	 */
	public String getRepresentative() {
		return representative;
	}

	/**
	 * 
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public boolean isAbleToCallServices() {
		return ableToCallServices;
	}

	/**
	 * 
	 * 
	 */
	public void setAbleToCallServices(boolean ableToCallServices) {
		this.ableToCallServices = ableToCallServices;
	}

	/**
	 * 
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributes(Map<String, UserAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * 
	 */
	public void setDefaultAppProvisionType(String defaultAppProvisionType) {
		this.defaultAppProvisionType = defaultAppProvisionType;
	}

	/**
	 * 
	 * 
	 */
	public void setDefaultUseType(String defaultUseType) {
		this.defaultUseType = defaultUseType;
	}

	/**
	 * 
	 * 
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * 
	 * 
	 */
	public void setHomepageUrl(EmbeddableStringValueClass<URL> homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	/**
	 * 
	 * 
	 */
	public void setLastAuthenticatedDate(Calendar lastAuthenticatedDate) {
		this.lastAuthenticatedDate = lastAuthenticatedDate;
	}

	/**
	 * 
	 * 
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * 
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public void setPasswordChangedDate(Calendar passwordChangedDate) {
		this.passwordChangedDate = passwordChangedDate;
	}

	/**
	 * 
	 * 
	 */
	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	private String userId;
	private String password;
	// profile
	private String organization;
	private String address;
	private String representative;
	private String emailAddress;
	private EmbeddableStringValueClass<URL> homepageUrl;
	// service attributes
	private String defaultAppProvisionType;
	private String defaultUseType;
	private boolean ableToCallServices;
	// attributes
	private Map<String, UserAttribute> attributes;
	private Calendar passwordChangedDate;
	private Calendar lastAuthenticatedDate;

	private static final long serialVersionUID = 35499654835023577L;
}
