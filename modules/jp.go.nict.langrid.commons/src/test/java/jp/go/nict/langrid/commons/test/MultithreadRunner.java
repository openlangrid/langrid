/*
 * $Id: MultithreadRunner.java 10341 2008-03-10 08:27:15Z nakaguchi $
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
package jp.go.nict.langrid.commons.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 複数スレッドから同時に特定の処理を呼び出す。
 * @author $Author: nakaguchi $
 * @version $Revision: 10341 $
 */
public class MultithreadRunner{
	/**
	 * コンストラクタ。
	 * @param count スレッド数
	 */
	public MultithreadRunner(int count){
		this.count = count;
	}

	/**
	 * スレッド数を取得する。
	 * @return スレッド数
	 */
	public int getCount(){
		return count;
	}

	/**
	 * runメソッドに要した時間を取得する。スレッド作成等の準備時間は含まれない。
	 * @return runメソッドに要した時間
	 */
	public long getDeltaTimeMillis(){
		return deltaTimeMillis;
	}

	/**
	 * runnableを指定された個数のスレッドで実行する。
	 * @param runnable 実行する処理
	 * @throws InterruptedException 同期処理が中断された
	 */
	public void run(final MultithreadRunnable runnable)
		throws InterruptedException
	{
		run(new SinglethreadRunnableFactory(){
			public SinglethreadRunnable create() throws Exception {
				return runnable;
			}
		});
	}

	/**
	 * factoryから指定された個数のrunnableとスレッドを作成し実行する。
	 * @param factory 実行する処理のファクトリ
	 * @throws InterruptedException 同期処理が中断された
	 */
	public void run(SinglethreadRunnableFactory factory)
		throws InterruptedException
	{
		ArrayList<Thread> threads = new ArrayList<Thread>();
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch endSignal = new CountDownLatch(count);
		for(int i = 0; i < count; i++){
			SinglethreadRunnable runnable = null;
			try{
				runnable = factory.create();
			} catch(Exception e){
				exceptions.add(e);
				continue;
			}
			Thread t = new Thread(new RunnableRunnable(
					runnable, startSignal, endSignal, exceptions
					));
			t.setName(runnable.getClass().getName() + "-" + i);
			threads.add(t);
			t.start();
		}
		long st = System.currentTimeMillis();
		startSignal.countDown();
		endSignal.await();
		for (Thread t : threads) {
			t.join();
		}
		deltaTimeMillis = System.currentTimeMillis() - st;
	}

	/**
	 * 処理の実行中に記録された例外を取得する。
	 * @return Collection 例外の集合
	 */
	public Collection<Exception> getExceptions(){
		return exceptions;
	}

	private static class RunnableRunnable implements Runnable{
		/**
		 * コンストラクタ。
		 * @param runnable
		 * @param startSignal
		 * @param endSignal
		 * @param exceptions
		 */
		public RunnableRunnable(SinglethreadRunnable runnable,
				CountDownLatch startSignal, CountDownLatch endSignal
				, List<Exception> exceptions) {
			this.runnable = runnable;
			this.startSignal = startSignal;
			this.endSignal = endSignal;
			this.exceptions = exceptions;
		}
		public void run() {
			try{
				startSignal.await();
				runnable.run();
			} catch(Exception e){
				exceptions.add(e);
			} finally{
				endSignal.countDown();
			}
		}
		private SinglethreadRunnable runnable;
		private CountDownLatch startSignal;
		private CountDownLatch endSignal;
		private List<Exception> exceptions;
	}

	private int count;
	private long deltaTimeMillis;
	private List<Exception> exceptions = Collections.synchronizedList(
			new ArrayList<Exception>());
}
