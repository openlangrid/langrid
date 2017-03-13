package jp.go.nict.langrid.management.web.view.page.admin.federation.component.form;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

import jp.go.nict.langrid.management.logic.FederationLogic;
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
import jp.go.nict.langrid.service_1_2.ProcessFailedException;

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
				try{
					requestFederation(
							gridId,
							url.getModelObject(), userId.getModelObject(),
							password.getModelObject());
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
				} catch(ProcessFailedException e){
					error(e.getDescription());
					setIsValidateError(true);
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

	private static String requestFederation(
			String selfGridId,
			String url, String userId, String password)
	throws ServiceManagerException, HttpException, IOException, ProcessFailedException{
		String token = FederationLogic.newToken();

		String inputedUrl = url;
		String[] hostAndPort = inputedUrl.split("/", 4)[2].split(":");

		String requestUrl = inputedUrl.endsWith("/") ? inputedUrl : inputedUrl + "/";
		requestUrl += "federation/request";
		URL rurl = new URL(requestUrl);

		String host = hostAndPort[0];
		int port = 1 < hostAndPort.length ? Integer.valueOf(hostAndPort[1]) : rurl.getDefaultPort();

		HttpClient hc = HttpClientUtil.createHttpClientWithHostConfig(rurl);
		SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
		manager.getParams().setConnectionTimeout(20000);
		manager.getParams().setSoTimeout(20000);
		hc.setHttpConnectionManager(manager);
		GridModel grid = ServiceFactory.getInstance().getGridService().get(selfGridId);
		FederationRequest request = new FederationRequest();
		request.setToken(token);
		request.setRequestUserId(userId);
		request.setSourceGrid(grid);
		request.setSourceUrl(grid.getUrl());
		PostMethod pm = new PostMethod(requestUrl);
		pm.setParameter("request", JSON.encode(request));
		pm.setParameter("requestType", RequestType.CONNECT.name());
		pm.setDoAuthentication(true);
		hc.getState().setCredentials(
			new AuthScope(host, port, null),
			new UsernamePasswordCredentials(userId, password)
			);
		hc.getParams().setAuthenticationPreemptive(true);
		int code = hc.executeMethod(pm);
		String targetGridId = null;
		if(code == HttpStatus.SC_OK) {
			String res = pm.getResponseBodyAsString();
			targetGridId = requestUrl;
			if(res != null) {
				FederationResponse fr = JSON.decode(res, FederationResponse.class);
				targetGridId = fr.getTargetGrid().getGridId();
				if(targetGridId == null) {
					targetGridId = requestUrl;
				}
				GridService gService = ServiceFactory.getInstance().getGridService();
				if(fr.isApproved() && gService.get(fr.getTargetGrid().getGridId()) == null) {
					fr.getTargetGrid().setHosted(false);
					gService.add(fr.getTargetGrid());
					LogWriter.writeInfo("Operator"
						, MessageManager.getMessage(
							"LanguageGridOperator.federation.log.connect.registration.Grid"
							, Locale.ENGLISH, targetGridId)
						, RequestOfConnectionForm.class);
					try {
						// add news
						NewsModel nm = new NewsModel();
						nm.setGridId(selfGridId);
						Map<String, String> map = new HashMap<String, String>();
						map.put("gridId", targetGridId);
						nm.setContents(MessageManager.getMessage("news.federation.Connected", map));
						// set node id
						nm.setNodeId(MessageUtil.getSelfNodeId());
						ServiceFactory.getInstance().getNewsService(selfGridId).add(nm);
					} catch(ServiceManagerException e) {
						LogWriter.writeError("Operator", e, RequestOfConnectionForm.class);
					}
				}
				FederationService fService = ServiceFactory.getInstance().getFederationService(selfGridId);
				FederationModel fm = fService.get(selfGridId, targetGridId);
				if(fm == null) {
					fm = new FederationModel();
					fm.setRequesting(!fr.isApproved());
					fm.setConnected(fr.isApproved());
					fm.setSourceGridId(selfGridId);
					fm.setSourceGridName(grid.getGridName());
					fm.setTargetGridId(targetGridId);
					fm.setTargetGridName(fr.getTargetGrid().getGridName());
					fm.setTargetGridAccessToken(token);
					fm.setTargetGridUserHomepage(new URL(fr.getOperatorHomepage()));
					fm.setTargetGridUserId(userId);
					fm.setTargetGridUserOrganization(fr.getOperatorOrganization());
					fService.add(fm);
				} else if(fr.isApproved()){
					fService.setRequesting(selfGridId, targetGridId, false);
					fService.setConnected(selfGridId, targetGridId, true);
				}
			}
		} else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR
			|| code == HttpStatus.SC_NOT_FOUND
			|| code == HttpStatus.SC_FORBIDDEN
			|| code == HttpStatus.SC_UNAUTHORIZED
			|| code == HttpStatus.SC_SERVICE_UNAVAILABLE)
		{
			throw new ProcessFailedException(pm.getResponseBodyAsString());
		}
		return targetGridId;
	}
	private RequiredURLField url;
	private RequiredPasswordTextField confirm;
	private RequiredPasswordTextField password;
	private RequiredUserIdField userId;
	private String targetGridId;
	private String gridId;
}
