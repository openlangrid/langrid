package jp.go.nict.langrid.servicecontainer.decorator;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jp.go.nict.langrid.commons.ws.LocalServiceContext;

import org.junit.Assert;
import org.junit.Test;

public class DecoratorTest {
	@Test
	public void test() throws Exception{
		final AtomicInteger first = new AtomicInteger();
		final AtomicInteger second = new AtomicInteger();
		final AtomicInteger third = new AtomicInteger();
		final DecoratorChain deco = new FirstDecoratorChain(
				Arrays.asList(new Decorator() {
					@Override
					public Object doDecorate(Request request, DecoratorChain chain)
					throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
						first.incrementAndGet();
						return chain.next(request);
					}
				}, new Decorator() {
					@Override
					public Object doDecorate(Request request, DecoratorChain chain)
					throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
						second.incrementAndGet();
						return chain.next(request);
					}
				}, new Decorator() {
					@Override
					public Object doDecorate(Request request, DecoratorChain chain)
					throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
						third.incrementAndGet();
						return chain.next(request);
					}
				}),
				new ServiceInvoker());
		final Request req = new Request(
				new LocalServiceContext(),
				"user",
				"service",
				new Runnable() {
					@Override
					public void run() {
					}
				},
				Runnable.class,
				Runnable.class.getMethod("run"),
				null);
		ExecutorService s = Executors.newFixedThreadPool(64);
		for(int i = 0; i < 10000; i++){
			s.submit(new Runnable(){
				@Override
				public void run() {
					try {
						deco.next(req);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		s.shutdown();
		s.awaitTermination(1000, TimeUnit.DAYS);
		Assert.assertEquals(10000, first.get());
		Assert.assertEquals(10000, second.get());
		Assert.assertEquals(10000, third.get());
	}
}
