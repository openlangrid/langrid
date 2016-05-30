package test.java.lang.util;

import java.util.EnumSet;

import org.junit.Test;

public class EnumSetTest {
	enum E{E1, E2};
	@Test
	public void test() throws Throwable{
		EnumSet<E> es = EnumSet.noneOf(E.class);
		es.add(E.E2);
		System.out.println(es);
	}
}
