/*
 * $Id: ReplacementTerm.java 260 2010-10-03 09:49:40Z t-nakaguchi $
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

package jp.go.nict.langrid.custominvoke.workflowsupport;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 形態素配列より、中間コードに置き換え文章を作成する
 * @author koyama
 * @version 1.0
 */
public class ReplacementTerm implements IAeInvokeHandler{
	public ReplacementTerm(){
		replacementTerm = new jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm();
	}

	public ReplacementTerm(jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm replacementTerm){
		this.replacementTerm = replacementTerm;
	}

	@SuppressWarnings("unchecked")
	public IAeWebServiceResponse handleInvoke(IAeInvoke invokeRequest,
			String queryData) {
		long start = start("prepare to invoke");	// ###
		long current = start;	// ###
		try {
			Map<?, ?> reqMsgParts = invokeRequest.getInputMessageData().getMessageData();
			String sourceLang = (String)reqMsgParts.get("sourceLang");
			String text = AeInvokeHelper.sanitize((String)reqMsgParts.get("text"));
			Document replacementDoc = (Document)reqMsgParts.get("replacementWords");
			Document searchDoc =(Document)reqMsgParts.get("searchWords");
			NodeList replacementNodes = AeInvokeHelper.selectNodeList(replacementDoc, "/replacementWords/string/text()");
			NodeList searchWordNodes = AeInvokeHelper.selectNodeList(searchDoc, "/searchWords/string/text()");
			String[] replacements = new String[replacementNodes.getLength()];
			for (int i = 0; i < replacements.length; i++) {
				replacements[i] = AeInvokeHelper.sanitize(replacementNodes.item(i).getNodeValue());
			}
			String[] searchWords = new String[searchWordNodes.getLength()];
			for (int i = 0; i < searchWords.length; i++) {
				searchWords[i] = AeInvokeHelper.sanitize(searchWordNodes.item(i).getNodeValue());
			}
			replacementDoc = null;
			searchDoc = null;
			current = lap(current, "preparation done. invoke doRepolace(..)"); //###
			String result = replacementTerm.doReplace(sourceLang, text, searchWords, replacements);
			current = lap(current, "invocation done. build response"); //###
			StringBuffer xml = new StringBuffer();
			xml.append("<replaceReturn>");
			xml.append(result);
			xml.append("</replaceReturn>");
			Document responseDoc = AeInvokeHelper.string2Document(xml.toString());
			Map respMsgParts = new HashMap();
			respMsgParts.put("replaceReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(resultQName, respMsgParts);
			current = lap(current, "build response done");	// ###
			return response;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			e.printStackTrace();
		} catch (AccessLimitExceededException e) {
			e.printStackTrace();
		} catch (NoAccessPermissionException e) {
			e.printStackTrace();
		} catch (NoValidEndpointsException e) {
			e.printStackTrace();
		} catch (ServerBusyException e) {
			e.printStackTrace();
		} catch (ServiceNotActiveException e) {
			e.printStackTrace();
		} catch (ServiceNotFoundException e) {
			e.printStackTrace();
		} finally{
			finish(start, current);	// ###
		}
		return null;
	}

	private long start(String message){
		if(!logEnabled) return 0;
		System.out.println(logHeader + "started. " + message);
		return System.currentTimeMillis();
	}

	private long lap(long current, String message){
		if(!logEnabled) return 0;
		long d = System.currentTimeMillis() - current;
		System.out.println(logHeader + "lap(" + d + "ms laps): " + message);
		return System.currentTimeMillis();
	}

	private void finish(long start, long current){
		if(!logEnabled) return;
		long f = System.currentTimeMillis();
		long dt = f - start;
		long dl = f - current;
		System.out.println(logHeader + "finished(" + dl + "ms laps, " + dt + "ms total)");
	}

	private jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm replacementTerm;
	private String logHeader = "[ReplacementTerm] ";
	private boolean logEnabled = false;
	private static QName resultQName = new QName(
			"http://localhost:8080/workflow-support/services/ReplacementTerm"
			, "replaceResponse"
			);
//	private static Logger logger = Logger.getLogger(
//			ReplacementTerm.class.getName()	);
}
