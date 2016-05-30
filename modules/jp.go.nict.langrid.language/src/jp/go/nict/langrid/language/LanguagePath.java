/*
 * $Id:LanguagePath.java 5274 2007-09-10 06:03:14Z nakaguchi $
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
package jp.go.nict.langrid.language;

import java.io.Serializable;
import java.util.Arrays;


/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:5274 $
 */
public class LanguagePath implements Serializable{
	/**
	 * 
	 * 
	 */
	public LanguagePath(Language... path){
		this.path = path;
	}

	/**
	 * 
	 * 
	 */
	public Language getSource(){
		return path[0];
	}

	/**
	 * 
	 * 
	 */
	public Language getTarget(){
		return path[path.length - 1];
	}

	/**
	 * 
	 * 
	 */
	public Language[] getPath(){
		return path;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePair createSourceTargetPair(){
		return new LanguagePair(getSource(), getTarget());
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath reverse(){
		Language[] newPath = new Language[path.length];
		for(int i = 0; i < path.length; i++){
			newPath[i] = path[path.length - i - 1];
		}
		return new LanguagePath(newPath);
	}
	
	@Override
	public int hashCode(){
		return Arrays.hashCode(path);
	}

	@Override
	public boolean equals(Object value){
		return equals((LanguagePath)value);
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(LanguagePath value){
		return Arrays.equals(path, value.path);
	}

	/**
	 * 
	 * 
	 */
	public String toCodeString(String separator){
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(Language l : path){
			if(!first){
				b.append(separator);
			} else{
				first = false;
			}
			b.append(l.getCode());
		}
		return b.toString();
	}

	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(Language l : path){
			if(!first){
				b.append(":");
			} else{
				first = false;
			}
			b.append(l);
		}
		return b.toString();
	}

	private Language[] path;

	private static final long serialVersionUID = -9193840592632032332L;
}
