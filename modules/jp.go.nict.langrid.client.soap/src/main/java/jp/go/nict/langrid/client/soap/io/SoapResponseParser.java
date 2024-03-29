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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.io.CascadingIOException;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class SoapResponseParser {
	public static <T> Trio<Collection<RpcHeader>, RpcFault, T> parseSoapResponse(
			Class<T> returnType, String methodName, InputStream is
			, Converter converter)
	throws IOException, SAXException{
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		XPathWorkspace w = workspace.get();
		try{
			Node envelope = w.docBuilder.parse(is).getDocumentElement();
			Node header = findFirstChildNamed(envelope, "Header");
			if(header != null) for(Node h = header.getFirstChild(); h != null; h = h.getNextSibling()){
				if(!(h instanceof Element)) continue;
				headers.add(new RpcHeader(
						h.getNamespaceURI(), h.getLocalName(), h.getTextContent()
						));
			}

			Node body = findFirstChildNamed(envelope, "Body");
			if(body != null) for(Node res = body.getFirstChild(); res != null; res = res.getNextSibling()){
				if(!(res instanceof Element)) continue;
				if(res.getLocalName().equals(methodName + "Response")){
					for(Node ret = res.getFirstChild(); ret != null; ret = ret.getNextSibling()){
						if(!(ret instanceof Element)) continue;
						if(ret.getLocalName().equals(methodName + "Return") || ret.getLocalName().equals(methodName + "Result")){
							try {
								T r = nodeToType(w, ret, returnType, converter);
								return new Trio<Collection<RpcHeader>, RpcFault, T>(
										headers, null, r);
							} catch(IllegalArgumentException e) {
								throw new CascadingIOException(e);
							} catch(InstantiationException e) {
								throw new CascadingIOException(e);
							} catch(IllegalAccessException e) {
								throw new CascadingIOException(e);
							} catch(InvocationTargetException e) {
								throw new CascadingIOException(e);
							} catch(ParseException e) {
								throw new CascadingIOException(e);
							}
						}
					}
				} else if(res.getLocalName().equals("Fault")){
					RpcFault f = new RpcFault();
					NodeList nl = res.getChildNodes();
					Converter c = new Converter();
					for(int i = 0; i < nl.getLength(); i++){
						Node n = nl.item(i);
						if(!(n instanceof Element)) continue;
						if(n.getLocalName().equals("faultcode")){
							f.setFaultCode(n.getTextContent());
						} else if(n.getLocalName().equals("faultstring")){
							f.setFaultString(n.getTextContent());
						} else if(n.getLocalName().equals("detail")){
							String detail = n.getNodeValue();
							f.setDetail(detail);
							Node expTag = n.getFirstChild();
							if(expTag != null){
								NodeList pl = expTag.getChildNodes();
								try{
									Class<?> expClass = Class.forName(expTag.getNodeName());
									Object expInstance = expClass.newInstance();
									for(int j = 0; j < pl.getLength(); j++){
										Node prop = pl.item(j);
										String propName = prop.getNodeName();
										Method setter = ClassUtil.findSetter(expClass, propName);
										if(setter == null) continue;
										try {
											setter.invoke(expInstance, c.convert(prop.getTextContent(), setter.getParameterTypes()[0]));
										} catch (IllegalArgumentException e) {
										} catch (InvocationTargetException e) {
										} catch (ConversionException e) {
										} catch (DOMException e) {
										}
									}
									f.setFaultString(expTag.getNodeName() + ":" + JSON.encode(expInstance));
								} catch(ClassNotFoundException e){
								}
							}
						}
					}
					return new Trio<Collection<RpcHeader>, RpcFault, T>(headers, f, null);
				}
			}
			if(returnType.equals(void.class)){
				return new Trio<Collection<RpcHeader>, RpcFault, T>(headers, null, null);
			}
			throw new IOException("not a valid SOAP message.");
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InstantiationException e){
			throw new RuntimeException(e);
		} finally{
			workspace.remove();
		}
	}

	private static Node findFirstChildNamed(Node parent, String localName){
		Node n = parent.getFirstChild();
		while(n != null && ((n.getNodeType() != Node.ELEMENT_NODE) || !n.getLocalName().equals(localName))){
			n = n.getNextSibling();
		}
		return n;
	}

	private static Node findFirstDescendantHasAttr(Node parent, String attrName, String attrValue){
		if(parent == null) return null;
		Node n = parent.getFirstChild();
		while(n != null){
			NamedNodeMap attrs = n.getAttributes();
			if(attrs != null){
				Node attr = attrs.getNamedItem(attrName);
				if(attr != null){
					if(attr.getNodeValue().equals(attrValue)){
						return n;
					}
				}
			}
			Node d = findFirstDescendantHasAttr(n, attrName, attrValue);
			if(d != null){
				return d;
			}
			n = n.getNextSibling();
		}
		return n;
	}

	@SuppressWarnings({ "unchecked" })
	private static <T> T nodeToType(XPathWorkspace w, Node node, Class<T> clazz, Converter converter)
	throws InstantiationException, IllegalAccessException, IllegalArgumentException
	, InvocationTargetException, ConversionException, DOMException, ParseException{
		if(clazz.isPrimitive() || clazz.getName().startsWith("java.lang.")){
			return converter.convert(resolveHref(w, node).getTextContent(), clazz);
		} else if(clazz.equals(String.class)){
			return clazz.cast(resolveHref(w, node).getTextContent());
		} else if(clazz.equals(byte[].class)){
			try{
				return clazz.cast(Base64.decodeBase64(resolveHref(w, node).getTextContent().getBytes("ISO8859-1")));
			} catch(UnsupportedEncodingException e){
				throw new RuntimeException(e);
			}
		} else if(clazz.equals(Calendar.class)){
			Date date = fmt.get().parse(resolveHref(w, node).getTextContent());
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return clazz.cast(c);
		} else if(clazz.isArray()){
			Class<?> ct = clazz.getComponentType();
			List<T> elements = new ArrayList<>();
			node = resolveHref(w, node);
			for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()){
				if(!(child instanceof Element)) continue;
				elements.add((T)nodeToType(w, child, ct, converter));
			}
			return (T)ArrayUtil.toArray(elements, ct);
		} else{
			T instance = clazz.newInstance();
			node = resolveHref(w, node);
			for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()){
				if(!(child instanceof Element)) continue;
				String nn = child.getLocalName();
				Method setter = ClassUtil.findSetter(clazz, nn);
				if(setter != null){
					setter.invoke(instance, nodeToType(w, resolveHref(w, child), setter.getParameterTypes()[0], converter));
				}
			}
			return instance;
		}
	}

	private static class XPathWorkspace{
		public XPathWorkspace() throws ParserConfigurationException{
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true);
			docBuilder = f.newDocumentBuilder();
		}
		private DocumentBuilder docBuilder;
	}

	private static Node resolveHref(XPathWorkspace w, Node node)
	throws DOMException{
		Node href = node.getAttributes().getNamedItem("href");
		if(href != null){
			return findFirstDescendantHasAttr(
					node.getOwnerDocument().getDocumentElement(), "id", href.getTextContent().substring(1));
		} else{
			return node;
		}
	}

	private static ThreadLocal<XPathWorkspace> workspace = new ThreadLocal<XPathWorkspace>(){
		protected XPathWorkspace initialValue() {
			try {
				return new XPathWorkspace();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			}
		};
	};

	private static ThreadLocal<DateFormat> fmt = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			f.setTimeZone(TimeZone.getTimeZone("GMT"));
			return f;
		};
	} ;
}
