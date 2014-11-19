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
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;

/**
 * @author Takao Nakaguchi
 */
public class MultihopTranslation
extends AbstractCompositeService
implements MultihopTranslationService{
	public void setMaxHop(int maxHop) {
		this.maxHop = Math.min(10, maxHop);
	}

	@Override
	public Iterable<Pair<Invocation, Class<?>>> invocations() {
		List<Pair<Invocation, Class<?>>> ret = new ArrayList<Pair<Invocation,Class<?>>>();
		for(int i = 0; i < maxHop; i++){
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
	public MultihopTranslationResult multihopTranslate(String sourceLang,
			String[] intermediateLangs, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePathNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePathException {
		if(intermediateLangs.length > (maxHop - 1)){
			throw new InvalidParameterException("intermediateLangs",
					"too many intermediate languages(max " + (maxHop - 1) + " langs).");
		}
		ComponentServiceFactory factory = getComponentServiceFactory();
		List<String> intermediates = new ArrayList<String>();
		int i = 0;
		String prevLang = sourceLang;
		String prevResult = source;
		for(String il : intermediateLangs){
			prevResult = factory.getService(pls[i++] + "TranslationPL", TranslationService.class)
					.translate(prevLang, il, prevResult);
			prevLang = il;
			intermediates.add(prevResult);
		}
		String target = factory.getService(pls[i++] + "TranslationPL", TranslationService.class)
					.translate(prevLang, targetLang, prevResult);
		return new MultihopTranslationResult(
				intermediates.toArray(new String[]{}), target
				);
	}

	private int maxHop = 10;
	private static String[] pls = {"First", "Second", "Third", "Fourth"
			, "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth"};

}
