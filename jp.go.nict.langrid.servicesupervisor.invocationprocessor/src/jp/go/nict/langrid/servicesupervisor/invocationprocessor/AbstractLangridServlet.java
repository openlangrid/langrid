/*
 * $Id: AbstractLangridServlet.java 552 2012-08-06 10:20:56Z t-nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServletParameterContext;
import jp.go.nict.langrid.commons.ws.util.LangridHttpUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.UserDao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 552 $
 */
public abstract class AbstractLangridServlet extends HttpServlet{
	@Override
	public void init() throws ServletException {
		try{
			daoFactory = DaoFactory.createInstance();
			gridDao = daoFactory.createGridDao();
			federationDao = daoFactory.createFederationDao();
			userDao = daoFactory.createUserDao();
			temporaryUserDao = daoFactory.createTemporaryUserDao();
			serviceDao = daoFactory.createServiceDao();
			nodeDao = daoFactory.createNodeDao();
		} catch(DaoException e){
			throw new ServletException(e);
		}
	}

	protected ParameterContext getParameterContext(){
		return new ServletParameterContext(this);
	}

	protected abstract void doProcess(
			HttpServletRequest request, HttpServletResponse response
			, ServiceContext serviceContext, DaoContext daoContext
			, String userGridId, String userId, String gridId, String serviceId, String query)
	throws IOException, ServletException;

	protected DaoFactory getDaoFactory(){
		return daoFactory;
	}

	protected GridDao getGridDao(){
		return gridDao;
	}

	protected FederationDao getFederationDao(){
		return federationDao;
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

	protected ServiceDao getServiceDao(){
		return serviceDao;
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
	throws ServletException, IOException{
		DaoContext c = getDaoFactory().getDaoContext();
		ServletServiceContext context = new ServletServiceContext(request);
		String[] callerUser = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				);
		if(callerUser != null){
			String[] userGridIdAndId = StringUtil.join(callerUser, ",").split(":");
			context.setAuthorized(userGridIdAndId[0], userGridIdAndId[1], context.getAuthPass());
		} else if(context.getAuthUser().contains(":")){
			String[] userGridIdAndId = context.getAuthUser().split(":");
			context.setAuthorized(userGridIdAndId[0], userGridIdAndId[1], context.getAuthPass());
		} else if(context.getAuthUserGridId() == null){
			context.setAuthorized(context.getSelfGridId(), context.getAuthUser(), context.getAuthPass());
		}

		String userGridId = context.getAuthUserGridId();
		String userId = context.getAuthUser();

		Trio<String, String, String> gsq = HttpServletRequestUtil.parseRequestUrl(request);
		String serviceGridId = gsq.getFirst();
		if(serviceGridId == null) serviceGridId = context.getSelfGridId();

		String serviceId = gsq.getSecond();
		if(serviceId == null){
			LangridHttpUtil.write500_InternalServerError(response, serviceGridId, serviceId);
			return;
		}

		doProcess(
				request, response, context, c, userGridId, userId, serviceGridId, serviceId, gsq.getThird()
				);
	}

	private DaoFactory daoFactory;
	private GridDao gridDao;
	private FederationDao federationDao;
	private UserDao userDao;
	private TemporaryUserDao temporaryUserDao;
	private ServiceDao serviceDao;
	private NodeDao nodeDao;
	private static final long serialVersionUID = 5951721154555359089L;
}
