package jp.go.nict.langrid.management.web.model;

import jp.go.nict.langrid.dao.entity.OperationType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class OperationRequestModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * 
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * 
	 */
	public String getRequestedUserId() {
		return requestedUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 
	 * 
	 */
	public OperationType getType() {
		return type;
	}

	/**
	 * 
	 * 
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestedUserId(String requestedUserId) {
		this.requestedUserId = requestedUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * 
	 * 
	 */
	public void setType(OperationType type) {
		this.type = type;
	}

	/**
	 * 
	 * 
	 */
	public void setId(int id) {
		this.id = id;
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
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	private int id;
	private String requestedUserId;
	private String targetId;
	private String nodeId;
	private OperationType type;
	private String contents;

	private static final long serialVersionUID = -6788686646636083728L;
}
