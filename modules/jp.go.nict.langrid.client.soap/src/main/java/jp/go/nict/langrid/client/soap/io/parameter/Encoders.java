package jp.go.nict.langrid.client.soap.io.parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Encoders {
	public static Encoder create(int indent, String name, Class<?> type, Object value)
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Constructor<? extends Encoder> ctor = ctors.get(type);
		if(ctor != null){
			try{
				return ctor.newInstance(indent, name, value);
			} catch(InstantiationException e){
				throw new RuntimeException(e);
			}
		}
		if(EncoderUtil.isSimpleType(type)) return new ToStringEncoder(indent, name, type, value);
		if(type.isArray()) return new ArrayEncoder(indent, name, type, value);
		return new BeanEncoder(indent, name, type, value);
	}

	private static Map<Class<?>, Constructor<? extends Encoder>> ctors = new HashMap<Class<?>, Constructor<? extends Encoder>>();
	private static Constructor<? extends Encoder> getCtor(Class<? extends Encoder> clazz)
	throws SecurityException, NoSuchMethodException{
		return clazz.getConstructor(int.class, String.class, Object.class);
	}
	static{
		try {
			ctors.put(byte[].class, getCtor(BytesEncoder.class));
			ctors.put(Calendar.class, getCtor(CalendarEncoder.class));
			ctors.put(Date.class, getCtor(DateEncoder.class));
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
