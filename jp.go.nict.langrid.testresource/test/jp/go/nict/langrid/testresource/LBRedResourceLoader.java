package jp.go.nict.langrid.testresource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

public class LBRedResourceLoader extends ResourceLoader{
	public LBRedResourceLoader(String path){
		super(LBRedResourceLoader.class.getPackage().getName().replace('.', '/') + "/" + path);
	}

	public LBRedResourceLoader(String path, boolean b){
		super(path);
	}

	@Override
	public InputStream load() throws IOException{
		// LBR作る
		String name = getPath();
		URL path = getClass().getClassLoader().getResource(name);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JarOutputStream jo = new JarOutputStream(bout);
		try{
			if(!path.getProtocol().equals("file")){
				throw new IOException("can't load resources not accessible via file: protocol");
			}
			String fpath = URLDecoder.decode(path.getFile(), "UTF-8");
			File[] files = new File(fpath).listFiles();
			for(File f : files){
				jo.putNextEntry(new JarEntry(f.getName()));
				InputStream is = new FileInputStream(f);
				try{
					StreamUtil.transfer(is, jo);
				} finally{
					is.close();
				}
			}
		} finally{
			jo.close();
		}
		return new ByteArrayInputStream(bout.toByteArray());
	}
}
