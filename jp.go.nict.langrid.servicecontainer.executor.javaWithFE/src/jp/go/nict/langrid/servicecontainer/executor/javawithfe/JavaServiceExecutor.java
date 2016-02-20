/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.executor.javawithfe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.lang.ObjectUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessStatDao;
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
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LangridException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractServiceExecutor;
import jp.go.nict.langrid.servicesupervisor.frontend.FrontEnd;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

public class JavaServiceExecutor<T>
extends AbstractServiceExecutor
implements InvocationHandler{
	public JavaServiceExecutor(String invocationName, Class<T> interfaceClass){
		super(invocationName);
		this.interfaceClass = interfaceClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		Trio<ServiceContext, String, Long> r = preprocessJava(method, args);
		long deltaTime = -1;
		int status = 500;
		int reqLen = ObjectUtil.getSize(args);
		int resLen = -1;
		boolean exceptionInPreprocess = true;
		try{
			// do preprocess
			String serviceId = doFrontEndPreprocessAndServiceLocating(r.getSecond());
			exceptionInPreprocess = false;
			// load and invoke service
			long s = System.currentTimeMillis();
			Object result = method.invoke(RIProcessor.getCurrentProcessorContext().getServiceLoader().load(
					serviceId
					, interfaceClass
					), args);
			deltaTime = System.currentTimeMillis() - s;
			resLen = ObjectUtil.getSize(result);
			// do postprocess
			doFrontEndPostprocess(resLen);
			status = 200;
			return result;
		} catch(InvocationTargetException e){
			throw e.getCause();
		} catch(DaoException e){
			ProcessFailedException pe = new ProcessFailedException(e);
			setException(pe);
			throw pe;
		} catch(LangridException e){
			setAndRethrowException(e);
			return null;  // unreachable
		} finally{
			// do logProcess
			try{
				doFrontEndLogProcess(r.getSecond(), status, reqLen, deltaTime, resLen,
						exceptionInPreprocess);
			} catch(DaoException e){
				logger.log(Level.WARNING, "failed to execute log process.", e);
			} finally{
				postprocessJava(r.getThird(), deltaTime);
			}
		}
	}

	protected Trio<ServiceContext, String, Long> preprocessJava(Method method, Object[] args)
	throws ServiceNotActiveException, ServiceNotFoundException{
		Map<String, Object> mimeHeaders = new HashMap<String, Object>();
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		Pair<Endpoint, Long> r = preprocess(mimeHeaders, headers, method, args);

		String query = r.getFirst().getServiceId( ) ;
		if( query == null ) {
			throw new ServiceNotFoundException("invocationName: " + getInvocationName());
		}
		String[] values = query.split("=");
		String serviceName = values[0];
		if(values.length > 1){
			serviceName = values[1];
		}

		ServiceContext sc = new JavaServiceContext(
				RIProcessor.getCurrentServiceContext(), mimeHeaders, headers
				);
		RIProcessor.start(sc);

		return Trio.create(sc, serviceName, r.getSecond());
	}

	protected void postprocessJava(long iid, long deltaTime){
		MimeHeaders resMimeHeaders = new MimeHeaders();
		List<RpcHeader> resRpcHeaders = new ArrayList<RpcHeader>();
		RIProcessor.finish(resMimeHeaders, resRpcHeaders);
		postprocess(iid, deltaTime, resMimeHeaders, resRpcHeaders, null);

		currentProcessContext.remove();
		currentException.remove();
	}

	protected String doFrontEndPreprocessAndServiceLocating(String serviceId)
	throws ServiceNotActiveException, ServiceNotFoundException, ProcessFailedException
	, AccessLimitExceededException, NoAccessPermissionException, DaoException{
		ServiceContext serviceContext = RIProcessor.getCurrentServiceContext();
		String sgridId = serviceContext.getSelfGridId();
		String[] srv = serviceId.split(":", 2);
		if (srv.length == 2) {
			serviceId = srv[1];
		}
		String[] v = serviceContext.getAuthUser().split(":", 2);
		String gridId, userId;
		if(v.length == 2){
			gridId = v[0];
			userId = v[1];
		} else{
			gridId = sgridId;
			userId = v[0];
		}
		FrontEnd fe = FrontEnd.getInstance();
		dc.beginTransaction();
		try{
			Grid g = gdao.getGrid(gridId);
			Node n = ndao.getNode(gridId, serviceContext.getSelfNodeId());
			Service service = sdao.getService(sgridId, serviceId);
			if(service.isUseAlternateService() && service.getAlternateServiceId() != null){
				service = sdao.getService(gridId, service.getAlternateServiceId());
			}
			if(!service.isActive()){
				throw new ServiceNotActiveException(serviceId);
			}
			User u = udao.getUser(gridId, userId);
			ProcessContext c = new ProcessContext(
					u, g, service, n
					, dc, rdao, ldao, asdao, aldao
					);
			currentProcessContext.set(c);
			fe.preprocess(c, serviceContext.getRequestMimeHeaders());
			return locateService(service);
		} catch(jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException e){
			throw new AccessLimitExceededException(e.getMessage());
		} catch(jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException e){
			throw new NoAccessPermissionException(userId);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw new ServiceNotFoundException(serviceId);
		} catch(Throwable e){
			throw new ProcessFailedException(e);
		} finally{
			dc.commitTransaction();
		}
	}

	protected void doFrontEndPostprocess(int responseLength)
	throws AccessLimitExceededException, ProcessFailedException, DaoException{
		ProcessContext pc = currentProcessContext.get();
		FrontEnd fe = FrontEnd.getInstance();
		dc.beginTransaction();
		try{
			fe.postprocess(pc, responseLength);
		} catch(jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException e){
			throw new AccessLimitExceededException(e.getMessage());
		} catch(Throwable t){
			throw new ProcessFailedException(t);
		} finally{
			dc.commitTransaction();
		}
		
	}

	protected void doFrontEndLogProcess(
			String serviceId, int status, int requestLength,
			long responseMillis, int responseLength,
			boolean exceptionInPreprocess)
	throws ProcessFailedException, DaoException{
		ProcessContext pc = currentProcessContext.get();
		FrontEnd fe = FrontEnd.getInstance();
		dc.beginTransaction();
		try{
			Throwable t = currentException.get();
			if(t == null){
				fe.logProcess(pc, FrontEnd.createJavaCallLogInfo(
						RIProcessor.getCurrentServiceContext().getRequestMimeHeaders()
						, serviceId, requestLength, responseMillis, status, responseLength
						, Protocols.JAVA_CALL
						), "", "", exceptionInPreprocess);
			} else{
				currentException.remove();
				fe.logProcess(pc, FrontEnd.createJavaCallLogInfo(
						RIProcessor.getCurrentServiceContext().getRequestMimeHeaders()
						, serviceId, requestLength, -1, 500, 0
						, Protocols.JAVA_CALL
						), "Server.userException", ExceptionUtil.getMessageWithStackTrace(t),
						exceptionInPreprocess);
			}
		} catch(SystemErrorException e){
			throw new ProcessFailedException(e);
		} finally{
			dc.commitTransaction();
		}
	}

	protected void setException(Throwable t){
		currentException.set(t);
	}

	protected void setAndRethrowException(LangridException e)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException{
		if(e instanceof AccessLimitExceededException){
			throw (AccessLimitExceededException)e;
		}
		if(e instanceof InvalidParameterException){
			throw (InvalidParameterException)e;
		}
		if(e instanceof NoAccessPermissionException){
			throw (NoAccessPermissionException)e;
		}
		if(e instanceof ProcessFailedException){
			throw (ProcessFailedException)e;
		}
		if(e instanceof NoValidEndpointsException){
			throw (NoValidEndpointsException)e;
		}
		if(e instanceof ServerBusyException){
			throw (ServerBusyException)e;
		}
		if(e instanceof ServiceNotActiveException){
			throw (ServiceNotActiveException)e;
		}
		if(e instanceof ServiceNotFoundException){
			throw (ServiceNotFoundException)e;
		}
		throw new ProcessFailedException(e);
	}

	private String locateService(Service service){
		for(ServiceEndpoint ep : service.getServiceEndpoints()){
			String file = ep.getUrl().getFile();
			int i = file.lastIndexOf("/");
			if(i != -1){
				file = file.substring(i + 1);
			}
			return file;
		}
		return null;
	}

	private ThreadLocal<ProcessContext> currentProcessContext
			= new ThreadLocal<ProcessContext>();
	private ThreadLocal<Throwable> currentException
			= new ThreadLocal<Throwable>();
	private static DaoContext dc;
	private static GridDao gdao;
	private static ServiceDao sdao;
	private static UserDao udao;
	private static NodeDao ndao;
	private static AccessRightDao rdao;
	private static AccessLimitDao ldao;
	private static AccessLogDao aldao;
	private static AccessStatDao asdao;
	private static Logger logger = Logger.getLogger(JavaServiceExecutor.class.getName());
	static{
		try{
			DaoFactory df = DaoFactory.createInstance();
			dc = df.getDaoContext();
			gdao = df.createGridDao();
			sdao = df.createServiceDao();
			udao = df.createUserDao();
			ndao = df.createNodeDao();
			rdao = df.createAccessRightDao();
			ldao = df.createAccessLimitDao();
			aldao = df.createAccessLogDao();
			asdao = df.createAccessStateDao();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	private Class<?> interfaceClass;
}
