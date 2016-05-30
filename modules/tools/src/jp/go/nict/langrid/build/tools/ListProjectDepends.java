package jp.go.nict.langrid.build.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import jp.go.nict.langrid.commons.lang.block.BlockP;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Eclipseプロジェクトの依存プロジェクトを列挙する。
 * 下記のファイルを調べて依存プロジェクトを列挙し、重複を省いてソートする。
 * <ul>
 * <li>.settings/org.clipse.wst.common.componentから参照しているプロジェクト</li>
 * <li>.projectから参照しているプロジェクト</li>
 * <li>.classpathから参照しているプロジェクト</li>
 * </ul>
 * @author Takao Nakaguchi
 */
public class ListProjectDepends {
	public static void go(File base, BlockP<String> block) throws Exception{
		Collection<String> list = new ArrayList<String>();
		listFromWstComponents(list, base);
		listFromProject(list, base);
		listFromClassPath(list, base);
		for(String v : new TreeSet<String>(list)){
			block.execute(v);
		}
	}

	private static void listFromWstComponents(Collection<String> ret, File base) throws Exception{
		Object o = XPathFactory.newInstance().newXPath().compile(
				"//wb-module/dependent-module/@handle"
				).evaluate(
						new InputSource(new FileInputStream(new File(base, ".settings/org.eclipse.wst.common.component")))
						, XPathConstants.NODESET
						);
		String projectPrefix = "module:/resource/";
		String libPrefix = "module:/classpath/";
		for(String v : toNodeValueList((NodeList)o)){
			if(v.startsWith(projectPrefix)){
				v = v.substring(projectPrefix.length());
				v = v.substring(0, v.indexOf('/'));
				ret.add(v);
			} else if(v.startsWith(libPrefix)){
				v = v.substring(libPrefix.length());
				v = v.substring(0, v.indexOf('/'));
				ret.add(v);
			}
		}
	}

	private static void listFromProject(Collection<String> ret, File base) throws Exception{
		Object o = XPathFactory.newInstance().newXPath().compile(
				"//projectDescription/projects/project"
				).evaluate(
						new InputSource(new FileInputStream(new File(base, ".project")))
						, XPathConstants.NODESET
						);
		for(String v : toTextContentList((NodeList)o)){
			if(v.startsWith("/")){
				ret.add(v.substring(1));
			}
		}
	}

	private static void listFromClassPath(Collection<String> ret, File base) throws Exception{
		Object o = XPathFactory.newInstance().newXPath().compile(
				"//classpath/classpathentry[@kind='src']"
				).evaluate(
						new InputSource(new FileInputStream(new File(base, ".classpath")))
						, XPathConstants.NODESET
						);
		for(String v : toAttrValueList((NodeList)o, "path")){
			if(v.startsWith("/")){
				ret.add(v.substring(1));
			}
		}
	}

	private static List<String> toNodeValueList(NodeList nl){
		int n = nl.getLength();
		List<String> ret = new ArrayList<String>();
		for(int i = 0; i < n; i++){
			String nv = nl.item(i).getNodeValue();
			if(nv != null) ret.add(nv);
		}
		return ret;
	}

	private static List<String> toTextContentList(NodeList nl){
		int n = nl.getLength();
		List<String> ret = new ArrayList<String>();
		for(int i = 0; i < n; i++){
			String nv = nl.item(i).getTextContent();
			if(nv != null) ret.add(nv);
		}
		return ret;
	}

	private static List<String> toAttrValueList(NodeList nl, String name){
		int n = nl.getLength();
		List<String> ret = new ArrayList<String>();
		for(int i = 0; i < n; i++){
			String nv = nl.item(i).getAttributes().getNamedItem(name).getNodeValue();
			if(nv != null) ret.add(nv);
		}
		return ret;
	}
}
