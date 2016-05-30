/*
 * $Id: ProcessContext.java 407 2011-08-25 02:21:46Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.servicesupervisor.frontend;

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class ProcessContext {
	/**
	 * 
	 * 
	 */
	public ProcessContext(
			User callerUser, Grid targetGrid, Service targetService, Node processingNode
			, DaoContext daoContext
			, AccessRightDao accessRightDao, AccessLimitDao accessLimitDao
			, AccessStatDao accessStateDao, AccessLogDao accessLogDao)
	{
		this.callerUser = callerUser;
		this.targetGrid = targetGrid;
		this.targetService = targetService;
		this.processingNode = processingNode;
		this.daoContext = daoContext;
		this.accessRightDao = accessRightDao;
		this.accessLimitDao = accessLimitDao;
		this.accessStateDao = accessStateDao;
		this.accessLogDao = accessLogDao;
	}

	/**
	 * 
	 * 
	 */
	public User getCallerUser() {
		return callerUser;
	}

	/**
	 * 
	 * 
	 */
	public void setCallerUser(User user){
		this.callerUser = user;
	}

	/**
	 * 
	 * 
	 */
	public Grid getTargetGrid() {
		return targetGrid;
	}

	/**
	 * 
	 * 
	 */
	public Service getTargetService() {
		return targetService;
	}

	/**
	 * 
	 * 
	 */
	public Node getProcessingNode() {
		return processingNode;
	}

	/**
	 * 
	 * 
	 */
	public DaoContext getDaoContext() {
		return daoContext;
	}

	/**
	 * 
	 * 
	 */
	public AccessRightDao getAccessRightDao(){
		return accessRightDao;
	}

	/**
	 * 
	 * 
	 */
	public AccessLimitDao getAccessLimitDao(){
		return accessLimitDao;
	}

	/**
	 * 
	 * 
	 */
	public AccessStatDao getAccessStateDao(){
		return accessStateDao;
	}

	/**
	 * 
	 * 
	 */
	public AccessLogDao getAccessLogDao(){
		return accessLogDao;
	}

	private User callerUser;
	private Grid targetGrid;
	private Service targetService;
	private Node processingNode;
	private DaoContext daoContext;
	private AccessRightDao accessRightDao;
	private AccessLimitDao accessLimitDao;
	private AccessStatDao accessStateDao;
	private AccessLogDao accessLogDao;
}
