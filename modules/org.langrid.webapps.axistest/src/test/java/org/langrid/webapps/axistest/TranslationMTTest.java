package org.langrid.webapps.axistest;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.lang.function.Functions;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class TranslationMTTest {
	@Test
	public void t() throws Throwable{
		TranslationService s = new SoapClientFactory().create(
				TranslationService.class,
				new URL("http://localhost:8080/org.langrid.webapps.axistest/services/SampleTranslationService")
				);
		s.translate("en", "ja", "hello");
	}
	@Test
	public void test() throws Throwable{
		int n = 10, count = 10;
		CountDownLatch ready = new CountDownLatch(n);
		CountDownLatch done = new CountDownLatch(n);
		Object gate = new Object();
		AtomicInteger c = new AtomicInteger();
		AtomicInteger c2 = new AtomicInteger();
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i = 0; i < n; i++){
			int id = i;
			es.submit(Functions.soften(() -> {
				ThreadLocal<ByteArrayOutputStream> tl = new ThreadLocal<>();
				try{
					SoapClientFactory.setInputStreamFilter(is -> {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						tl.set(baos);
						return new DuplicatingInputStream(is, baos);
					});
					TranslationService s = new SoapClientFactory().create(
							TranslationService.class,
							new URL("http://localhost:8080/org.langrid.webapps.axistest/services/SampleTranslationService")
							);
					synchronized(gate){
						System.out.println("wait" + id);
						ready.countDown();
						gate.wait();
						System.out.println("start" + id);
					}
					for(int j = 0; j < count; j++){
						s.translate("en", "ja", "hello");
						System.out.println("invoked" + id + ":" + j);
						c.incrementAndGet();
					}
				} catch(Throwable e){
					System.out.println(e);
					e.printStackTrace();
					try{
						if(tl.get() != null){
							System.out.println(new String(tl.get().toByteArray(), "UTF-8"));
						} else{
							System.out.println("no response");
						}
					} catch(Exception e2){
						e2.printStackTrace();
					}
				} finally{
					c2.incrementAndGet();
					done.countDown();
				}
			}));
		}
		ready.await();
		synchronized(gate){
			gate.notifyAll();
		}
		done.await();
		es.shutdown();
		System.out.println(c.get() + " times invoked.");
		System.out.println(c2.get() + " times invoked.");
	}
}
