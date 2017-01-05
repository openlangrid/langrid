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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.HttpHeaderParameterContext;
import jp.go.nict.langrid.commons.ws.util.LangridHttpUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.Executor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ExecutorParams;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.invm.InVmExecutor;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class InvocationProcessor
extends AbstractLangridServlet{

	@Override
	public void init() throws ServletException {
		super.init();
		try{
			new ParameterLoader().load(params, getParameterContext());
		} catch(ParameterRequiredException e){
			throw new ServletException(e);
		}
		try{
			inVm = new InVmExecutor(getDaoFactory(), params);
		} catch(DaoException e){
			throw new ServletException(e);
		}
		try{
			Class<?> clazz = Class.forName("jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid.IntraGridExecutor");
			intraGrid = (Executor)clazz.getConstructor(
							DaoFactory.class, ExecutorParams.class
					).newInstance(
							getDaoFactory(), params
					);
		} catch(ClassNotFoundException e){
		} catch(IllegalArgumentException e){
		} catch(SecurityException e){
		} catch(InstantiationException e){
		} catch(IllegalAccessException e){
		} catch(InvocationTargetException e){
		} catch(NoSuchMethodException e){
		}
		try{
			Class<?> clazz = Class.forName("jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intergrid.InterGridExecutor");
			interGrid = (Executor)clazz.getConstructor(
							DaoFactory.class, ExecutorParams.class
					).newInstance(
							getDaoFactory(), params
							);
		} catch(ClassNotFoundException e){
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
	}

	protected void doProcess(
			HttpServletRequest request, HttpServletResponse response,
			ServiceContext serviceContext, DaoContext daoContext,
			String userGridId, String userId,
			String serviceGridId, String serviceId,
			String additionalUrlPart)
	throws IOException, ServletException{
		try{
			getExecutor(serviceContext.getSelfGridId(), serviceGridId).execute(
					getServletContext(), request, response,
					serviceContext, daoContext,
					userGridId, userId,
					serviceGridId, serviceId,
					createHeaders(request),
					additionalUrlPart, getProtocol(request),
					StreamUtil.readAsBytes(request.getInputStream())
					);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			LangridHttpUtil.write404_ServiceNotFound(response, serviceGridId, serviceId);
			return;
		} catch(jp.go.nict.langrid.dao.FederationNotFoundException e){
			LangridHttpUtil.write403_FederationNotFound(response, serviceContext.getSelfGridId(), serviceGridId);
			return;
		} catch(DaoException e){
			LangridHttpUtil.write500_Exception(response, serviceGridId, serviceId, e);
			return;
		} catch(IOException e){
			logger.log(Level.SEVERE, "unexpected exception occurred", e);
			LangridHttpUtil.write500_Exception(response, serviceGridId, serviceId, e);
			return;
		} catch(TooManyCallNestException e){
			LangridHttpUtil.write403_TooManyCallNest(response, serviceGridId, serviceId
					, e.getThreashold(), e.getNest());
			return;
		} catch(NoValidEndpointsException e){
			LangridHttpUtil.write500_Exception(response, serviceGridId, serviceId, e);
			return;
		} catch(ProcessFailedException e){
			LangridHttpUtil.write500_Exception(response, serviceGridId, serviceId, e);
			return;
		}
	}

	private Executor getExecutor(String selfGridId, String gridId){
		if(interGrid == null || gridId.equals(selfGridId)){
			if(intraGrid != null) return intraGrid;
			else return inVm;
		} else{
			return interGrid;
		}
	}

	private Map<String, String> createHeaders(HttpServletRequest request){
		ParameterContext c = new HttpHeaderParameterContext(request);

		Map<String, String> headers = new HashMap<String, String>();

		String fromAddr = request.getRemoteAddr();
		if(fromAddr.equals("127.0.0.1")){
			fromAddr = c.getString(
					LangridConstants.HTTPHEADER_FROMADDRESS
					, fromAddr);
		}
		headers.put(
				LangridConstants.HTTPHEADER_FROMADDRESS, fromAddr
				);
		int nest = c.getInteger(
				LangridConstants.HTTPHEADER_CALLNEST
				, 0
				);
		headers.put(
			LangridConstants.HTTPHEADER_CALLNEST
				, Integer.toString(nest)
				);
		Enumeration<String> en = request.getHeaderNames();
		String transHeader1 = LangridConstants.HTTPHEADER_TRANSFER_TO_ENDPOINT.toLowerCase();
		String transHeader2 = LangridConstants.HTTPHEADER_TRANSFER_TO_ENDPOINT_OBSOLETE.toLowerCase();
		while(en.hasMoreElements()){
			String name = en.nextElement().toLowerCase();
			if(transHeaders.containsKey(name)){
				headers.put(transHeaders.get(name), request.getHeader(name));
			} else if(name.startsWith(transHeader1)){
				String n = name.substring(transHeader1.length());
				headers.put(n, request.getHeader(name));
			} else if(name.startsWith(transHeader2)){
				String n = name.substring(transHeader2.length());
				headers.put(n, request.getHeader(name));
			}
		}

		return headers;
	}

	private static Map<String, String> transHeaders = new HashMap<>();
	static{
		for(String s : new String[]{
				LangridConstants.HTTPHEADER_PROTOCOL,
				LangridConstants.HTTPHEADER_TYPEOFAPPPROVISION,
				LangridConstants.HTTPHEADER_TYPEOFUSE,
				LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION,
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CREATESHORTCUT,
				LangridConstants.HTTPHEADER_FEDERATEDCALL_REMOVESHORTCUT,
				LangridConstants.HTTPHEADER_FEDERATEDCALL_ROUTE,
				}){
			transHeaders.put(s.toLowerCase(), s);
		}
		transHeaders = Collections.unmodifiableMap(transHeaders);
	}

	private String getProtocol(HttpServletRequest request){
		String p = request.getHeader(LangridConstants.HTTPHEADER_PROTOCOL);
		if(p != null) return p;
		else return params.defaultProtocol;
	}

	private ExecutorParams params = new ExecutorParams();
	private Executor inVm;
	private Executor intraGrid;
	private Executor interGrid;

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
	private static Logger logger = Logger.getLogger(InvocationProcessor.class.getName());
	private static final long serialVersionUID = 6695509468882848868L;
}
