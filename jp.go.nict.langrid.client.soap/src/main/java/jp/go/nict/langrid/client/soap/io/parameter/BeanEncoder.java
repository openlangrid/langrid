package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.util.Pair;

public class BeanEncoder extends Encoder{
	public BeanEncoder(int indent, String name, Class<?> type, Object value)
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		super(indent, name);
		this.ns = EncoderUtil.getNamespace(type);
		this.type = type.getSimpleName();
		List<Encoder> params = new ArrayList<Encoder>();
		for(Pair<String, Method> p : ClassUtil.getReadableProperties(type)){
			Method m = p.getSecond();
			params.add(Encoders.create(indent + 1, p.getFirst(), m.getReturnType(), m.invoke(value)));
		}
		values = params.toArray(new Encoder[]{});
	}
	public void write(PrintWriter writer) throws IOException{
		writeIndent(writer);
		writer.println(String.format("<%s %s xsi:type=\"ns:%s\" %s xmlns:ns=\"%s\">"
				, getName(), Constants.encodingStyle, type, Constants.soapenc, ns));
		for(Encoder p : values){
			p.write(writer);
		}
		writeClosingTag(writer);
	}
/*
    soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"
    xsi:type="ns3:Translation"
    xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
    xmlns:ns3="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/">
*/
	private String ns;
	private String type;
	private Encoder[] values;
}
