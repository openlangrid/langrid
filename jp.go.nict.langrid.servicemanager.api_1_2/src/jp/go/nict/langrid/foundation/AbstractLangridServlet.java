/*
 * $Id: AbstractLangridServlet.java 920 2013-08-15 09:12:55Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServletParameterContext;
import jp.go.nict.langrid.dao.ConnectException;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.UserDao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 920 $
 */
public abstract class AbstractLangridServlet extends HttpServlet{
	protected ParameterContext getParameterContext(){
		return new ServletParameterContext(this);
	}

	protected abstract void doPrepare(DaoFactory daoFactory)
	throws DaoException;

	protected abstract void doProcess(
			HttpServletRequest request, HttpServletResponse response
			, ServiceContext serviceContext, DaoContext daoContext
			, String userId, String serviceGridId, String serviceId
			)
	throws ConnectException, DaoException, IOException
	, ServletException, ServiceNotFoundException;

	protected DaoFactory getDaoFactory(){
		return daoFactory;
	}

	protected GridDao getGridDao(){
		return gridDao;
	}

	protected NodeDao getNodeDao(){
		return nodeDao;
	}

	protected UserDao getUserDao(){
		return userDao;
	}

	protected TemporaryUserDao getTemporaryUserDao(){
		return temporaryUserDao;
	}

	protected ServiceTypeDao getServiceTypeDao(){
		return serviceTypeDao;
	}

	protected ServiceDao getServiceDao(){
		return serviceDao;
	}

	protected SystemPropertyDao getSystemPropertyDao(){
		return systemPropertyDao;
	}

	protected String getGridId(){
		return getParameterContext().getValue("langrid.node.gridId");
	}

	protected final synchronized void prepare()
	throws DaoException{
		if(daoFactory != null) return;
		daoFactory = DaoFactory.createInstance();
		gridDao = daoFactory.createGridDao();
		userDao = daoFactory.createUserDao();
		temporaryUserDao = daoFactory.createTemporaryUserDao();
		serviceTypeDao = daoFactory.createServiceTypeDao();
		serviceDao = daoFactory.createServiceDao();
		nodeDao = daoFactory.createNodeDao();
		systemPropertyDao = daoFactory.createSystemPropertyDao();
		doPrepare(daoFactory);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		try{
			prepare();
			DaoContext c = getDaoFactory().getDaoContext();
			ServletServiceContext context = new ServletServiceContext(
					request, new ArrayList<RpcHeader>());
			String userId = context.getAuthUser();
			String serviceId = null;
			String pathInfo = request.getPathInfo();
			if(pathInfo != null && pathInfo.length() > 1){
				serviceId = pathInfo.substring(1);
			}
			Trio<String, String, String> gsq = HttpServletRequestUtil.parseRequestUrl(request);
			String serviceGridId = gsq.getFirst();
			if(serviceGridId == null) serviceGridId = context.getSelfGridId();
			if(serviceId == null){
				throw new jp.go.nict.langrid.dao.ServiceNotFoundException(serviceGridId, "null");
			}
			String[] v = serviceId.split(":");
			if(v.length == 2){
				serviceGridId = v[0];
				serviceId = v[1];
			}
			
			serviceGridId = serviceGridId.contains("kyotou.langrid")
				? serviceGridId.replaceAll("kyotou\\.langrid", "kyoto1.langrid")
				: serviceGridId;
			doProcess(
					request, response, context, c, userId, serviceGridId, serviceId
					);
		} catch(DaoException e){
			throw new ServletException(e);
		} catch(SocketException e){
			logger.log(
					Level.SEVERE
					, "socket exception occurred."
					, e);
			response.getOutputStream().close();
		}
	}

	private DaoFactory daoFactory;
	private GridDao gridDao;
	private UserDao userDao;
	private TemporaryUserDao temporaryUserDao;
	private ServiceTypeDao serviceTypeDao;
	private ServiceDao serviceDao;
	private NodeDao nodeDao;
	private SystemPropertyDao systemPropertyDao;
	private static Logger logger = Logger.getLogger(AbstractLangridServlet.class.getName());
	private static final long serialVersionUID = 5951721154555359089L;
}
