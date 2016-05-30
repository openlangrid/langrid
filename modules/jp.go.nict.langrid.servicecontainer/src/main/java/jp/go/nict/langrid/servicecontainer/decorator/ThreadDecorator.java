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
package jp.go.nict.langrid.servicecontainer.decorator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * 
 * @author Shingo Furukido
 * @author Takao Nakaguchi
 */
public class ThreadDecorator implements Decorator{
	/**
	 * 
	 * 
	 */
	public int getMaxThreads(){
		return maxThreads ;
	}

	/**
	 * 
	 * 
	 */
	public void setMaxThreads(int maxThreads){
		this.maxThreads = maxThreads ;
	}

	/**
	 * 
	 * 
	 */
	public int getMaxWaitMillisForThread(){
		return maxThreads ;
	}

	/**
	 * 
	 * 
	 */
	public void setMaxWaitMillisForThread(int maxWaitMillisForThread){
		this.maxWaitMillisForThread = maxWaitMillisForThread ;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public Object doDecorate(Request resuest, DecoratorChain chain)
	throws InvocationTargetException, IllegalArgumentException, IllegalAccessException{
		try{
			acquireSemaphore(resuest.getServiceId());
			try {
				return chain.next( resuest ) ;
			} finally {
				releaseSemaphore();
			}
		} catch(InterruptedException e){
			throw new InvocationTargetException(e);
		} catch(TimeoutException e){
			throw new InvocationTargetException(e);
		}
	}

	/**
	 * 
	 * 
	 * @throws ClassNotFoundException
	 * @throws ProcessFailedException
	 */
	public void acquireSemaphore(String serviceId)
	throws TimeoutException, InterruptedException{
	    if( maxThreads <= 0 ) return ;

	    Class< ? extends ThreadDecorator > clazz = this.getClass( ) ;
	    Semaphore s = semaphoresNew.get(serviceId);
	    if( s == null ) {
	        synchronized( clazz ) {
	            s = semaphoresNew.get(serviceId);
	            if(s == null){
	                s = new Semaphore(maxThreads, true);
	                semaphoresNew.put(serviceId, s);
	            }
	        }
	    }

		if( s != null ) {
			if(!s.tryAcquire(maxWaitMillisForThread, TimeUnit.MILLISECONDS)){
				throw new TimeoutException("timed out when acquiring semaphore.");
			}

			// スレッド内の初回実行時は Stack が生成されていないので、ここで生成
			if( currentSemaphoreStack.get( ) == null ) {
				currentSemaphoreStack.set( new Stack<Semaphore>( ) ) ;
			}

			// カレントのセマフォを退避
			currentSemaphoreStack.get( ).push( currentSemaphore.get( ) ) ;

			// 現在のセマフォをセット
			currentSemaphore.set( s ) ;
		}
	}

	/**
	 * 
	 * 
	 */
	public void releaseSemaphore(){
		if( maxThreads <= 0 ) return ;

		Semaphore s = currentSemaphore.get( ) ;
		if( s != null ) {
			s.release( ) ;
			currentSemaphore.set( currentSemaphoreStack.get( ).pop( ) ) ;
		}
	}

	private static ThreadLocal<Semaphore> currentSemaphore
		= new ThreadLocal<Semaphore>();

	private static Map<String, Semaphore> semaphoresNew
		= Collections.synchronizedMap( new HashMap<String, Semaphore>( ) ) ;

	private static ThreadLocal<Stack<Semaphore>> currentSemaphoreStack
		= new ThreadLocal<Stack<Semaphore>>( ) ;

	private int maxThreads = 10 ;
	private int maxWaitMillisForThread = 10000 ;
}
