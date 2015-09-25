/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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
package jp.go.nict.langrid.client.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.function.Filters;

public class TestContext {
	/**
	 * Creates instance. The base class is automatically detected using stack trace.
	 * @param factory
	 * @throws IOException
	 */
	public TestContext(ClientFactory factory) throws IOException{
		this(getBaseClass(), factory);
	}

	private static Class<?> getBaseClass(){
		try{
			return Class.forName(getCallerStackTraceElement().getClassName());
		} catch(ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}

	public TestContext(Class<?> base, ClientFactory factory) throws IOException{
		this.baseClass = base;
		this.factory = factory;

		{
			InputStream is = getResourceAsStream("TestContext_url");
			if(is == null) throw new RuntimeException("TestContext_url is needed beside the test class and also Context_auth if needed.");
			try{
				this.baseUrl = findFirstValidString(StreamUtil.readLines(
						new InputStreamReader(is, "UTF-8"), Filters.ignoreBlankAndComment()
						).iterator());
				if(this.baseUrl == null) throw new RuntimeException("no valid information in Context_url");
			} finally{
				is.close();
			}
		}

		InputStream is = getResourceAsStream("TestContext_auth");
		if(is == null) return;
		try{
			Iterator<String> it = StreamUtil.readLines(
					new InputStreamReader(is, "UTF-8"), Filters.ignoreBlankAndComment()
					).iterator();
			this.userId = findFirstValidString(it);
			this.password = findFirstValidString(it);
		} finally{
			is.close();
		}
	}

	/**
	 * Creates the service client for passed service.
	 * This method reads base url (actual url is base url + name) from TestContext_url file
	 * and reads userName and password of basic auth from TestContext_auth.
	 * @param name service name that preceded by url in TestContext_url file.
	 * @param serviceInterface
	 * @return client
	 */
	public <T> T createClient(String name, Class<T> serviceInterface){
		T c = factory.create(serviceInterface, url(name));
		RequestAttributes reqAttrs = (RequestAttributes)c;
		if(userId != null){
			reqAttrs.setUserId(userId);
			reqAttrs.setPassword(password);
		}
		return c;
	}

	/**
	 * Get InputStream of resource "name" from getResourceAsStream method of base class.
	 * @param name
	 * @return
	 */
	public InputStream getResourceAsStream(String name){
		return baseClass.getResourceAsStream(name);
	}

	public String getResourceAsString(String encoding, String name)
	throws UnsupportedEncodingException, IOException{
		InputStream is = getResourceAsStream(name);
		if(is == null) return null;
		try{
			return StreamUtil.readAsString(is, encoding);
		} finally{
			is.close();
		}
	}

	/**
	 * Get InputStream of resource that has name consists from simple class name and method name of caller class.
	 * @return
	 */
	public InputStream getTestResourceAsStream(String... postfix){
		StackTraceElement ste = getCallerStackTraceElement();
		String fqcn = ste.getClassName();
		String cn = fqcn.substring(fqcn.lastIndexOf('.') + 1).replaceAll("\\$", "_");
		String mn = ste.getMethodName();
		StringBuilder b = new StringBuilder(cn).append("_").append(mn);
		for(String p : postfix){
			b.append(p);
		}
		return getResourceAsStream(b.toString());
	}

	public String getTestResourceAsString(String encoding, String... postfix)
	throws UnsupportedEncodingException, IOException{
		InputStream is = getTestResourceAsStream(postfix);
		if(is == null) return null;
		try{
			return StreamUtil.readAsString(is, encoding);
		} finally{
			is.close();
		}
	}

	private static StackTraceElement getCallerStackTraceElement(){
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		String thisClassName = TestContext.class.getName();
		for(int i = 1; i < stes.length; i++){
			if(!stes[i].getClassName().equals(thisClassName)){
				return stes[i];
			}
		}
		return null;
	}

	private URL url(String name){
		try {
			return new URL(baseUrl + name);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String findFirstValidString(Iterator<String> it){
		if(it == null) return null;
		while(it.hasNext()){
			String s = it.next().trim();
			if(s.length() == 0) continue;
			if(s.startsWith("#")) continue;
			return s;
		}
		return null;
	}

	private Class<?> baseClass;
	private ClientFactory factory;
	private String baseUrl;
	private String userId;
	private String password;
}
