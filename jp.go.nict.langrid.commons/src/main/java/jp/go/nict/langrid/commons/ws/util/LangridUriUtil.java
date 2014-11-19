/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.util;

import java.net.URI;
import java.net.URISyntaxException;

import jp.go.nict.langrid.commons.util.Pair;

public class LangridUriUtil {
	public static Pair<String, String> extractServiceIds(URI uri)
	throws InvalidLangridUriException{
		String scheme = uri.getScheme();
		String[] parts = uri.getSchemeSpecificPart().split(":");
		if(!scheme.equalsIgnoreCase("servicegrid")
				|| parts.length != 3
				|| !parts[0].equalsIgnoreCase("service")
				|| parts[1].contains(":")
				|| parts[2].contains(":")
		){
			throw new InvalidLangridUriException(uri);
		}
		return Pair.create(parts[1], parts[2]);
	}

	public static Pair<String, String> extractServiceTypeIds(URI uri)
	throws InvalidLangridUriException{
		String scheme = uri.getScheme();
		String[] parts = uri.getSchemeSpecificPart().split(":");
		if(!scheme.equalsIgnoreCase("servicegrid")
				|| parts.length != 3
				|| !parts[0].equalsIgnoreCase("servicetype")
				|| parts[1].contains(":")
				|| parts[2].contains(":")
		){
			throw new InvalidLangridUriException(uri);
		}
		return Pair.create(parts[1], parts[2]);
	}

	public static URI createServiceUri(String gridId, String serviceId){
		try{
			return new URI("servicegrid:service:" + gridId + ":" + serviceId);
		} catch(URISyntaxException e){
			// must be unreachable
			throw new RuntimeException(e);
		}
	}

	public static URI createServiceTypeUri(String domainId, String serviceTypeId){
		try{
			return new URI("servicegrid:servicetype:" + domainId + ":" + serviceTypeId);
		} catch(URISyntaxException e){
			// must be unreachable
			throw new RuntimeException(e);
		}
	}
}
