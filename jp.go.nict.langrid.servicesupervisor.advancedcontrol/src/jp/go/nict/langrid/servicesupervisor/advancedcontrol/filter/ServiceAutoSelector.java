/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.advancedcontrol.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.FilterConfigParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.servicesupervisor.advancedcontrol.filter.SelectionLogic.Mode;

/**
 * Service Auto Select Filter.
 * @author Takao Nakaguchi
 */
public class ServiceAutoSelector extends AbstractHttpFilter implements Filter{
	@Override
	public void init(FilterConfig config) throws ServletException {
		ParameterContext pc = new FilterConfigParameterContext(config);
		gridId = pc.getValue("langrid.node.gridId");
		if(gridId == null){
			throw new ServletException("langrid.node.gridId is not set.");
		}
		nodeId = pc.getValue("langrid.node.nodeId");
		if(nodeId == null){
			throw new ServletException("langrid.node.nodeId is not set.");
		}

		mode = pc.getEnum("mode", Mode.FASTEST, Mode.class);
		
		prefix = pc.getValue("prefix");
		if(prefix == null){
			throw new ServletException("prefix is not set.");
		}

		try{
			factory = DaoFactory.createInstance();
			stdao = factory.createServiceTypeDao();
			sdao = factory.createServiceDao();
			logic = new SelectionLogic(
					factory.getDaoContext()
					, sdao
					, factory.createAccessRightDao()
					);
			for(ServiceType t : stdao.listAllServiceTypes()){
				mappings.put(prefix + t.getServiceTypeId(), Pair.create(t.getDomainId(), t.getServiceTypeId()));
			}
		} catch(DaoException e){
			throw new ServletException(e);
		}
	}

	@Override
	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Trio<String, String, String> ids = HttpServletRequestUtil.parseRequestUrl(request);
		Map<String, List<String>> queries = HttpServletRequestUtil.queryToMap(request.getQueryString());
		if(ids == null){
			chain.doFilter(request, response);
			return;
		}
		String gid = ids.getFirst();
		if(gid != null && !gid.equals(gridId)){
			chain.doFilter(request, response);
			return;
		}
		String sid = ids.getSecond();
		if(!sid.startsWith(prefix)){
			chain.doFilter(request, response);
			return;
		}

		Pair<String, String> type = mappings.get(sid);
		if(type == null){
			String typeId = sid.substring(prefix.length());
			try{
				for(ServiceType st : stdao.listAllServiceTypes()){
					if(st.getServiceTypeId().equals(typeId)){
						type = Pair.create(st.getDomainId(), st.getServiceTypeId());
						mappings.put(sid, type);
					}
				}
			} catch(DaoException e){
			}
			if(type == null){
				chain.doFilter(request, response);
				return;
			}
		}

		ServletServiceContext context = new ServletServiceContext(request);
		try {
			String[] candidates = {"supportedLanguages"
					, "supportedLanguagePairs", "supportedLanguagePaths"};
			String langQueryName = null;
			String langQueryValue = null;
			for(String n : candidates){
				if(queries.containsKey(n)){
					langQueryName = n;
					List<String> q = queries.get(n);
					if(q != null & q.size() > 0){
						try{
							langQueryValue = URLDecoder.decode(q.get(0), "UTF-8").replace('|', ' ');
						} catch(UnsupportedEncodingException e){
						}
					}
					break;
				}
			}
			sid = logic.select(mode, gridId, nodeId, type.getFirst(), type.getSecond()
					, langQueryName, langQueryValue, 3
					, context.getAuthUserGridId(), context.getAuthUser());
		} catch (DaoException e) {
			logger.log(Level.WARNING, "failed to select service.", e);
			chain.doFilter(request, response);
			return;
		}
		if(sid == null){
			chain.doFilter(request, response);
			return;
		}
		final String oldId = ids.getSecond();
		final String newId = gridId + ":" + sid;
		chain.doFilter(new HttpServletRequestWrapper(request){
			@Override
			public String getRequestURI() {
				return replace(super.getRequestURI());
			}
			@Override
			public StringBuffer getRequestURL() {
				return new StringBuffer(replace(super.getRequestURL().toString()));
			}
			@Override
			public String getPathInfo() {
				return replace(super.getPathInfo());
			}
			private String replace(String path){
				return path.replaceFirst("(invoker\\/)([^:]*:)?" + oldId, "$1\\" + newId);
			}
		}, response);
	}

	@Override
	public void destroy() {
	}

	private String gridId;
	private String nodeId;
	private Mode mode = Mode.MOST_POPULAR;
	private String prefix;
	private SelectionLogic logic;
	private DaoFactory factory;
	private ServiceTypeDao stdao;
	private ServiceDao sdao;
	private static Map<String, Pair<String, String>> mappings = new ConcurrentHashMap<String, Pair<String,String>>();
	private static Logger logger = Logger.getLogger(ServiceAutoSelector.class.getName());
}
