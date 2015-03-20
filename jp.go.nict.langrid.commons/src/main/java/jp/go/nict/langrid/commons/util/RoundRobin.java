/*
 * $Id: RoundRobin.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class RoundRobin<T> {
	/**
	 * 
	 * 
	 */
	public int size(){
		return elements.size();
	}

	/**
	 * 
	 * 
	 */
	public void add(T element){
		elements.add(element);
	}

	/**
	 * 
	 * 
	 */
	public void remove(T element){
		int i = elements.indexOf(element);
		if(i == -1) return;
		if(i < current){
			current--;
		}
	}

	/**
	 * 
	 * 
	 */
	public T next()
	throws NoSuchElementException
	{
		current %= elements.size();
		if(current >= elements.size()){
			throw new NoSuchElementException();
		}
		T ret = elements.get(current);
		current++;
		return ret;
	}

	private int current;
	private List<T> elements = new ArrayList<T>(); 
}
