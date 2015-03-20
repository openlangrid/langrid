/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package jp.go.nict.langrid.custominvoke.workflowsupport;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.activebpel.wsio.AeWebServiceMessageData;
import org.activebpel.wsio.invoke.AeInvokeResponse;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AeInvokeHelper {
	private static Logger logger = Logger.getLogger(AeInvokeHelper.class.getName());

	@SuppressWarnings("unchecked")
	public static Map parseQueryData(String queryData)
	{
		if (queryData == null) {return null;}
		
		Map queryDataAsMap = new HashMap();
		StringTokenizer st = new StringTokenizer(queryData, "&");
		while(st.hasMoreTokens()) {
			String nvPair = st.nextToken();
			String[] nvArray = nvPair.split("=");
			if (nvArray.length == 2) {
				queryDataAsMap.put(nvArray[0], nvArray[1]);
			}
		}
		return queryDataAsMap;
	}
	
	@SuppressWarnings("unchecked")
	public static AeInvokeResponse createOutputMessage(QName msgQName, Map respData) 
	{
		AeWebServiceMessageData msgData = 
			new AeWebServiceMessageData(msgQName, respData);
		AeInvokeResponse response = new AeInvokeResponse();
		response.setMessageData(msgData);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static AeInvokeResponse createFaultMessage(QName faultQName, QName faultMsgQName, Map faultData) 
	{
		AeWebServiceMessageData msgData = 
			new AeWebServiceMessageData(faultMsgQName, faultData);
		AeInvokeResponse response = new AeInvokeResponse();
		response.setFaultData(faultQName, msgData);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static List selectNodes(Document doc, String xpath, Map namespaces)
	{
		List results = null;
		
		try {
			XPath expr = new DOMXPath(xpath);
			if (namespaces != null) {
				Set prefixSet = namespaces.keySet();
				for (Iterator iter = prefixSet.iterator(); iter.hasNext();) {
					String prefix = (String) iter.next();
					String uri = (String) namespaces.get(prefix);
					expr.addNamespace(prefix, uri);
				}
			}
			results = expr.selectNodes(doc);

		} catch (JaxenException e) {
			e.printStackTrace();
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public static List selectNodes(Document doc, String xpath)
	{
		List results = AeInvokeHelper.selectNodes(doc, xpath, null);
		return results;
	}
	
//	public static Node selectNode(Element elem, String strXpath) throws XPathExpressionException {
//	    XPathFactory factory = XPathFactory.newInstance();
//	    javax.xml.xpath.XPath xpath = factory.newXPath();
//	    XPathExpression expr = xpath.compile(strXpath);
//
//	    Object result = expr.evaluate(elem, XPathConstants.NODE);
//	    Node node = (Node)result;
//	    return node;
//	}
//	public static NodeList selectNodeList(Element elem, String strXpath) throws XPathExpressionException {
//	    XPathFactory factory = XPathFactory.newInstance();
//	    javax.xml.xpath.XPath xpath = factory.newXPath();
//	    XPathExpression expr = xpath.compile(strXpath);
//
//	    Object result = expr.evaluate(elem, XPathConstants.NODESET);
//	    NodeList nodes = (NodeList)result;
//	    return nodes;
//	}
	
	public static NodeList selectNodeList(Document doc, String strXpath) throws XPathExpressionException {
	    XPathFactory factory = XPathFactory.newInstance();
	    javax.xml.xpath.XPath xpath = factory.newXPath();
	    XPathExpression expr 
	     = xpath.compile(strXpath);

	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList)result;
	    return nodes;
	}
	@SuppressWarnings("unchecked")
	public static Object selectSingleNode(Document doc, String xpath, Map namespaces)
	{
		List results = AeInvokeHelper.selectNodes(doc, xpath, namespaces);
		return results.get(0);
	}
	
	public static Object selectSingleNode(Document doc, String xpath)
	{
		Object results = AeInvokeHelper.selectSingleNode(doc, xpath, null);
		return results;
	}
	
	public static Document string2Document(String xml)
	{
		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setNamespaceAware(true);

		Document doc = null;
		try {
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	public static String document2String(Document doc) {
		String str = null;
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformer = null;
		try {
			transformer = factory.newTransformer();
			transformer.transform(new DOMSource(doc.getDocumentElement()), result);
			str = result.getWriter().toString();
		} catch (TransformerConfigurationException e) {
			logger.severe(e.getMessage());
		} catch (TransformerException e) {
			logger.severe(e.getMessage());
		}
		return str;
	}
	
	public static String printNode(Node node) {
		String result = "";
		try {
			TransformerFactory transformerFactory =	TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("method", "xml");
			StringWriter sw = new StringWriter();					
			DOMSource source = new DOMSource(node);
			transformer.transform(source, new StreamResult(sw));
			result = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * Sanitize string.
	 */
	private static final String SANITIZE_STRING[][] = {
		{ "&(?!amp;)",  "&amp;" },
		{ "<",  "&lt;" },
		{ ">",  "&gt;" },
		{ "\"", "&quot;" },
//		{ "'",  "&#39;" }
	};
		
	/**
	 * It returns Sanitize string.
	 * @param string message
	 * @return
	 */
	public static String sanitize(String str) {
		String result = str;
		if (result != null && result.length() > 0) {
			for (int i = 0; i < SANITIZE_STRING.length; i++) {
				result = Pattern.compile(SANITIZE_STRING[i][0]).matcher(result).replaceAll(SANITIZE_STRING[i][1]);
			}
		}
		return result;
	}
}
