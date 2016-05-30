/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.hibernate.searchsupport;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.entity.Attribute;
import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 */
public class LanguagePathArrayAllSearchSupport<T extends AttributedElement<U>, U extends Attribute>
implements SearchSupport<T>
{
	@Override
	public void appendWhereClouse(MatchingCondition condition,
			String elementAlias, String parameterName, StringBuilder query,
			Map<String, Object> parameters)
	{
		if(!condition.getMatchingMethod().equals(MatchingMethod.LANGUAGEPATH)){
			return;
		}
		LanguagePath[] paths = null;
		try{
			paths = LanguagePathUtil.decodeLanguagePathArray(
					condition.getMatchingValue().toString()
					);
		} catch(InvalidLanguagePathException e){
			logger.log(
					Level.WARNING
					, "invalid matchingValue for LanguagePathArray: "
					+ condition.getMatchingValue().toString()
					, e);
			return;
		} catch(InvalidLanguageTagException e){
			logger.log(
					Level.WARNING
					, "invalid matchingValue for LanguagePathArray: "
					+ condition.getMatchingValue().toString()
					, e);
			return;
		}
		boolean first = true;
		StringBuilder q = new StringBuilder(); 
		for(LanguagePath p : paths){
			for(Language l : p.getPath()){
				String c = l.getCode();
				if(c.equals("*")) continue;
				if(c.endsWith("-*")){
					c = c.substring(0, c.length() - 2);
				}
				if(first){
					q.append(" and (select count(*) from ")
						.append(elementAlias).append(".attributes attr%1$s")
						.append(" where attr%1$s.value like '%%\\*%%' or (");
					first = false;
				} else{
					q.append(" and ");
				}
				q.append("attr%1$s.value like '%%").append(c).append("%%'");
			}
		}
		if(!first){
			q.append(")) = 1");
		}
		String fq = String.format(q.toString(), "A");
		query.append(fq);
	}
	
	@Override
	public boolean isFileteringNeeded() {
		return false;
	}

	@Override
	public void filterResults(MatchingCondition condition, List<T> results) { /* noop */ }

	private static Logger logger = Logger.getLogger(
			LanguagePathArrayAllSearchSupport.class.getName());
}
