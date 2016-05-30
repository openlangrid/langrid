package jp.go.nict.langrid.client.soap.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.RpcRequestAttributes;
import jp.go.nict.langrid.client.soap.io.SoapRequestWriter;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.lang.ClassResource;
import jp.go.nict.langrid.commons.lang.ClassResourceLoader;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.Assert;
import org.junit.Test;

public class SoapRequestWriterTest{
	@Test
	public void test_translation() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getProxy(TranslationService.class, baos).translate("en", "ja", "hello");
		compareLines(translationService_req, baos.toByteArray());
	}

	@Test
	public void test_translation_withBinding() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TranslationService service = getProxy(TranslationService.class, baos);
		((RequestAttributes)service).getTreeBindings().add(new BindingNode("invocationname", "serviceId"));
		service.translate("en", "ja", "hello");
		System.out.println(new String(baos.toByteArray(), "UTF-8"));
	}

	@Test
	public void test_bilingualdictionary() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getProxy(BilingualDictionaryService.class, baos).search("en", "ja", "bank", "COMPLETE");
		System.out.println(new String(baos.toByteArray(), "UTF-8"));
		//compareLines(translationService_req, baos.toByteArray());
	}

	@Test
	public void test_backtranslationwithtemporaldictionary() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getProxy(BackTranslationWithTemporalDictionaryService.class, baos)
			.backTranslate("en", "ja", "bank", new Translation[]{new Translation("bank", new String[]{"銀行"})}, "ja");
		compareLines(backTranslationWithTemporalDictionaryService_req, baos.toByteArray());
	}

	private <T> T getProxy(Class<T> clazz, final OutputStream os){
		return clazz.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{clazz, RequestAttributes.class}
				, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if(method.getDeclaringClass().equals(RequestAttributes.class)){
					return method.invoke(reqAttrs, args);
				}
				SoapRequestWriter.writeSoapRequest(os, reqAttrs.getAllRpcHeaders(), method, args);
				Class<?> rt = method.getReturnType();
				if(rt.isPrimitive()){
					return ClassUtil.getDefaultValueForPrimitive(rt);
				}
				return null;
			}
			private RpcRequestAttributes reqAttrs = new RpcRequestAttributes();
		}));
	}

	private void compareLines(byte[] expected, byte[] actual) throws IOException{
		BufferedReader exp = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(expected), "UTF-8"));
		BufferedReader act = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(actual), "UTF-8"));
		while(true){
			String e = exp.readLine();
			String a = act.readLine();
			if(e == null && a == null) break;
			Assert.assertEquals(e, a);
		}
	}

	@ClassResource(path="TranslationService.req")
	private byte[] translationService_req;
	@ClassResource(path="BackTranslationWithTemporalDictionaryService.req")
	private byte[] backTranslationWithTemporalDictionaryService_req;
	{
		try {
			ClassResourceLoader.load(this);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
