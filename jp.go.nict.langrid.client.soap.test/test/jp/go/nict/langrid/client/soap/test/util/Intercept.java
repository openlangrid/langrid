package jp.go.nict.langrid.client.soap.test.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jp.go.nict.langrid.client.soap.InputStreamFilter;
import jp.go.nict.langrid.client.soap.OutputStreamFilter;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.io.DuplicatingOutputStream;

public class Intercept {
	public static Intercepted requestXmlToFile(String fileName){
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SoapClientFactory.setOutputStreamFilter(new OutputStreamFilter() {
			@Override
			public OutputStream filter(OutputStream os) {
				return new DuplicatingOutputStream(os, baos);
			}
		});
		return new InterceptedXmlToFile(baos, fileName);
	}

	public static Intercepted responseXmlToFile(String fileName){
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SoapClientFactory.setInputStreamFilter(new InputStreamFilter() {
			@Override
			public InputStream filter(InputStream is) {
				return new DuplicatingInputStream(is, baos);
			}
		});
		return new InterceptedXmlToFile(baos, fileName);
	}
}
