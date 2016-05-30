package jp.go.nict.langrid.client.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.function.Filters;

public class TestContext {
	public TestContext(ClientFactory factory) throws IOException{
		this(getBaseClass(), factory);
	}

	private static Class<?> getBaseClass(){
		try{
			return Class.forName(new Exception().getStackTrace()[2].getClassName());
		} catch(ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}

	public TestContext(Class<?> base, ClientFactory factory) throws IOException{
		this.factory = factory;

		{
			InputStream is = base.getResourceAsStream("TestContext_url");
			if(is == null) throw new RuntimeException("TestContext_url is needed beside the test class and also Context_auth if needed.");
			try{
				Iterator<String> it = StreamUtil.readLines(
						new InputStreamReader(is, "UTF-8"), Filters.ignoreBlankAndComment()
						).iterator();
				if(!it.hasNext()) throw new RuntimeException("no information in Context_url");
				this.baseUrl = it.next();
			} finally{
				is.close();
			}
		}

		InputStream is = base.getResourceAsStream("TestContext_auth");
		if(is == null) return;
		try{
			Iterator<String> it = StreamUtil.readLines(
					new InputStreamReader(is, "UTF-8"), Filters.ignoreBlankAndComment()
					).iterator();
			if(it.hasNext()) userId = it.next();
			if(it.hasNext()) password = it.next();
		} finally{
			is.close();
		}
	}

	public <T> T createClient(String name, Class<T> clazz){
		T c = factory.create(clazz, url(name));
		RequestAttributes reqAttrs = (RequestAttributes)c;
		if(userId != null){
			reqAttrs.setUserId(userId);
			reqAttrs.setPassword(password);
		}
		return c;
	}

	private URL url(String name){
		try {
			return new URL(baseUrl + name);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private ClientFactory factory;
	private String baseUrl;
	private String userId;
	private String password;
}
