package jp.go.nict.langrid.composite.commons.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.reflect.LoggingProxy;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.function.Filters;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

public class TestContext {
	public TestContext(Class<?> base, ClientFactory factory) throws IOException{
		this.factory = factory;

		{
			InputStream is = base.getResourceAsStream("Context_url");
			if(is == null) throw new RuntimeException("Context_url is needed beside the test class and also Context_auth if needed.");
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

		InputStream is = base.getResourceAsStream("Context_auth");
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

	@SuppressWarnings("unchecked")
	public <T> T createClient(String name, Class<T> clazz){
		T c = factory.create(clazz, url(name));
		RequestAttributes reqAttrs = (RequestAttributes)c;
		if(userId != null){
			reqAttrs.setUserId(userId);
			reqAttrs.setPassword(password);
		}
		return (T)LoggingProxy.create(c);
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public static Converter getConverter() {
		return converter;
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

	static Converter converter = new Converter();
	
	static class PartOfSpeechToStringTransformer implements Transformer<PartOfSpeech, String>{
		@Override
		public String transform(PartOfSpeech value)
				throws TransformationException {
			return value.getExpression();
		}
	}
	static{
		converter.addTransformerConversion(new PartOfSpeechToStringTransformer());
	}
}
