/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.handler;

import java.util.Timer;
import java.util.TimerTask;

import jp.go.nict.langrid.servicecontainer.executor.Poller;

/**
 * 
 * 
 */
public class PollingServiceFactory extends TargetServiceFactory{
	public void setIntervalMillis(long intervalMillis) {
		if(timer != null){
			timer.cancel();
		}
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Object s = getService();
				if(s == null) return;
				if(!(s instanceof Poller)) return;
				Poller p = (Poller)s;
				p.poll();
			}
		}, 2000, intervalMillis);
	}

	private Timer timer;
}
