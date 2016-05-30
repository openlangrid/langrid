/*
 * $Id:ServiceType.java 3343 2006-10-13 14:06:48 +0900 (金, 13 10 2006) nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:3343 $
 */
public enum OldServiceType {
	/**
	 * 
	 * 
	 */
	TRANSLATION

	/**
	 * 
	 * 
	 */
	, DICTIONARY

	/**
	 * 
	 * 
	 */
	, PARALLELTEXT

	/**
	 * 
	 * 
	 */
	, BILINGUALDICTIONARY

	/**
	 * 
	 * 
	 */
	, PARAPHRASE

	/**
	 * 
	 * 
	 */
	, ADJACENCYPAIR

	/**
	 * 
	 * 
	 */
	, MORPHOLOGICALANALYSIS

	/**
	 * 
	 * 
	 */
	, SIMILARITYCALCULATION

	/**
	 * 
	 * 
	 */
	, MULTIHOPTRANSLATION

	/**
	 * 
	 * 
	 */
	, BACKTRANSLATION

	/**
	 * 
	 * 
	 */
	, OTHER

	/**
	 * 
	 * 
	 */
	, BILINGUALDICTIONARYHEADWORDSEXTRACTION

	/**
	 * 
	 * 
	 */
	, EDITABLEBILINGUALDICTIONARY

	/**
	 * 
	 * 
	 */
	, PICTOGRAMDICTIONARY

	/**
	 * 
	 * 
	 */
	, CONCEPTDICTIONARY

	/**
	 * 
	 * 
	 */
	, DEPENDENCYPARSER
	
	/**
	 * 
	 * 
	 */
	, PARALLELTEXTWITHEMBEDDEDMETADATA
	
	/**
	 * 
	 * 
	 */
	, PARALLELTEXTWITHEXTERNALMETADATA

	/**
	 * 
	 * 
	 */
	, METADATAFORPARALLELTEXT

	/**
	 * 
	 * 
	 */
	,BILINGUALDICTIONARYWITHLONGESTMATCHSEARCH

	/**
	 * 
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
	 * 
	 */
	, TEXTTOSPEECH

	/**
	 * 
	 * 
	 */
	, LANGUAGEIDENTIFICATION
	;
}
