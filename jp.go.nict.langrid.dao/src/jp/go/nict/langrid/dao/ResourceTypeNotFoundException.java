/*
 * $Id: ResourceTypeNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class ResourceTypeNotFoundException extends DaoException{
	/**
	 * 
	 * 
	 */
	public ResourceTypeNotFoundException(String resourceTypeId) {
		super(toMessage(resourceTypeId));
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public ResourceTypeNotFoundException(String resourceTypeId, Throwable cause){
		super(toMessage(resourceTypeId), cause);
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceTypeId(){
		return resourceTypeId;
	}

	private String resourceTypeId;

	private static String toMessage(String resourceTypeId){
		return "resourceType \"" + resourceTypeId + "\" is not found.";
	}

	private static final long serialVersionUID = -377717318711913215L;
}
