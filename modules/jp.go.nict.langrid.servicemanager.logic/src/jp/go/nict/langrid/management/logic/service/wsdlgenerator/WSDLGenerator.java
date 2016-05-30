/*
 * $Id: WSDLGenerator.java 497 2012-05-24 04:13:03Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic.service.wsdlgenerator;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class WSDLGenerator {
	/**
	 * 
	 * 
	 */
	public static byte[] generate(
			URI targetNamespace, String serviceName, String endpointUrl
			, InputStream baseWsdl, boolean baseWsdlZipped)
	throws IOException{
		if(baseWsdl == null){
			Map<String, String> map = new HashMap<String, String>();
			map.put("message", "The WSDL of this service is not available.");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			emptyWsdl.make(map).writeTo(new OutputStreamWriter(out, "UTF-8"));
			return out.toByteArray();
		}
		String base = StreamUtil.readAsString(
				baseWsdlZipped ? getUnzippedWsdl(baseWsdl) : baseWsdl
				, "UTF-8");
//		System.out.println("---- base ----");
//		System.out.println(base);
		return replaceNs(base, targetNamespace, serviceName, endpointUrl).getBytes("UTF-8");
	}

	private static InputStream getUnzippedWsdl(InputStream baseWsdl)
	throws IOException{
		ZipInputStream zis = new ZipInputStream(baseWsdl);
		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null){
			if(entry.getName().endsWith(".wsdl")) return zis;
			if(entry.getName().endsWith(".xml")) return zis;
		}
		throw new FileNotFoundException("no wsdl files(*.wsdl or *.xml) found.");
	}

	private static String replaceNs(
			String wsdl, URI namespace, String serviceName, String endpointUrl){
		String n = namespace.toString();
		String u = endpointUrl.toString();
		StringBuffer b = new StringBuffer();
		Matcher m = nsPattern.matcher(wsdl);
		String firstTns = null;
		while(m.find()){
			String pImplOrIntf = m.group(1);
			String pTns = m.group(3);
			String pIpns = m.group(5);
			String pSn = m.group(7);
			String pAddr = m.group(8);
			String pBodyNs = m.group(9);
			if(pImplOrIntf != null){
				String nsPrefix = m.group(2);
				if(nsPrefix.equals("impl") || nsPrefix.equals("intf")){
					m.appendReplacement(b, "xmlns:$2=\"" + n + "\"");
				} else{
					m.appendReplacement(b, m.group());
				}
			} else if(pTns != null){
				String value = m.group(4);
				if(firstTns == null){
					firstTns = value;
				}
				if(value.equals(firstTns)){
					m.appendReplacement(b, "targetNamespace=\"" + n + "\"");
				} else{
					m.appendReplacement(b, m.group());
				}
			} else if(pIpns != null){
				String value = m.group(6);
				if(value.equals(firstTns)){
					m.appendReplacement(b, "namespace=\"" + n + "\"");
				} else{
					m.appendReplacement(b, m.group());
				}
			} else if(pSn != null){
				m.appendReplacement(b, "service name=\"" + serviceName + "\"");
			} else if(pAddr != null){
				m.appendReplacement(b, "address location=\"" + u + "\"");
			} else if(pBodyNs != null){
				m.appendReplacement(b, m.group(10) + n + m.group(11));
			}
		}
		m.appendTail(b);
		return b.toString();
	}

	private static Template emptyWsdl;
	private static Pattern nsPattern = Pattern.compile(
			"(xmlns:(\\w+)=\"[\\w\\.,:\\/\\?&=_-]+\")|" +
			"(targetNamespace=\"([\\w\\.,:\\/\\?&=_-]+)\")|" +
			"(namespace=\"([\\w\\.,:\\/\\?&=_-]+)\")|" +
			"(service +name=\"[\\w\\._]+\")|" +
			"(address +location=\"[\\w\\.,:\\/\\?&=_-]+\")|" +
			"((body +.*namespace=\")[\\w\\.,:\\/\\?&=_-]+(\"))"
			);
	static {
		try {
			Class< WSDLGenerator > clazz = WSDLGenerator.class;
			TemplateEngine engine = new SimpleTemplateEngine();
			emptyWsdl = engine.createTemplate( clazz.getResource( "errorWsdl.xml.template" ) );
		} catch ( ClassNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}
}
