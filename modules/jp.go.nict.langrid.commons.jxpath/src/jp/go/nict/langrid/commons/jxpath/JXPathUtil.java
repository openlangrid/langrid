/*
 * $Id: JXPathUtil.java 191 2010-10-02 11:29:54Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.jxpath;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;

import jp.go.nict.langrid.commons.dom.DocumentUtil;

import org.apache.commons.jxpath.JXPathContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 191 $
 */
public class JXPathUtil {
	/**
	 * 
	 * 
	 */
	public static JXPathContext newXMLContext(String anXml)
		throws IOException, SAXException
	{
		DocumentBuilder builder = DocumentUtil.newDocumentBuilder();
		return JXPathContext.newContext(builder.parse(
			new InputSource(new StringReader(anXml))
			));
	}

	/**
	 * 
	 * 
	 */
	public static JXPathContext newXMLContext(InputStream anInputStream)
		throws IOException, SAXException
	{
		DocumentBuilder builder = DocumentUtil.newDocumentBuilder();
		return JXPathContext.newContext(builder.parse(anInputStream));
	}
}
