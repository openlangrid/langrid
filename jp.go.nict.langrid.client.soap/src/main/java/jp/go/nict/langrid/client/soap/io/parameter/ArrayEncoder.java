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

	public static String getTagAttributes(Class<?> type, Object value){
		StringBuilder brackets = new StringBuilder();
		do{
			brackets.append("[");
			Class<?> ct = type.getComponentType();
			if(!ct.isArray()){
				brackets.append(Array.getLength(value));
			}
			brackets.append("]");
			type = ct;
			if(Array.getLength(value) != 0){
				value = Array.get(value, 0);
			}
		} while(type.isArray());
		String xsiType = EncoderUtil.typeToXsdType(type);
		if(xsiType != null){
			return String.format(
					"soapenc:arrayType=\"xsd:%s%s\" xsi:type=\"soapenc:Array\"",
					xsiType, brackets
					);
		} else{
			return String.format(
					"soapenc:arrayType=\"ns:%s%s\" xsi:type=\"soapenc:Array\" xmlns:ns=\"%s\"",
					type.getSimpleName(), brackets, EncoderUtil.getNamespace(type)
					);
		}
	}

	public void write(PrintWriter writer) throws IOException{
		Class<?> ct = type.getComponentType();
		int n = Array.getLength(value);
		writeIndent(writer);
		String tagAttributes = getTagAttributes(type, value);
		writer.println(String.format(
				"<%s %s %s>", getName(), tagAttributes, Constants.soapenc));

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
