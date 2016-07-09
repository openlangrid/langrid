/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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
package jp.go.nict.langrid.commons.util.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Functions {
	public static <T> Consumer<T> nullComsumer(){
		return new Consumer<T>() {
			@Override
			public void accept(T value) {
			}
		};
	}

	public static <T> Predicate<T> truePredicate(){
		return new Predicate<T>() {
			@Override
			public boolean test(T value) {
				return true;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <E extends Throwable> void unsoft(SoftenedException e) throws E{
		Throwable t = e.getCause();
		if(t instanceof RuntimeException) throw (RuntimeException)t;
		if(t instanceof Error) throw (Error)t;
		throw (E)t;
	}

	public static interface RunnableWithException<E extends Throwable>{
		void run() throws E;
	}
	public static <E extends Throwable> Runnable soften(
			final RunnableWithException<E> r){
		return new Runnable(){
			@Override
			public void run() {
				try{
					r.run();
				} catch(Throwable e){
					throw new SoftenedException(e);
				}
			}
		};
	}

	public static interface ConsumerWithException<T, E extends Throwable>{
		void accept(T value) throws E;
	}
	public static <T, E extends Throwable> Consumer<T> soften(
			final ConsumerWithException<T, E> c){
		return new Consumer<T>(){
			@Override
			public void accept(T value) {
				try{
					c.accept(value);
				} catch(Throwable e){
					throw new SoftenedException(e);
				}
			}
		};
	}

	public static interface FunctionWithException<T, R, E extends Throwable>{
		R apply(T value) throws E;
	}
	public static <T, R, E extends Throwable> Function<T, R> soften(
			final FunctionWithException<T, R, E> f){
		return new Function<T, R>(){
			@Override
			public R apply(T value) {
				try{
					return f.apply(value);
				} catch(Throwable e){
					throw new SoftenedException(e);
				}
			}
		};
	}
}
