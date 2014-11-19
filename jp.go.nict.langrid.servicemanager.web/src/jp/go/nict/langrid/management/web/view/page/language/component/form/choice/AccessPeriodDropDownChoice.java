/*
 * $Id: AccessPeriodDropDownChoice.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AccessPeriodDropDownChoice extends DropDownChoice<String>{
	/**
	 * 
	 * 
	 */
	public AccessPeriodDropDownChoice(String componentId){
		super(componentId, new Model<String>(), new ArrayList<String>(Arrays.asList(frequencyPeriods)));
	}

	@Override
	public CharSequence getDefaultChoice(Object selected){
		return "";
	}

	/**
	 * 
	 * 
	 */
	public Period getSelectedPeriod(){
		String value = getSelectedString();
		if(value != null){
			return periods.get(value);
		}else{
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public String getSelectedString(){
		String value = getInput();
		if(value == null){
			value = "0";
		}
		if(getChoices().size() == 3){
			return frequencyPeriods[Integer.parseInt(value)];
		}else if(getChoices().size() == 4){
			return capacityPeriods[Integer.parseInt(value)];
		}
		return null;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitType(LimitType type){
		switch(type){
		case FREQUENCY:
			setChoices(new ArrayList<String>(Arrays.asList(frequencyPeriods)));
			break;
		case CAPACITY:
		default:
			setChoices(new ArrayList<String>(Arrays.asList(capacityPeriods)));
			break;
		}
	}

	/**
	 * 
	 * 
	 */
	public void setSelectedPeriod(Period period){
		setModelObject(strings.get(period));
	}

	private static final String ACCESS = "Access";
	private static final String DAY = "Day";
	private static final String MONTH = "Month";
	private static final String YEAR = "Year";
	private static final String[] frequencyPeriods = {DAY, MONTH, YEAR};
	private static final String[] capacityPeriods = {ACCESS, DAY, MONTH, YEAR};
	private static Map<String, Period> periods = new HashMap<String, Period>();
	private static final long serialVersionUID = 1L;
	private static Map<Period, String> strings = new HashMap<Period, String>();
	static{
		periods.put(ACCESS, Period.EACHTIME);
		periods.put(DAY, Period.DAY);
		periods.put(MONTH, Period.MONTH);
		periods.put(YEAR, Period.YEAR);
		strings.put(Period.EACHTIME, ACCESS);
		strings.put(Period.DAY, DAY);
		strings.put(Period.MONTH, MONTH);
		strings.put(Period.YEAR, YEAR);
	}
}
