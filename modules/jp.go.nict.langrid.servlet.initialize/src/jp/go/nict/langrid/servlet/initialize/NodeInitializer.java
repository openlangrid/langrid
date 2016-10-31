/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servlet.initialize;

import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.ws.param.ServletContextParameterContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.User;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public class NodeInitializer
extends HttpServlet{
	@ParameterConfig(loadAllFields=true, prefix="langrid.node.")
	public static class NodeParams{
		@Parameter(required=true)
		private String gridId;
		private String gridName;

		@Parameter(required=true)
		private String nodeId;

		@Parameter(required=true)
		private String name;

		@Parameter(required=true)
		private URL url;

		@Parameter(required=true, defaultValue="90")
		private int initialPasswordExpirationDate;

		private boolean autoApproveEnabled = true;
		private URL gridHostUrl;
		private String accessToken;
		private String ownerUserId;
		private String ownerUserOrganization;
		
		private String os;
		private String cpu;
		private String memory;
		private String specialNotes;
	}

	@ParameterConfig(loadAllFields=true, prefix="langrid.operator.")
	private static class OperatorParams{
		@Parameter(required=true)
		private String userId;
//		@Parameter(required=true)
		private String initialPassword;
//		@Parameter(required=true)
		private String organization;
//		@Parameter(required=true)
		private String responsiblePerson;
//		@Parameter(required=true)
		private String emailAddress;
//		@Parameter(required=true)
		private URL homepageUrl;
//		@Parameter(required=true)
		private String address;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		try{
			DaoFactory f = DaoFactory.createInstance();

			ParameterLoader pl = new ParameterLoader();
			ParameterContext c = new ServletContextParameterContext(config.getServletContext());

			NodeParams np = new NodeParams();
			pl.load(np, c);
			OperatorParams op = new OperatorParams();
			pl.load(op, c);
			if(np.ownerUserId == null){
				np.ownerUserId = op.userId;
			}

			initGrid(f, op.userId, np);
			initNode(f, np);
			if(np.gridHostUrl == null){
				initUser(f, np.gridId, op);
			}
		} catch(ParameterRequiredException e){
			throw new ServletException(e);
		} catch(DaoException e){
			throw new ServletException(e);
		}
	}

	private void initGrid(DaoFactory factory, String gridOwnerUserId, NodeParams np) throws DaoException{
		DaoContext c = factory.getDaoContext();
		c.beginTransaction();
		try{
			GridDao gdao = factory.createGridDao();
			if(!gdao.isGridExist(np.gridId)){
				Grid g = new Grid(np.gridId, gridOwnerUserId);
				g.setAutoApproveEnabled(np.autoApproveEnabled);
				g.setCommercialUseAllowed(false);
				if(np.gridHostUrl != null){
					g.setUrl(np.gridHostUrl.toString());
					g.setHosted(false);
				} else{
					g.setUrl(np.url.toString());
					g.setHosted(true);
				}
				if(np.gridName != null){
					g.setGridName(np.gridName);
				} else{
					g.setGridName(np.gridId);
				}
				gdao.addGrid(g);
			} else{
				Grid g = gdao.getGrid(np.gridId);
				if(g.getGridName() == null){
					g.setGridName(g.getGridId());
				} else if(np.gridName != null && !g.getGridName().equals(np.gridName)){
					g.setGridName(np.gridName);
				}
			}
			c.commitTransaction();
		} catch(DaoException e){
			c.rollbackTransaction();
			throw e;
		}
	}

	private void initNode(DaoFactory factory, NodeParams p) throws DaoException{
		DaoContext c = factory.getDaoContext();
		c.beginTransaction();
		try{
			SystemPropertyDao spDao = factory.createSystemPropertyDao();
			String expday = spDao.getProperty(p.gridId, SystemPropertyDao.PASSWORD_EXPIRATION_DAYS);
			if(expday == null){
				spDao.setProperty(
						p.gridId
						, SystemPropertyDao.PASSWORD_EXPIRATION_DAYS
						, Integer.toString(p.initialPasswordExpirationDate)
						);
			}
			NodeDao ndao = factory.createNodeDao();
			if(!ndao.isNodeExist(p.gridId, p.nodeId)){
				Node node = new Node(
						p.gridId, p.nodeId, p.name
						, p.url, p.ownerUserId, p.ownerUserOrganization, true
						, p.os, p.cpu, p.memory, p.specialNotes
						);
				if(p.gridHostUrl != null){
					node.setMirror(true);
					node.setAccessToken(p.accessToken);
				}
				ndao.addNode(node);
			}
			c.commitTransaction();
		} catch(DaoException e){
			c.rollbackTransaction();
			throw e;
		}
	}

	private void initUser(DaoFactory factory, String gridId, OperatorParams p) throws DaoException{
		DaoContext daoContext = factory.getDaoContext();
		UserDao userDao = factory.createUserDao();
		if(userDao.listAllUsers(gridId).size() > 0){
			return;
		}
		logger.info("No user found. Initialize user information.");
		daoContext.beginTransaction();
		try{
			User u = new User(
					gridId, p.userId
					, MessageDigestUtil.digestBySHA512(p.initialPassword)
					);
			u.setOrganization(p.organization);
			u.setRepresentative(p.responsiblePerson);
			u.setEmailAddress(p.emailAddress);
			u.setHomepageUrl(p.homepageUrl);
			u.setAddress(p.address);
			userDao.addUser(u, "langriduser", "langridadmin");
			logger.info("operator user \"" + p.userId + "\" added.");
			daoContext.commitTransaction();
		} catch(DaoException e){
			daoContext.rollbackTransaction();
			throw e;
		}
	}

	private static Logger logger = Logger.getLogger(NodeInitializer.class.getName());

	private static final long serialVersionUID = 4670511416199285649L;
}
