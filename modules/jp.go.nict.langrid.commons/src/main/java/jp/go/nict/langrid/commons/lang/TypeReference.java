package jp.go.nict.langrid.commons.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements TypeHolder<T>{
	public Type get() {
		Type sc = getClass().getGenericSuperclass();
		return ((ParameterizedType)sc).getActualTypeArguments()[0];
	}
}
