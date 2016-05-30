/*
 * $Id: MultihopBackTranslationClientImpl.java 562 2012-08-06 10:56:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;
import java.util.Collection;

import jp.go.nict.langrid.client.ws_1_2.MultihopBackTranslationClient;
import jp.go.nict.langrid.client.ws_1_2.MultihopTranslationClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
public class MultihopBackTranslationClientImpl  
implements MultihopBackTranslationClient
{
	/**
	 * 
	 * 
	 */
	public MultihopBackTranslationClientImpl(URL serviceUrl){
		client = new MultihopTranslationClientImpl(serviceUrl);
	}

	public void setUserId(String userId) {
		client.setUserId(userId);
	}

	public void setPassword(String password) {
		client.setPassword(password);
	}

	public Collection<BindingNode> getTreeBindings() {
		return client.getTreeBindings();
	}

	@Override
	public void addRequestHeader(String name, String value) {
		client.addRequestHeader(name, value);
	}

	public String getLastName() {
		return client.getLastName();
	}

	public String getLastCopyrightInfo() {
		return client.getLastCopyrightInfo();
	}

	public String getLastLicenseInfo() {
		return client.getLastLicenseInfo();
	}

	public Collection<CallNode> getLastCallTree() {
		return client.getLastCallTree();
	}

	/**
	 * 
	 * 
	 */
	public MultihopTranslationResult multihopBackTranslate(
			Language sourceLang, Language[] intermediateLangs
			, Language targetLang, String source
			)
			throws LangridException
	{
		int n = intermediateLangs.length;
		Language[] langs = new Language[n * 2 + 1];
		for(int i = 0; i < n; i++){
			langs[i] = intermediateLangs[i];
		}
		langs[n] = targetLang;
		for(int i = 0; i < n; i++){
			langs[i + n + 1] = intermediateLangs[n - i - 1];
		}
		return client.multihopTranslate(
				sourceLang, langs, sourceLang, source);
	}

	private MultihopTranslationClient client;
}
