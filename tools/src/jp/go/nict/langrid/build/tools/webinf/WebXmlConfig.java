package jp.go.nict.langrid.build.tools.webinf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.SAXException;

public class WebXmlConfig {
	public WebXmlConfig(File webXmlFile){
		this.file = webXmlFile;
	}

	public String getServletInitParam(String servletName, String paramName)
	throws FileNotFoundException, IOException, XPathExpressionException, SAXException, ParserConfigurationException{
		InputStream is = new FileInputStream(file);
		try{
			Object o = XPathFactory.newInstance().newXPath().compile(
					"//web-app/servlet[servlet-name/text()='" +
					servletName +
					"']/init-param[param-name/text()='" +
					paramName +
					"']/param-value/text()"
					).evaluate(
							DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is),
							XPathConstants.STRING);
			return o.toString();
		} finally{
			is.close();
		}
	}

	public String getContextInitParam(String paramName)
	throws FileNotFoundException, IOException, XPathExpressionException, SAXException, ParserConfigurationException{
		InputStream is = new FileInputStream(file);
		try{
			Object o = XPathFactory.newInstance().newXPath().compile(
					"//web-app/context-param[param-name/text()='" +
					paramName +
					"']/param-value/text()"
					).evaluate(
							DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is),
							XPathConstants.STRING);
			return o.toString();
		} finally{
			is.close();
		}
	}

	private File file;
}
