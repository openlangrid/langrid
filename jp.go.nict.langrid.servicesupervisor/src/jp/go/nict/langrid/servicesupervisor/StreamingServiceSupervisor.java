/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.InitParam;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.servlet.ByteArrayOutputHttpServletResponseWrapper;
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
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.FrontEnd;
import jp.go.nict.langrid.servicesupervisor.frontend.LogInfo;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;
import jp.go.nict.langrid.servicesupervisor.responder.FaultResponder;
import jp.go.nict.langrid.servicesupervisor.responder.HttpFaultResponder;
import jp.go.nict.langrid.servicesupervisor.responder.SoapFaultResponder;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class StreamingServiceSupervisor implements Filter {
	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
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
		} catch (DaoException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			doFilter((HttpServletRequest) request,
					(HttpServletResponse) response, chain);
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "failed to execute filter process.", e);
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
	}

	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			// gridId, serviceId, userId
			ServletServiceContext context = new ServletServiceContext(request);
			String[] callerUser = context.getRequestMimeHeaders().getHeader(
					LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER);
			if (callerUser != null) {
				String[] userGridIdAndId = StringUtil.join(callerUser, ",")
						.split(":");
				context.setAuthorized(userGridIdAndId[0], userGridIdAndId[1],
						context.getAuthPass());
			} else if (context.getAuthUser().contains(":")) {
				String[] userGridIdAndId = context.getAuthUser().split(":");
				context.setAuthorized(userGridIdAndId[0], userGridIdAndId[1],
						context.getAuthPass());
			} else if (context.getAuthUserGridId() == null) {
				context.setAuthorized(context.getSelfGridId(),
						context.getAuthUser(), context.getAuthPass());
			}

			String userGridId = context.getAuthUserGridId();
			String userId = context.getAuthUser();

			Trio<String, String, String> ret = HttpServletRequestUtil
					.parseRequestUrl(request);
			if (ret == null) {
				String msg = "failed to parse URL [" + request.getRequestURL()
						+ "]";
				logger.severe(msg);
				throw new ServletException(msg);
			}
			String serviceGridId = ret.getFirst();
			if (serviceGridId == null)
				serviceGridId = context.getSelfGridId();

			String serviceId = ret.getSecond();
			doFilterProcess(request, response, chain, context, userGridId,
					userId, serviceGridId, serviceId);
		} catch (DaoException e) {
			throw new ServletException(e);
		}
	}

	private ProcessContext prepareContextAndSetServiceInfoHeader(
			HttpServletResponse response, ServiceContext serviceContext,
			String userGridId, String userId, String serviceGridId,
			String serviceId) throws GridNotFoundException,
			NodeNotFoundException,
			jp.go.nict.langrid.dao.ServiceNotFoundException,
			ServiceNotActiveException, UserNotFoundException, DaoException {
		Grid g = gdao.getGrid(serviceGridId);
		Node n = ndao.getNode(serviceContext.getSelfGridId(),
				serviceContext.getSelfNodeId());
		Service service = sdao.getService(serviceGridId, serviceId);
		response.setHeader(LangridConstants.HTTPHEADER_SERVICENAME, service
				.getServiceName() != null ? service.getServiceName() : "");
		response.setHeader(
				LangridConstants.HTTPHEADER_SERVICECOPYRIGHT,
				service.getCopyrightInfo() != null ? StringUtil
						.encodeHttpHeaderValueAsUTF8(service.getCopyrightInfo())
						: "");
		response.setHeader(
				LangridConstants.HTTPHEADER_SERVICELICENSE,
				service.getLicenseInfo() != null ? StringUtil
						.encodeHttpHeaderValueAsUTF8(service.getLicenseInfo())
						: "");
		if (!service.isActive()) {
			throw new ServiceNotActiveException(serviceGridId + ":" + serviceId);
		}
		User u = udao.getUser(userGridId, userId);
		return new ProcessContext(u, g, service, n, daoContext, ardao, aldao,
				asdao, algdao);
	}

	protected void doFilterProcess(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			ServiceContext serviceContext, String userGridId, String userId,
			String serviceGridId, String serviceId) throws ConnectException,
			DaoException, IOException, ServletException {
		// prepare
		String protocol = getProtocol(request);
		FaultResponder responder = null;
		if (protocol.equals(Protocols.PROTOBUF_RPC)) {
			responder = new HttpFaultResponder(request.getLocalName(),
					serviceGridId, serviceId);
		} else if (protocol.equals(Protocols.JSON_RPC)) {
			responder = new HttpFaultResponder(request.getLocalName(),
					serviceGridId, serviceId);
		} else {
			responder = new SoapFaultResponder(request.getLocalName(),
					serviceGridId, serviceId);
		}
		ProcessContext c = null;
		FrontEnd fe = FrontEnd.getInstance();

		// preprocess
		Throwable expInPreprocess = null;
		daoContext.beginTransaction();
		try {
			c = prepareContextAndSetServiceInfoHeader(response, serviceContext,
					userGridId, userId, serviceGridId, serviceId);
			fe.preprocess(c, serviceContext.getRequestMimeHeaders());
		} catch (Throwable e) {
			expInPreprocess = e;
		} finally {
			daoContext.commitTransaction();
		}
		if (expInPreprocess != null) {
			ByteArrayOutputHttpServletResponseWrapper expResponse = new ByteArrayOutputHttpServletResponseWrapper(
					response);
			responder.setResponse(expResponse);
			responder.respond(expInPreprocess);
			byte[] res = expResponse.getOutput();
			response.getOutputStream().write(res);
			if (c != null && c.getTargetService() != null) {
				daoContext.beginTransaction();
				try {
					if (res.length > 4000) {
						res = Arrays.copyOf(res, 4000);
					}
					LogInfo li = FrontEnd.createLogInfo(request,
							new ByteArrayInputStream(res), -1,
							expResponse.getStatus(), res.length, protocol);
					String olg = serviceContext
							.getInitParameter(InitParam.openlangrid
									.getParameterName());
					if (olg != null && olg.equals("true")) {
						li.setRemoteHost("");
						li.setRemoteAddress("");
					}
					fe.logProcess(c, li, "Server.serverException",
							ExceptionUtil
									.getMessageWithStackTrace(expInPreprocess));
				} catch (SystemErrorException e) {
					logger.log(
							Level.SEVERE,
							"unexpected exception occurred in log process(commit fase).",
							e);
				} finally {
					daoContext.commitTransaction();
				}
			} else {
				if (expInPreprocess instanceof ServiceNotActiveException) {
					logger.log(Level.WARNING, expInPreprocess.toString());
				} else {
					logger.log(Level.WARNING, "exception for unknown service.",
							expInPreprocess);
				}
			}
			return;
		}

		// invoke, reply and postprocess
		boolean isStreaming = c.getTargetService().getStreaming();
		if (!isStreaming) {
			String v = request.getHeader(LangridConstants.HTTPHEADER_STREAMING);
			if (v != null && !v.equalsIgnoreCase("false")) {
				isStreaming = true;
			}
		}
		long millis = -1;
		int len = -1;
		int code = -1;
		byte[] responseHead = null;
		Throwable expInProcess = null;
		if (!isStreaming) {
			ByteArrayOutputHttpServletResponseWrapper wresponse = new ByteArrayOutputHttpServletResponseWrapper(
					response);
			wresponse.setStatus(200);
			try {
				long s = System.currentTimeMillis();
				chain.doFilter(request, wresponse);
				millis = System.currentTimeMillis() - s;
			} catch (Throwable e) {
				expInProcess = e;
			}
			byte[] responseBytes = wresponse.getOutput();
			// postprocess
			if (expInProcess == null
					&& serviceContext.getSelfGridId().equals(
							c.getTargetGrid().getGridId())) {
				daoContext.beginTransaction();
				try {
					fe.postprocess(c, responseBytes.length);
				} catch (Throwable t) {
					expInProcess = t;
				} finally {
					daoContext.commitTransaction();
				}
			}
			if (expInProcess == null) {
				code = wresponse.getStatus();
			} else {
				ByteArrayOutputHttpServletResponseWrapper wresponse2 = new ByteArrayOutputHttpServletResponseWrapper(
						response);
				responder.setResponse(wresponse2);
				responder.respond(expInProcess);
				responseBytes = wresponse2.getOutput();
				code = wresponse2.getStatus();
			}
			response.setStatus(code);
			response.getOutputStream().write(responseBytes);
			len = responseBytes.length;
			responseHead = Arrays.copyOf(responseBytes,
					Math.min(responseBytes.length, 4000));
		} else {
			PostprocessingHttpServletResponseWrapper wresponse = new PostprocessingHttpServletResponseWrapper(
					response, c, fe, serviceContext.getSelfGridId().equals(
							c.getTargetGrid().getGridId()));
			try {
				long s = System.currentTimeMillis();
				// invoke
				chain.doFilter(request, wresponse);
				wresponse.getOutputStream().flush();
				wresponse.getOutputStream().close();
				millis = System.currentTimeMillis() - s;
			} catch (Throwable e) {
				expInProcess = e;
			}
			if (wresponse.getWrittenBytes() == 0) {
				responder.setResponse(wresponse);
				Exception exp = wresponse.getExceptionInProcessing();
				if (exp != null) {
					responder.respond(exp);
					expInProcess = exp;
				} else if (expInProcess != null) {
					responder.respond(expInProcess);
				}
			}
			len = wresponse.getWrittenBytes();
			code = wresponse.getStatus();
			responseHead = wresponse.getResponseHead();
		}

		// logProcess
		daoContext.beginTransaction();
		try {
			Pair<String, String> soapFault = Pair.create((String) null,
					(String) null);
			if (code != 200) {
				soapFault = SOAPBodyUtil
						.extractSoapFaultString(new ByteArrayInputStream(
								responseHead));
				if (soapFault.getFirst() == null
						&& soapFault.getSecond() == null) {
					// protobuf error?
					soapFault = Pair.create("Server.unknownError",
							StringEscapeUtils.escapeHtml(new String(
									responseHead, 0, Math.min(700,
											responseHead.length),
									CharsetUtil.UTF_8)));

				}
			} else if (expInProcess != null) {
				soapFault = Pair.create("Server.processError",
						expInProcess.toString());
			}

			LogInfo li = FrontEnd.createLogInfo(request,
					new ByteArrayInputStream(responseHead), millis, code, len,
					protocol);
			String olg = serviceContext.getInitParameter(InitParam.openlangrid
					.getParameterName());
			if (olg != null && olg.equals("true")) {
				li.setRemoteHost("");
				li.setRemoteAddress("");
			}

			fe.logProcess(c, li, soapFault.getFirst(), soapFault.getSecond());
		} catch (SystemErrorException e) {
			logger.log(
					Level.SEVERE,
					"unexpected exception occurred in log process(commit fase).",
					e);
		} finally {
			daoContext.commitTransaction();
		}
	}

	private static String getProtocol(HttpServletRequest request) {
		String p = request.getHeader("X-Langrid-Protocol");
		if (p != null)
			return p;
		else
			return Protocols.DEFAULT;
	}

	/*
	 * private static String tempOutput =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
	 * "   <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
	 * + "      <soapenv:Body>" +
	 * "         <ns1:translate soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://translation.wrapper.langrid.nict.go.jp\">"
	 * +
	 * "            <sourceLang xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">ja</sourceLang>"
	 * +
	 * "            <targetLang xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">en</targetLang>"
	 * +
	 * "            <source xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">奈良女子大で塩水ぶっ掛け懲戒処分奈良女子大（奈良市）は４日、在学生と卒業生計約７０００人分の成績や学籍を管理しているコンピューターに塩水をかけて使えなくしたとして、学務課の男性職員（４９）を半日分の減給とする懲戒処分にしたと発表した。データの入ったハードディスクは無事で、コンピューターの買い替え費用約６５万円は職員が負担するという。同大学によると、職員は９月２６日早朝、ほかの職員が出てくる前に塩水をかけた。職員は仕事の負担が大きいことなどに不満を持っており、「上司や同僚を困らせようと思った」と話しているという。処分は１１月２０日付。</source>"
	 * + "         </ns1:translate>" + "      </soapenv:Body>" +
	 * "   </soapenv:Envelope>";
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

	private static Logger logger = Logger
			.getLogger(StreamingServiceSupervisor.class.getName());
}
