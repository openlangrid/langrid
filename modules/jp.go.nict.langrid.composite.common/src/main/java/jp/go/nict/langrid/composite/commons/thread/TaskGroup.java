/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
import java.util.concurrent.CountDownLatch;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessorContext;

public class TaskGroup {
	public void add(Task<?> task){
		tasks.add(task);
	}

	public void runAndWait()
	throws InterruptedException{
		latch = new CountDownLatch(tasks.size());
		for(Task<?> t : tasks){
			t.setDoneCallback(new BlockP<RIProcessorContext>(){
				@Override
				public void execute(RIProcessorContext p1) {
					done(p1);
				}
			});
			TaskRunner.run(t);
		}
		try{
			latch.await();
		} finally{
			for(RIProcessorContext c : contexts){
				RIProcessor.mergeContext(c);
			}
			tasks.clear();
			contexts.clear();
		}
	}

	private synchronized void done(RIProcessorContext context){
		latch.countDown();
		contexts.add(context);
	}

	private List<Task<?>> tasks = new ArrayList<Task<?>>();
	private List<RIProcessorContext> contexts = new ArrayList<RIProcessorContext>();
	private CountDownLatch latch;
}
