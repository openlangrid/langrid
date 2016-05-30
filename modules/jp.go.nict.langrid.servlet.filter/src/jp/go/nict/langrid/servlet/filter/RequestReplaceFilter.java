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
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;

public class RequestReplaceFilter extends AbstractHttpFilter{
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(
		HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException
	{
		request = new HttpServletRequestWrapper(request) {
			@Override
			public StringBuffer getRequestURL() {
				StringBuffer s = super.getRequestURL();
				return s.toString().contains("kyotou.langrid")
					? new StringBuffer(s.toString().replaceAll("kyotou\\.langrid", "kyoto1.langrid"))
					: s;
			}
		};
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	private static Logger logger = Logger.getLogger(RequestReplaceFilter.class.getName());
}
