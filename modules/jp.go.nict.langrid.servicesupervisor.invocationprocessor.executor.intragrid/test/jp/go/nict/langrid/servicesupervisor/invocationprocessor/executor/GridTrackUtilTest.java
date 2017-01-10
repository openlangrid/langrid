package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import jp.go.nict.langrid.commons.test.CollectionFixture;
import jp.go.nict.langrid.dao.util.JSON;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil.GridTrack;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil.Scanner;

public class GridTrackUtilTest {
	private static <T> CollectionFixture<T> fixture(Collection<T> c){
		return new CollectionFixture<T>(c);
	}
	private static void isNull(Object act){
		assertNull(act);
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
		List<GridTrack> actual = GridTrackUtil.decode(
				"kyoto1.langrid:100 ->"
				+ " usa1.openlangrid:200("
					+ "MorphPL:usa1.openlangrid:200 ->"
					+ " kyotooplg:128(MorphPL2:kyotooplg:200),"
					+ "TransPL:kyotooplg:300"
					+ ") ->"
				+ " kyoto0.langrid:164");
		fixture(actual)
			.next(t -> {
				assertEquals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.next(t -> {
				equals("usa1.openlangrid", t.getGridId());
				equals(200, t.getProcessMillis());
				fixture(t.getInvocations())
					.next(i -> {
						assertEquals("MorphPL", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								assertEquals("usa1.openlangrid", t2.getGridId());
								assertEquals(200, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.next(t2 -> {
								assertEquals("kyotooplg", t2.getGridId());
								fixture(t2.getInvocations())
									.next(i2 -> {
										equals("MorphPL2", i2.getInvocationName());
										fixture(i2.getGridTrack())
											.next(t3 -> {
												equals("kyotooplg", t3.getGridId());
												equals(200, t3.getProcessMillis());
												empty(t3.getInvocations());
											})
											.assertNotHasNext();
									})
									.assertNotHasNext()
									;
							})
							.assertNotHasNext();
					})
					.next(i -> {
						equals("TransPL", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								assertEquals("kyotooplg", t2.getGridId());
								assertEquals(300, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.assertNotHasNext();
					})
					.assertNotHasNext();
			})
			.next(t -> {
				equals("kyoto0.langrid", t.getGridId());
				equals(164, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.assertNotHasNext()
			;
	}

	@Test
	public void test_scan1() throws Throwable{
		fixture(GridTrackUtil.decode("kyoto1.langrid:100"))
			.next(t -> {
				equals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.assertNotHasNext();
	}

	@Test
	public void test_scan2() throws Throwable{
		fixture(GridTrackUtil.decode("kyoto1.langrid:100 -> kyoto0.langrid:128"))
			.next(t -> {
				equals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.next(t -> {
				equals("kyoto0.langrid", t.getGridId());
				equals(128, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.assertNotHasNext();
	}

	@Test
	public void test_scan3() throws Throwable{
//		fixture(decode(newScanner("[{kyoto1.langrid,100,[{PL1, [{g1,128},{g2,256}]}]},{kyoto0.langrid,128}]")))
		List<GridTrack> gt = GridTrackUtil.decode("kyoto1.langrid:100(PL1:g1:128(PL2:g2:256))");
		fixture(gt)
			.next(t -> {
				equals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				fixture(t.getInvocations())
					.next(i -> {
						equals("PL1", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								equals("g1", t2.getGridId());
								equals(128, t2.getProcessMillis());
								fixture(t2.getInvocations())
								.next(i2 -> {
									equals("PL2", i2.getInvocationName());
									fixture(i2.getGridTrack())
										.next(t3 -> {
											equals("g2", t3.getGridId());
											equals(256, t3.getProcessMillis());
											empty(t3.getInvocations());
										})
										.assertNotHasNext();
								})
								.assertNotHasNext();
							})
							.assertNotHasNext();
					})
					.assertNotHasNext();
			})
			.assertNotHasNext();
	}

	@Test
	public void test_scan4() throws Throwable{
//		fixture(decode(newScanner("[{kyoto1.langrid,100,[{PL1, [{g1,128},{g2,256}]}]},{kyoto0.langrid,128}]")))
		fixture(GridTrackUtil.decode("kyoto1.langrid:100(PL1:g1:128 -> g2:256) -> kyoto0.langrid:128"))
			.next(t -> {
				equals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				fixture(t.getInvocations())
					.next(i -> {
						equals("PL1", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								equals("g1", t2.getGridId());
								equals(128, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.next(t2 -> {
								equals("g2", t2.getGridId());
								equals(256, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.assertNotHasNext();
					})
					.assertNotHasNext();
			})
			.next(t -> {
				equals("kyoto0.langrid", t.getGridId());
				equals(128, t.getProcessMillis());
				empty(t.getInvocations());
			})
			.assertNotHasNext();
	}

	@Test
	public void test_scan5() throws Throwable{
//		fixture(decode(newScanner("[{kyoto1.langrid,100,[{PL1, [{g1,128},{g2,256}]}]},{kyoto0.langrid,128}]")))
		fixture(GridTrackUtil.decode("kyoto1.langrid:100(PL1:g1:128 -> g2:256,PL2:g3:512)"))
			.next(t -> {
				equals("kyoto1.langrid", t.getGridId());
				equals(100, t.getProcessMillis());
				fixture(t.getInvocations())
					.next(i -> {
						equals("PL1", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								equals("g1", t2.getGridId());
								equals(128, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.next(t2 -> {
								equals("g2", t2.getGridId());
								equals(256, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.assertNotHasNext();
					})
					.next(i -> {
						equals("PL2", i.getInvocationName());
						fixture(i.getGridTrack())
							.next(t2 -> {
								equals("g3", t2.getGridId());
								equals(512, t2.getProcessMillis());
								empty(t2.getInvocations());
							})
							.assertNotHasNext();
					})
					.assertNotHasNext();
			})
			.assertNotHasNext();
	}

	@Test
	public void test_tokenize() throws Throwable{
/*
		String gt = 
			"kyoto1.langrid:100"
			+ " -> "
			+ "usa1.openlangrid:200("
				+ "MorphPL:usa1.openlangrid:200"
				+ " -> "
				+ "kyotooplg:128(MorphPL:kyotooplg:200)"
				+ " -> "
				+ "TransPL:kyotooplg:300"
				+ ")"
			+ " -> "
			+ "kyoto0.langrid:164";

		Scanner s = GridTrackUtil.newScanner(gt);
		while(s.next()){
			System.out.println("tok: " + s.getToken());
			System.out.println("del: " + s.getDelim());
		}
		System.out.println("tok: " + s.getToken());
//*/
	}
}
