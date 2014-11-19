/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.commons.thread;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.ws.LocalServiceContext;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessorContext;

public abstract class Task<T> implements Runnable{
	public void run() {
		startTime = System.currentTimeMillis();
		workingThread = Thread.currentThread();
		if(processorContext != null) RIProcessor.fork(processorContext);
		try{
			doWork();
		} catch(Exception e){
			if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			}
			exception = e;
		} finally{
			done = true;
			if(callback != null){
				callback.execute(RIProcessor.join());
			} else{
				RIProcessor.join();
			}
			workingThread = null;
		}
	}

	public boolean isDone(){
		return done;
	}

	public boolean isExceptionOccurred(){
		return exception != null;
	}

	public void interrupt(){
		interrupted = true;
	}

	public boolean interrupted(){
		boolean b = this.interrupted;
		this.interrupted = false;
		return b;
	}

	protected long getStartTime(){
		return startTime;
	}

	public long getExpirationPeriod(){
		return expirationPeriod;
	}

	public void setExpirationPeriod(long expirationPeriod){
		this.expirationPeriod = expirationPeriod;
	}

	protected Thread getWorkingThread(){
		return workingThread;
	}

	protected abstract void doWork() throws Exception;

	protected void throwExceptionIfOccurred()
	throws Exception{
		if(exception != null){
			Exception e = exception;
			exception = null;
			throw e;
		}
	}

	void setUp(){
		RIProcessorContext pc = RIProcessor.getCurrentProcessorContext();
		if(pc == null) return;
		final ServiceContext sc = pc.getContext();
		LocalServiceContext lc = new LocalServiceContext(){
			public String getRealPath(String path) {
				return sc.getRealPath(path);
			}
		};
		lc.setRequestMimeHeaders(sc.getRequestMimeHeaders());
		lc.setRequestRpcHeaders(sc.getRequestRpcHeaders());
		this.processorContext = new RIProcessorContext(
				lc, pc.getProcessId(), pc.getHeaderMessageHandler());
	}

	void setDoneCallback(BlockP<RIProcessorContext> callback){
		this.callback = callback;
	}

	private volatile RIProcessorContext processorContext;
	private volatile BlockP<RIProcessorContext> callback;
	private volatile long startTime = - 1;
	private volatile long expirationPeriod = 60 * 10 * 1000;
	private volatile Thread workingThread;
	private volatile boolean done = false;
	private volatile boolean interrupted;
	private volatile Exception exception = null;
}
