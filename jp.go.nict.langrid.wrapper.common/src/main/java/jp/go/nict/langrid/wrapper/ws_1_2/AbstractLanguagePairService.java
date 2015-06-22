/*
 * $Id: AbstractLanguagePairService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.ws_1_2;

import static jp.go.nict.langrid.language.LangridLanguageTags.any;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.LanguagePair;

/** 
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractLanguagePairService
extends AbstractService
{
	/**
	 * 
	 * 
	 */
	public AbstractLanguagePairService() {
	}

	/**
	 * 
	 * 
	 */
	public AbstractLanguagePairService(ServiceContext context) {
		super(context);
	}

	/**
	 * 
	 * 
	 */
	public jp.go.nict.langrid.service_1_2.LanguagePair[] getSupportedLanguagePairs(){
		return supportedLanguagePairs;
	}

	/**
	 * 
	 * 
	 */
	protected Collection<LanguagePair> getSupportedLanguagePairCollection(){
		return Collections.unmodifiableCollection(
				supportedLanguagePairCollection
				);
	}

	/**
	 * 
	 * 
	 */
	protected void setSupportedLanguagePairs(
			Collection<LanguagePair> supportedLanguagePairCollection){
		this.supportedLanguagePairCollection = supportedLanguagePairCollection;
		this.supportedLanguagePairs = convertLanguagePairs(
				supportedLanguagePairCollection);
	}

	@SuppressWarnings("serial")
	private Collection<LanguagePair> supportedLanguagePairCollection = new ArrayList<LanguagePair>(){{
		add(new LanguagePair(any, any));
	}};
	private jp.go.nict.langrid.service_1_2.LanguagePair[] supportedLanguagePairs = {
			new jp.go.nict.langrid.service_1_2.LanguagePair("*", "*")
	};
}
