package jp.go.nict.langrid.management.web.view.page.admin.federation.component.form;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.management.logic.service.HttpClientUtil;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.servlet.DisconnectRequest;
import jp.go.nict.langrid.management.web.servlet.DisconnectResponse;
import jp.go.nict.langrid.management.web.servlet.RequestType;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.page.admin.federation.AllOperatorsPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.validator.FormRegistUserValidator;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
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
public abstract class RequestOfDisconnectionForm extends AbstractForm<String> {
	/**
	 * 
	 * 
	 */
	public RequestOfDisconnectionForm(String formId, String gridId, String targetGridId) {
		super(formId);
		this.gridId = gridId;
		this.targetGridId = targetGridId;

		ServiceFactory f = ServiceFactory.getInstance();
		try {
			FederationModel fm = f.getFederationService(gridId).get(gridId, targetGridId);
//			FederationModel fm = getFederationModel(gridId, targetGridId);
			GridModel targetGrid = f.getGridService().get(targetGridId);
			targetUrl = targetGrid.getUrl() + "/federation/request";
			targetUserId = fm.getTargetGridUserId();
			accessToken = fm.getTargetGridAccessToken();
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	@Override
	protected void addComponents(String initialParameter)
	throws ServiceManagerException {
		add(userId = new RequiredUserIdField("userId", new Model<String>()));
		add(password = new RequiredPasswordTextField("password", new Model<String>()));
		add(confirm = new RequiredPasswordTextField("confirm", new Model<String>()));
		add(new FormRegistUserValidator(password, confirm));
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new AllOperatorsPage());
			}
		});
		add(new Button("submit") {
			@Override
			public void onSubmit() {
				try {
					disconnectFederation(targetUrl, gridId, targetGridId, accessToken, targetUserId,
							userId.getModelObject(), password.getModelObject());
				} catch(MalformedURLException e) {
					doErrorProcess(new ServiceManagerException(e, getClass()));
				} catch(ConnectTimeoutException e) {
					error("Connection time out");
					setIsValidateError(true);
				} catch(IOException e) {
					error("Connection failed to target service grid: '" + targetUrl + "'");
					setIsValidateError(true);
				} catch (ServiceManagerException e) {
					doErrorProcess(e);
				} catch(ProcessFailedException e){
					error(e.getDescription());
					setIsValidateError(true);
				}
			}
		});
	}

	private static void disconnectFederation(
			String targetUrl, String selfGridId, String targetGridId,
			String accessToken, String targetUserId,
			String userId, String password) throws HttpException, IOException, ServiceManagerException, ProcessFailedException
	{
		DisconnectRequest dr = new DisconnectRequest();
		dr.setGridId(selfGridId);
		dr.setRequestUserId(targetUserId);
		dr.setToken(accessToken);
		
		URL turl = new URL(targetUrl);
		int port = turl.getPort() != -1 ? turl.getPort() : turl.getDefaultPort();
	
		HttpClient hc = HttpClientUtil.createHttpClientWithHostConfig(turl);
		PostMethod pm = new PostMethod(targetUrl);
		pm.setParameter("request", JSON.encode(dr));
		pm.setParameter("requestType", RequestType.DISCONNECT.name());
		pm.addRequestHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE,
				Boolean.TRUE.toString());
		pm.setDoAuthentication(true);
		hc.getState().setCredentials(
			new AuthScope(turl.getHost(), port, null)
			,new UsernamePasswordCredentials(
				userId, password)
			);
		hc.getParams().setAuthenticationPreemptive(true);
		int code = hc.executeMethod(pm);
		if(code == HttpStatus.SC_OK) {
			String res = pm.getResponseBodyAsString();
			if(res != null) {
				DisconnectResponse dres = JSON.decode(res, DisconnectResponse.class);
				FederationService fs = ServiceFactory.getInstance().getFederationService(selfGridId);
				
				FederationModel fm = fs.get(selfGridId, targetGridId);
				fs.delete(fm);
				
				GridService gs = ServiceFactory.getInstance().getGridService();
				GridModel gm = gs.get(targetGridId);
				gs.delete(gm);
					
			}
		} else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR
			|| code == HttpStatus.SC_NOT_FOUND
			|| code == HttpStatus.SC_FORBIDDEN
			|| code == HttpStatus.SC_UNAUTHORIZED
			|| code == HttpStatus.SC_SERVICE_UNAVAILABLE)
		{
			String res = pm.getResponseBodyAsString();
			if(res != null) {
				throw new ProcessFailedException(code + ": " + res);
			} else {
				throw new ProcessFailedException(code + ": Unknown error response.");
			}
		}
	}

	@Override
	protected String getResultParameter() {
		return "";
	}
	
	protected String getRequestUrl(){
		return targetUrl;
	}

	protected String getUserId() {
		return userId.getModelObject();
	}

	protected String getTargetGridId() {
		return targetGridId;
	}

	private RequiredPasswordTextField confirm;
	private RequiredPasswordTextField password;
	private RequiredUserIdField userId;
	private String targetGridId;
	private String gridId;
	
	private String targetUrl;
	private String targetUserId;
	private String accessToken;
}
