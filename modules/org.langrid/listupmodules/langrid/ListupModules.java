package langrid;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ListupModules{
	public static void main(String[] args) throws Throwable{
		System.out.println("pom.xml");
		XPath p = XPathFactory.newInstance().newXPath();
		Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(
				"pom.xml"));
		NodeList nl = (NodeList)p.compile("//module/text()").evaluate(d, XPathConstants.NODESET);
		int n = nl.getLength();
		for(int i = 0; i < n; i++){
			Text t = (Text)nl.item(i);
			File orig = new File(t.getData());
			System.out.println(orig);
		}
	}
}
