/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.translation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;

/**
 * @author Takao Nakaguchi
 */
public class StaticMultihopTranslation
extends AbstractCompositeService
implements TranslationService{
	@Override
	public Iterable<Pair<Invocation, Class<?>>> invocations() {
		List<Pair<Invocation, Class<?>>> ret = new ArrayList<Pair<Invocation,Class<?>>>();
		int n = intermediateLangs.length + 1;
		for(int i = 0; i < n; i++){
			final int index = i;
			ret.add(new Pair<Invocation, Class<?>>(new Invocation() {
				public Class<? extends Annotation> annotationType() {
					return Invocation.class;
				}
				public boolean required() {
					return false;
				}
				public String name() {
					return pls[index] + "TranslationPL";
				}
			}, TranslationService.class));
		}
		return ret;
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		ComponentServiceFactory factory = getComponentServiceFactory();
		List<String> intermediates = new ArrayList<String>();
		int i = 0;
		String prevLang = sourceLang;
		String prevResult = source;
		for(String il : intermediateLangs){
			prevResult = getService(pls[i++] + "TranslationPL", factory)
					.translate(prevLang, il, prevResult);
			prevLang = il;
			intermediates.add(prevResult);
		}
		return getService(pls[i++] + "TranslationPL", factory)
				.translate(prevLang, targetLang, prevResult);
	}

	public void setIntermediateLangs(String[] intermediateLangs) {
		this.intermediateLangs = intermediateLangs;
		if(this.intermediateLangs.length > 9){
			this.intermediateLangs = Arrays.copyOf(this.intermediateLangs, 9);
		}
	}

	private TranslationService getService(String name, ComponentServiceFactory factory)
	throws ProcessFailedException{
		TranslationService service = factory.getService(
				name, TranslationService.class);
		if(service == null){
			throw new ProcessFailedException(name + " is not binded.");
		}
		return service;
	}

	private String[] intermediateLangs = new String[]{};
	private static String[] pls = {"First", "Second", "Third", "Fourth"
		, "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth"};
}
