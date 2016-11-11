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
package jp.go.nict.langrid.commons.ws;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.util.Base64Util;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class BasicAuthUtil {
	/**
	 * 
	 * 
	 */
	public static Pair<String, String> decode(String authorization){
		if(authorization == null) return null;
		String[] values = authorization.split(" ");
		if(values.length != 2) return null;
		if(!values[0].equalsIgnoreCase("Basic")) return null;
		String[] v = Base64Util.decode(values[1]).split(":", 2);
		String user = v.length > 0 ? v[0] : "";
		String pass = v.length > 1 ? v[1] : "";
		return Pair.create(user, pass);
	}

	/**
	 * 
	 * 
	 */
	public static String encode(String user, String pass) {
		if(user == null || pass == null) return null;
		String userAndPass = user + ":" + pass;
		return "Basic " + Base64Util.encode(userAndPass);
	}
}
