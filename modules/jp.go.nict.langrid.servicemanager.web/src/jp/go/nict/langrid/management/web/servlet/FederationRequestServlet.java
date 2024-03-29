/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.web.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.OperationRequest;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.enumeration.FederationGridType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class FederationRequestServlet extends HttpServlet{
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException {
		ServiceContext sc = new ServletServiceContext(request);
		RequestType rt;
		// do validate
		if(request.getParameter("requestType") == null || request.getParameter("requestType").equals("")) {
			throw new ServletException("parameter not found: 'requestType' is required.");
		} else {
			try {
				rt = RequestType.valueOf(request.getParameter("requestType"));
			} catch(Exception e) {
				throw new ServletException("Invalid parameter: 'requestType'");
			}
		}
		// do request process
		try {
			selfGridId = ServiceFactory.getInstance().getGridService().getSelfGridId();
			if(rt.equals(RequestType.CONNECT)){
				Grid g = DaoFactory.createInstance().createGridDao().getGrid(selfGridId);
				boolean autoApproveEnabled = g.isAutoApproveEnabled();
				FederationRequest req = JSON.decode(request.getParameter("request"), FederationRequest.class);
				req.setRequestUserId(sc.getAuthUser());
				FederationResponse res = doConnectFederationRequest(
						sc.getAuthUser(), req, autoApproveEnabled);
				response.setContentType("text/plain");
				JSON.encode(res, response.getOutputStream());
			} else if(rt.equals(RequestType.DISCONNECT)) {
				DisconnectRequest req = JSON.decode(request.getParameter("request"), DisconnectRequest.class);
				DisconnectResponse res = new DisconnectResponse(doDisconnectFederationRequset(req));
				response.setContentType("text/plain");
				JSON.encode(res, response.getOutputStream());
			} else{
				throw new ServletException("invalid request type: " + rt);
			}
		} catch(IOException e) {
			e.printStackTrace();
			logger.severe("Can't get response connection: " + e.getMessage());
			throw new ServletException("Can't get response connection", e);
		} catch(DaoException e) {
			e.printStackTrace();
			throw new ServletException("Can't accept your request", e);
		} catch(ServiceManagerException e) {
			e.printStackTrace();
			throw new ServletException("Internal server error", e);
		}
	}

	/**
	 * 
	 * 
	 */
	private boolean doDisconnectFederationRequset(DisconnectRequest request) {
		Boolean	response = true;
		String federationSourceGridId = request.getFederationSourceGridId();
		String federationTargetGridId = request.getFederationTargetGridId();		

		try {
			String deleteGridId = request.getRequestedGridType().equals(FederationGridType.SOURCEGRID)
					? federationSourceGridId : federationTargetGridId;
			FederationService fs = ServiceFactory.getInstance().getFederationService(selfGridId);
			fs.setConnected(federationSourceGridId, federationTargetGridId, false);
			
			// log for federation
			LogWriter.writeInfo("Operator", MessageManager.getMessage(
				"LanguageGridOperator.federation.log.disconnect.Federation"
				, Locale.ENGLISH, federationSourceGridId, federationTargetGridId)
				, getClass());
			
			if ( fs.get(federationTargetGridId, federationSourceGridId) == null ) {
				try {
					GridService gs = ServiceFactory.getInstance().getGridService();
					GridModel gm;
					if((gm = gs.get(deleteGridId)) != null){
						gs.delete(gm);
						// log for grid
						LogWriter.writeInfo("Operator", MessageManager.getMessage(
							"LanguageGridOperator.federation.log.disconnect.delete.Grid"
							, Locale.ENGLISH, deleteGridId)
							, getClass());
					}
				} catch(ServiceManagerException e) {
					LogWriter.writeError("Operator", e, getClass());
				}
			}
			
			// add news
			NewsModel nm = new NewsModel();
			nm.setGridId(selfGridId);
			Map<String, String> map = new HashMap<String, String>();
			map.put("gridId", deleteGridId);
			nm.setContents(MessageManager.getMessage("news.federation.Disonnected", map));
			// set node id
			nm.setNodeId(MessageUtil.getSelfNodeId());
			ServiceFactory.getInstance().getNewsService(selfGridId).add(nm);
			
		} catch(ServiceManagerException e) {
			LogWriter.writeWarn("Operator", e.getMessage(), getClass());
			response = false;
		}
		return response;
	}

	/**
	 * 
	 * 
	 */
	private FederationResponse doConnectFederationRequest(
			String userId, FederationRequest req, boolean autoApproveEnabled)
	throws DaoException, ServletException{
		GridModel sourceGrid = req.getSourceGrid();
		String sourceUrl = req.getSourceGrid().getUrl();
		String requestedUserId = userId;

		String selfGridId = getServletContext().getInitParameter("langrid.node.gridId");
		String selfNodeId = getServletContext().getInitParameter("langrid.node.nodeId");
		String selfOrganization = getServletContext().getInitParameter("langrid.operator.organization");
		String selfHomepage = getServletContext().getInitParameter("langrid.operator.homepageUrl");

		ServiceFactory f = ServiceFactory.getInstance();
		try{
			FederationService fs = f.getFederationService(selfGridId);
			GridService gs = f.getGridService();
			GridModel gm = gs.get(selfGridId);
			
			{
				FederationModel fm = fs.get(sourceGrid.getGridId(), selfGridId);
				if(fm == null){
					fm = new FederationModel();
					fm.setConnected(false);
					fm.setRequesting(true);
					fm.setSymmetricRelationEnabled(req.isSymmetric());
					fm.setTransitiveRelationEnabled(req.isTransitive());
					fm.setTargetGridAccessToken(req.getToken());
					fm.setSourceGridId(sourceGrid.getGridId());
					fm.setSourceGridName(sourceGrid.getGridName());
					fm.setTargetGridId(selfGridId);
					fm.setTargetGridName(gm.getGridName());
					fm.setTargetGridUserHomepage(new URL(selfHomepage));
					fm.setTargetGridUserId(requestedUserId);
					fm.setTargetGridUserOrganization(selfOrganization);
					fs.add(fm);
				}else {
					fs.setRequesting(sourceGrid.getGridId(), selfGridId, true);
				}
			}
			LogWriter.writeInfo("Operator"
				, MessageManager.getMessage(
					"LanguageGridOperator.federation.log.connect.Federation"
					, Locale.ENGLISH, userId, selfGridId)
				, getClass());
			
			FederationResponse fr = new FederationResponse();
			fr.setTargetGrid(gm);
			fr.setOperatorOrganization(selfOrganization);
			fr.setOperatorHomepage(selfHomepage);
			fr.setApproved(autoApproveEnabled);
			if(autoApproveEnabled) {
				fs.setConnected(sourceGrid.getGridId(), selfGridId, true);
				fs.setRequesting(sourceGrid.getGridId(), selfGridId, false);
				if(gs.get(req.getSourceGrid().getGridId()) == null) {
					gs.add(req.getSourceGrid());
					LogWriter.writeInfo("Operator"
						, MessageManager.getMessage(
							"LanguageGridOperator.federation.log.connect.registration.Grid"
							, Locale.ENGLISH, sourceGrid.getGridId())
						, getClass());
				}
				try {
					// add news
					NewsModel nm = new NewsModel();
					nm.setGridId(selfGridId);
					Map<String, String> map = new HashMap<String, String>();
					map.put("gridId", sourceGrid.getGridId());
					nm.setContents(MessageManager.getMessage("news.federation.Connected", map));
					// set node id
					nm.setNodeId(MessageUtil.getSelfNodeId());
					f.getNewsService(selfGridId).add(nm);
				} catch(ServiceManagerException e) {
					LogWriter.writeError("Operator", e, getClass());					
				}
			} else{
				OperationRequest or = new OperationRequest();
				or.setGridId(selfGridId);
				or.setTargetType(OperationType.FEDERATION);
				or.setTargetId(sourceGrid.getGridId());
				or.setRequestedUserId(requestedUserId);
				or.setContents(sourceUrl);
				or.setNodeId(selfNodeId);
				DaoFactory.createInstance().createOperationRequestDao().addOperationRequest(or);
			}
			
			return fr;
		} catch(ServiceManagerException e) {
			logger.severe("invalid url value: context parameter: 'langrid.operator.homepageUrl'");
			throw new ServletException("proccess failed", e);
		} catch(MalformedURLException e) {
			logger.severe("invalid url value: context parameter: 'langrid.operator.homepageUrl'");
			throw new ServletException("proccess failed", e);
		}
	}

	private String selfGridId;
	private static Logger logger = Logger.getLogger(FederationRequestServlet.class.getName());
	
	private static final long serialVersionUID = 1L;
}
