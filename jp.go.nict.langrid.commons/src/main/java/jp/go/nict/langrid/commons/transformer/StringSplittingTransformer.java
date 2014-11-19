/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009-2010 NICT Language Grid Project.
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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class StringSplittingTransformer
implements Transformer<String, String[]>{
	/**
	 * 
	 * 
	 */
	public StringSplittingTransformer(String regex){
		this.regex = regex;
	}

	/**
	 * 
	 * 
	 */
	public StringSplittingTransformer(String regex, boolean trimElements){
		this.regex = regex;
		this.trimElements = trimElements;
	}

	public String[] transform(String value){
		String[] values = value.split(regex);
		if(trimElements){
			List<String> ret = new ArrayList<String>();
			for(String s : values){
				String v = s.trim();
				if(v.length() > 0){
					ret.add(v);
				}
			}
			return ret.toArray(new String[]{});
		} else{
			return values;
		}
	}

	private String regex;
	private boolean trimElements = true;
}
