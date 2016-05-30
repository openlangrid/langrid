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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * Task manager.
 * @author Takao Nakaguchi
 */
public class TaskManager {
	public TaskManager(){
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				clean();
			}
		}, 1000 * 60 * 10, 1000 * 60 * 10);
	}

	public <T> String addAndRunTask(Task<T> task){
		while(true){
			String token = StringUtil.randomString(40);
			if(tokenToTask.containsKey(token)) continue;
			tokenToTask.put(token, task);
			TaskRunner.run(task);
			return token;
		}
	}

	public void removeTask(String token){
		Task<?> task = getTask(token);
		if(task == null) {
			return;
		}

		task.interrupt();
		tokenToTask.remove(token);
	}

	@SuppressWarnings("unchecked")
	public <T extends Task<?>> T getTask(String token){
		return (T)tokenToTask.get(token);
	}

	public int currentTaskCount(){
		return tokenToTask.size();
	}

	protected void clean(){
		long current = System.currentTimeMillis();
		List<String> garbages = new ArrayList<String>();
		for(Map.Entry<String, Task<?>> entry : tokenToTask.entrySet()){
			Task<?> task = entry.getValue();
			if(current - task.getStartTime() > task.getExpirationPeriod()){
				task.interrupt();
				garbages.add(entry.getKey());
			}
		}
		for(String g : garbages){
			tokenToTask.remove(g);
		}
	}

	private Map<String, Task<?>> tokenToTask = new ConcurrentHashMap<String, Task<?>>();
}
