package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;

public interface ServiceExecutor {
	public List<ServiceEndpoint> getEndpoints(
			Service service, String protocol)
	throws DaoException;
	
	public void invokeRequest(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response 
			, ServiceContext serviceContext, DaoContext daoContext
			, Service service, List<ServiceEndpoint> endpoint
			, String additionalUrlPart, Map<String, String> headers
			, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException
	, ProcessFailedException, IOException;
}
