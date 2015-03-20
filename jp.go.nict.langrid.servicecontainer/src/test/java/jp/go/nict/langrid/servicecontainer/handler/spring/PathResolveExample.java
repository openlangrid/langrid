package jp.go.nict.langrid.servicecontainer.handler.spring;

import java.io.File;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class PathResolveExample {
	public static void main(String[] args) throws Exception{
		String path = new File("data/Bean.xml").getAbsoluteFile().toURI().toString();
		System.out.println("abs: " + path);
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(path);
		try{
			Bean b = ctx.getBean(Bean.class);
			System.out.println(b);
		} finally{
			ctx.close();
		}
	}
}
