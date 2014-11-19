/*
 * $Id: FilterConfigParameterContext.java 379 2011-08-23 02:52:06Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.param;

import javax.servlet.FilterConfig;

import jp.go.nict.langrid.commons.parameter.ParameterContext;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 379 $
 */
public class FilterConfigParameterContext extends ParameterContext{
	/**
	 * 
	 * 
	 */
	public FilterConfigParameterContext(FilterConfig config, boolean inheritServletContext){
		this.config = config;
		this.inheritServletContext = inheritServletContext;
	}

	/**
	 * 
	 * 
	 */
	public FilterConfigParameterContext(FilterConfig config){
		this(config, true);
	}

	public String getValue(String name) {
		String value = config.getInitParameter(name);
		if(value == null && inheritServletContext){
			return config.getServletContext().getInitParameter(name);
		} else{
			return value;
		}
	}

	private FilterConfig config;
	private boolean inheritServletContext;
}
