package jp.go.nict.langrid.client.soap.test;

import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class SoapClientFactoryAxis14EchoServiceTest{
	@Test
	public void test_all() throws Exception{
		for(Method m : Axis14EchoService.class.getDeclaredMethods()){
			if(m.getParameterTypes().length != 1) continue;
			Class<?> c = m.getReturnType();
			if(!m.getParameterTypes()[0].equals(c)) continue;
			Object v = null;
			if(c.isPrimitive()){
				v = ClassUtil.getRandomValueForPrimitive(c);
			} else if(c.equals(String.class)){
				v = StringUtil.randomString(20);
			} else if(c.equals(Calendar.class)){
				v = Calendar.getInstance();
				DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSZ");
				Assert.assertEquals(
						f.format(((Calendar)v).getTime())
						, f.format(((Calendar)m.invoke(service, v)).getTime())
						);
				continue;
			} else{
				System.out.println("skip method: " + m);
				continue;
			}

			Assert.assertEquals(
					"test for " + c
					, v
					, m.invoke(service, v)
					);
		}
	}

	@Before
	public void setUp() throws Exception{
		service = new SoapClientFactory().create(Axis14EchoService.class, new URL(
				"http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/services/Echo"));
	}

	private Axis14EchoService service;
}
