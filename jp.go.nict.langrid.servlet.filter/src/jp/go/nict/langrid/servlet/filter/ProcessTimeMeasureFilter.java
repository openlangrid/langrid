/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.servlet.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.param.FilterConfigParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;

/**
 * Filter for measuring process time.
 * @author Takao Nakaguchi
 */
public class ProcessTimeMeasureFilter extends AbstractHttpFilter{
	@Override
	public void init(FilterConfig config) throws ServletException {
		ParameterContext c = new FilterConfigParameterContext(config, true);
		measureProcessTime = c.getBoolean("langrid.measureProcessTime", false);
		String p = c.getString("langrid.urlPatterns", "");
		if(p.length() > 0){
			patterns = p.split(",");
		}
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean doMeasure = false;
		String uri = request.getRequestURI();
		do{
			if(!measureProcessTime) break;
			if(patterns == null){
				doMeasure = true;
				break;
			}
			for(String p : patterns){
				doMeasure = uri.startsWith(p);
				if(doMeasure) break;
			}
		} while(false);
		long s = System.currentTimeMillis();
		if(doMeasure){
			logger.info("[" + uri + "] process start at " + s);
		}
		try{
			chain.doFilter(request, response);
		} finally{
			if(doMeasure){
				long e = System.currentTimeMillis();
				logger.info("[" + uri + "] process end at " + e
						+ "  d: " + (e - s));
			}
		}
	}

	@Override
	public void destroy() {
	}

	private boolean measureProcessTime;
	private String[] patterns;
	private static Logger logger = Logger.getLogger(ProcessTimeMeasureFilter.class.getName());
}
