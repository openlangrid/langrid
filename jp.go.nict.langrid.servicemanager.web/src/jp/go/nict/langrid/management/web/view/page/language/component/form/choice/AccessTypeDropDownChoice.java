/*
 * $Id: AccessTypeDropDownChoice.java 303 2010-12-01 04:21:52Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.page.language.component.form.choice;

import jp.go.nict.langrid.dao.entity.LimitType;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AccessTypeDropDownChoice extends AccessLimitTypeDropDownChoice{
	/**
	 * 
	 * 
	 */
	public AccessTypeDropDownChoice(){
		super("accessType", frequency, capacity);
	}

	@Override
	public LimitType getSelectedType(){
		String value = getInput();
		if(value == null){
			value = "0";
		}
		if(value.equals("0")){
			return LimitType.FREQUENCY;
		}else if(value.equals("1")){
			return LimitType.CAPACITY;
		}else{
			return LimitType.FREQUENCY;
		}
	}

	@Override
	public void setSelectedType(LimitType type){
		if(type.equals(LimitType.FREQUENCY)){
			setModelObject(frequency);
		}else{
			setModelObject(capacity);
		}
	}

	private static final String capacity = "Data transfer size [KB]";
	private static final String frequency = "Access [hits]";
	private static final long serialVersionUID = 1L;
}
