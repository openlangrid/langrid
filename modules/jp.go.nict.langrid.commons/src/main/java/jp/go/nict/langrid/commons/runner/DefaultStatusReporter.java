/*
 * $Id: DefaultStatusReporter.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import jp.go.nict.langrid.commons.io.NullOutputStream;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class DefaultStatusReporter implements StatusReporter{
	/**
	 * 
	 * 
	 */
	public DefaultStatusReporter(PrintStream stream){
		this.stream = stream;
		this.exceptionStream = new PrintStream(new NullOutputStream());
	}

	/**
	 * 
	 * 
	 */
	public DefaultStatusReporter(
			PrintStream stream, PrintStream exceptionStream)
	{
		this.stream = stream;
		this.exceptionStream = exceptionStream;
	}

	public void start() {
		startMillis = System.currentTimeMillis();
		current = 0;
	}

	public synchronized void report(
			long dt, int doneCount, int faultCount, int total)
	{
		deltas.add(dt);
		dtSum += dt;
		int base = total / 100;
		current++;
		if((base != 0) && ((current % base) != 0)) return;

		long d = System.currentTimeMillis() - startMillis;
		Runtime rt = Runtime.getRuntime();
		if(total == 0){
			stream.println(String.format(
					"100% done (success:%d fault:%d) in %d msec. avg:%dmsec.  heap:%dKB/%dKB"
					, doneCount
					, faultCount
					, d
					, d
					, rt.totalMemory() / 1024
					, rt.maxMemory() / 1024
					));
		} else{
			stream.println(String.format(
					"%d%% done (success:%d fault:%d) in %d msec. avg:%dmsec.  heap:%dKB/%dKB"
					, (doneCount + faultCount) * 100 / total
					, doneCount
					, faultCount
					, d
					, d / (doneCount + faultCount)
					, rt.totalMemory() / 1024
					, rt.maxMemory() / 1024
					));
		}
	}

	public synchronized void reportException(Exception exception){
		exceptionStream.println("----- exception at " + current + "th call -----");
		exception.printStackTrace(exceptionStream);
		exceptionCount++;
	}

	public synchronized void end(int doneCount, int faultCount) {
		stream.println("delta times:");
		for(long dt : deltas){
			stream.println(dt);
		}

		int total = doneCount + faultCount;
		long d = System.currentTimeMillis() - startMillis;
		stream.println(d + "msec total. avg:" + (d / total) + "msec.");
		stream.println(faultCount + " times fault.");

		Collections.sort(deltas);
		stream.println("fastest millis:");
		ListIterator<Long> it = deltas.listIterator();
		for(int i = 0; (i < 10) && it.hasNext(); i++){
			stream.println(" " + it.next() + "msec");
		}
		stream.println("latest millis:");
		it = deltas.listIterator(deltas.size());
		for(int i = 0; (i < 10) && it.hasPrevious(); i++){
			stream.println(" " + it.previous() + "msec");
		}
		stream.println("average millis: " + dtSum / total + "msec");
		Runtime rt = Runtime.getRuntime();
		stream.printf("latest heap: %d/%d."
				, rt.totalMemory() / 1024
				, rt.maxMemory() / 1024
				);
		stream.println();
		System.gc();
		stream.printf("heap after gc: %d/%d."
				, rt.totalMemory() / 1024
				, rt.maxMemory() / 1024
				);
		stream.println();

		exceptionStream.println(
				"total " + exceptionCount + " exceptions in "
				+ current + " times call.");
	}

	private long startMillis;
	private int current;
	private int exceptionCount;
	private List<Long> deltas = new LinkedList<Long>();
	private long dtSum;
	private PrintStream stream;
	private PrintStream exceptionStream;
}
