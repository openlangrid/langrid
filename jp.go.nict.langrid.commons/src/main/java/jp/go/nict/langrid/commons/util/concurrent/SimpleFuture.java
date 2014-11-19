/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleFuture<V> implements Future<V>{
	@Override
	public synchronized boolean isCancelled() {
		return cancelled;
	}

	@Override
	public synchronized boolean isDone() {
		return done;
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		cancelled = true;
		return true;
	}

	@Override
	public synchronized V get() throws InterruptedException, ExecutionException {
		if(done){
			if(exception != null) throw exception;
			return value;
		}
		if(cancelled) throw new InterruptedException("operation cancelled.");
		wait();
		if(exception != null) throw exception;
		return value;
	}

	@Override
	public synchronized V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		if(done){
			if(exception != null) throw exception;
			return value;
		}
		if(cancelled) throw new InterruptedException("operation cancelled.");
		wait(unit.toMillis(timeout));
		if(exception != null) throw exception;
		if(value == null) throw new TimeoutException();
		return value;
	}

	public synchronized void set(V value) throws IllegalStateException{
		if(done) throw new IllegalStateException("The result or exception is already set.");
		this.value = value;
		done = true;
		notifyAll();
	}

	public synchronized void setException(ExecutionException exception) throws IllegalStateException{
		if(done) throw new IllegalStateException("The result or exception is already set.");
		this.exception = exception;
		done = true;
		notifyAll();
	}

	private V value;
	private ExecutionException exception;
	private boolean done;
	private boolean cancelled;
}
