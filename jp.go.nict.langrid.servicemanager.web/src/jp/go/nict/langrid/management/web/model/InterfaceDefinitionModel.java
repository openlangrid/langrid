package jp.go.nict.langrid.management.web.model;

import java.sql.Blob;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class InterfaceDefinitionModel extends ServiceManagerModel {
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
	public Blob getDefinition() {
		return definition;
	}

	/**
	 * 
	 * 
	 */
	public void setDefinition(Blob definition) {
		this.definition = definition;
	}

	private String protocolId;
	private Blob definition;

	private static final long serialVersionUID = 8541827940318924877L;
}
