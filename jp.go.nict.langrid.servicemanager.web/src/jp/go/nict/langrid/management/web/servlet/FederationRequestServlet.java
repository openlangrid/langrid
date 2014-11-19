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
import java.io.PrintWriter;
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

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
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
		String app = config.getInitParameter("autoApproveEnabled");
		if(app != null && app.equals("true")) {
			autoApproveEnabled = true;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException
	{
		RequestType request;
		// do validate
		if(req.getParameter("requestType") == null || req.getParameter("requestType").equals("")) {
			throw new ServletException("parameter not found: 'requestType' is required.");
		} else {
			try {
				request = RequestType.valueOf(req.getParameter("requestType"));
			} catch(Exception e) {
				throw new ServletException("Invalid parameter: 'requestType'");
			}
		}
		// do request process
		try {
			selfGridId = ServiceFactory.getInstance().getGridService().getSelfGridId();
			autoApproveEnabled = DaoFactory.createInstance().createGridDao().getGrid(selfGridId).isAutoApproveEnabled();
			String responseString = "";
			if(request.equals(RequestType.CONNECT)){
				responseString = doConnectFederationRequest(req.getParameter("request"));
			} else if(request.equals(RequestType.DISCONNECT)) {
				responseString = doDisconnectFederationRequset(req.getParameter("request"));
			}
			// send response
			resp.setContentType("text/plain");
			PrintWriter w = resp.getWriter();
			try{
				w.print(responseString);
			}finally{
				w.close();
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
	private String doDisconnectFederationRequset(String requestString) {
		DisconnectRequest request = JSON.decode(requestString, DisconnectRequest.class);
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
//		// make response
		DisconnectResponse dr = new DisconnectResponse();
		dr.setDisconnect(response);
		return JSON.encode(dr);
	}

	/**
	 * 
	 * 
	 */
	private String doConnectFederationRequest(String requestString)
	throws DaoException, ServletException
	{
		FederationRequest request = JSON.decode(requestString, FederationRequest.class);
		String token = request.getToken();
		String requestedGridId = request.getGridId();
		String requestedUserId = request.getRequestUserId();
		String sourceUrl = request.getSourceUrl();
		String requestedGridName = request.getGridName();
		
		String selfGridId = getServletContext().getInitParameter("langrid.node.gridId");
		String selfNodeId = getServletContext().getInitParameter("langrid.node.nodeId");
		String selfOrganization = getServletContext().getInitParameter("langrid.operator.organization");
		String selfHomepage = getServletContext().getInitParameter("langrid.operator.homepageUrl");
		
		ServiceFactory f = ServiceFactory.getInstance();
		try{
			FederationService fs = f.getFederationService(selfGridId);
			GridService gs = f.getGridService();
			GridModel gm = gs.get(selfGridId);
			
			FederationModel fm = fs.get(requestedGridId, selfGridId);
			if(fm == null){
				fm = new FederationModel();
				fm.setConnected(false);
				fm.setRequesting(true);
				fm.setTargetGridAccessToken(token);
				fm.setSourceGridId(requestedGridId);
				fm.setSourceGridName(request.getGridName());
				fm.setTargetGridId(selfGridId);
				fm.setTargetGridName(gm.getGridName());
				fm.setTargetGridUserHomepage(new URL(selfHomepage));
				fm.setTargetGridUserId(requestedUserId);
				fm.setTargetGridUserOrganization(selfOrganization);
				fs.add(fm);
			}else {
				fs.setRequesting(requestedGridId, selfGridId, true);
			}
			LogWriter.writeInfo("Operator"
				, MessageManager.getMessage(
					"LanguageGridOperator.federation.log.connect.Federation"
					, Locale.ENGLISH, requestedGridId, selfGridId)
				, getClass());
			
			if(autoApproveEnabled) {
				fs.setConnected(requestedGridId, selfGridId, true);
				fs.setRequesting(requestedGridId, selfGridId, false);
				if(gs.get(request.getGridId()) == null) {
					GridModel sourceGrid = new GridModel();
					sourceGrid.setGridId(requestedGridId);
					sourceGrid.setGridName(requestedGridName);
					sourceGrid.setAutoApproveEnabled(Boolean.valueOf(request.isAutoApproveEnabled()));
					sourceGrid.setCommercialUseAllowed(Boolean.valueOf(request.isCommercialUseAllowed()));
					sourceGrid.setHosted(false);
					sourceGrid.setOperatorUserId(request.getOperatorId());
					sourceGrid.setUrl(request.getGridUrl());
					gs.add(sourceGrid);
					LogWriter.writeInfo("Operator"
						, MessageManager.getMessage(
							"LanguageGridOperator.federation.log.connect.registration.Grid"
							, Locale.ENGLISH, requestedGridId)
						, getClass());
				}
				try {
					// add news
					NewsModel nm = new NewsModel();
					nm.setGridId(selfGridId);
					Map<String, String> map = new HashMap<String, String>();
					map.put("gridId", requestedGridId);
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
				or.setTargetId(requestedGridId);
				or.setRequestedUserId(requestedUserId);
				or.setContents(sourceUrl);
				or.setNodeId(selfNodeId);
				DaoFactory.createInstance().createOperationRequestDao().addOperationRequest(or);
			}
			
			FederationResponse fr = new FederationResponse();
			fr.setGridId(selfGridId);
			fr.setGridName(gm.getGridName());
			fr.setOperatorOrganization(selfOrganization);
			fr.setOperatorHomepage(selfHomepage);
			fr.setApproved(autoApproveEnabled);
			fr.setAutoApproveEnabled(gm.isAutoApproveEnabled());
			fr.setCommercialUseAllowed(gm.isCommercialUseAllowed());
			fr.setHosted(false);
			fr.setGridUrl(gm.getUrl());
			fr.setOperatorId(gm.getOperatorUserId());
			return JSON.encode(fr);
		} catch(ServiceManagerException e) {
			logger.severe("invalid url value: context parameter: 'langrid.operator.homepageUrl'");
			throw new ServletException("proccess failed", e);
		} catch(MalformedURLException e) {
			logger.severe("invalid url value: context parameter: 'langrid.operator.homepageUrl'");
			throw new ServletException("proccess failed", e);
		}
	}

	private boolean autoApproveEnabled;
	private String selfGridId;
	private static Logger logger = Logger.getLogger(FederationRequestServlet.class.getName());
	
	private static final long serialVersionUID = 1L;
}
