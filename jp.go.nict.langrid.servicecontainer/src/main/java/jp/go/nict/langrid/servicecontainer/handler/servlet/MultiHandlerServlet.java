/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.handler.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MultiHandlerServlet extends HttpServlet{
	protected void put(String path, HttpServlet servlet){
		if(initialized) throw new RuntimeException("Servlet is already initialized.");
		servlets.put(path, servlet);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		for(HttpServlet s : servlets.values()){
			s.init(config);
		}
		initialized = true;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] paths = req.getRequestURI().split("/");
		if(paths.length >= 3){
			HttpServlet s = servlets.get(paths[2]);
			if(s != null){
				s.service(req, resp);
				return;
			}
		}
		super.service(req, resp);
	}

	private Map<String, HttpServlet> servlets = new HashMap<String, HttpServlet>();
	private boolean initialized;
}
