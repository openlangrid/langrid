/*
 * $Id: MultithreadRunner.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.commons.runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class MultithreadRunner {
	/**
	 * 
	 * 
	 */
	public MultithreadRunner(int threadCount, int retryCount, StatusReporter reporter){
		this.threadCount = threadCount;
		this.retryCount = retryCount;
		this.reporter = reporter;
	}

	/**
	 * 
	 * 
	 */
	public int getThreadCount(){
		return threadCount;
	}

	/**
	 * 
	 * 
	 */
	public int getRetryCount(){
		return retryCount;
	}

	/**
	 * 
	 * 
	 */
	public long getDeltaTimeMillis(){
		return deltaTimeMillis;
	}

	/**
	 * 
	 * 
	 */
	public void runAndWait(final MultithreadRunnable runnable)
		throws InterruptedException
	{
		runAndWait(new SinglethreadRunnableFactory(){
			public SinglethreadRunnable create() throws Exception {
				return runnable;
			}
		});
	}

	/**
	 * 
	 * 
	 */
	public void runAndWait(SinglethreadRunnableFactory factory)
		throws InterruptedException
	{
		ArrayList<Thread> threads = new ArrayList<Thread>();
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch endSignal = new CountDownLatch(threadCount);
		for(int i = 0; i < threadCount; i++){
			SinglethreadRunnable runnable = null;
			try{
				runnable = factory.create();
			} catch(Exception e){
				e.printStackTrace();
				continue;
			}
			String name = runnable.getClass().getName() + "-t" + i;
			Thread t = new Thread(createRunnable(
					name, runnable, startSignal, endSignal, exceptions
					));
			t.setName(name);
			threads.add(t);
			t.start();
		}
		long st = System.currentTimeMillis();
		reporter.start();
		startSignal.countDown();
		endSignal.await();
		for (Thread t : threads) {
			t.join();
		}
		deltaTimeMillis = System.currentTimeMillis() - st;
		reporter.end(done.intValue(), fault.intValue());
	}

	/**
	 * 
	 * 
	 */
	public int getRetryWaitMinMillis() {
		return retryWaitMinMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setRetryWaitMinMillis(int retryWaitMinMillis) {
		this.retryWaitMinMillis = retryWaitMinMillis;
	}

	/**
	 * 
	 * 
	 */
	public int getRetryWaitRangeMillis() {
		return retryWaitRangeMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setRetryWaitRangeMillis(int retryWaitRangeMillis) {
		this.retryWaitRangeMillis = retryWaitRangeMillis;
	}

	/**
	 * 
	 * 
	 */
	public Collection<Exception> getExceptions(){
		return exceptions;
	}

	protected int getTotalCount(){
		return getThreadCount();
	}

	private class RunnableRunnable implements Runnable{
		public RunnableRunnable(String name
				, SinglethreadRunnable runnable
				, CountDownLatch startSignal
				, CountDownLatch endSignal
				, List<Exception> exceptions) {
			this.name = name;
			this.runnable = runnable;
			this.startSignal = startSignal;
			this.endSignal = endSignal;
			this.exceptions = exceptions;
		}
		public void run() {
			try{
				startSignal.await();
				int c = 0;
				long s = System.currentTimeMillis();
				while(true){
					try{
						runnable.run();
						done(System.currentTimeMillis() - s);
						break;
					} catch(Exception e){
						exceptions.add(new RunException(
								"exception in " + name + " at " + (c + 1) + " try", e
								));
						if(c == retryCount){
							fault(System.currentTimeMillis() - s, e);
							break;
						} else{
							c++;
							Thread.sleep(Math.round(
									Math.random() * retryWaitRangeMillis
									+ retryWaitMinMillis
									));
						}
					}
				}
			} catch(InterruptedException e){
				fault(0);
			} finally{
				endSignal.countDown();
			}
		}
		private String name;
		private SinglethreadRunnable runnable;
		private CountDownLatch startSignal;
		private CountDownLatch endSignal;
		private List<Exception> exceptions;
	}

	protected Runnable createRunnable(
			String name
			, SinglethreadRunnable runnable
			, CountDownLatch startSignal
			, CountDownLatch endSignal
			, List<Exception> exceptions)
	{
		return new RunnableRunnable(
				name, runnable, startSignal, endSignal, exceptions
		);
	}

	protected void done(long dt){
		done.incrementAndGet();
		report(dt);
	}

	protected void fault(long dt){
		fault.incrementAndGet();
		report(dt);
	}

	protected void fault(long dt, Exception e){
		reporter.reportException(e);
		fault.incrementAndGet();
		report(dt);
	}

	private void report(long dt){
		int current = count.incrementAndGet();
		if(current == 0) return;
		int total = getTotalCount();
		reporter.report(
				dt
				, done.intValue()
				, fault.intValue()
				, total
				);
	}

	private StatusReporter reporter;
	private AtomicInteger done = new AtomicInteger();
	private AtomicInteger fault = new AtomicInteger();
	private AtomicInteger count = new AtomicInteger();

	private int threadCount;
	private int retryCount;
	private int retryWaitMinMillis = 3000;
	private int retryWaitRangeMillis = 2000;
	private long deltaTimeMillis;
	private List<Exception> exceptions = Collections.synchronizedList(
			new ArrayList<Exception>());
}
