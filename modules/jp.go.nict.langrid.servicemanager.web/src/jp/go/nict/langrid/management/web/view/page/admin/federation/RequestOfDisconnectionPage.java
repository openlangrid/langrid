package jp.go.nict.langrid.management.web.view.page.admin.federation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.management.logic.service.HttpClientUtil;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.enumeration.FederationGridType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.servlet.DisconnectRequest;
import jp.go.nict.langrid.management.web.servlet.DisconnectResponse;
import jp.go.nict.langrid.management.web.servlet.RequestType;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1143 $
 */
public class RequestOfDisconnectionPage extends ServiceManagerPage {
	public RequestOfDisconnectionPage(
		String sourceGridId, String targetGridId, FederationGridType isSelfGridType)
	{
		federationSourceGridId = sourceGridId;
		federationTargetGridId = targetGridId;
		selfGridType = isSelfGridType;
		try {
			GridModel requestTargetGrid;
			if(selfGridType.equals(FederationGridType.SOURCEGRID)){
				requestTargetGrid = ServiceFactory.getInstance().getGridService().get(federationTargetGridId);
			}else {
				requestTargetGrid = ServiceFactory.getInstance().getGridService().get(federationSourceGridId);
			}
			requestUrl = requestTargetGrid.getUrl() + (requestTargetGrid.getUrl().endsWith("/") ? "" : "/") + REQUESTPATH;
			add(new Label("url", new Model<String>(requestUrl)));
			FederationModel fm = ServiceFactory.getInstance().getFederationService(getSelfGridId()).get(federationSourceGridId, federationTargetGridId);
			targetUserId = fm.getTargetGridUserId();
			accessToken = fm.getTargetGridAccessToken();
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
		add(new AbstractForm<Boolean>("form") {
			@Override
			protected void addComponents(Boolean initialParameter) throws ServiceManagerException {
				add(new Button("submit") {
					@Override
					public void onSubmit() {
						int code = 0;
						
						// create request param
						DisconnectRequest dr = new DisconnectRequest();
						dr.setGridId(getSelfGridId());
						dr.setRequestUserId(targetUserId);
						dr.setToken(accessToken);
						dr.setFederationSourceGridId(federationSourceGridId);
						dr.setFederationTargetGridId(federationTargetGridId);
						dr.setRequestedGridType(selfGridType);
						
						// create request method
						PostMethod pm = new PostMethod(requestUrl);
						pm.setParameter("request", JSON.encode(dr));
						pm.setParameter("requestType", RequestType.DISCONNECT.name());
						pm.addRequestHeader(
								LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE,
								Boolean.TRUE.toString());
						pm.setDoAuthentication(true);
						String res = "";
						try {
							// create http client
							URL rurl = new URL(requestUrl);
							int port = rurl.getPort() != -1 ? rurl.getPort() : rurl.getDefaultPort();
							
							HttpClient hc = HttpClientUtil.createHttpClientWithHostConfig(rurl);
							hc.getState().setCredentials(
								new AuthScope(rurl.getHost(), port, null)
								, new UsernamePasswordCredentials(getSelfGridId(), accessToken)
							);
							hc.getParams().setAuthenticationPreemptive(true);

							// execute
							code = hc.executeMethod(pm);
							// response
							resultParameter = false;
							if(code == HttpStatus.SC_OK) {
								res = pm.getResponseBodyAsString();
								if(res != null) {
									DisconnectResponse dres = JSON.decode(res, DisconnectResponse.class);
									resultParameter = dres.isDisconnect();
								}
							}
						}catch(JSONException e) {
							code = 0;
							resultParameter = false;
							LogWriter.writeError("Operator", e, getPageClass(), res);
						}catch(MalformedURLException e) {
							doErrorProcess(new ServiceManagerException(e, getClass()));
						}catch(ConnectTimeoutException e) {
							code = 0;
							resultParameter = false;
						}catch(IOException e) {
							error(MessageManager.getMessage(
								"LanguageGridOperator.federation.message.delete.error.connection"
								, getLocale(), requestUrl));
							LogWriter.writeError("Operator", e, getPageClass());
							setIsValidateError(true);
						}
						
						// delete federation
						try {
							String deletedGridId = federationTargetGridId;
							if(selfGridType.equals(FederationGridType.TARGETGRID)) {
								deletedGridId = federationSourceGridId;
							}

							FederationService fs = ServiceFactory.getInstance().getFederationService(getSelfGridId());
//							fs.delete(fs.get(federationSourceGridId, federationTargetGridId));
							fs.setConnected(federationSourceGridId, federationTargetGridId, false);
							// log for federation
							LogWriter.writeInfo("Operator", MessageManager.getMessage(
								"LanguageGridOperator.federation.log.disconnect.Federation"
								, getLocale(), federationSourceGridId, federationTargetGridId)
								, getPageClass());
							
							
							if ( fs.get(federationTargetGridId, federationSourceGridId) == null ) {
								try {
									GridService gs = ServiceFactory.getInstance().getGridService();
									gs.delete(gs.get(deletedGridId));
	
									// log for grid
									LogWriter.writeInfo("Operator", MessageManager.getMessage(
											"LanguageGridOperator.federation.log.disconnect.delete.Grid"
											, getLocale(), deletedGridId)
										, getPageClass());
								} catch(ServiceManagerException e) {
									LogWriter.writeError("Operator", e, getClass());
								}
							}
							
							if( ! resultParameter){
								// log for remote
								LogWriter.writeWarn("Operator"
									, MessageManager.getMessage(
										"LanguageGridOperator.federation.log.disconnect.remote.Deletion"
										, getLocale()
										, deletedGridId, (code == 0 ? "timeout" : code))
									, getPageClass());
							}
							
							// add news
							NewsModel nm = new NewsModel();
							nm.setGridId(getSelfGridId());
							Map<String, String> map = new HashMap<String, String>();
							map.put("gridId", deletedGridId);
							nm.setContents(MessageManager.getMessage("news.federation.Disonnected", map));
							// set node id
							nm.setNodeId(MessageUtil.getSelfNodeId());
							ServiceFactory.getInstance().getNewsService(getSelfGridId()).add(nm);
						} catch (ServiceManagerException e) {
							doErrorProcess(e);
						}
					}
				});
				add(new Link("cancel") {
					@Override
					public void onClick() {
						setResponsePage(new AllOperatorsPage());
					}
				});
			}
			
			@Override
			protected void setResultPage(Boolean resultParameter) throws ServiceManagerException {
				setResponsePage(new RequestOfDisconnectionResultPage(requestUrl, resultParameter));
			}
		});
	}

	
	private String federationTargetGridId;
	private String federationSourceGridId;

	private String requestUrl;
	private String targetUserId;
	private String accessToken;
	
	private FederationGridType selfGridType;
	
	private static final String REQUESTPATH = "federation/request";
}
