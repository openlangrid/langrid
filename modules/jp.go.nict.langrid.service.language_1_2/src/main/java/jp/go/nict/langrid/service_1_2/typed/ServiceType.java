/*
 * $Id: ServiceType.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.typed;

/**
 * 
 * Displays the service's type.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public enum ServiceType {
	/**
	 * 
	 * Constant indicating translation service.
	 * 
	 */
	TRANSLATION

	/**
	 * 
	 * Constant indicating a dictionary service.
	 * 
	 */
	, DICTIONARY

	/**
	 * 
	 * Constant indicating parallel text.
	 * 
	 */
	, PARALLELTEXT

	/**
	 * 
	 * Constant indicating the bilingual dictionary.
	 * 
	 */
	, BILINGUALDICTIONARY

	/**
	 * 
	 * Constant indicating a paraphrasing.
	 * 
	 */
	, PARAPHRASE

	/**
	 * 
	 * Constant indicating an adjacency pair.
	 * 
	 */
	, ADJACENCYPAIR

	/**
	 * 
	 * Constant indicating morphological analysis.
	 * 
	 */
	, MORPHOLOGICALANALYSIS

	/**
	 * 
	 * Constant indicating similarity calculation.
	 * 
	 */
	, SIMILARITYCALCULATION

	/**
	 * 
	 * Constant indicating multi-hop translation.
	 * 
	 */
	, MULTIHOPTRANSLATION

	/**
	 * 
	 * Constant indicating back translation.
	 * 
	 */
	, BACKTRANSLATION

	/**
	 * 
	 * Constant giving the other services.
	 * 
	 */
	, OTHER

	/**
	 * 
	 * Constant showing a bilingual dictionary supporting extract.
	 * 
	 */
	, BILINGUALDICTIONARYHEADWORDSEXTRACTION

	/**
	 * 
	 * Constant indicating editing the bilingual dictionary.
	 * 
	 */
	, EDITABLEBILINGUALDICTIONARY

	/**
	 * 
	 * Constant indicating a pictogram dictionary.
	 * 
	 */
	, PICTOGRAMDICTIONARY

	/**
	 * 
	 * Constant indicating a concept dictionary.
	 * 
	 */
	, CONCEPTDICTIONARY

	/**
	 * 
	 * Constant indicating dependency analysis.
	 * 
	 */
	, DEPENDENCYPARSER
	
	/**
	 * 
	 * Constant indicating a parallel text with internal metadata.
	 * 
	 */
	, PARALLELTEXTWITHEMBEDDEDMETADATA

	/**
	 * 
	 * Constant indicating a parallel text with external metadata.
	 * 
	 */
	, PARALLELTEXTWITHEXTERNALMETADATA

	/**
	 * 
	 * Constant indicating parallel text metadata.
	 * 
	 */
	, METADATAFORPARALLELTEXT

	/**
	 * 
	 * Constant indicating the bilingual dictionary longest match search.
	 * 
	 */
	,BILINGUALDICTIONARYWITHLONGESTMATCHSEARCH

	/**
	 * 
	 * Constant indicating a translation that used a temporary dictionary.
	 * 
	 */
	, TRANSLATIONWITHTEMPORALDICTIONARY

	/**
	 * 
	 * 
	 */
	, PARALLELTEXTWITHID

	/**
	 * 
	 * Constant indicating a back translation that used a temporary dictionary.
	 * 
	 */
	, BACKTRANSLATIONWITHTEMPORALDICTIONARY

	/**
	 * 
	 * Constant indicating a text to speech.
	 * 
	 */
	, TEXTTOSPEECH

	/**
	 * 
	 * Constant indicating a language identification.
	 * 
	 */
	, LANGUAGEIDENTIFICATION

	/**
	 * 
	 * Constant indicating a multihop translation that used a temporary dictionary.
	 * 
	 */
//	, MULTIHOPTRANSLATIONWITHTEMPORALDICTIONARY

	/**
	 * 
	 * Constant indicating a multilingual translation that used a temporary dictionary.
	 * 
	 */
//	, MULTILINGUALTRANSLATIONWITHTEMPORALDICTIONARY
	;
}
