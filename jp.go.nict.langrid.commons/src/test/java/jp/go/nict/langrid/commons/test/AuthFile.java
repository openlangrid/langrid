package jp.go.nict.langrid.commons.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.Pair;

public class AuthFile{
	public static Pair<String, String> load(Class<?> clazz)
	throws IOException{
		InputStream is = clazz.getResourceAsStream("auth");
		if(is == null){
			throw new IOException("'auth' file beside class " + clazz.getName() + " was not found .");
		}
		try{
			String id = "", pass = "";
			Iterator<String> it = StreamUtil.readLines(is, "UTF-8").iterator();
			if(it.hasNext()){
				id = it.next();
				if(it.hasNext()){
					pass = it.next();
				}
			}
			return Pair.create(id, pass);
		} finally{
			is.close();
		}
	}
}
