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
package jp.go.nict.langrid.cosee;

import java.net.URI;
import java.util.Map;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class UserInfoEndpointRewriter extends AbstractEndpointRewriter{
	@Override
	public Endpoint rewrite(Endpoint original, Map<String, Object> properties,
			URI processNamespace, String partnerLinkName, URI serviceNamespace,
			String methodName, String[] paramNames, Object[] args) {
		String ui = original.getAddress().getUserInfo();
		do{
			if(ui == null) break;
			String [] v = ui.split(":");
			if(v[0].length() == 0) break;
			Endpoint ret = new Endpoint(original);
			ret.setUserName(v[0]);
			ret.setPassword(v[1]);
			return ret;
		} while(false);
		return original;
	}
}
