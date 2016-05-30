/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at 
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
package jp.go.nict.langrid.webapps.mock;

import java.util.Date;

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
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.executor.StreamingReceiver;

/**
 * ストリーミング対応対訳辞書最長一致検索サービスのモック。
 * @author Takao Nakaguchi
 */
public class StreamingBilingualDictionaryWithLongestMatchSearchService
extends BilingualDictionaryService
implements jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService
	, StreamingNotifier<TranslationWithPosition>{
	@Override
	public TranslationWithPosition[] searchLongestMatchingTerms(
			String headLang, String targetLang, Morpheme[] morphemes)
	throws AccessLimitExceededException, InvalidParameterException,
		LanguagePairNotUniquelyDecidedException,
		NoAccessPermissionException, NoValidEndpointsException,
		ProcessFailedException, ServerBusyException,
		ServiceNotActiveException, ServiceNotFoundException,
		UnsupportedLanguagePairException,
		UnsupportedMatchingMethodException
	{
		System.out.println("start. tid: " + Thread.currentThread().getId() + ". d: " + new Date());
		try{
			if(receiver != null){
				for(TranslationWithPosition t : dummy){
					System.out.println("streamed response by " + receiver + ". tid: " + Thread.currentThread().getId());
					receiver.receive(t);
					Thread.sleep(1000);
				}
				return empty;
			}
			return dummy;
		} catch(InterruptedException e){
			e.printStackTrace();
			return null;
		} finally{
			System.out.println("end. tid: " + Thread.currentThread().getId() + ". d: " + new Date());
		}
	}

	public void setReceiver(StreamingReceiver<TranslationWithPosition> receiver) {
		if(this.receiver != null && receiver != null){
			throw new IllegalStateException("clear previous receiver before setting new receiver.");
		}
		this.receiver = receiver;
	}

	private StreamingReceiver<TranslationWithPosition> receiver;
	private static TranslationWithPosition[] empty = {};
	private static TranslationWithPosition[] dummy = {
		new TranslationWithPosition(new Translation("本", new String[]{"book"}), 1, 1)
		, new TranslationWithPosition(new Translation("日本海", new String[]{"Japan Sea"}), 3, 2)
	};
}
