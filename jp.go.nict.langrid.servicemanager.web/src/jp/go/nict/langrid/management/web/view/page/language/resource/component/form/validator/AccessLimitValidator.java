/*
 * $Id: AccessLimitValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.component.form.validator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.AccessLimitFieldPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AccessLimitValidator implements IFormValidator{
	/**
	 * 
	 * 
	 */
	public AccessLimitValidator(RepeatingView repeatingView){
		this.repeatingView = repeatingView;
	}

	public FormComponent<?>[] getDependentFormComponents(){
		return new FormComponent<?>[]{};
	}

	public void validate(Form<?> form){
		Set<Pair<Period, LimitType>> pls = new HashSet<Pair<Period, LimitType>>();
		Iterator it = repeatingView.iterator();
		while(it.hasNext()){
			AccessLimitFieldPanel c = (AccessLimitFieldPanel)it.next();
			if(c.getCount().getInput() == null){
				continue;
			}
			if(c.getCount().getInput().length() == 0){
			   continue;
			}
			LimitType lt = c.getType().getSelectedType();
			Period p = c.getPeriod().getSelectedPeriod();
			Pair<Period, LimitType> pair = Pair.create(p, lt);
			if(pls.contains(pair)){
				form.error(MessageManager.getMessage("AccessLimitValidator",form.getLocale()));
				return;
			}
			pls.add(pair);
		}
	}

	private RepeatingView repeatingView;
	private static final long serialVersionUID = 1L;
}
