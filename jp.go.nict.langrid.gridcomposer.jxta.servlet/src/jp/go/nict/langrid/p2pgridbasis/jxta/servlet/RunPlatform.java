/*
 * $Id: RunPlatform.java 780 2013-04-01 17:28:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.p2pgridbasis.jxta.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.FileUtil;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;

/**
 * Servlet implementation class for Servlet: RunPlatform
 *
 */
public class RunPlatform extends HttpServlet {
	private static final long serialVersionUID = -8961074803812392302L;
	
	@Override
	public void init(ServletConfig config)
	throws ServletException{
		super.init(config);
		String p2pEnabled = config.getServletContext().getInitParameter("p2pEnabled");
		if(p2pEnabled != null && p2pEnabled.equals("false")) return;
		try {
			File cm = new File(config.getServletContext().getRealPath("WEB-INF") + File.separator + "jxta/cm");
			if(cm.exists()) {
				FileUtil.forceDelete(cm);
			}
			InitServletServiceContext context = new InitServletServiceContext(config);
			JXTAController.setBaseDir(context.getApplicationDirectory());
			JXTAController.setUpInstance(context);
			JXTAController.getInstance().start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}  	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	public void destroy() {
		try{
			JXTAController.getInstance().shutdown();
		} catch(ControllerException e){
			e.printStackTrace();
		}
	}
}
