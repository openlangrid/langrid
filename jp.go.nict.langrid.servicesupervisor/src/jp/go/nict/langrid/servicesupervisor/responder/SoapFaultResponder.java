package jp.go.nict.langrid.servicesupervisor.responder;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.servicesupervisor.ServiceNotActiveException;
import jp.go.nict.langrid.servicesupervisor.ServiceNotFoundException;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException;

import org.apache.commons.lang.StringEscapeUtils;

public class SoapFaultResponder extends FaultResponder{
	public SoapFaultResponder(
			String hostName, String gridId, String serviceId){
		this.hostName = hostName;
	}

	@Override
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doRespond(ServiceNotActiveException e) throws IOException {
		processFaultTemplate(response, e, Arrays.asList(
				Pair.create("serviceId", e.getServiceId())
				));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doRespond(ServiceNotFoundException e) throws IOException {
		processFaultTemplate(response, e, Arrays.asList(
				Pair.create("serviceId", e.getServiceId())
				));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doRespond(jp.go.nict.langrid.dao.ServiceNotFoundException e) throws IOException {
		processFaultTemplate(response, e, Arrays.asList(
				Pair.create("serviceId", e.getServiceGridId() + ":" + e.getServiceId())
				));
	}

	@Override
	protected void doRespond(AccessLimitExceededException e) throws IOException {
		processFaultTemplate(response, e, new ArrayList<Pair<String, String>>());
	}

	@Override
	protected void doRespond(NoAccessPermissionException e) throws IOException {
		processFaultTemplate(response, e, new ArrayList<Pair<String, String>>());
	}

	@Override
	protected void doRespond(Throwable e) throws IOException {
		processFaultTemplate(response, e, new ArrayList<Pair<String, String>>());
	}

	private void processFaultTemplate(
			HttpServletResponse response, Throwable exception
			, List<Pair<String, String>> props
			)
		throws IOException
	{
		response.setStatus(200);
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Server", "LanguageGrid-CoreNode/1.0");
		response.setDateHeader("Date", new Date().getTime());
		response.setHeader("X-ServiceGrid-Exception", exception.toString().replaceAll("\n", "\\n"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("escapeUtils", StringEscapeUtils.class);
		map.put("exception", exception);
		map.put("hostName", hostName);
		String cn = exception.getClass().getName();
		map.put("nsSuffix", "");
		map.put("properties", props);

		if(cn.startsWith("jp.go.nict.langrid.service_1_2.")){
			String prefix = cn.substring("jp.go.nict.langrid.service_1_2.".length());
			int i = prefix.lastIndexOf(".");
			if(i != -1){
				map.put(
						"nsSuffix"
						, prefix.substring(0, i).replace(".", "/") + "/"
						);
			}
		}
		map.put("description", exception.getMessage());

		Writer w = response.getWriter();
		exceptionTemplate.make(map).writeTo(response.getWriter());
		w.flush();
	}

	private static final String RES_EXCEPTION = "exception.template";
	private static Template exceptionTemplate;
	static{
		SimpleTemplateEngine engine = new SimpleTemplateEngine();
		try {
			exceptionTemplate = engine.createTemplate(
					SoapFaultResponder.class.getResource(RES_EXCEPTION)
					);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String hostName;
	private HttpServletResponse response;
}
