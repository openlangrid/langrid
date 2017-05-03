/*
 * $Id: CallTreeUtil.java 1143 2014-02-13 01:18:48Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.cs.calltree;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.codec.URLCodec;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.soap.MimeHeaders;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1143 $
 */
public class CallTreeUtil {
	/**
	 * 
	 * 
	 */
	public static String encodeTree(Collection<CallNode> value){
		return JSON.encode(value);
	}

	/**
	 * 
	 * 
	 */
	public static String encodeTree(Collection<CallNode> value, int indentFactor){
		return JSON.encode(value, true);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("serial")
	public static Collection<CallNode> decodeTree(String value)
	throws ParseException
	{
		try{
			return JSON.decode(
					value, new ArrayList<CallNode>(){}.getClass().getGenericSuperclass()
					);
		} catch(JSONException e){
			throw new ParseException(e.getMessage(), -1);
		}
	}

	/**
	 * 
	 * 
	 */
	public static Collection<CallNode> decodeTreeFromHttpBody(
			String body)
	throws ParseException{
		int i = body.indexOf("calltree");
		int begin = body.indexOf(">", i) + 1;
		int end = body.indexOf("<", begin);
		String callTreeJson = body.substring(begin, end);
		callTreeJson = URLCodec.decode(callTreeJson);
		callTreeJson = callTreeJson.replace("&quot;", "\"");
		return decodeTree(callTreeJson);
	}

	/**
	 * 
	 * 
	 */
	public static CallNode createNode(MimeHeaders mimeHeaders, Iterable<RpcHeader> rpcHeaders)
	throws ParseException{
		CallNode node = new CallNode();
		node.setServiceName(MimeHeadersUtil.getJoinedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICENAME
				));
		node.setServiceCopyright(MimeHeadersUtil.getJoinedAndDecodedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICECOPYRIGHT
				));
		node.setServiceLicense(MimeHeadersUtil.getJoinedAndDecodedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICELICENSE
				));
		if(rpcHeaders != null) for(RpcHeader h : rpcHeaders){
			if(h.getNamespace().equals(LangridConstants.ACTOR_SERVICE_CALLTREE)){
				Collection<CallNode> cn = CallTreeUtil.decodeTree(h.getValue());
				node.getChildren().addAll(cn);
			}
		}
		return node;
	}

	/**
	 * 
	 * 
	 */
	public static List<CallNode> extractNodes(Iterable<RpcHeader> headers)
	throws ParseException{
		List<CallNode> nodes = new ArrayList<CallNode>();
		for(RpcHeader h : headers){
			if(h.getNamespace().equals(LangridConstants.ACTOR_SERVICE_CALLTREE)){
				Collection<CallNode> cn = CallTreeUtil.decodeTree(h.getValue());
				nodes.addAll(cn);
			}
		}
		return nodes;
	}
}
