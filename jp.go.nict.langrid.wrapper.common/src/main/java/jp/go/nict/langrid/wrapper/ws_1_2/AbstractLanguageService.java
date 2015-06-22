/*
 * $Id: AbstractLanguageService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;

/** 
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractLanguageService
extends AbstractService
{
	/**
	 * 
	 * 
	 */
	public AbstractLanguageService() {
	}

	/**
	 * 
	 * 
	 */
	public AbstractLanguageService(ServiceContext context) {
		super(context);
	}

	/**
	 * 
	 * 
	 */
	public String[] getSupportedLanguages()
	throws NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException{
		return supportedLanguages;
	}

	/** 
	 * 
	 * 
	 */
	protected Collection<Language> getSupportedLanguageCollection(){
		return Collections.unmodifiableCollection(
				supportedLanguageCollection
				);
	}

	/**
	 * @deprecated #getSupportedLanguageCollection
	 */
	@Deprecated
	protected Collection<Language> getSupportedLanguagePairCollection(){
		return Collections.unmodifiableCollection(
				supportedLanguageCollection
				);
	}

	/**
	 * 
	 * 
	 */
	protected void setSupportedLanguageCollection(
			Collection<Language> supportedLanguageCollection){
		this.supportedLanguageCollection = supportedLanguageCollection;
		this.supportedLanguages = convertLanguages(supportedLanguageCollection);
	}

	/**
	 * @deprecated #setSupportedLanguageCollection
	 */
	@Deprecated
	protected void setSupportedLanguages(
			Collection<Language> supportedLanguageCollection){
		setSupportedLanguageCollection(supportedLanguageCollection);
	}

	@SuppressWarnings("serial")
	private Collection<Language> supportedLanguageCollection = new ArrayList<Language>(){{
		add(any);
	}};
	private String[] supportedLanguages = {"*"};
}
