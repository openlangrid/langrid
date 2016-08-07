package jp.go.nict.langrid.management.web.view.page.admin.federation.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.logic.service.HttpClientUtil;
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
import jp.go.nict.langrid.management.web.servlet.FederationResponse;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.AllOperatorsPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1143 $
 */
public class OperationOfConnectionRequestPage extends ServiceManagerPage {
	public OperationOfConnectionRequestPage(final String sourceGridId, final String targetGridId) {
		try {
			FederationModel model = ServiceFactory.getInstance().getFederationService(
				getSelfGridId()).get(sourceGridId, targetGridId);
			add(new Label("sourceGridId", model.getSourceGridId()));
			String gridId = getSelfGridId();
			OperationRequestModel or = ServiceFactory.getInstance().getOperationService(
				gridId, gridId, getSessionUserId()).getByTargetId(
					sourceGridId, OperationType.FEDERATION);
			ExternalHomePageLink hLink = new ExternalHomePageLink(
				"requestedGridLink", or.getContents(), "");
			add(hLink);
			UserProfileLink link;
			add(link = new UserProfileLink("userIdLink", getSelfGridId(), model.getTargetGridUserId(), ""));
			link.add(new Label("organization"
				, ServiceFactory.getInstance().getUserService(getSelfGridId()).get(
					model.getTargetGridUserId()).getOrganization()));
			add(new Link("cancel") {
				@Override
				public void onClick() {
					setResponsePage(getCancelPage());
				}
			});
			add(new Link("reject") {
				@Override
				public void onClick() {
					try {
						String gridId = getSelfGridId();
						OperationRequestService oService = ServiceFactory.getInstance().getOperationService(
							gridId, gridId, getSessionUserId());
						String requestUrl = oService.getByTargetId(sourceGridId
							, OperationType.FEDERATION).getContents();
						FederationService fService = ServiceFactory.getInstance().getFederationService(gridId);
						FederationModel model = fService.get(sourceGridId, targetGridId);
						HttpClient hc = createClient(gridId, model, requestUrl, false);
						PostMethod pm = createPostMethod(gridId, requestUrl, false);
						int code = hc.executeMethod(pm);
						if(code == HttpStatus.SC_OK) {
							if(fService.get(model.getSourceGridId(), model.getTargetGridId()) != null) {
								fService.delete(model);
							}
							oService.deleteByTargetId(sourceGridId, OperationType.FEDERATION);
							setResponsePage(getResultPage(sourceGridId, targetGridId
								, model.getTargetGridUserId(), true, requestUrl));
						} else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR
							|| code == HttpStatus.SC_NOT_FOUND
							|| code == HttpStatus.SC_FORBIDDEN
							|| code == HttpStatus.SC_UNAUTHORIZED
							|| code == HttpStatus.SC_SERVICE_UNAVAILABLE)
						{
							error(pm.getResponseBodyAsString());
						}
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					} catch(HttpException e) {
						doErrorProcess(new ServiceManagerException(e));
					} catch(IOException e) {
						doErrorProcess(new ServiceManagerException(e));
					}
				}
			});
			add(new Link("accept") {
				@Override
				public void onClick() {
					try {
						String gridId = getSelfGridId();
						FederationService fService = ServiceFactory.getInstance().getFederationService(gridId);
						FederationModel model = fService.get(sourceGridId, targetGridId);
						OperationRequestService oService = ServiceFactory.getInstance().getOperationService(
								gridId, gridId, getSessionUserId());
						OperationRequestModel orm = oService.getByTargetId(sourceGridId, OperationType.FEDERATION);

						String requestUrl = orm.getContents();
						HttpClient hc = createClient(gridId, model, requestUrl, true);
						PostMethod pm = createPostMethod(gridId, requestUrl, true);
						int code = hc.executeMethod(pm);
						
						if(code == HttpStatus.SC_OK) {
							fService.setRequesting(sourceGridId, targetGridId, false);
							fService.setConnected(sourceGridId, targetGridId, true);

							// log for federation
							LogWriter.writeInfo("Operator", MessageManager.getMessage(
								"LanguageGridOperator.federation.log.connect.Federation"
								, Locale.ENGLISH, sourceGridId, targetGridId)
								, getPageClass());
							
							oService.deleteByTargetId(sourceGridId, OperationType.FEDERATION);
							
							FederationResponse jo = JSON.decode(pm.getResponseBodyAsString(), FederationResponse.class);

							GridService gService = ServiceFactory.getInstance().getGridService();
							if(gService.get(jo.getTargetGrid().getGridId()) == null) {
								jo.getTargetGrid().setHosted(false);
								gService.add(jo.getTargetGrid());
								// log for grid
								LogWriter.writeInfo("Operator", MessageManager.getMessage(
									"LanguageGridOperator.federation.log.connect.registration.Grid"
									, Locale.ENGLISH, jo.getTargetGrid().getGridId())
									, getPageClass());
							}
							// add news
							NewsModel nm = new NewsModel();
							nm.setGridId(gridId);
							Map<String, String> map = new HashMap<String, String>();
							map.put("gridId", sourceGridId);
							nm.setContents(MessageManager.getMessage("news.federation.Connected", map));
							// set node id
							nm.setNodeId(MessageUtil.getSelfNodeId());
							ServiceFactory.getInstance().getNewsService(gridId).add(nm);
							
							setResponsePage(getResultPage(sourceGridId, targetGridId,
								model.getTargetGridUserId(), false, requestUrl));
						} else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR
							|| code == HttpStatus.SC_NOT_FOUND
							|| code == HttpStatus.SC_FORBIDDEN
							|| code == HttpStatus.SC_UNAUTHORIZED
							|| code == HttpStatus.SC_SERVICE_UNAVAILABLE) {
							error(pm.getResponseBodyAsString());
						}
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					} catch(HttpException e) {
						doErrorProcess(new ServiceManagerException(e));
					} catch(IOException e) {
						doErrorProcess(new ServiceManagerException(e));
					}
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected Page getCancelPage() {
		return new AllOperatorsPage();
	}

	protected Page getResultPage(String sourceGridId, String targetGridId,
		String targetGridUserId, boolean isReject, String requestUrl) {
		return new OperationOfConnectionRequestResultPage(sourceGridId, targetGridId,
			targetGridUserId, isReject, requestUrl);
	}

	private HttpClient createClient(String selfGridId, FederationModel model
		, String requestUrl, boolean accept)
	throws ServiceManagerException {
		String token = model.getTargetGridAccessToken();
		requestUrl = requestUrl + "/federation/response";
		String host = requestUrl.split("/", 4)[2];
		String[] urls = host.split(":");
		host = urls[0];
		int port = 1 < urls.length ? Integer.valueOf(urls[1]) : 80;
		try {
			HttpClient hc = HttpClientUtil.createHttpClientWithHostConfig(new URL(
				requestUrl));
			SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
			manager.getParams().setConnectionTimeout(20000);
			manager.getParams().setSoTimeout(20000);
			hc.setHttpConnectionManager(manager);
			hc.getState().setCredentials(
				new AuthScope(host, port, null)
				, new UsernamePasswordCredentials(selfGridId, token)
				);
			hc.getParams().setAuthenticationPreemptive(true);
			return hc;
		} catch(MalformedURLException e) {
			throw new ServiceManagerException(e);
		}
	}

	private PostMethod createPostMethod(String selfGridId, String requestUrl,
		boolean accept)
	throws ServiceManagerException {
		requestUrl = requestUrl + "/federation/response";
		GridModel grid = ServiceFactory.getInstance().getGridService().get(selfGridId);
		FederationResponse response = new FederationResponse();
		response.setApproved(accept);
		response.setTargetGrid(grid);
		// add news
		Map<String, String> map = new HashMap<String, String>();
		map.put("gridId", getSelfGridId());
		response.setNewsMessage(MessageManager.getMessage("news.federation.Connected", map));
		
		PostMethod pm = new PostMethod(requestUrl);
		pm.setParameter("response", JSON.encode(response));
		pm.addRequestHeader(
			LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE,
			Boolean.TRUE.toString());
		pm.setDoAuthentication(true);
		return pm;
	}
}
