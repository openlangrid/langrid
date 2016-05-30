/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.util.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.function.BiFunction;
import jp.go.nict.langrid.commons.util.function.Consumer;
import jp.go.nict.langrid.commons.util.function.Function;
import jp.go.nict.langrid.commons.util.function.Predicate;

public class Stream<T> {
	public Stream(Provider<T> provider){
		this.provider = provider;
	}

	public <U> Stream<U> map(final Function<T, U> func){
		assertProviderValid();
		return new Stream<U>(new Provider<U>(){
			@Override
			public U next() {
				T v = p.next();
				if(v != null) return func.apply(v);
				return null;
			}
			Provider<T> p = provider;
			{ provider = null;}
		});
	}

	public <U> Stream<U> map(final Transformer<T, U> func){
		assertProviderValid();
		return new Stream<U>(new Provider<U>(){
			@Override
			public U next() {
				T v = p.next();
				if(v != null) return func.transform(v);
				return null;
			}
			Provider<T> p = provider;
			{ provider = null;}
		});
	}

	public T reduce(T identity, final BiFunction<T, T, T> func){
		assertProviderValid();
		T ret = identity;
		T r = null;
		while((r = provider.next()) != null){
			ret = func.apply(ret, r);
		}
		return ret;
	}

	public Stream<T> filter(final Predicate<T> pred){
		assertProviderValid();
		return new Stream<T>(new Provider<T>(){
			@Override
			public T next() {
				T v = null;
				while((v = p.next()) != null){
					if(pred.test(v)) return v;
				}
				return null;
			}
			private Provider<T> p = provider;
			{ provider = null;}
		});
	}

	public <U> Stream<U> expload(final Function<T, Provider<U>> func){
		assertProviderValid();
		return new Stream<U>(new Provider<U>(){
			@Override
			public U next() {
				if(p == null){
					T ov = org.next();
					if(ov == null) return null;
					p = func.apply(ov);
				}
				U v = p.next();
				if(v != null) return v;
				T ov = org.next();
				if(ov == null) return null;
				p = func.apply(ov);
				return p.next();
			}
			Provider<U> p;
			Provider<T> org = provider;
			{ provider = null;}
		});
	}

	public void forEach(Consumer<T> consumer){
		assertProviderValid();
		T v = null;
		while((v = provider.next()) != null){
			consumer.accept(v);
		}
	}

	public <C extends Collection<T>> C into(C collection){
		assertProviderValid();
		T v = null;
		while((v = provider.next()) != null){
			collection.add(v);
		}
		return collection;
	}

	public List<T> asList(){
		List<T> ret = new ArrayList<T>();
		into(ret);
		return ret;
	}

	private void assertProviderValid(){
		if(provider == null){
			throw new IllegalStateException("This stream has been already consumed.");
		}
	}

	private Provider<T> provider;
}
