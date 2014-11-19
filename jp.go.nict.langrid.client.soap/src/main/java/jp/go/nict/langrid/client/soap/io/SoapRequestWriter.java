/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.client.soap.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.client.soap.io.parameter.Encoder;
import jp.go.nict.langrid.client.soap.io.parameter.EncoderUtil;
import jp.go.nict.langrid.client.soap.io.parameter.Encoders;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.ClassResource;
import jp.go.nict.langrid.commons.lang.ClassResourceLoader;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.commons.transformer.UTF8ByteArrayToStringTransformer;

import org.apache.commons.lang.StringEscapeUtils;

import com.floreysoft.jmte.Engine;

public class SoapRequestWriter {
	public static void writeSoapRequest(OutputStream os,
			Iterable<RpcHeader> headers
			, Method method, Object... args)
	throws IOException, IllegalAccessException, InvocationTargetException{
		writeSoapRequest(os, null, headers, method, args);
	}
	public static void writeSoapRequest(OutputStream os, 
			String namespace, Iterable<RpcHeader> headers
			, Method method, Object... args)
	throws IOException, IllegalAccessException, InvocationTargetException{
		Service sa = method.getDeclaringClass().getAnnotation(Service.class);
		Map<String, Object> bindings = new HashMap<String, Object>();
		Class<?>[] paramTypes = method.getParameterTypes();
		Annotation[][] paramAnnots = method.getParameterAnnotations();
		List<Encoder> params = new ArrayList<Encoder>();
		int n = paramTypes.length;
		for(int i = 0; i < n; i++){
			String name = EncoderUtil.getParameterName(paramAnnots[i]);
			if(name == null) name = "in" + i;
			params.add(Encoders.create(3, name, paramTypes[i], args[i]));
		}
		String ns = namespace;
		if(ns == null){
			if(sa != null && sa.namespace().length() > 0){
				ns = sa.namespace();
			} else{
				String sn = method.getDeclaringClass().getSimpleName();
				if(sn.endsWith("Service")){
					sn = sn.substring(0, sn.length() - 7);
				}
				ns = "servicegrid:servicetype:nict.nlp:" + sn;
			}
		}
		bindings.put("namespace", ns);
		bindings.put("headers", escapeValueXml(headers));
		bindings.put("methodName", method.getName());
		bindings.put("parameters", params);

		StreamUtil.writeString(os,
				jmte.transform(template, bindings), "UTF-8");
	}

	private static Iterable<RpcHeader> escapeValueXml(Iterable<RpcHeader> v){
		List<RpcHeader> r = new ArrayList<RpcHeader>();
		for(RpcHeader h : v){
			r.add(new RpcHeader(h.getNamespace(), h.getName()
					, StringEscapeUtils.escapeXml(h.getValue())));
		}
		return r;
	}

	@ClassResource(path="/SoapRequest.tmpl.jmte", converter=UTF8ByteArrayToStringTransformer.class)
	private static String template;
	private static Engine jmte;
	static{
		try {
			ClassResourceLoader.load(SoapRequestWriter.class);
			jmte = Engine.createNonCachingEngine();
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

/*
<soapenv:Envelope
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:xsd="http://www.w3.org/2001/XMLSchema"
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
xmlns:ser="servicegrid:servicetype:nict.nlp:Translation">
  <soapenv:Header/>
  <soapenv:Body>
     <ser:translate soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
        <sourceLang xsi:type="xsd:string">ja</sourceLang>
        <targetLang xsi:type="xsd:string">en</targetLang>
        <source xsi:type="xsd:string">hello</source>
     </ser:translate>
  </soapenv:Body>
</soapenv:Envelope>
*/
