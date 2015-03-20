/*
 * $Id: DataID.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class DataID {
	private String id;
	public DataID(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object target) {
	       if (this == target) {
	            return true;
	        }
	        
	        if (target instanceof DataID ) {
	            DataID peerTarget = (DataID)target;
	            
	            if( id == peerTarget.id ) {
	                return true;
	            }
	            
	            boolean result = id.equals( peerTarget.id );
	            
	            // if true then we can have the two ids share the id bytes
	            if( result ) {
	                peerTarget.id = this.id;
	            }
	            
	            return result;
	        } else {
	            return false;
	        }
	}
	
	public String toString() {
		return this.id;
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
