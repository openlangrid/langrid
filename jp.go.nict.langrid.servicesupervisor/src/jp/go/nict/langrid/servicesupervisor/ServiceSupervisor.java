/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.EmptyInputStream;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.servlet.AlternateOutputHttpServletResponseWrapper;
import jp.go.nict.langrid.commons.ws.servlet.OutputStreamServletOutputStream;
import jp.go.nict.langrid.commons.ws.util.LangridHttpUtil;
import jp.go.nict.langrid.commons.ws.util.SOAPBodyUtil;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.ConnectException;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.FrontEnd;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceSupervisor
implements Filter{
	@Override
	public void init(FilterConfig config) throws ServletException {
		try{
			factory = DaoFactory.createInstance();
			daoContext = factory.getDaoContext();
			gdao = factory.createGridDao();
			ndao = factory.createNodeDao();
			sdao = factory.createServiceDao();
			udao = factory.createUserDao();
			ardao = factory.createAccessRightDao();
			aldao = factory.createAccessLimitDao();
			asdao = factory.createAccessStateDao();
			algdao = factory.createAccessLogDao();
		} catch(DaoException e){
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	@Override
	public void destroy() {
	}

	protected void doFilter(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		try{
			// gridId, serviceId, userId
			ServletServiceContext context = new ServletServiceContext(
					request, new ArrayList<RpcHeader>());
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

			Trio<String, String, String> ret = HttpServletRequestUtil.parseRequestUrl(request);
			String serviceGridId = ret.getFirst();
			if(serviceGridId == null) serviceGridId = context.getSelfGridId();

			String serviceId = ret.getSecond();
			doFilterProcess(
					request, response, chain
					, context, userGridId, userId, serviceGridId, serviceId
					);
		} catch(DaoException e){
			throw new ServletException(e);
		}
	}

	protected void doFilterProcess(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain
			, ServiceContext serviceContext
			, String userGridId, String userId, String serviceGridId, String serviceId)
	throws ConnectException, DaoException, IOException, ServletException{
		// prepare
		String protocol = getProtocol(request);

		// preprocess
		ProcessContext c = null;
		Service service = null;
		FrontEnd fe = FrontEnd.getInstance();
		daoContext.beginTransaction();
		Throwable expInPreprocess = null;
		try{
			Grid g = gdao.getGrid(serviceGridId);
			Node n = ndao.getNode(serviceContext.getSelfGridId(), serviceContext.getSelfNodeId());
			service = sdao.getService(serviceGridId, serviceId);
			if(!service.isActive()){
				LangridHttpUtil.write403_ServiceNotActive(response, serviceGridId, serviceId);
				return;
			}
			response.setHeader(
					LangridConstants.HTTPHEADER_SERVICENAME
					, service.getServiceName() != null ? service.getServiceName() : ""
					);
			response.setHeader(
					LangridConstants.HTTPHEADER_SERVICECOPYRIGHT
					, service.getCopyrightInfo() != null ?
							StringUtil.encodeHttpHeaderValueAsUTF8(service.getCopyrightInfo()) :
							""
					);
			response.setHeader(
					LangridConstants.HTTPHEADER_SERVICELICENSE
					, service.getLicenseInfo() != null ?
							StringUtil.encodeHttpHeaderValueAsUTF8(service.getLicenseInfo()) :
							""
					);
			User u = udao.getUser(userGridId, userId);
			c = new ProcessContext(
					u, g, service, n
					, daoContext, ardao, aldao, asdao, algdao
					);
			if(serviceContext.getSelfGridId().equals(g.getGridId())){
				fe.preprocess(c, serviceContext.getRequestMimeHeaders());
			}
		} catch(jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException e){
			LangridHttpUtil.write403_AccessLimitExceeded(response, serviceGridId, serviceId);
			expInPreprocess = e;
		} catch(jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException e){
			LangridHttpUtil.write403_NoAccessPermission(response, serviceGridId, serviceId);
			expInPreprocess = e;
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			LangridHttpUtil.write404_ServiceNotFound(response, serviceGridId, serviceId);
			expInPreprocess = e;
		} catch(Throwable e){
			LangridHttpUtil.write500_Exception(response, serviceGridId, serviceId, e);
			logger.log(Level.SEVERE, "unexpected exception occurred", e);
			expInPreprocess = e;
		} finally{
			daoContext.commitTransaction();
		}

		// invoke
		if(expInPreprocess == null){
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			AlternateOutputHttpServletResponseWrapper wresponse = new AlternateOutputHttpServletResponseWrapper(
					response, new OutputStreamServletOutputStream(bout));
			wresponse.setStatus(200);
			HttpServletRequest wrequest = request;
			long millis = -1;
			boolean invocationFailed = false;
			try{
				long s = System.currentTimeMillis();
				chain.doFilter(request, response);
				millis = System.currentTimeMillis() - s;
				wrequest = request;
			} catch(ServletException e){
				invocationFailed = true;
				LangridHttpUtil.write500_Exception(wresponse, serviceGridId, serviceId, e);
			} catch(IOException e){
				invocationFailed = true;
				LangridHttpUtil.write500_Exception(wresponse, serviceGridId, serviceId, e);
			}
			byte[] responseBytes = bout.toByteArray();

			// postprocess
			if(serviceContext.getSelfGridId().equals(c.getTargetGrid().getGridId())
					&& !invocationFailed){
				daoContext.beginTransaction();
				try{
					fe.postprocess(c, responseBytes.length);
				} catch(jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException e){
					LangridHttpUtil.write403_AccessLimitExceeded(wresponse, serviceGridId, serviceId);
					return;
				} catch(Throwable t){
					LangridHttpUtil.write500_Exception(wresponse, serviceGridId, serviceId, t);
					logger.log(
							Level.SEVERE
							, "unexpected exception occurred in invoke process."
							, t);
					return;
				} finally{
					daoContext.commitTransaction();
				}
			}
	
			// reply
			response.setStatus(wresponse.getStatus());
			/*
			for(Map.Entry<String, List<String>> e : wresponse.getResponseHeaders().entrySet()){
				if(e.getKey().startsWith("X-LanguageGrid-")){
					for(String v : e.getValue()){
						response.addHeader(e.getKey(), v);
					}
				}
			}*/
			OutputStream os = response.getOutputStream();
			StreamUtil.transfer(new ByteArrayInputStream(responseBytes), os);
			os.flush();

			// 
			// 
			// logProcess
			daoContext.beginTransaction();
			try{
				int len = responseBytes.length;
				int code = wresponse.getStatus();
				Pair<String, String> soapFault = SOAPBodyUtil.extractSoapFaultString(new ByteArrayInputStream(responseBytes));
				if(soapFault.getFirst() == null && soapFault.getSecond() == null){
					// pb?
				}
				if(((code < 200) || (400 <= code)) && soapFault.getFirst() == null && soapFault.getSecond() == null){
					soapFault = Pair.create(
							"Server.unknownError"
							, StringEscapeUtils.escapeHtml(new String(
									responseBytes, 0, Math.min(700, responseBytes.length)
									, CharsetUtil.UTF_8
									))
							);
				}
				fe.logProcess(c, FrontEnd.createLogInfo(
								wrequest, new ByteArrayInputStream(responseBytes)
								, millis, code, len, protocol
							), soapFault.getFirst(), soapFault.getSecond()
						);
			} catch(SystemErrorException e){
				logger.log(Level.SEVERE
						, "unexpected exception occurred in log process(commit fase)."
						, e);
			} finally{
				daoContext.commitTransaction();
			}
		} else if(c != null){
			// 
			// 
			// logProcess
			daoContext.beginTransaction();
			try{
				fe.logProcess(c
						, FrontEnd.createLogInfo(
								request, new EmptyInputStream()
								, -1, 500, 0, protocol
						)
						, "Server.serverException"
						, ExceptionUtil.getMessageWithStackTrace(expInPreprocess)  // TODO: faultどうすんの?
						);
			} catch(SystemErrorException e){
				logger.log(Level.SEVERE
						, "unexpected exception occurred in log process(commit fase)."
						, e);
			} finally{
				daoContext.commitTransaction();
			}
		}
	}

	@SuppressWarnings("unused")
	private static HttpServletRequest invoke2(
			FilterChain chain, HttpServletRequest request, HttpServletResponse response
			, String gridId, String serviceId)
	throws ServletException, IOException{
		final String sid = serviceId;
		HttpServletRequestWrapper wrequest = new HttpServletRequestWrapper(request){
			@Override
			public String getRequestURI() {
				String u = super.getRequestURI();
				int i = u.lastIndexOf('/');
				u = u.substring(0, i);
				return u + "/Translation?serviceName=" + sid;
			}
			@Override
			public String getPathInfo() {
				return "/Translation";
			}
		};
		request.getSession().getServletContext().getRequestDispatcher(
				"/axisServices/Translation?serviceName=" + serviceId
				).include(wrequest, response);
		return wrequest;
	}

	private static String getProtocol(HttpServletRequest request){
		String p = request.getHeader("X-Langrid-Protocol");
		if(p != null) return p;
		else return Protocols.DEFAULT;
	}

/*
	private static String tempOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "   <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
		+ "      <soapenv:Body>"
		+ "         <ns1:translate soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://translation.wrapper.langrid.nict.go.jp\">"
		+ "            <sourceLang xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">ja</sourceLang>"
		+ "            <targetLang xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">en</targetLang>"
		+ "            <source xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">奈良女子大で塩水ぶっ掛け懲戒処分奈良女子大（奈良市）は４日、在学生と卒業生計約７０００人分の成績や学籍を管理しているコンピューターに塩水をかけて使えなくしたとして、学務課の男性職員（４９）を半日分の減給とする懲戒処分にしたと発表した。データの入ったハードディスクは無事で、コンピューターの買い替え費用約６５万円は職員が負担するという。同大学によると、職員は９月２６日早朝、ほかの職員が出てくる前に塩水をかけた。職員は仕事の負担が大きいことなどに不満を持っており、「上司や同僚を困らせようと思った」と話しているという。処分は１１月２０日付。</source>"
		+ "         </ns1:translate>"
		+ "      </soapenv:Body>"
		+ "   </soapenv:Envelope>";
*/
	private DaoFactory factory;
	private DaoContext daoContext;
	private GridDao gdao;
	private NodeDao ndao;
	private ServiceDao sdao;
	private UserDao udao;
	private AccessRightDao ardao;
	private AccessLimitDao aldao;
	private AccessStatDao asdao;
	private AccessLogDao algdao;

	private static Logger logger = Logger.getLogger(ServiceSupervisor.class.getName());
}
