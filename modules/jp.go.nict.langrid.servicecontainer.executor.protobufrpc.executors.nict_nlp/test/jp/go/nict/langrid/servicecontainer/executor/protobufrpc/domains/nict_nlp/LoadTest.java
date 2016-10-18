package jp.go.nict.langrid.servicecontainer.executor.protobufrpc.domains.nict_nlp;

import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.executor.protobufrpc.PbComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

public class LoadTest {
	@Test
	public void test_classPathResource() throws Exception{
		ComponentServiceFactory exe = new XmlBeanFactory(
				new ClassPathResource("test.xml", LoadTest.class))
			.getBean("factory", ComponentServiceFactory.class);
		PbComponentServiceFactory pbe = (PbComponentServiceFactory)exe;
		Assert.assertNotNull(pbe.getExecutor(TranslationService.class));
	}

	@Test
	public void test_fileSystemResource() throws Exception{
		ComponentServiceFactory exe = new XmlBeanFactory(
				new FileSystemResource("test/test.xml"))
			.getBean("factory", ComponentServiceFactory.class);
		PbComponentServiceFactory pbe = (PbComponentServiceFactory)exe;
		Assert.assertNotNull(pbe.getExecutor(TranslationService.class));
	}
}
