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
package jp.go.nict.langrid.commons.transformer;

/**
 * 
 */
public class StringToClassTransformer<T>
implements Transformer<String, Class<T>>{
	public StringToClassTransformer() {
	}
	public StringToClassTransformer(boolean ignoreException){
		this.ignoreException = ignoreException;
	}
	@SuppressWarnings("unchecked")
	public Class<T> transform(String value) throws TransformationException {
		try{
			return (Class<T>)Class.forName(value);
		} catch (ClassNotFoundException e) {
			if(ignoreException) return null;
			throw new TransformationException(e);
		}
	}
	private boolean ignoreException;
}