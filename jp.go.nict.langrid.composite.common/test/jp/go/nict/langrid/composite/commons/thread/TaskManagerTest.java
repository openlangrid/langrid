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

import jp.go.nict.langrid.service_1_2.LangridException;
import junit.framework.Assert;

import org.junit.Test;

public class TaskManagerTest {
	@Test
	public void test() throws Exception{
		TaskManager m = new TaskManager();
		for(int i = 0; i < 100; i++){
			Task<String> task = new Task<String>() {
				@Override
				protected void doWork() throws LangridException {
					try{
						Thread.sleep(1000000000);
					} catch(InterruptedException e){
					}
				}
			};
			task.setExpirationPeriod(0);
			m.addAndRunTask(task);
		}
		m.clean();
		Assert.assertEquals(0, m.currentTaskCount());
	}
}
