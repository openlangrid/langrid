package jp.go.nict.langrid.management.web.model;

import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.ServiceCallLog;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class IndividualExecutionInformationModel extends ServiceInformationModel {
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
	public String getAgent() {
		return agent;
	}

	/**
	 * 
	 * 
	 */
	public int getCallNest() {
		return callNest;
	}

	/**
	 * 
	 * 
	 */
	public String getCallTree() {
		return callTree;
	}

	/**
	 * 
	 * 
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultCode() {
		return faultCode;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultString() {
		return faultString;
	}

	/**
	 * 
	 * 
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * 
	 */
	public String getLicense() {
		return license;
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
	public String getProtocol() {
		return protocol;
	}

	/**
	 * 
	 * 
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * 
	 * 
	 */
	public int getRequestBytes() {
		return requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public String getRequestUri() {
		return requestUri;
	}
	
	/**
	 * 
	 * 
	 */
	public int getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseTimeMillis() {
		return responseTimeMillis;
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
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * 
	 * 
	 */
	public void setCallNest(int callNest) {
		this.callNest = callNest;
	}

	/**
	 * 
	 * 
	 */
	public void setCallTree(String callTree) {
		this.callTree = callTree;
	}

	/**
	 * 
	 * 
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * 
	 * 
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	/**
	 * 
	 * 
	 */
	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}

	/**
	 * 
	 * 
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * 
	 */
	public void setLicense(String license) {
		this.license = license;
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
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * 
	 * 
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestBytes(int requestBytes) {
		this.requestBytes = requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseBytes(int responseBytes) {
		this.responseBytes = responseBytes;
	}
	
	/**
	 * 
	 * 
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseTimeMillis(long responseTimeMillis) {
		this.responseTimeMillis = responseTimeMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	private String address;
	private String agent;
	// composite service properties
	private int callNest;
	private String callTree;
	private String copyright;
	private Calendar dateTime;
	private String faultCode;
	private String faultString;
	private String host;
	private String license;
	private String nodeId;
	private String protocol;
	private String referer;
	private int requestBytes;
	private String requestUri;
	private int responseBytes;
	private int responseCode;
	private long responseTimeMillis;
	private String serviceName;

	private static final long serialVersionUID = -949730811661669497L;
}
