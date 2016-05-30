/*
 * $Id: BPELUtil.java 191 2010-10-02 11:29:54Z t-nakaguchi $
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
import java.net.URI;
import java.net.URISyntaxException;

import jp.go.nict.langrid.commons.ws.Constants;

import org.apache.commons.jxpath.JXPathContext;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 191 $
 */
public class BPELUtil {
	/**
	 * 
	 * 
	 */
	public static JXPathContext newBPELContext(
		InputStream bpel, String bpelNSPrefix)
		throws IOException, SAXException
	{
		JXPathContext context = JXPathUtil.newXMLContext(bpel);
		context.setLenient(true);
		context.registerNamespace(bpelNSPrefix, Constants.BPEL_URI);
		return context;
	}

	/**
	 * 
	 * 
	 */
	public static JXPathContext newWSBPEL_2_0_Context(
		InputStream bpel, String bpelNSPrefix)
		throws IOException, SAXException
	{
		JXPathContext context = JXPathUtil.newXMLContext(bpel);
		context.setLenient(true);
		context.registerNamespace(bpelNSPrefix, Constants.WSBPEL_2_0_URI);
		return context;
	}

	/**
	 * 
	 * 
	 */
	public static URI getTargetNamespace(InputStream body)
		throws IOException, SAXException, URISyntaxException
	{
		String value = (String)newBPELContext(body, "_").getValue(
				"_:process/@targetNamespace"
				);
		if(value == null) return null;
		return new URI(value);
	}

	/**
	 * 
	 * 
	 */
	public static URI getWSBPEL_2_0_TargetNamespace(InputStream body)
		throws IOException, SAXException, URISyntaxException
	{
		String value = (String)newWSBPEL_2_0_Context(body, "_").getValue(
				"_:process/@targetNamespace"
				);
		if(value == null) return null;
		return new URI(value);
	}

	/**
	 * 
	 * 
	 */
	public static String getProcessName(InputStream body)
		throws IOException, SAXException
	{
		return (String)newBPELContext(body, "_").getValue(
			"_:process/@name"
			);
	}
}
