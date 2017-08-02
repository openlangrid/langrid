package jp.go.nict.langrid.language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class JaNameToCode {
	public static void main(String[] args) throws Throwable{
		Properties p = new Properties();
		try(InputStream lp = JaNameToCode.class.getResourceAsStream("Language.properties");
				InputStreamReader lpr = new InputStreamReader(lp, "UTF-8");
				InputStream jsl = JaNameToCode.class.getResourceAsStream("/JServerLangs.txt");
				Reader r = new InputStreamReader(jsl, "UTF-8");
				BufferedReader br = new BufferedReader(r)){
			p.load(lpr);
			Map<String, String> nameToLang = new HashMap<>();
			for(Entry<Object, Object> e : p.entrySet()){
				nameToLang.put(e.getValue().toString(), e.getKey().toString());
			}
			System.out.println();
			String line = null;
			while((line = br.readLine()) != null){
				if(line.trim().length() == 0) continue;
				line = Normalizer.normalize(
						Normalizer.normalize(line, Form.NFKD),
						Form.NFKC);
				String lang = nameToLang.get(line);
				if(lang != null){
					//System.out.printf("%s: %s%n", line, lang);
					System.out.printf("%s ", lang);
				} else{
					System.out.printf("%s: UNKNOWN%n", line);
				}
			}
		}
	}
}
