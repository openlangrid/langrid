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
package jp.go.nict.langrid.commons.beanutils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.FilteredIterator;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.function.Supplier;

public class MapToBeanTransformer<U> extends ToBeanTransformer<Map<String, Object>, U> implements Transformer<Map<String, Object>, U>{
	@SuppressWarnings("unchecked")
	public MapToBeanTransformer(Converter converter, Class<U> targetClass){
		this(converter, targetClass, Collections.EMPTY_MAP);
	}

	protected MapToBeanTransformer(Converter converter, Map<String, String> propertyAliases){
		super(converter, propertyAliases);
	}

	public MapToBeanTransformer(Converter converter, Class<U> targetClass, Map<String, String> propertyAliases){
		super(converter, targetClass, propertyAliases);
	}

	public U transform(final Map<String, Object> value) throws TransformationException {
		if(value == null) return null;
		return doTransform(new Iterable<Pair<String, Supplier<Object>>>(){
			public Iterator<Pair<String, Supplier<Object>>> iterator(){
				return new FilteredIterator<Map.Entry<String, Object>, Pair<String, Supplier<Object>>>(
						value.entrySet().iterator()) {
					public Pair<String, Supplier<Object>> next() {
						final Map.Entry<String, Object> next = getOrig().next();
						return new Pair<String, Supplier<Object>>(next.getKey(), new Supplier<Object>() {
							@Override
							public Object get() {
								return next.getValue();
							}
						});
					}
				};
			}
		});
	}
}
