/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Service Grid Project.
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

import java.util.NoSuchElementException;

public abstract class Optional<T> {
	public static class Present<U> extends Optional<U>{
		public Present(U value) {
			this.value = value;
		}
		public boolean isEmpty(){ return false;}
		public void ifPresent(Consumer<U> consumer){ consumer.accept(value);}
		public void ifEmpty(Runnable consumer){}
		public void presentOrEmpty(Consumer<U> present, Runnable empty){ present.accept(value);}
		public U get(){ return value;}
		public U orElse(U elseValue){ return value;}
		public <V> Optional<V> map(Function<U, V> f) {
			V v = f.apply(value);
			if(v != null) return new Present<V>(v);
			else return empty();
		}
		private U value;
	}

	public static class Empty<U> extends Optional<U>{
		public boolean isEmpty() { return true;}
		public void ifPresent(Consumer<U> consumer) {}
		public void ifEmpty(Runnable consumer){ consumer.run();}
		public void presentOrEmpty(Consumer<U> present, Runnable empty){ empty.run();}
		public U get(){ throw new NoSuchElementException();}
		public U orElse(U elseValue){ return elseValue;}
		public <V> Optional<V> map(Function<U, V> f){ return empty();}
	}

	public static <U> Optional<U> of(U value){
		if(value == null) throw new NullPointerException();
		return new Present<U>(value);
	}

	public static <U> Optional<U> ofNullable(U value){
		if(value != null){
			return new Present<U>(value);
		} else{
			return new Empty<U>();
		}
	}

	@SuppressWarnings("unchecked")
	public static final <U> Optional<U> empty(){
		return empty;
	}

	public abstract boolean isEmpty();
	public abstract void ifPresent(Consumer<T> consumer);
	public abstract void ifEmpty(Runnable consumer);
	public abstract void presentOrEmpty(Consumer<T> present, Runnable empty);
	public abstract T get() throws NoSuchElementException;
	public abstract T orElse(T elseValue);
	public abstract <U> Optional<U> map(Function<T, U> f);

	@SuppressWarnings("rawtypes")
	private static final Empty empty = new Empty();
}
