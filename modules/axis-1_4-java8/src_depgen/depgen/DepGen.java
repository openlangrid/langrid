package depgen;

import java.io.File;
import java.io.FilenameFilter;

public class DepGen {
	public static void main(String[] args) throws Throwable{
		String[] names = new File("lib").list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		for(String name : names){
			System.out.println(String.format(
					"\t\t<dependency>%n" +
					"\t\t\t<groupId>%1$s</groupId>%n" +
					"\t\t\t<artifactId>%1$s</artifactId>%n" +
					"\t\t\t<version>1.0</version>%n" +
					"\t\t\t<scope>system</scope>%n" +
					"\t\t\t<systemPath>${basedir}/lib/%1$s</systemPath>%n" +
					"\t\t\t<optional>true</optional>%n" +
					"\t\t</dependency>",
					name
					));
		}
	}
}
