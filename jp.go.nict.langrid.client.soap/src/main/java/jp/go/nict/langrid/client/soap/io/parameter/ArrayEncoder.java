package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import jp.go.nict.langrid.commons.io.CascadingIOException;

public class ArrayEncoder extends Encoder{
	public ArrayEncoder(int indent, String name, Class<?> type, Object value){
		super(indent, name);
		this.type = type;
		this.value = value;
	}

	public void write(PrintWriter writer) throws IOException{
		Class<?> ct = type.getComponentType();
		int n = Array.getLength(value);
		writeIndent(writer);
		String xsiType = EncoderUtil.typeToXsdType(ct);
		if(xsiType != null){
			writer.println(String.format(
					"<%s soapenc:arrayType=\"xsd:%s[%s]\" xsi:type=\"soapenc:Array\" %s>"
					, getName(), xsiType, (n == 0) ? "" : Integer.toString(n)
					, Constants.soapenc
					));
		} else{
			writer.println(String.format(
					"<%s soapenc:arrayType=\"ns:%s[%s]\" xsi:type=\"soapenc:Array\" xmlns:ns=\"%s\" %s>"
					, getName(), ct.getSimpleName(), (n == 0) ? "" : Integer.toString(n)
					, EncoderUtil.getNamespace(ct), Constants.soapenc
					));
		}
		try{
			for(int i = 0; i < n; i++){
				Object element = Array.get(value, i);
				Encoders.create(getIndent() + 1, getName(), ct, element).write(writer);
			}
		} catch(IllegalAccessException e){
			throw new CascadingIOException(e);
		} catch (IllegalArgumentException e) {
			throw new CascadingIOException(e);
		} catch (InvocationTargetException e) {
			throw new CascadingIOException(e);
		}
		writeClosingTag(writer);
	}
/*
      <temporalDict
       soapenc:arrayType="ns2:Translation[1]"
       xsi:type="soapenc:Array"
       xmlns:ns2="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/"
       xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">
        <temporalDict href="#id0"/>
      </temporalDict>
 */
	private Class<?> type;
	private Object value;
}
