package langrid;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ListupProjects{
	public static void main(String[] args) throws Throwable{
		System.out.println("net.servicegrid");
		XPath p = XPathFactory.newInstance().newXPath();
		Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(
				"pom.xml"));
		NodeList nl = (NodeList)p.compile("//module/text()").evaluate(d, XPathConstants.NODESET);
		int n = nl.getLength();
		Pattern pat = Pattern.compile("\\.\\.\\/([^\\/]+)\\/pom\\.xml");
		for(int i = 0; i < n; i++){
			Text t = (Text)nl.item(i);
			Matcher m = pat.matcher(t.getData());
			m.matches();
			System.out.println(m.group(1));
		}
	}
}
