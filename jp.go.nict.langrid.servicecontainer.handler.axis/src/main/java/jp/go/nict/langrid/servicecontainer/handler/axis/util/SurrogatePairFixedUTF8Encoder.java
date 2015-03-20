package jp.go.nict.langrid.servicecontainer.handler.axis.util;

import java.io.IOException;
import java.io.Writer;

import org.apache.axis.components.encoding.AbstractXMLEncoder;
import org.apache.axis.components.encoding.XMLEncoderFactory;
import org.apache.axis.utils.Messages;

/**
 * サロゲートペアへの対応を行ったXMLのエンコーダ
 * 
 * @author Masaaki Kamiya
 * @author $Author$
 * @version $Revision$
 */
public class SurrogatePairFixedUTF8Encoder
extends AbstractXMLEncoder
{
	public String getEncoding() {
		return XMLEncoderFactory.ENCODING_UTF_8;
	}

	public void writeEncoded(Writer writer, String xmlString)
	throws IOException {
		if(xmlString == null) {
			return;
		}
		int length = xmlString.length();
		char character;
		for(int i = 0; i < length; i++) {
			character = xmlString.charAt(i);
			switch(character){
			// we don't care about single quotes since axis will
			// use double quotes anyway
			case '&':
				writer.write(AMP);
				break;
			case '"':
				writer.write(QUOTE);
				break;
			case '<':
				writer.write(LESS);
				break;
			case '>':
				writer.write(GREATER);
				break;
			case '\n':
				writer.write(LF);
				break;
			case '\r':
				writer.write(CR);
				break;
			case '\t':
				writer.write(TAB);
				break;
			default:
				if(character < 0x20) {
					throw new IllegalArgumentException(Messages.getMessage(
						"invalidXmlCharacter00",
						Integer.toHexString(character),
						xmlString.substring(0, i)));
				} else if(character > 0x7F) {
					if(Character.isHighSurrogate(character)) {
						i++;
						if(i < length) {
							char lowCharacter = xmlString.charAt(i);
							if(Character.isLowSurrogate(lowCharacter)) {
								int codePoint = Character.toCodePoint(character, lowCharacter);
								writer.write("&#x");
								writer.write(Integer.toHexString(codePoint).toUpperCase());
								writer.write(";");
								break;
							}
						}
						throw new IllegalArgumentException(Messages.getMessage(
							"invalidXmlCharacter00", Integer.toHexString(character),
							xmlString.substring(0, i)));
					} else {
						writer.write("&#x");
						writer.write(Integer.toHexString(character).toUpperCase());
						writer.write(";");
					}
				} else {
					writer.write(character);
				}
				break;
			}
		}
	}
}
