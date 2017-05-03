package jp.go.nict.langrid.commons.util;

import jp.go.nict.langrid.commons.util.function.Consumer;

/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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

public class LapTimer {
	public LapTimer(){
		lap = System.nanoTime();
	}

	public long lapNanos(){
		long d = System.nanoTime() - lap;
		lap = System.nanoTime();
		return d;
	}

	public void lapNanos(Consumer<Long> consumer){
		consumer.accept(lapNanos());
		lap = System.nanoTime();
	}

	public long lapMillis(){
		return lapNanos() / 1000000;
	}

	public void lapMillis(Consumer<Long> consumer){
		consumer.accept(lapMillis());
		lap = System.nanoTime();
	}

	private static long lap;
}
