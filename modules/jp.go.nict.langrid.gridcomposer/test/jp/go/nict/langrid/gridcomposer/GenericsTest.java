package jp.go.nict.langrid.gridcomposer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.catalina.tribes.util.Arrays;
import org.junit.Test;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;

public class GenericsTest {
	static class Abst<T> implements Callable<Double>{
		public Abst() {
			ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
			System.out.println(pt.getActualTypeArguments()[0]);
			Type t = GenericsUtil.getActualTypeArgumentTypes(
					this.getClass(), Abst.class)[0];
			System.out.println((Class<?>)t);
			System.out.println((ParameterizedType)t);
		}
		@Override
		public Double call() throws Exception {
			return null;
		}
	}
	static class Derived extends Abst<Collection<Integer>>{}
	@Test
	public void test() throws Throwable{
		System.out.println(Arrays.toString(Derived.class.getGenericInterfaces()));
		new Derived();
	}
}
