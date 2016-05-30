package jp.go.nict.langrid.client.soap.test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class InterceptedXmlToFile implements Intercepted{
	public InterceptedXmlToFile(ByteArrayOutputStream baos, String fileName) {
		this.baos = baos;
		this.fileName = fileName;
	}

	public void finish() throws IOException{
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,"2");
			DOMSource source = new DOMSource(db.parse(new ByteArrayInputStream(baos.toByteArray())));
			OutputStream os = new FileOutputStream(fileName);
			try{
				t.transform(source, new StreamResult(os));
			} finally{
				os.close();
			}
		} catch(ParserConfigurationException e){
			throw new IOException(e);
		} catch (TransformerConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (TransformerException e) {
			throw new IOException(e);
		}
	}

	private ByteArrayOutputStream baos;
	private String fileName;
}
