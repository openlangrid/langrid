package jp.go.nict.langrid.custominvoke.workflowsupport.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.activebpel.wsio.AeWebServiceMessageData;
import org.activebpel.wsio.invoke.AeInvokeResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AeInvokeHelper {

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
	
	public static NodeList selectNodeList(Document doc, String strXpath) throws XPathExpressionException {
	    XPathFactory factory = XPathFactory.newInstance();
	    javax.xml.xpath.XPath xpath = factory.newXPath();
	    XPathExpression expr 
	     = xpath.compile(strXpath);

	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList)result;
	    return nodes;
	}
	
	public static Object selectSingleNode(Document doc, String xpath) 	{
		Object results = AeInvokeHelper.selectSingleNode(doc, xpath);
		return results;
	}
	
	public static Document createDocument(String xml) 	{
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
}
