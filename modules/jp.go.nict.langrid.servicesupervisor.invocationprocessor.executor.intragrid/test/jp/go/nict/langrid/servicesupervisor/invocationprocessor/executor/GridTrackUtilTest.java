package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import jp.go.nict.langrid.commons.test.CollectionFixture;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil.GridTrack;

public class GridTrackUtilTest {
	private static <T> CollectionFixture<T> fixture(Collection<T> c){
		return new CollectionFixture<T>(c);
	}
	private static void equals(long exp, long act){
		assertEquals(exp, act);
	}
	private static void equals(Object exp, Object act){
		assertEquals(exp, act);
	}
	private static <T> void empty(Collection<T> c){
		assertEquals(0, c.size());
	}
	@Test
	public void test() throws Throwable{
		List<GridTrack> actual = GridTrackUtil.decode("kyoto1.langrid:100 ->"
				+ " usa1.openlangrid:200(MorphPL:usa1.openlangrid:200 -> kyotooplg:128(MorphPL:kyotooplg:200),TransPL:kyotooplg:300) ->"
				+ " kyoto0.langrid:164");
		fixture(actual)
			.assertSize(3)
			.next(g -> {
				assertNull(g.getInvocationName());
				assertEquals("kyoto1.langrid", g.getGridId());
				equals(100, g.getProcessMillis());
				assertNull(g.getChildren());
			})
			.next(g -> {
				assertNull(g.getInvocationName());
				equals("usa1.openlangrid", g.getGridId());
				equals(200, g.getProcessMillis());
				fixture(g.getChildren())
					.assertSize(2)
					.next(g2 -> {
						assertEquals("MorphPL", g2.getInvocationName());
						assertEquals("usa1.openlangrid", g2.getGridId());
						assertEquals(200, g2.getProcessMillis());
						empty(g2.getChildren());
					})
					.next(g2 -> {
						assertNull(g2.getInvocationName());
						assertEquals("kyotooplg", g2.getGridId());
						fixture(g2.getChildren())
							.assertSize(1)
							.next(g3 -> {
								equals("MorphPL", g3.getInvocationName());
								equals("kyotooplg", g3.getGridId());
								equals(200, g3.getProcessMillis());
								empty(g3.getChildren());
							})
							.assertNotHasNext()
							;
					})
					.next(g2 -> {
						assertEquals("TransPL", g2.getInvocationName());
						assertEquals("kyotooplg", g2.getGridId());
						assertEquals(300, g2.getProcessMillis());
						empty(g2.getChildren());
					})
					;
			})
			.next(g -> {
				assertNull(g.getInvocationName());
				equals("kyoto0.langrid", g.getGridId());
				equals(164, g.getProcessMillis());
				empty(g.getChildren());
			})
			.assertNotHasNext()
			;
	}
}
