/*
 * $Id: DocumentUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * Copyright (c) 2002, 2004 Takao Nakaguchi.
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
package jp.go.nict.langrid.commons.dom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class DocumentUtil {
	/**
	 * 
	 * 
	 */
	public static Document newDocument(){
		return defaultDocumentBuilder.newDocument();
	}

	/**
	 * 
	 * 
	 */
	public static Document getDefaultDocument(){
		return defaultDocument;
	}

	/**
	 * 
	 * 
	 */
	public static DocumentBuilder newDocumentBuilder(){
		try{
			return defaultDocumentBuilderFactory.newDocumentBuilder();
		} catch(ParserConfigurationException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static DocumentBuilder getDefaultDocumentBuilder(){
		return defaultDocumentBuilder;
	}

	/**
	 * 
	 * 
	 */
	public static String toString(Document document){
		ByteArrayOutputStream out = null;
		try{
			out = new ByteArrayOutputStream();
			defaultTextTransformer.transform(
					new DOMSource(document)
					, new StreamResult(out));
		return new String(out.toByteArray(), "UTF-8");
		} catch(TransformerConfigurationException e){
			throw new RuntimeException(e);
		} catch(TransformerException e){
			throw new RuntimeException(e);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
				out = null;
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public static void writeDocument(Document document, OutputStream outputStream)
		throws TransformerException
	{
		defaultTransformer.transform(
				new DOMSource(document)
				, new StreamResult(outputStream)
				);
	}

	/**
	 * 
	 * 
	 */
	public static void writeDocument(Document document, Writer writer)
		throws TransformerException
	{
		defaultTransformer.transform(
				new DOMSource(document)
				, new StreamResult(writer)
				);
	}

	/**
	 * 
	 * 
	 */
	public static void writeDocument(Document document, StreamResult streamResult)
		throws TransformerException
	{
		defaultTransformer.transform(new DOMSource(document), streamResult);
	}

	/**
	 * 
	 * 
	 */
	public static TransformerFactory getDefaultTransformerFactory(){
		return defaultTransformerFactory;
	}

	/**
	 * 
	 * 
	 */
	public static Transformer getDefaultTransformer(){
		return defaultTransformer;
	}

	/**
	 * 
	 * 
	 */
	public static XPathFactory getDefaultXPathFactory(){
		return defaultXPathFactory;
	}

	/**
	 * 
	 * 
	 */
	public static XPath getDefaultXPath(){
		return defaultXPath;
	}

	private static DocumentBuilderFactory defaultDocumentBuilderFactory;
	private static DocumentBuilder defaultDocumentBuilder;
	private static Document defaultDocument;
	private static TransformerFactory defaultTransformerFactory;
	private static Transformer defaultTransformer;
	private static Transformer defaultTextTransformer;
	private static XPathFactory defaultXPathFactory;
	private static XPath defaultXPath;
	static{
		defaultDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		defaultDocumentBuilder = newDocumentBuilder();
		defaultDocument = newDocument();
		try {
			defaultTransformerFactory = TransformerFactory.newInstance();
		} catch (TransformerFactoryConfigurationError e) {
			throw new RuntimeException(e);
		}
		try{
			defaultTransformer = defaultTransformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		try{
			defaultTextTransformer = defaultTransformerFactory.newTransformer();
			defaultTextTransformer.setOutputProperty("encoding", "UTF-8");
			defaultTextTransformer.setOutputProperty("indent", "yes");
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		defaultXPathFactory = XPathFactory.newInstance();
		defaultXPath = defaultXPathFactory.newXPath();
	}
}
