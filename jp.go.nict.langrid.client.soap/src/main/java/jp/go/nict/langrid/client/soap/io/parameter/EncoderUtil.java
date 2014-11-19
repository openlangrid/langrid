package jp.go.nict.langrid.client.soap.io.parameter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.rpc.intf.Schema;

public class EncoderUtil {
	public static String getParameterName(Annotation[] annots){
		for(Annotation a : annots){
			if(a instanceof jp.go.nict.langrid.commons.rpc.intf.Parameter){
				return ((jp.go.nict.langrid.commons.rpc.intf.Parameter)a).name();
			}
		}
		return null;
	}

	public static String typeToXsdType(Class<?> type){
		return xsdTypes.get(type);
	}

	public static String valueToString(Object value){
		return value.toString();
	}

	public static String getNamespace(Class<?> clazz){
		Schema s = clazz.getAnnotation(Schema.class);
		String ns = null;
		if(s != null){
			ns = s.namespace();
			if(ns.trim().length() == 0) ns = null;
		}
		if(ns == null){
			String cn = clazz.getName();
			if(cn.startsWith("jp.go.nict.langrid.service_1_2.")){
				cn = cn.substring("jp.go.nict.langrid.service_1_2.".length());
				int i = cn.lastIndexOf(".");
				if(i != -1){
					ns = "http://langrid.nict.go.jp/ws_1_2/" + cn.substring(0, i + 1).replace('.', '/');
				}
			}
		}
		if(ns == null){
			ns = "java:" + clazz.getPackage().getName();
		}
		return ns;
	}

	public static boolean isSimpleType(Class<?> type){
		return simples.contains(type);
	}

	private static Set<Class<?>> simples = new HashSet<Class<?>>();
	static{
		simples.add(String.class);
		simples.add(Boolean.class);
		simples.add(Character.class);
		simples.add(Short.class);
		simples.add(Integer.class);
		simples.add(Long.class);
		simples.add(Float.class);
		simples.add(Double.class);
		simples.add(boolean.class);
		simples.add(char.class);
		simples.add(short.class);
		simples.add(int.class);
		simples.add(long.class);
		simples.add(float.class);
		simples.add(double.class);
	}
	private static Map<Class<?>, String> xsdTypes = new HashMap<Class<?>, String>();
	static{
		xsdTypes.put(String.class, "string");
		xsdTypes.put(Boolean.class, "boolean");
		xsdTypes.put(Character.class, "unsignedShort");
		xsdTypes.put(Short.class, "short");
		xsdTypes.put(Integer.class, "int");
		xsdTypes.put(Long.class, "long");
		xsdTypes.put(Float.class, "float");
		xsdTypes.put(Double.class, "double");
		xsdTypes.put(boolean.class, "boolean");
		xsdTypes.put(char.class, "unsignedShort");
		xsdTypes.put(short.class, "short");
		xsdTypes.put(int.class, "int");
		xsdTypes.put(long.class, "long");
		xsdTypes.put(float.class, "float");
		xsdTypes.put(double.class, "double");
	}
}
