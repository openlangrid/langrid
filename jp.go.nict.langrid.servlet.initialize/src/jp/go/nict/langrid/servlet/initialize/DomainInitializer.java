/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servlet.initialize;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 */
public class DomainInitializer extends HttpServlet{
	@Override
	public void destroy() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		try{
			ParameterContext c = new ServletConfigParameterContext(config);
			String gridId = c.getValue("langrid.node.gridId");
			if(gridId == null){
				logger.info("langrid.node.gridId is not set. ignore initializing domain.");
				return;
			}
			DaoFactory f = DaoFactory.createInstance();
			DaoContext dc = f.getDaoContext();
			jp.go.nict.langrid.dao.initializer.DomainInitializer.init(f, dc, gridId
						, c.getBoolean("dropAndCreate", false)
						, config.getServletContext().getRealPath("/WEB-INF/init"));
		} catch(DaoException e) {
			throw new ServletException(e);
		} catch(IOException e) {
			throw new ServletException(e);
		} catch(SQLException e) {
			throw new ServletException(e);
		}
	}

	private static Logger logger = Logger.getLogger(DomainInitializer.class.getName());
	private static final long serialVersionUID = -4569347409727752706L;
}
