/*
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
package jp.go.nict.langrid.servicesupervisor.frontend.processors.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.LogInfo;
import jp.go.nict.langrid.servicesupervisor.frontend.LogProcess;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class AccessStat implements LogProcess{
	public void process(
			ProcessContext context, LogInfo logInfo
			, String faultCode, String faultString)
	throws SystemErrorException
	{
		if(logInfo.getResponseCode() != HttpServletResponse.SC_OK) return;
		try{
			User u = context.getCallerUser();
			Service s = context.getTargetService();
			Node n = context.getProcessingNode();
			context.getAccessStateDao().increment(
					u.getGridId(), u.getUserId()
					, s.getGridId(), s.getServiceId(), n.getNodeId()
					, logInfo.getRequestBytes(), logInfo.getResponseBytes()
					, (int)logInfo.getResponseMillis()
					);
		} catch(DaoException e){
			logger.log(Level.SEVERE, "exception when adding log.", e);
			throw new SystemErrorException(
					ExceptionUtil.getMessageWithStackTrace(e)
					);
		}
	}

	private static Logger logger = Logger.getLogger(AccessStat.class.getName());
}
