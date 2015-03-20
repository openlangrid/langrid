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

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.executor.StreamingReceiver;

/**
 * ストリーミング対応形態素解析サービスのモック。
 * @author Takao Nakaguchi
 */
public class StreamingMorphologicalAnalysisService
implements MorphologicalAnalysisService, StreamingNotifier<Morpheme>{
	@Override
	public Morpheme[] analyze(String language, String text)	{
		System.out.println("start. tid: " + Thread.currentThread().getId() + ". d: " + new Date());
		try{
			if(receiver != null){
				for(Morpheme t : dummy){
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

	public void setReceiver(StreamingReceiver<Morpheme> receiver) {
		if(this.receiver != null && receiver != null){
			throw new IllegalStateException("clear previous receiver before setting new receiver.");
		}
		this.receiver = receiver;
	}

	private StreamingReceiver<Morpheme> receiver;
	private static Morpheme[] empty = {};
	private static Morpheme[] dummy = {
		new Morpheme("hello", "hello", "other")
		, new Morpheme("world", "world", "noun.common")
	};
}
