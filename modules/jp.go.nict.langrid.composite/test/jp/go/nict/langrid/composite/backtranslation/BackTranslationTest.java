package jp.go.nict.langrid.composite.backtranslation;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

import org.junit.Test;

public class BackTranslationTest {
	@Test
	public void test() throws Exception{
		BackTranslationResult result = new BackTranslation(){
			@Override
			public ComponentServiceFactory getComponentServiceFactory() {
				return new ComponentServiceFactory() {
					@Override
					public <T> T getService(String invocationName, Class<T> interfaceClass) {
						try{
							String id = "langridId";
							String pass = "langridPass";
							ClientFactory f = new SoapClientFactory();
							if(invocationName.equals("ForwardTranslationPL")){
								return interfaceClass.cast(f.create(
										TranslationService.class,
										new URL("http://langrid.org/service_manager/invoker/KyotoUJServer"),
										id, pass));
							} else if(invocationName.equals("BackwardTranslationPL")){
								return interfaceClass.cast(f.create(
										TranslationService.class,
										new URL("http://langrid.org/service_manager/invoker/KyotoUJServer"),
										id, pass));
							}
						} catch(MalformedURLException e){
						}
						return null;
					}
				};
			}
		}.backTranslate("en", "ja", "hello");
		System.out.println(result);
	}
}
