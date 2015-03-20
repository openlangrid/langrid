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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.HangulAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.util.AeInvokeHelper;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;

/**
 * 形態素配列より、中間コードに置き換え文章を作成する
 * @author koyama
 * @version 1.0
 */
public class ReplacementTerm extends AbstractCustomInvokeService {
	/**
	 * 最終更新日付を返す
	 * @return 更新日付
	 * @throws ProcessFailedException
	 */
	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro("$Date: 2010-10-03 18:49:40 +0900 (Sun, 03 Oct 2010) $");
	}
	
	protected static Logger logger = Logger.getLogger(
			ReplacementTerm.class.getName()	);

	protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public String doReplace(String sourceLang, String text,
			String[] searchWords, String[] replacementWords)
	throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException 
	{
//		logger.info(sdf.format(Calendar.getInstance().getTime()) + "  TermReplacementService called.");
		if (text != null) {
			text = text.replace("..", ".");
			//ピリオドの前の空白を削除
			int index = text.indexOf(" .");
			while (index > -1) {
				text = text.substring(0, index) + text.substring(index + 1);
				index = text.indexOf(" .");
			}
			text.trim();
		}
		if (searchWords == null || replacementWords == null || searchWords.length == 0 || replacementWords.length == 0) {
			return text;
		}
		
		return replacementFromIntermediateCode(sourceLang, text, searchWords, replacementWords);
	}
	/**
	 * 形態素の間に空白が入る言語群
	 */
	protected static final Set<String> LANGUAGES = new HashSet<String>();
	static {
		LANGUAGES.add("ja");
		LANGUAGES.add("zh");
	}
	/**
	 * 中間コードから文章に戻します。
	 * @param sourceLang 言語コード
	 * @param source 文章
	 * @param searchWords 対象語
	 * @param replacementWords 置換え語
	 * @return 置換え後の文章
	 */
	protected String replacementFromIntermediateCode(String sourceLang, String source, String[] searchWords, String[] replacementWords) {
		for (int i = 0; i < searchWords.length; i++) {
			if (!searchWords[i].equals("") && !searchWords[i].equals("")) {
				int index = source.indexOf(searchWords[i]);
				if (index > -1) {
					source = source.replace(searchWords[i], replacementWords[i]);
					if (sourceLang.toLowerCase().contains("ko")) {
						source = HangulAnalysis.convertParticl(source, replacementWords[i], index);
					}
				}
			}
			if (source.indexOf(replacementWords[i]) == 0 && !LANGUAGES.contains(sourceLang)) {
				char[] ch = source.toCharArray();
				ch[0] = Character.toUpperCase(ch[0]);
				source = new String(ch);
			}
		}
		return source;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected IAeWebServiceResponse invoke(IAeInvoke invokeRequest,
			String queryData, QName qName) {
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
			String result = doReplace(sourceLang, text, searchWords, replacements);
			StringBuffer xml = new StringBuffer();
			xml.append("<replaceReturn>");
			xml.append(result);
			xml.append("</replaceReturn>");
			Document responseDoc = AeInvokeHelper.string2Document(xml.toString());
			Map respMsgParts = new HashMap();
			respMsgParts.put("replaceReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(new QName(qName.getNamespaceURI(), "replaceResponse"), respMsgParts);
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
		}
		return null;
	}
	@Override
	protected Port getPort(Service service) {
		super.setPortTypeName("ReplacementTerm");
		Port port = service.getPort("ReplacementTerm");
//		logger.severe("ReplacementTerm:start");
		return port;
	}
	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.custominvoke.workflowsupport.AbstractCustomInvokeService#handleInvoke(org.activebpel.wsio.invoke.IAeInvoke, java.lang.String)
	 */
	@Override
	public IAeWebServiceResponse handleInvoke(IAeInvoke invokeRequest,
			String queryData) {
		// なぜか、レスポンスタイプを出力できない為、空実装を行う。
		return invoke(invokeRequest, queryData, invokeRequest.getPortType());
	}
}
