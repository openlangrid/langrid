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
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 * 
 * @author Masaaki Kamiya
 */
public class FederationResponseServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		FederationResponse response = JSON.decode(req.getParameter("response"), FederationResponse.class);
		boolean accepted = response.isApproved();
		String targetGridId = response.getTargetGrid().getGridId();

		try {
			String selfGridId = getServletContext().getInitParameter("langrid.node.gridId");
			String selfNodeId = getServletContext().getInitParameter("langrid.node.nodeId");
			ServiceFactory f = ServiceFactory.getInstance();
			FederationService fs = f.getFederationService(selfGridId);
			GridService gs = f.getGridService();
			
			if (accepted) {
				if(gs.get(response.getTargetGrid().getGridId()) == null) {
					response.getTargetGrid().setHosted(false);
					gs.add(response.getTargetGrid());
					LogWriter.writeInfo("Operator"
						, MessageManager.getMessage(
							"LanguageGridOperator.federation.log.connect.registration.Grid"
							, Locale.ENGLISH, targetGridId)
						, getClass());
				}
				fs.setRequesting(selfGridId, targetGridId, false);
				fs.setConnected(selfGridId, targetGridId, true);
				LogWriter.writeInfo("Operator"
					, MessageManager.getMessage(
						"LanguageGridOperator.federation.log.connect.Federation"
						, Locale.ENGLISH, selfGridId, targetGridId)
					, getClass());
				NewsModel nm = new NewsModel();
				nm.setContents(response.getNewsMessage());
				nm.setGridId(selfGridId);
				nm.setNodeId(selfNodeId);
				f.getNewsService(selfGridId).add(nm);
				
				resp.setContentType("text/plain");
				PrintWriter w = resp.getWriter();
				try{
					String selfOrganization = getServletContext().getInitParameter("langrid.operator.organization");
					String selfHomepage = getServletContext().getInitParameter("langrid.operator.homepageUrl");
					GridModel gm = gs.get(selfGridId);
					
					FederationResponse fr = new FederationResponse();
					fr.setTargetGrid(gm);
					fr.setOperatorOrganization(selfOrganization);
					fr.setOperatorHomepage(selfHomepage);
					fr.setApproved(true);
					w.print(JSON.encode(fr));
				}finally{
					w.close();
				}
			} else {
				FederationModel fm = fs.get(selfGridId, targetGridId);					
				fs.deleteById(fm.getSourceGridId(), fm.getTargetGridId());
//					dao.deleteFederation(f.getSourceGridId(), f.getTargetGridId());
				OperationRequestService os = f.getOperationService(selfGridId, selfGridId, "");
				OperationRequestModel orm = new OperationRequestModel();
				orm.setGridId(selfGridId);
				orm.setTargetId(fm.getTargetGridId());
				orm.setType(OperationType.FEDERATIONREJECT);
				orm.setNodeId(selfNodeId);
				orm.setRequestedUserId(fm.getTargetGridUserId());
				os.add(orm);
				
//					OperationRequest or = new OperationRequest();
//					or.setGridId(selfGridId);
//					or.setTargetId(fm.getTargetGridId());
//					or.setTargetType(OperationType.FEDERATIONREJECT);
//					or.setNodeId(selfNodeId);
//					DaoFactory.createInstance().createOperationRequestDao().addOperationRequest(or);
				
				LogWriter.writeInfo("Operator"
					, MessageManager.getMessage(
						"LanguageGridOperator.federation.log.connect.Rejected"
						, Locale.ENGLISH, targetGridId)
					, getClass());
			}
		} catch(ServiceManagerException e) {
			LogWriter.writeError("Operator", e, getClass());
			throw new ServletException("Can't accept your request.");
		}
	}

	private static final long serialVersionUID = 1L;
}
