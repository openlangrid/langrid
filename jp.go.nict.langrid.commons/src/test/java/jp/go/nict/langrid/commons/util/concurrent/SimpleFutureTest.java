package jp.go.nict.langrid.commons.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class SimpleFutureTest {
	@Test
	public void test_initialState() throws Exception{
		SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		Assert.assertFalse(f.isCancelled());
		Assert.assertFalse(f.isDone());
		try{
			f.get(1, TimeUnit.MILLISECONDS);
			Assert.fail();
		} catch(TimeoutException e){
		}
	}

	@Test
	public void test_set_get() throws Exception{
		SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		f.set(100);
		Assert.assertFalse(f.isCancelled());
		Assert.assertTrue(f.isDone());
		Assert.assertEquals(100, f.get().intValue());
		Assert.assertEquals(100, f.get(1, TimeUnit.SECONDS).intValue());
	}

	@Test
	public void test_setFromAnotherThread() throws Exception{
		final SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				f.set(100);
			}
		});
		Assert.assertFalse(f.isCancelled());
		t.run();
		Assert.assertEquals(100, f.get().intValue());
		Assert.assertEquals(100, f.get().intValue());
		Assert.assertEquals(100, f.get().intValue());
	}

	@Test
	public void test_setTwice() throws Exception{
		final SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		f.set(100);
		try{
			f.set(100);
			Assert.fail();
		} catch(IllegalStateException e){
		}
	}

	@Test
	public void test_multithread() throws Exception{
		final SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		for(int i = 0; i < 100; i++){
			new Thread(new Runnable() {
				public void run() {
					try {
						Assert.assertEquals(100, f.get().intValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		f.set(100);
		new Thread(new Runnable() {
			public void run() {
				try {
					Assert.assertEquals(100, f.get().intValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Test
	public void test_setException() throws Exception{
		SimpleFuture<Integer> f = new SimpleFuture<Integer>();
		ExecutionException ex = new ExecutionException(new RuntimeException());
		f.setException(ex);
		try{
			f.get();
			Assert.fail();
		} catch(ExecutionException e){
			Assert.assertEquals(ex, e);
		}
	}
}
