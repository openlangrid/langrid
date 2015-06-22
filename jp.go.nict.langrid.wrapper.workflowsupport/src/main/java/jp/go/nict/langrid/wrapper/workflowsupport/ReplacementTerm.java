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
package jp.go.nict.langrid.wrapper.workflowsupport;

import static java.lang.Character.UnicodeBlock.BASIC_LATIN;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.wrapper.workflowsupport.analysis.HangulAnalysis;
import jp.go.nict.langrid.wrapper.ws_1_2.workflowsupport.AbstractReplacementTermService;

/**
 * 
 * 
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class ReplacementTerm extends AbstractReplacementTermService {
	public ReplacementTerm(){
	}

	public ReplacementTerm(ServiceContext context){
		super(context);
	}

	public String doReplace(String sourceLang, String text,
			String[] searchWords, String[] replacementWords)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		if (text != null) {
			text = text.replaceAll("\\.\\.| \\.", ".").trim();
		}
		if (searchWords == null || replacementWords == null || searchWords.length == 0 || replacementWords.length == 0) {
			return text;
		}

		return replacementFromIntermediateCode(sourceLang, text, searchWords, replacementWords);
	}

	protected String replacementFromIntermediateCode(String sourceLang, String text, String[] searchWords, String[] replacementWords) {
		for (int i = 0; i < searchWords.length; i++) {
			String sw = searchWords[i];
			String rw = replacementWords[i];
			if(sw == null || sw.equals("")) continue;
			int index = text.indexOf(searchWords[i]);
			if(index == -1) continue;
			if(index != 0){
				char c = text.charAt(index - 1);
				if(c != ' ' && UnicodeBlock.of(c).equals(BASIC_LATIN)
						&& UnicodeBlock.of(rw.charAt(0)).equals(BASIC_LATIN)){
					rw = ' ' + rw;
					index++;
				}
			}
			if((index + sw.length()) < text.length()){
				char c = text.charAt(index + sw.length());
				if(c != ' ' && UnicodeBlock.of(c).equals(BASIC_LATIN)
						&& UnicodeBlock.of(rw.charAt(rw.length() - 1)).equals(BASIC_LATIN)){
					rw = rw + ' ';
				}
			}
			text = text.replace(sw, rw);
			if(sourceLang.toLowerCase().contains("ko")){
				text = HangulAnalysis.convertParticl(text, replacementWords[i], index);
			}
			if(index == 0 && !LANGUAGES.contains(sourceLang)){
				char[] ch = text.toCharArray();
				ch[0] = Character.toUpperCase(ch[0]);
				text = new String(ch);
			}
		}
		return text;
	}

	protected static final Set<String> LANGUAGES = new HashSet<String>();

	static {
		LANGUAGES.add("ja");
		LANGUAGES.add("zh");
	}
}
