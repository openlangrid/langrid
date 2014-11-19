package jp.go.nict.langrid.management.web.utility.resource;

import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
@ParameterConfig(loadAllFields = true)
public class ContextInitialMessage {
	public String getCorenodeUrl() {
		return corenodeUrl;
	}

	public void setCorenodeUrl(String corenodeUrl) {
		this.corenodeUrl = corenodeUrl;
	}

	public String getServiceManagerOperatingOrganization() {
		return serviceManagerOperatingOrganization;
	}

	public void setServiceManagerOperatingOrganization(
			String serviceManagerOperatingOrganization) {
		this.serviceManagerOperatingOrganization = serviceManagerOperatingOrganization;
	}

	public String getServiceManagerCopyright() {
		return serviceManagerCopyright;
	}

	public void setServiceManagerCopyright(String serviceManagerCopyright) {
		this.serviceManagerCopyright = serviceManagerCopyright;
	}

	public String getActiveBpelUrl() {
		return activeBpelUrl;
	}

	public void setActiveBpelUrl(String activeBpelUrl) {
		this.activeBpelUrl = activeBpelUrl;
	}

	public void setServiceGridName(String serviceGridName) {
		this.serviceGridName = serviceGridName;
	}

	public String getServiceGridName() {
		return serviceGridName;
	}

	public String getSelfNodeId() {
		return selfNodeId;
	}

	public void setSelfNodeId(String selfNodeId) {
		this.selfNodeId = selfNodeId;
	}

	public void setServiceGridId(String serviceGridId) {
		this.serviceGridId = serviceGridId;
	}

	public String getServiceGridId() {
		return serviceGridId;
	}

	public String getOperatorUserId() {
		return operatorUserId;
	}

	public void setOperatorUserId(String operatorUserId) {
		this.operatorUserId = operatorUserId;
	}

	@Parameter(required = true, name="langrid.node.url", defaultValue = "http://localhost:8080/langrid-2.0/")
	private String corenodeUrl;

	@Parameter(required = true, name="langrid.operator.organization")
	private String serviceManagerOperatingOrganization;

	@Parameter(required = true, name="langrid.serviceManagerCopyright")
	private String serviceManagerCopyright;

	@Parameter(name="langrid.activeBpelServicesUrl")
	private String activeBpelUrl;

	@Parameter(name="langrid.gridName", defaultValue="Service Grid")
	private String serviceGridName;

	@Parameter(name="langrid.node.gridId", defaultValue="ServiceGrid")
	private String serviceGridId;

	@Parameter(name="langrid.node.nodeId")
	private String selfNodeId;

	@Parameter(name="langrid.operator.userId")
	private String operatorUserId;


}
