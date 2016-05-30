/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class URLParameterContext extends ParameterContext{
	public URLParameterContext(URL url){
		params = HttpServletRequestUtil.queryToMap(url.getQuery());
	}

	public URLParameterContext(String query){
		try{
			params = HttpServletRequestUtil.queryToMap(URLDecoder.decode(query, "UTF-8"));
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getValue(String name) {
		List<String> v = params.get(name);
		if(v != null){
			return StringUtil.join(v.toArray(new String[]{}), ",");
		}
		return null;
	}

	private Map<String, List<String>> params;
}
