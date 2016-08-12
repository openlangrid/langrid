package jp.go.nict.langrid.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import jp.go.nict.langrid.commons.lang.ObjectUtil;

public class EntityUtil {
	public static String[] getIdFieldNames(Class<?> entityClass){
		List<String> ret = new ArrayList<>();
		IdClass idc = entityClass.getAnnotation(IdClass.class);
		if(idc != null){
			getFieldNames(idc.value(), ret);
		} else{
			ret.add(getIdFieldName(entityClass));
		}
		return ret.toArray(new String[]{});
	}
	
	public static Object getId(Class<?> entityClass, Object[] idValues)
	throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		IdClass idc = entityClass.getAnnotation(IdClass.class);
		if(idc != null){
			Class<?> c = idc.value();
			Object ret = c.newInstance();
			int i = 0;
			for(String name : getFieldNames(c)){
				ObjectUtil.setProperty(ret, name, idValues[i++]);
			}
			return ret;
		} else{
			return idValues[0];
		}
	}

	public static Class<?> getIdClass(Class<?> entityClass){
		IdClass idc = entityClass.getAnnotation(IdClass.class);
		if(idc != null) return idc.value();
		while(!entityClass.equals(Object.class) && entityClass.getAnnotation(Entity.class) != null){
			for(Field f : entityClass.getDeclaredFields()){
				if(!f.isAccessible()) f.setAccessible(true);
				Id id = f.getAnnotation(Id.class);
				if(id == null) continue;
				return f.getType();
			}
			entityClass = entityClass.getSuperclass();
		}
		return null;
		
	}

	static String getIdFieldName(Class<?> clazz){
		while(!clazz.equals(Object.class) && clazz.getAnnotation(Entity.class) != null){
			for(Field f : clazz.getDeclaredFields()){
				if(!f.isAccessible()) f.setAccessible(true);
				Id id = f.getAnnotation(Id.class);
				if(id == null) continue;
				return f.getName();
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	static String[] getFieldNames(Class<?> clazz){
		List<String> ret = new ArrayList<>();
		getFieldNames(clazz, ret);
		return ret.toArray(new String[]{});
	}

	static void getFieldNames(Class<?> clazz, Collection<String> ret){
		for(Field f : clazz.getDeclaredFields()){
			if((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;
			if((f.getModifiers() & Modifier.STATIC) != 0) continue;
			if(!f.isAccessible()) f.setAccessible(true);
			ret.add(f.getName());
		}
	}
}
