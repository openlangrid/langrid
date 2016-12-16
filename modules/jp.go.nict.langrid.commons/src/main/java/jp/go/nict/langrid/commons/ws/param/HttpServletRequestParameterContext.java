/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jp.go.nict.langrid.commons.parameter.ParameterContext;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class HttpServletRequestParameterContext extends ParameterContext{
	/**
	 * 
	 * 
	 */
	public HttpServletRequestParameterContext(HttpServletRequest request){
		this.request = request;
	}

	public String getValue(String name) {
		return request.getParameter(name);
	}

	@Override
	public String[] getStrings(String parameterName, String[] defaultValue) {
		String[] values = request.getParameterValues(parameterName);
		List<String> ret = new ArrayList<>();
		if(values != null){
			for(String v : values){
				ret.addAll(Arrays.asList(v.split(",")));
			}
			return ret.toArray(new String[]{});
		}
		return defaultValue;
	}

	private HttpServletRequest request;
}
