package jp.go.nict.langrid.client.soap.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.InputStreamFilter;
import jp.go.nict.langrid.client.soap.OutputStreamFilter;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.io.DuplicatingOutputStream;
import jp.go.nict.langrid.commons.lang.reflect.MethodUtil;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class SoapClientFactoryLanguageServiceWithBindingTest{
	@Test
	public void test_TranslationService() throws Exception{
		TranslationService service = create(TranslationService.class);
		((RequestAttributes)service).getTreeBindings().add(new BindingNode("Translation", "Service1"));
		assertFieldsNotNull(service.translate("en", "ja", "hello"));
	}

	private static final boolean intercept = true;
	private String resFile;
	private OutputStream inputOs;
	private String reqFile;
	private OutputStream outputOs;

	@After
	public void tearDown() throws Exception{
		if(intercept){
			inputOs.close();
			outputOs.close();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,"2");
			for(String s : new String[]{reqFile, resFile}){
				DOMSource source = new DOMSource(db.parse(new File(s)));
				OutputStream os = new FileOutputStream(s);
				try{
					t.transform(source, new StreamResult(os));
				} finally{
					os.close();
				}
			}
		}
	}

	private <T> T create(Class<T> intf) throws MalformedURLException, FileNotFoundException{
		if(intercept){
			resFile = intf.getSimpleName() + ".res.withBinding.xml";
			inputOs = new FileOutputStream(resFile);
			SoapClientFactory.setInputStreamFilter(new InputStreamFilter() {
				@Override
				public InputStream filter(InputStream is) {
					return new DuplicatingInputStream(is, inputOs);
				}
			});
			reqFile = intf.getSimpleName() + ".req.withBinding.xml";
			outputOs = new FileOutputStream(reqFile);
			SoapClientFactory.setOutputStreamFilter(new OutputStreamFilter() {
				@Override
				public OutputStream filter(OutputStream os) {
					return new DuplicatingOutputStream(os, outputOs);
				}
			});
		}
		int i = intf.getSimpleName().indexOf("Service");
		return f.create(intf, new URL(base + intf.getSimpleName().substring(0, i)));
	}

	private void assertFieldsNotNull(Object object)
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Assert.assertNotNull(object);
		Class<?> clazz = object.getClass();
		while(clazz != null && !clazz.equals(Object.class)){
			for(Method m : clazz.getDeclaredMethods()){
				if(!MethodUtil.isGetter(m)) continue;
				Assert.assertNotNull(m.invoke(object));
			}
			clazz = clazz.getSuperclass();
		}
	}

	private ClientFactory f = new SoapClientFactory();
	private String base = "http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/services/";
}
