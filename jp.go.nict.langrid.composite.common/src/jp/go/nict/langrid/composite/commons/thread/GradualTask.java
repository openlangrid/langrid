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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class GradualTask<T> extends Task<T> implements Runnable{
	/**
	 * Gets results produced by the task.
	 * @return
	 * @throws InvalidParameterException
	 * @throws ProcessFailedException
	 */
	public List<T> getCurrentResult(int queueWaitMillis)
	throws Exception{
		List<T> ret = new ArrayList<T>();
		if(results.drainTo(ret) == 0 && !isDone()){
			try{
				T r = results.poll(queueWaitMillis, TimeUnit.MILLISECONDS);
				if(r != null){
					ret.add(r);
					results.drainTo(ret);
				}
			} catch(InterruptedException e){
			}
		}
		if(ret.size() == 0 && isDone()){
			throwExceptionIfOccurred();
		}
		return ret;
	}

	protected void addResult(T result){
		results.add(result);
	}

	protected abstract void doWork() throws Exception;

	private LinkedBlockingQueue<T> results = new LinkedBlockingQueue<T>();
}
