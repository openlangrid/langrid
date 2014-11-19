package jp.go.nict.langrid.testresource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

public class LERedResourceLoader extends ResourceLoader{
	public LERedResourceLoader(String path){
		super(path);
	}
	@Override
	public InputStream load() throws IOException{
		// LER作る
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JarOutputStream jo = new JarOutputStream(bout);
		try{
			InputStream is = super.load();
			try{
				String name = new File(getPath()).getName();
				if(!name.endsWith(".wsdl")){
					name = name + ".wsdl";
				}
				jo.putNextEntry(new JarEntry(name));
				StreamUtil.transfer(is, jo);
			} finally{
				is.close();
			}
		} finally{
			jo.close();
		}
		return new ByteArrayInputStream(bout.toByteArray());
	}
}
