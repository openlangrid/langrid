/*
 * $Id: TranslationComparator.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary;

import java.util.Comparator;

import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;

/**
 * 
 * 
 * @author koyama
 *
 */
public class TranslationComparator implements Comparator<Translation> {

	public int compare(Translation o1, Translation o2) {
		// TODO Auto-generated method stub
		if (o1.getHeadWord().length() > o2.getHeadWord().length()) {
			return -1;
		} else if (o1.getHeadWord().length() < o2.getHeadWord().length()) {
			return 1;
		}
		return 0;
	}

}
