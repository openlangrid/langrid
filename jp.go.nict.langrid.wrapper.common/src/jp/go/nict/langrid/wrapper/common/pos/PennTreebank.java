/*
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.common.pos;

import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.adjective;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.adverb;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.noun_common;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.noun_pronoun;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.noun_proper;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.other;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.unknown;
import static jp.go.nict.langrid.service_1_2.typed.PartOfSpeech.verb;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

/**
 * This class implements POS mapping between Penn Treebank POS and Language Grid POS.
 */
public class PennTreebank {
	@Deprecated
	public static PartOfSpeech get(String pennTreebankPos){
		return ptToLg(pennTreebankPos);
	}
	
	public static PartOfSpeech ptToLg(String pennTreebankPos){
		PartOfSpeech pos = ptToLg.get(pennTreebankPos);
		if(pos == null){
			return unknown;
		}
		return pos;
	}

	/**
	 * returns PennTreebank-style pos.
	 * @param lgPos the POS language grid project defined.
	 * @param word word
	 * @return POS
	 */
	public static String lgToPt(PartOfSpeech lgPos, String word){
		if(lgPos.equals(other)){
			if(word.equals(",")) return ",";
			else if(word.equals(".")) return ".";
			else if(word.equals("to")) return "TO";
			else return "FW";
		}
		String pos = lgToPt.get(lgPos);
		if(pos == null){
			return "FW";
		}
		return pos;
	}

	static final Map<String, PartOfSpeech> ptToLg;
	static final Map<PartOfSpeech, String> lgToPt;
	private static void put(String ptPos, PartOfSpeech lgPos){
		ptToLg.put(ptPos, lgPos);
		lgToPt.put(lgPos, ptPos);
	}
	static {
		ptToLg = new HashMap<String, PartOfSpeech>();
		lgToPt = new HashMap<PartOfSpeech, String>();
		put("CC", other); 	// Coordinating conjunction
		put("CD", other); 	// Cardinal number
		put("DT", other); 	// Determiner
		put("EX", other); 	// Existential there
		put("FW", other); 	// Foreign word
		put("IN", other); 	// Preposition or subordinating conjunction
		put("JJ", adjective);	// Adjective
		put("JJR", adjective);	// Adjective, comparative
		put("JJS", adjective);	// Adjective, superlative
		put("LS", other); 	// List item marker
		put("MD", other); 	// Modal
		put("NN", noun_common);	// Noun, singular or mass
		put("NNS", noun_common);	// Noun, plural
		put("NNP", noun_proper);	// Proper noun, singular
		put("NNPS", noun_proper);	// Proper noun, plural
		put("PDT", other); 	// Predeterminer
		put("POS", other); 	// Possessive ending
		put("PRP", noun_pronoun);	// Personal pronoun
		put("PRP$", noun_pronoun);	// Possessive pronoun
		put("RB", adverb);	// Adverb
		put("RBR", adverb);	// Adverb, comparative
		put("RBS", adverb);	// Adverb, superlative
		put("RP", other); 	// Particle
		put("SYM", other); 	// Symbol
		put("TO", other); 	// to
		put("UH", other); 	// Interjection
		put("VB", verb);	// Verb, base form
		put("VBD", verb);	// Verb, past tense
		put("VBG", verb);	// Verb, gerund or present participle
		put("VBN", verb);	// Verb, past participle
		put("VBP", verb);	// Verb, non­3rd person singular present
		put("VBZ", verb);	// Verb, 3rd person singular present
		put("WDT", other); 	// Wh­determiner
		put("WP", other); 	// Wh­pronoun
		put("WP$", other); 	// Possessive wh­pronoun
		put("WRB", other); 	// Wh­adverb 
		put(".", other);
		put(",", other);
	}
}
