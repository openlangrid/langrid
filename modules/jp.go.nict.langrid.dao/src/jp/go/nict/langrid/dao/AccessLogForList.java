package jp.go.nict.langrid.dao;

import java.io.Serializable;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.Service;

public class AccessLogForList implements Serializable {
	public AccessLogForList(AccessLog accessLog, Service service ) {
		this.accessLog = accessLog;
		this.service   = service;
	}
	
	/**
	 * @return the accessLog
	 */
	public AccessLog getAccessLog() {
		return accessLog;
	}
	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	private AccessLog accessLog;
	private Service service;
}
