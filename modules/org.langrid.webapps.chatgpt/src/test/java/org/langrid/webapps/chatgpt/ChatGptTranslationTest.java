package org.langrid.webapps.chatgpt;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.langrid.wrapper.chatgpt.ChatGPTTranslation;

public class ChatGptTranslationTest {
	@Before
	public void init() throws Throwable{
		var p = new Properties();
		try(var is = new FileInputStream("test.properties")){
			p.load(is);
			service.setApikey(p.getProperty("APIKEY"));
		}
	}

	@Test
	public void testTranslate() throws Throwable {
		var src = "The quick brown fox jumps over the lazy dog.";
		System.out.println(service.translate("en", "ja", src));
	}

	private ChatGPTTranslation service = new ChatGPTTranslation();
}
