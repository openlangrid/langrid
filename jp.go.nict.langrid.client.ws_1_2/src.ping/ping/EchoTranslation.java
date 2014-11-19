package ping;

import static jp.go.nict.langrid.language.ISO639_1LanguageTags.en;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ja;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import jp.go.nict.langrid.client.ws_1_2.ClientFactory;
import jp.go.nict.langrid.client.ws_1_2.TranslationClient;

public class EchoTranslation {
	public static void main(String[] args) throws Exception{
		Properties p = null;
		File f = new File("ping.properties");
		if(f.exists() && f.isFile()){
			p = new Properties();
			try{
				InputStream is = new FileInputStream(f);
				try{
					p.load(is);
				} finally{
					is.close();
				}
			} catch(IOException e){
				p = null;
			}
		}
		if(p == null){
			p = new Properties();
			InputStream is = EchoTranslation.class.getResourceAsStream("ping.properties");
			try{
				p.load(is);
			} finally{
				is.close();
			}
		}
		TranslationClient client = ClientFactory.createTranslationClient(new URL(
				p.getProperty("url")
				));
		client.setUserId(p.getProperty("username"));
		client.setPassword(p.getProperty("password"));
		client.translate(en, ja, "hello");
	}
}
