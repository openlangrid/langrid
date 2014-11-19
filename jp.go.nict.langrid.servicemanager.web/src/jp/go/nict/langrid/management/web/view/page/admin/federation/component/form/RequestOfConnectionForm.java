package jp.go.nict.langrid.management.web.view.page.admin.federation.component.form;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.management.logic.service.HttpClientUtil;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.servlet.FederationRequest;
import jp.go.nict.langrid.management.web.servlet.FederationResponse;
import jp.go.nict.langrid.management.web.servlet.RequestType;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredURLField;
import jp.go.nict.langrid.management.web.view.page.admin.federation.AllOperatorsPage;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
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
public abstract class RequestOfConnectionForm extends AbstractForm<String> {
	/**
	 * 
	 * 
	 */
	public RequestOfConnectionForm(String formId, String gridId) {
		super(formId);
		this.gridId = gridId;
	}

	@Override
	protected void addComponents(String initialParameter)
	throws ServiceManagerException {
		add(url = new RequiredURLField("url", new Model<String>()));
		add(userId = new RequiredUserIdField("userId", new Model<String>()));
		add(password = new RequiredPasswordTextField("password", new Model<String>()));
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new AllOperatorsPage());
			}
		});
		add(new Button("submit") {
			@Override
			public void onSubmit() {
				String token = StringUtil.randomString(25);
				
				String inputedUrl = url.getModelObject();
				String[] hostAndPort = inputedUrl.split("/", 4)[2].split(":");
				
				String host = hostAndPort[0];
				int port = 1 < hostAndPort.length ? Integer.valueOf(hostAndPort[1]) : 80;
				
				String requestUrl = inputedUrl.endsWith("/") ? inputedUrl : inputedUrl + "/";
				requestUrl += "federation/request";
				
				try {
					HttpClient hc = HttpClientUtil.createHttpClientWithHostConfig(new URL(requestUrl));
					SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
					manager.getParams().setConnectionTimeout(20000);
					manager.getParams().setSoTimeout(20000);
					hc.setHttpConnectionManager(manager);
					GridModel grid = ServiceFactory.getInstance().getGridService().get(gridId);
					FederationRequest request = new FederationRequest();
					request.setToken(token);
					request.setGridId(gridId);
					request.setGridName(grid.getGridName());
					request.setRequestUserId(userId.getModelObject());
					request.setSourceUrl(MessageUtil.getCoreNodeUrl());
					request.setCommercialUseAllowed(grid.isCommercialUseAllowed());
					request.setAutoApproveEnabled(grid.isAutoApproveEnabled());
					request.setGridUrl(grid.getUrl());
					request.setHosted(grid.isHosted());
					request.setOperatorId(grid.getOperatorUserId());
					PostMethod pm = new PostMethod(requestUrl);
					pm.setParameter("request", JSON.encode(request));
					pm.setParameter("requestType", RequestType.CONNECT.name());
					pm.setDoAuthentication(true);
					hc.getState().setCredentials(
						new AuthScope(host, port, null)
						,new UsernamePasswordCredentials(
							userId.getModelObject(), password.getModelObject())
						);
					hc.getParams().setAuthenticationPreemptive(true);
					int code = hc.executeMethod(pm);
					if(code == HttpStatus.SC_OK) {
						String res = pm.getResponseBodyAsString();
						targetGridId = requestUrl;
						if(res != null) {
							FederationResponse jo = JSON.decode(res, FederationResponse.class);
							targetGridId = jo.getGridId();
							if(targetGridId == null) {
								targetGridId = requestUrl;
							}
							GridService gService = ServiceFactory.getInstance().getGridService();
							if(jo.isApproved() && gService.get(jo.getGridId()) == null) {
								GridModel newGrid = new GridModel();
								newGrid.setAutoApproveEnabled(true);
								newGrid.setCommercialUseAllowed(jo.isCommercialUseAllowed());
								newGrid.setGridId(jo.getGridId());
								newGrid.setGridName(jo.getGridName());
								newGrid.setHosted(false);
								newGrid.setOperatorUserId(jo.getOperatorId());
								newGrid.setUrl(jo.getGridUrl());
								gService.add(newGrid);
								
								LogWriter.writeInfo("Operator"
									, MessageManager.getMessage(
										"LanguageGridOperator.federation.log.connect.registration.Grid"
										, Locale.ENGLISH, targetGridId)
									, getClass());
								try {
									// add news
									NewsModel nm = new NewsModel();
									nm.setGridId(gridId);
									Map<String, String> map = new HashMap<String, String>();
									map.put("gridId", targetGridId);
									nm.setContents(MessageManager.getMessage("news.federation.Connected", map));
									// set node id
									nm.setNodeId(MessageUtil.getSelfNodeId());
									ServiceFactory.getInstance().getNewsService(gridId).add(nm);
								} catch(ServiceManagerException e) {
									LogWriter.writeError("Operator", e, getClass());					
								}
							}
							FederationService fService = ServiceFactory.getInstance().getFederationService(gridId);
							FederationModel fm = fService.get(gridId, targetGridId);
							if(fm == null) {
								fm = new FederationModel();
								fm.setRequesting(!jo.isApproved());
								fm.setConnected(jo.isApproved());
								fm.setSourceGridId(gridId);
								fm.setSourceGridName(grid.getGridName());
								fm.setTargetGridId(targetGridId);
								fm.setTargetGridName(jo.getGridName());
								fm.setTargetGridAccessToken(token);
								fm.setTargetGridUserHomepage(new URL(jo.getOperatorHomepage()));
								fm.setTargetGridUserId(userId.getModelObject());
								fm.setTargetGridUserOrganization(jo.getOperatorOrganization());
								fService.add(fm);
							} else {
								fService.setRequesting(gridId, targetGridId, ! jo.isApproved());
								if(jo.isApproved()){
									fService.setConnected(gridId, targetGridId, jo.isApproved());
								}
							}
						}
					} else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR
						|| code == HttpStatus.SC_NOT_FOUND
						|| code == HttpStatus.SC_FORBIDDEN
						|| code == HttpStatus.SC_UNAUTHORIZED
						|| code == HttpStatus.SC_SERVICE_UNAVAILABLE)
					{
						error(pm.getResponseBodyAsString());
						setIsValidateError(true);
					}
				} catch(MalformedURLException e) {
					doErrorProcess(new ServiceManagerException(e));
				} catch(ConnectTimeoutException e) {
					error("Connection time out");
					setIsValidateError(true);
				} catch(HttpException e) {
					doErrorProcess(new ServiceManagerException(e));
				} catch(IOException e) {
					doErrorProcess(new ServiceManagerException(e));
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
		});
	}

	@Override
	protected String getResultParameter() {
		return "";
	}

	protected String getRequestUrl() {
		return url.getModelObject();
	}

	protected String getUserId() {
		return userId.getModelObject();
	}

	protected String getTargetGridId() {
		return targetGridId;
	}

	private RequiredURLField url;
	private RequiredPasswordTextField confirm;
	private RequiredPasswordTextField password;
	private RequiredUserIdField userId;
	private String targetGridId;
	private String gridId;
}
