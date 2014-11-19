/*
 * $Id: RepeatingMultithreadRunner.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class RepeatingMultithreadRunner extends MultithreadRunner{
	/**
	 * 
	 * 
	 */
	public RepeatingMultithreadRunner(
			int threadCount, int repeatCount, int retryCount
			, StatusReporter reporter)
	{
		super(threadCount, retryCount, reporter);
		this.repeatCount = repeatCount;
	}

	/**
	 * 
	 * 
	 */
	public int getRepeatCount(){
		return repeatCount;
	}

	protected int getTotalCount(){
		return repeatCount * getThreadCount();
	}

	protected Runnable createRunnable(
			String name
			, SinglethreadRunnable runnable
			, CountDownLatch startSignal
			, CountDownLatch endSignal
			, List<Exception> exceptions)
	{
		return new RunnableRunnable(
				name, repeatCount
				, runnable, startSignal, endSignal, exceptions
		);
	}

	private class RunnableRunnable implements Runnable{
		/**
		 * コンストラクタ。
		 * @param repeatCount
		 * @param name
		 * @param runnable
		 * @param startSignal
		 * @param endSignal
		 * @param exceptions
		 */
		public RunnableRunnable(String name
				, int repeatCount
				, SinglethreadRunnable runnable
				, CountDownLatch startSignal
				, CountDownLatch endSignal
				, List<Exception> exceptions) {
			this.name = name;
			this.repeatCount = repeatCount;
			this.runnable = runnable;
			this.startSignal = startSignal;
			this.endSignal = endSignal;
			this.exceptions = exceptions;
		}
		public void run() {
			int i = 0;
			try{
				startSignal.await();
				for(; i < repeatCount; i++){
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
							if(c < getRetryCount()){
								c++;
								Thread.sleep(Math.round(
										Math.random() * getRetryWaitRangeMillis()
										+ getRetryWaitMinMillis()
										));
							} else{
								fault(System.currentTimeMillis() - s, e);
								break;
							}
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
		private int repeatCount;
		private SinglethreadRunnable runnable;
		private CountDownLatch startSignal;
		private CountDownLatch endSignal;
		private List<Exception> exceptions;
	}

	private int repeatCount;
}
