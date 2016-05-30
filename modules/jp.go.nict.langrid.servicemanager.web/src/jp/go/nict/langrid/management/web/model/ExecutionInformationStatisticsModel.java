package jp.go.nict.langrid.management.web.model;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ExecutionInformationStatisticsModel extends ServiceInformationModel {
	/**
	 * 
	 * 
	 */
	public int getAccessCount() {
		return accessCount;
	}

	/**
	 * 
	 * 
	 */
	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * 
	 * 
	 */
	public void setUserOrganization(String userOrganization) {
		this.userOrganization = userOrganization;
	}

	/**
	 * 
	 * 
	 */
	public long getRequestBytes() {
		return requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestBytes(long requestBytes) {
		this.requestBytes = requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseBytes(long responseBytes) {
		this.responseBytes = responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseMillis() {
		return responseMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseMillis(long responseMillis) {
		this.responseMillis = responseMillis;
	}

	private String serviceName;
	private String userOrganization;
	private int accessCount;
	private long requestBytes;
	private long responseBytes;
	private long responseMillis;

	private static final long serialVersionUID = 5872826160690746609L;
}
