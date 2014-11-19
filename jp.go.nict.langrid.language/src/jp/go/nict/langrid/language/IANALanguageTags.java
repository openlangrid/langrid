/*
 * $Id: IANALanguageTags.java 217 2010-10-02 14:45:56Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.language;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 * @see <a href="http://www.iana.org/assignments/language-tags">IANA LANGUAGE TAGS</a>
 */
public final class IANALanguageTags {

	/**
	 * 
	 * 
	 */
	private static LanguageTag itag = new LanguageTag(){
		public String getCode(){
			return "i";
		}
	};

	/**
	 * Lojban
	 * @deprecated use ISO 639-2 jbo, registered Sept. 2, 2003.
	 */
	@Deprecated
	public static final Language art_lojban
		= Language.get(ISO639_2.ART, "lojban");

	/**
	 * Azerbaijani in Arabic script
	 */
	public static final Language az_Arab
		= Language.get(ISO639_1.AZ, "Arab");

	/**
	 * Azerbaijani in Cyrillic script
	 */
	public static final Language az_Cyrl
		= Language.get(ISO639_1.AZ, "Cyrl");

	/**
	 * Azerbaijani in Latin script
	 */
	public static final Language az_Latn
		= Language.get(ISO639_1.AZ, "Latn");

	/**
	 * Belarusian in Latin script
	 */
	public static final Language be_Latn
		= Language.get(ISO639_1.BE, "Latn");

	/**
	 * Bosnian in Cyrillic script
	 */
	public static final Language bs_Cyrl
		= Language.get(ISO639_1.BS, "Cyrl");

	/**
	 * Bosnian in Latin script
	 */
	public static final Language bs_Latn
		= Language.get(ISO639_1.BS, "Latn");

	/**
	 * Gaulish
	 */
	public static final Language cel_gaulish
		= Language.get(ISO639_2.CEL, "gaulish");

	/**
	 * German, traditional orthography
	 */
	public static final Language de_1901
		= Language.get(ISO639_1.DE, "1901");

	/**
	 * German, orthography of 1996
	 */
	public static final Language de_1996
		= Language.get(ISO639_1.DE, "1996");

	/**
	 * German, Austrian variant, traditional orthography
	 */
	public static final Language de_AT_1901
		= Language.get(ISO639_1.DE, ISO3166.AT, "1901");

	/**
	 * German, Austrian variant, orthography of 1996
	 */
	public static final Language de_AT_1996
		= Language.get(ISO639_1.DE, ISO3166.AT, "1996");

	/**
	 * German, Swiss variant, traditional orthography
	 */
	public static final Language de_CH_1901
		= Language.get(ISO639_1.DE, ISO3166.CH, "1901");

	/**
	 * German, Swiss variant, orthography of 1996
	 */
	public static final Language de_CH_1996
		= Language.get(ISO639_1.DE, ISO3166.CH, "1996");

	/**
	 * German, German variant, traditional orthography
	 */
	public static final Language de_DE_1901
		= Language.get(ISO639_1.DE, ISO3166.DE, "1901");

	/**
	 * German, German variant, orthography of 1996
	 */
	public static final Language de_DE_1996
		= Language.get(ISO639_1.DE, ISO3166.DE, "1996");

	/**
	 * Greek in Latin script
	 */
	public static final Language el_Latn
		= Language.get(ISO639_1.EL, "Latn");

	/**
	 * Boontling
	 */
	public static final Language en_boont
		= Language.get(ISO639_1.EN, "boont");

	/**
	 * English, Oxford English Dictionary spell
	 */
	public static final Language en_GB_oed
		= Language.get(ISO639_1.EN, ISO3166.GB, "oed");

	/**
	 * English Liverpudlian dialect known as 'Scouse'
	 */
	public static final Language en_scouse
		= Language.get(ISO639_1.EN, "scouse");

	/**
	 * Latin American Spanish
	 */
	public static final Language es_419
		= Language.get(ISO639_1.ES, "419");

	/**
	 * Amis
	 */
	public static final Language i_ami
		= Language.get(itag, "ami");

	/**
	 * Bunun
	 */
	public static final Language i_bnn
		= Language.get(itag, "bnn");

	/**
	 * Default Language Context
	 */
	public static final Language i_default
		= Language.get(itag, "default");

	/**
	 * Enochian
	 */
	public static final Language i_enochian
		= Language.get(itag, "enochian");

	/**
	 * Hakka
	 * @deprecated use IETF zh-hakka, registered Jan. 10, 2000
	 */
	@Deprecated
	public static final Language i_hak
		= Language.get(itag, "hak");

	/**
	 * Klingon
	 * @deprecated use ISO 639-2 tlh, registered Feb. 24, 2004.
	 */
	@Deprecated
	public static final Language i_klingon
		= Language.get(itag, "klingon");

	/**
	 * Luxembourgish
	 * @deprecated use ISO 639 lb, registered Sept. 9, 1998 [iso639]
	 */
	@Deprecated
	public static final Language i_lux
		= Language.get(itag, "lux");

	/**
	 * Mingo
	 */
	public static final Language i_mingo
		= Language.get(itag, "mingo");

	/**
	 * Navajo
	 * @deprecated use ISO 639 nv, registered Feb. 18, 2000 [iso639]
	 */
	@Deprecated
	public static final Language i_navajo
		= Language.get(itag, "navajo");

	/**
	 * Paiwan
	 */
	public static final Language i_pwn
		= Language.get(itag, "pwn");

	/**
	 * Tao
	 */
	public static final Language i_tao
		= Language.get(itag, "tao");

	/**
	 * Tayal
	 */
	public static final Language i_tay
		= Language.get(itag, "tay");

	/**
	 * Tsou
	 */
	public static final Language i_tsu
		= Language.get(itag, "tsu");

	/**
	 * Inuktitut in Canadian Aboriginal
	 */
	public static final Language iu_Cans
		= Language.get(ISO639_1.IU, "Cans");

	/**
	 * Inuktitut in Latin script
	 */
	public static final Language iu_Latn
		= Language.get(ISO639_1.IU, "Latn");

	/**
	 * Mongolian in Cyrillic script
	 */
	public static final Language mn_Cyrl
		= Language.get(ISO639_1.MN, "Cyrl");

	/**
	 * Mongolian in Mongolian script
	 */
	public static final Language mn_Mong
		= Language.get(ISO639_1.MN, "Mong");

	/**
	 * Norwegian "Book language"
	 * @deprecated 639 nb, registered Feb. 18, 2000 [iso639]
	 */
	@Deprecated
	public static final Language no_bok
		= Language.get(ISO639_1.NO, "bok");

	/**
	 * Norwegian "New Norwegian"
	 * @deprecated 639 nn, registered Feb. 18, 2000 [iso639]
	 */
	@Deprecated
	public static final Language no_nyn
		= Language.get(ISO639_1.NO, "nyn");

	/**
	 * Belgian-French Sign Language
	 */
	public static final Language sgn_BE_fr
		= Language.get(ISO639_2.SGN, ISO3166.BE, "fr");

	/**
	 * Belgian-Flemish Sign Language
	 */
	public static final Language sgn_BE_nl
		= Language.get(ISO639_2.SGN, ISO3166.BE, "nl");

	/**
	 * Brazilian Sign Language
	 */
	public static final Language sgn_BR
		= Language.get(ISO639_2.SGN, ISO3166.BR);

	/**
	 * Swiss German Sign Language
	 */
	public static final Language sgn_CH_de
		= Language.get(ISO639_2.SGN, ISO3166.CH, "de");

	/**
	 * Colombian Sign Language
	 */
	public static final Language sgn_CO
		= Language.get(ISO639_2.SGN, ISO3166.CO);

	/**
	 * German Sign Language
	 */
	public static final Language sgn_DE
		= Language.get(ISO639_2.SGN, ISO3166.DE);

	/**
	 * Danish Sign Language
	 */
	public static final Language sgn_DK
		= Language.get(ISO639_2.SGN, ISO3166.DK);

	/**
	 * Spanish Sign Language
	 */
	public static final Language sgn_ES
		= Language.get(ISO639_2.SGN, ISO3166.ES);

	/**
	 * French Sign Language
	 */
	public static final Language sgn_FR
		= Language.get(ISO639_2.SGN, ISO3166.FR);

	/**
	 * British Sign Language
	 */
	public static final Language sgn_GB
		= Language.get(ISO639_2.SGN, ISO3166.GB);

	/**
	 * Greek Sign Language
	 */
	public static final Language sgn_GR
		= Language.get(ISO639_2.SGN, ISO3166.GR);

	/**
	 * Irish Sign Language
	 */
	public static final Language sgn_IE
		= Language.get(ISO639_2.SGN, ISO3166.IE);

	/**
	 * Italian Sign Language
	 */
	public static final Language sgn_IT
		= Language.get(ISO639_2.SGN, ISO3166.IT);

	/**
	 * Japanese Sign Language
	 */
	public static final Language sgn_JP
		= Language.get(ISO639_2.SGN, ISO3166.JP);

	/**
	 * Mexican Sign Language
	 */
	public static final Language sgn_MX
		= Language.get(ISO639_2.SGN, ISO3166.MX);

	/**
	 * Nicaraguan Sign Language
	 */
	public static final Language sgn_NI
		= Language.get(ISO639_2.SGN, ISO3166.NI);

	/**
	 * Dutch Sign Language
	 */
	public static final Language sgn_NL
		= Language.get(ISO639_2.SGN, ISO3166.NL);

	/**
	 * Norwegian Sign Language
	 */
	public static final Language sgn_NO
		= Language.get(ISO639_2.SGN, ISO3166.NO);

	/**
	 * Portuguese Sign Language
	 */
	public static final Language sgn_PT
		= Language.get(ISO639_2.SGN, ISO3166.PT);

	/**
	 * Swedish Sign Language
	 */
	public static final Language sgn_SE
		= Language.get(ISO639_2.SGN, ISO3166.SE);

	/**
	 * American Sign Language
	 */
	public static final Language sgn_US
		= Language.get(ISO639_2.SGN, ISO3166.US);

	/**
	 * South African Sign Language
	 */
	public static final Language sgn_ZA
		= Language.get(ISO639_2.SGN, ISO3166.ZA);

	/**
	 * Natisone dialect, Nadiza dialect
	 */
	public static final Language sl_nedis
		= Language.get(ISO639_1.SL, "nedis");

	/**
	 * Resian, Resianic, Rezijan
	 */
	public static final Language sl_rozaj
		= Language.get(ISO639_1.SL, "rozaj");

	/**
	 * Serbian in Cyrillic script
	 */
	public static final Language sr_Cyrl
		= Language.get(ISO639_1.SR, "Cyrl");

	/**
	 * Serbian in Latin script
	 */
	public static final Language sr_Latn
		= Language.get(ISO639_1.SR, "Latn");

	/**
	 * Tajik in Arabic script
	 */
	public static final Language tg_Arab
		= Language.get(ISO639_1.TG, "Arab");

	/**
	 * Tajik in Cyrillic script
	 */
	public static final Language tg_Cyrl
		= Language.get(ISO639_1.TG, "Cyrl");

	/**
	 * Uzbek in Cyrillic script
	 */
	public static final Language uz_Cyrl
		= Language.get(ISO639_1.UZ, "Cyrl");

	/**
	 * Uzbek in Latin script
	 */
	public static final Language uz_Latn
		= Language.get(ISO639_1.UZ, "Latn");

	/**
	 * Yiddish, in Latin script
	 */
	public static final Language yi_latn
		= Language.get(ISO639_1.YI, "latn");

	/**
	 * Mandarin Chinese
	 */
	public static final Language zh_cmn
		= Language.get(ISO639_1.ZH, "cmn");

	/**
	 * Mandarin Chinese (Simplified)
	 */
	public static final Language zh_cmn_Hans
		= Language.get(ISO639_1.ZH, "cmn", "Hans");

	/**
	 * Mandarin Chinese (Traditional)
	 */
	public static final Language zh_cmn_Hant
		= Language.get(ISO639_1.ZH, "cmn", "Hant");

	/**
	 * Chinese, in simplified script
	 */
	public static final Language zh_Hans
		= Language.get(ISO639_1.ZH, "Hans");

	/**
	 * PRC Mainland Chinese in simplified scrip
	 */
	public static final Language zh_Hans_CN
		= Language.get(ISO639_1.ZH, "Hans", "CN");

	/**
	 * Hong Kong Chinese in simplified script
	 */
	public static final Language zh_Hans_HK
		= Language.get(ISO639_1.ZH, "Hans", "HK");

	/**
	 * Macao Chinese in simplified script
	 */
	public static final Language zh_Hans_MO
		= Language.get(ISO639_1.ZH, "Hans", "MO");

	/**
	 * Singapore Chinese in simplified script
	 */
	public static final Language zh_Hans_SG
		= Language.get(ISO639_1.ZH, "Hans", "SG");

	/**
	 * Taiwan Chinese in simplified script
	 */
	public static final Language zh_Hans_TW
		= Language.get(ISO639_1.ZH, "Hans", "TW");

	/**
	 * Chinese, in traditional script
	 */
	public static final Language zh_Hant
		= Language.get(ISO639_1.ZH, "Hant");

	/**
	 * PRC Mainland Chinese in traditional scri
	 */
	public static final Language zh_Hant_CN
		= Language.get(ISO639_1.ZH, "Hant", "CN");

	/**
	 * Hong Kong Chinese in traditional script
	 */
	public static final Language zh_Hant_HK
		= Language.get(ISO639_1.ZH, "Hant", "HK");

	/**
	 * Macao Chinese in traditional script
	 */
	public static final Language zh_Hant_MO
		= Language.get(ISO639_1.ZH, "Hant", "MO");

	/**
	 * Singapore Chinese in traditional script
	 */
	public static final Language zh_Hant_SG
		= Language.get(ISO639_1.ZH, "Hant", "SG");

	/**
	 * Taiwan Chinese in traditional script
	 */
	public static final Language zh_Hant_TW
		= Language.get(ISO639_1.ZH, "Hant", "TW");

	/**
	 * Kan or Gan
	 */
	public static final Language zh_gan
		= Language.get(ISO639_1.ZH, "gan");

	/**
	 * Mandarin or Standard Chinese
	 * @deprecated Deprecated, use zh-cmn registered in July 2005
	 */
	@Deprecated
	public static final Language zh_guoyu
		= Language.get(ISO639_1.ZH, "guoyu");

	/**
	 * Hakka
	 */
	public static final Language zh_hakka
		= Language.get(ISO639_1.ZH, "hakka");

	/**
	 * Min, Fuzhou, Hokkien, Amoy or Taiwanese
	 */
	public static final Language zh_min
		= Language.get(ISO639_1.ZH, "min");

	/**
	 * Minnan, Hokkien, Amoy, Taiwanese, Southern Min, Southern Fujian, Hoklo, Southern Fukien, Ho-lo
	 */
	public static final Language zh_min_nan
		= Language.get(ISO639_1.ZH, "min", "nan");

	/**
	 * Shanghaiese or Wu
	 */
	public static final Language zh_wuu
		= Language.get(ISO639_1.ZH, "wuu");

	/**
	 * Xiang or Hunanese
	 */
	public static final Language zh_xiang
		= Language.get(ISO639_1.ZH, "xiang");

	/**
	 * Cantonese
	 */
	public static final Language zh_yue
		= Language.get(ISO639_1.ZH, "yue");

	/**
	 * 
	 * 
	 */
	public static Language get(String languageExpression){
		return capitalToLanguage.get(languageExpression.toUpperCase());
	}

	/**
	 * 
	 * 
	 */
	public static String[] getDescriptions(Language language){
		return (String[])capitalToDescription.get(language.getCode().toUpperCase());
	}

	/**
	 * 
	 * 
	 */
	public static Set<Language> values(){
		return values;
	}

	private static Map<String, Language> capitalToLanguage
		= new LinkedHashMap<String, Language>();
	private static Map<String, Object> capitalToDescription
		= new HashMap<String, Object>();
	private static Set<Language> values
		= new LinkedHashSet<Language>();

	static{
		capitalToLanguage.put(art_lojban.getCode().toUpperCase(), art_lojban);
		capitalToDescription.put(
			art_lojban.getCode().toUpperCase()
			, new String[]{"Lojban"});
		capitalToLanguage.put(az_Arab.getCode().toUpperCase(), az_Arab);
		capitalToDescription.put(
			az_Arab.getCode().toUpperCase()
			, new String[]{"Azerbaijani in Arabic script"});
		capitalToLanguage.put(az_Cyrl.getCode().toUpperCase(), az_Cyrl);
		capitalToDescription.put(
			az_Cyrl.getCode().toUpperCase()
			, new String[]{"Azerbaijani in Cyrillic script"});
		capitalToLanguage.put(az_Latn.getCode().toUpperCase(), az_Latn);
		capitalToDescription.put(
			az_Latn.getCode().toUpperCase()
			, new String[]{"Azerbaijani in Latin script"});
		capitalToLanguage.put(be_Latn.getCode().toUpperCase(), be_Latn);
		capitalToDescription.put(
			be_Latn.getCode().toUpperCase()
			, new String[]{"Belarusian in Latin script"});
		capitalToLanguage.put(bs_Cyrl.getCode().toUpperCase(), bs_Cyrl);
		capitalToDescription.put(
			bs_Cyrl.getCode().toUpperCase()
			, new String[]{"Bosnian in Cyrillic script"});
		capitalToLanguage.put(bs_Latn.getCode().toUpperCase(), bs_Latn);
		capitalToDescription.put(
			bs_Latn.getCode().toUpperCase()
			, new String[]{"Bosnian in Latin script"});
		capitalToLanguage.put(cel_gaulish.getCode().toUpperCase(), cel_gaulish);
		capitalToDescription.put(
			cel_gaulish.getCode().toUpperCase()
			, new String[]{"Gaulish"});
		capitalToLanguage.put(de_1901.getCode().toUpperCase(), de_1901);
		capitalToDescription.put(
			de_1901.getCode().toUpperCase()
			, new String[]{"German","traditional orthography"});
		capitalToLanguage.put(de_1996.getCode().toUpperCase(), de_1996);
		capitalToDescription.put(
			de_1996.getCode().toUpperCase()
			, new String[]{"German","orthography of 1996"});
		capitalToLanguage.put(de_AT_1901.getCode().toUpperCase(), de_AT_1901);
		capitalToDescription.put(
			de_AT_1901.getCode().toUpperCase()
			, new String[]{"German","Austrian variant","traditional orthography"});
		capitalToLanguage.put(de_AT_1996.getCode().toUpperCase(), de_AT_1996);
		capitalToDescription.put(
			de_AT_1996.getCode().toUpperCase()
			, new String[]{"German","Austrian variant","orthography of 1996"});
		capitalToLanguage.put(de_CH_1901.getCode().toUpperCase(), de_CH_1901);
		capitalToDescription.put(
			de_CH_1901.getCode().toUpperCase()
			, new String[]{"German","Swiss variant","traditional orthography"});
		capitalToLanguage.put(de_CH_1996.getCode().toUpperCase(), de_CH_1996);
		capitalToDescription.put(
			de_CH_1996.getCode().toUpperCase()
			, new String[]{"German","Swiss variant","orthography of 1996"});
		capitalToLanguage.put(de_DE_1901.getCode().toUpperCase(), de_DE_1901);
		capitalToDescription.put(
			de_DE_1901.getCode().toUpperCase()
			, new String[]{"German","German variant","traditional orthography"});
		capitalToLanguage.put(de_DE_1996.getCode().toUpperCase(), de_DE_1996);
		capitalToDescription.put(
			de_DE_1996.getCode().toUpperCase()
			, new String[]{"German","German variant","orthography of 1996"});
		capitalToLanguage.put(el_Latn.getCode().toUpperCase(), el_Latn);
		capitalToDescription.put(
			el_Latn.getCode().toUpperCase()
			, new String[]{"Greek in Latin script"});
		capitalToLanguage.put(en_boont.getCode().toUpperCase(), en_boont);
		capitalToDescription.put(
			en_boont.getCode().toUpperCase()
			, new String[]{"Boontling"});
		capitalToLanguage.put(en_GB_oed.getCode().toUpperCase(), en_GB_oed);
		capitalToDescription.put(
			en_GB_oed.getCode().toUpperCase()
			, new String[]{"English","Oxford English Dictionary spell"});
		capitalToLanguage.put(en_scouse.getCode().toUpperCase(), en_scouse);
		capitalToDescription.put(
			en_scouse.getCode().toUpperCase()
			, new String[]{"English Liverpudlian dialect known as 'Scouse'"});
		capitalToLanguage.put(es_419.getCode().toUpperCase(), es_419);
		capitalToDescription.put(
			es_419.getCode().toUpperCase()
			, new String[]{"Latin American Spanish"});
		capitalToLanguage.put(i_ami.getCode().toUpperCase(), i_ami);
		capitalToDescription.put(
			i_ami.getCode().toUpperCase()
			, new String[]{"Amis"});
		capitalToLanguage.put(i_bnn.getCode().toUpperCase(), i_bnn);
		capitalToDescription.put(
			i_bnn.getCode().toUpperCase()
			, new String[]{"Bunun"});
		capitalToLanguage.put(i_default.getCode().toUpperCase(), i_default);
		capitalToDescription.put(
			i_default.getCode().toUpperCase()
			, new String[]{"Default Language Context"});
		capitalToLanguage.put(i_enochian.getCode().toUpperCase(), i_enochian);
		capitalToDescription.put(
			i_enochian.getCode().toUpperCase()
			, new String[]{"Enochian"});
		capitalToLanguage.put(i_hak.getCode().toUpperCase(), i_hak);
		capitalToDescription.put(
			i_hak.getCode().toUpperCase()
			, new String[]{"Hakka"});
		capitalToLanguage.put(i_klingon.getCode().toUpperCase(), i_klingon);
		capitalToDescription.put(
			i_klingon.getCode().toUpperCase()
			, new String[]{"Klingon"});
		capitalToLanguage.put(i_lux.getCode().toUpperCase(), i_lux);
		capitalToDescription.put(
			i_lux.getCode().toUpperCase()
			, new String[]{"Luxembourgish"});
		capitalToLanguage.put(i_mingo.getCode().toUpperCase(), i_mingo);
		capitalToDescription.put(
			i_mingo.getCode().toUpperCase()
			, new String[]{"Mingo"});
		capitalToLanguage.put(i_navajo.getCode().toUpperCase(), i_navajo);
		capitalToDescription.put(
			i_navajo.getCode().toUpperCase()
			, new String[]{"Navajo"});
		capitalToLanguage.put(i_pwn.getCode().toUpperCase(), i_pwn);
		capitalToDescription.put(
			i_pwn.getCode().toUpperCase()
			, new String[]{"Paiwan"});
		capitalToLanguage.put(i_tao.getCode().toUpperCase(), i_tao);
		capitalToDescription.put(
			i_tao.getCode().toUpperCase()
			, new String[]{"Tao"});
		capitalToLanguage.put(i_tay.getCode().toUpperCase(), i_tay);
		capitalToDescription.put(
			i_tay.getCode().toUpperCase()
			, new String[]{"Tayal"});
		capitalToLanguage.put(i_tsu.getCode().toUpperCase(), i_tsu);
		capitalToDescription.put(
			i_tsu.getCode().toUpperCase()
			, new String[]{"Tsou"});
		capitalToLanguage.put(iu_Cans.getCode().toUpperCase(), iu_Cans);
		capitalToDescription.put(
			iu_Cans.getCode().toUpperCase()
			, new String[]{"Inuktitut in Canadian Aboriginal"});
		capitalToLanguage.put(iu_Latn.getCode().toUpperCase(), iu_Latn);
		capitalToDescription.put(
			iu_Latn.getCode().toUpperCase()
			, new String[]{"Inuktitut in Latin script"});
		capitalToLanguage.put(mn_Cyrl.getCode().toUpperCase(), mn_Cyrl);
		capitalToDescription.put(
			mn_Cyrl.getCode().toUpperCase()
			, new String[]{"Mongolian in Cyrillic script"});
		capitalToLanguage.put(mn_Mong.getCode().toUpperCase(), mn_Mong);
		capitalToDescription.put(
			mn_Mong.getCode().toUpperCase()
			, new String[]{"Mongolian in Mongolian script"});
		capitalToLanguage.put(no_bok.getCode().toUpperCase(), no_bok);
		capitalToDescription.put(
			no_bok.getCode().toUpperCase()
			, new String[]{"Norwegian \"Book language\""});
		capitalToLanguage.put(no_nyn.getCode().toUpperCase(), no_nyn);
		capitalToDescription.put(
			no_nyn.getCode().toUpperCase()
			, new String[]{"Norwegian \"New Norwegian\""});
		capitalToLanguage.put(sgn_BE_fr.getCode().toUpperCase(), sgn_BE_fr);
		capitalToDescription.put(
			sgn_BE_fr.getCode().toUpperCase()
			, new String[]{"Belgian-French Sign Language"});
		capitalToLanguage.put(sgn_BE_nl.getCode().toUpperCase(), sgn_BE_nl);
		capitalToDescription.put(
			sgn_BE_nl.getCode().toUpperCase()
			, new String[]{"Belgian-Flemish Sign Language"});
		capitalToLanguage.put(sgn_BR.getCode().toUpperCase(), sgn_BR);
		capitalToDescription.put(
			sgn_BR.getCode().toUpperCase()
			, new String[]{"Brazilian Sign Language"});
		capitalToLanguage.put(sgn_CH_de.getCode().toUpperCase(), sgn_CH_de);
		capitalToDescription.put(
			sgn_CH_de.getCode().toUpperCase()
			, new String[]{"Swiss German Sign Language"});
		capitalToLanguage.put(sgn_CO.getCode().toUpperCase(), sgn_CO);
		capitalToDescription.put(
			sgn_CO.getCode().toUpperCase()
			, new String[]{"Colombian Sign Language"});
		capitalToLanguage.put(sgn_DE.getCode().toUpperCase(), sgn_DE);
		capitalToDescription.put(
			sgn_DE.getCode().toUpperCase()
			, new String[]{"German Sign Language"});
		capitalToLanguage.put(sgn_DK.getCode().toUpperCase(), sgn_DK);
		capitalToDescription.put(
			sgn_DK.getCode().toUpperCase()
			, new String[]{"Danish Sign Language"});
		capitalToLanguage.put(sgn_ES.getCode().toUpperCase(), sgn_ES);
		capitalToDescription.put(
			sgn_ES.getCode().toUpperCase()
			, new String[]{"Spanish Sign Language"});
		capitalToLanguage.put(sgn_FR.getCode().toUpperCase(), sgn_FR);
		capitalToDescription.put(
			sgn_FR.getCode().toUpperCase()
			, new String[]{"French Sign Language"});
		capitalToLanguage.put(sgn_GB.getCode().toUpperCase(), sgn_GB);
		capitalToDescription.put(
			sgn_GB.getCode().toUpperCase()
			, new String[]{"British Sign Language"});
		capitalToLanguage.put(sgn_GR.getCode().toUpperCase(), sgn_GR);
		capitalToDescription.put(
			sgn_GR.getCode().toUpperCase()
			, new String[]{"Greek Sign Language"});
		capitalToLanguage.put(sgn_IE.getCode().toUpperCase(), sgn_IE);
		capitalToDescription.put(
			sgn_IE.getCode().toUpperCase()
			, new String[]{"Irish Sign Language"});
		capitalToLanguage.put(sgn_IT.getCode().toUpperCase(), sgn_IT);
		capitalToDescription.put(
			sgn_IT.getCode().toUpperCase()
			, new String[]{"Italian Sign Language"});
		capitalToLanguage.put(sgn_JP.getCode().toUpperCase(), sgn_JP);
		capitalToDescription.put(
			sgn_JP.getCode().toUpperCase()
			, new String[]{"Japanese Sign Language"});
		capitalToLanguage.put(sgn_MX.getCode().toUpperCase(), sgn_MX);
		capitalToDescription.put(
			sgn_MX.getCode().toUpperCase()
			, new String[]{"Mexican Sign Language"});
		capitalToLanguage.put(sgn_NI.getCode().toUpperCase(), sgn_NI);
		capitalToDescription.put(
			sgn_NI.getCode().toUpperCase()
			, new String[]{"Nicaraguan Sign Language"});
		capitalToLanguage.put(sgn_NL.getCode().toUpperCase(), sgn_NL);
		capitalToDescription.put(
			sgn_NL.getCode().toUpperCase()
			, new String[]{"Dutch Sign Language"});
		capitalToLanguage.put(sgn_NO.getCode().toUpperCase(), sgn_NO);
		capitalToDescription.put(
			sgn_NO.getCode().toUpperCase()
			, new String[]{"Norwegian Sign Language"});
		capitalToLanguage.put(sgn_PT.getCode().toUpperCase(), sgn_PT);
		capitalToDescription.put(
			sgn_PT.getCode().toUpperCase()
			, new String[]{"Portuguese Sign Language"});
		capitalToLanguage.put(sgn_SE.getCode().toUpperCase(), sgn_SE);
		capitalToDescription.put(
			sgn_SE.getCode().toUpperCase()
			, new String[]{"Swedish Sign Language"});
		capitalToLanguage.put(sgn_US.getCode().toUpperCase(), sgn_US);
		capitalToDescription.put(
			sgn_US.getCode().toUpperCase()
			, new String[]{"American Sign Language"});
		capitalToLanguage.put(sgn_ZA.getCode().toUpperCase(), sgn_ZA);
		capitalToDescription.put(
			sgn_ZA.getCode().toUpperCase()
			, new String[]{"South African Sign Language"});
		capitalToLanguage.put(sl_nedis.getCode().toUpperCase(), sl_nedis);
		capitalToDescription.put(
			sl_nedis.getCode().toUpperCase()
			, new String[]{"Natisone dialect","Nadiza dialect"});
		capitalToLanguage.put(sl_rozaj.getCode().toUpperCase(), sl_rozaj);
		capitalToDescription.put(
			sl_rozaj.getCode().toUpperCase()
			, new String[]{"Resian","Resianic","Rezijan"});
		capitalToLanguage.put(sr_Cyrl.getCode().toUpperCase(), sr_Cyrl);
		capitalToDescription.put(
			sr_Cyrl.getCode().toUpperCase()
			, new String[]{"Serbian in Cyrillic script"});
		capitalToLanguage.put(sr_Latn.getCode().toUpperCase(), sr_Latn);
		capitalToDescription.put(
			sr_Latn.getCode().toUpperCase()
			, new String[]{"Serbian in Latin script"});
		capitalToLanguage.put(tg_Arab.getCode().toUpperCase(), tg_Arab);
		capitalToDescription.put(
			tg_Arab.getCode().toUpperCase()
			, new String[]{"Tajik in Arabic script"});
		capitalToLanguage.put(tg_Cyrl.getCode().toUpperCase(), tg_Cyrl);
		capitalToDescription.put(
			tg_Cyrl.getCode().toUpperCase()
			, new String[]{"Tajik in Cyrillic script"});
		capitalToLanguage.put(uz_Cyrl.getCode().toUpperCase(), uz_Cyrl);
		capitalToDescription.put(
			uz_Cyrl.getCode().toUpperCase()
			, new String[]{"Uzbek in Cyrillic script"});
		capitalToLanguage.put(uz_Latn.getCode().toUpperCase(), uz_Latn);
		capitalToDescription.put(
			uz_Latn.getCode().toUpperCase()
			, new String[]{"Uzbek in Latin script"});
		capitalToLanguage.put(yi_latn.getCode().toUpperCase(), yi_latn);
		capitalToDescription.put(
			yi_latn.getCode().toUpperCase()
			, new String[]{"Yiddish","in Latin script"});
		capitalToLanguage.put(zh_cmn.getCode().toUpperCase(), zh_cmn);
		capitalToDescription.put(
			zh_cmn.getCode().toUpperCase()
			, new String[]{"Mandarin Chinese"});
		capitalToLanguage.put(zh_cmn_Hans.getCode().toUpperCase(), zh_cmn_Hans);
		capitalToDescription.put(
			zh_cmn_Hans.getCode().toUpperCase()
			, new String[]{"Mandarin Chinese (Simplified)"});
		capitalToLanguage.put(zh_cmn_Hant.getCode().toUpperCase(), zh_cmn_Hant);
		capitalToDescription.put(
			zh_cmn_Hant.getCode().toUpperCase()
			, new String[]{"Mandarin Chinese (Traditional)"});
		capitalToLanguage.put(zh_Hans.getCode().toUpperCase(), zh_Hans);
		capitalToDescription.put(
			zh_Hans.getCode().toUpperCase()
			, new String[]{"Chinese","in simplified script"});
		capitalToLanguage.put(zh_Hans_CN.getCode().toUpperCase(), zh_Hans_CN);
		capitalToDescription.put(
			zh_Hans_CN.getCode().toUpperCase()
			, new String[]{"PRC Mainland Chinese in simplified scrip"});
		capitalToLanguage.put(zh_Hans_HK.getCode().toUpperCase(), zh_Hans_HK);
		capitalToDescription.put(
			zh_Hans_HK.getCode().toUpperCase()
			, new String[]{"Hong Kong Chinese in simplified script"});
		capitalToLanguage.put(zh_Hans_MO.getCode().toUpperCase(), zh_Hans_MO);
		capitalToDescription.put(
			zh_Hans_MO.getCode().toUpperCase()
			, new String[]{"Macao Chinese in simplified script"});
		capitalToLanguage.put(zh_Hans_SG.getCode().toUpperCase(), zh_Hans_SG);
		capitalToDescription.put(
			zh_Hans_SG.getCode().toUpperCase()
			, new String[]{"Singapore Chinese in simplified script"});
		capitalToLanguage.put(zh_Hans_TW.getCode().toUpperCase(), zh_Hans_TW);
		capitalToDescription.put(
			zh_Hans_TW.getCode().toUpperCase()
			, new String[]{"Taiwan Chinese in simplified script"});
		capitalToLanguage.put(zh_Hant.getCode().toUpperCase(), zh_Hant);
		capitalToDescription.put(
			zh_Hant.getCode().toUpperCase()
			, new String[]{"Chinese","in traditional script"});
		capitalToLanguage.put(zh_Hant_CN.getCode().toUpperCase(), zh_Hant_CN);
		capitalToDescription.put(
			zh_Hant_CN.getCode().toUpperCase()
			, new String[]{"PRC Mainland Chinese in traditional scri"});
		capitalToLanguage.put(zh_Hant_HK.getCode().toUpperCase(), zh_Hant_HK);
		capitalToDescription.put(
			zh_Hant_HK.getCode().toUpperCase()
			, new String[]{"Hong Kong Chinese in traditional script"});
		capitalToLanguage.put(zh_Hant_MO.getCode().toUpperCase(), zh_Hant_MO);
		capitalToDescription.put(
			zh_Hant_MO.getCode().toUpperCase()
			, new String[]{"Macao Chinese in traditional script"});
		capitalToLanguage.put(zh_Hant_SG.getCode().toUpperCase(), zh_Hant_SG);
		capitalToDescription.put(
			zh_Hant_SG.getCode().toUpperCase()
			, new String[]{"Singapore Chinese in traditional script"});
		capitalToLanguage.put(zh_Hant_TW.getCode().toUpperCase(), zh_Hant_TW);
		capitalToDescription.put(
			zh_Hant_TW.getCode().toUpperCase()
			, new String[]{"Taiwan Chinese in traditional script"});
		capitalToLanguage.put(zh_gan.getCode().toUpperCase(), zh_gan);
		capitalToDescription.put(
			zh_gan.getCode().toUpperCase()
			, new String[]{"Kan or Gan"});
		capitalToLanguage.put(zh_guoyu.getCode().toUpperCase(), zh_guoyu);
		capitalToDescription.put(
			zh_guoyu.getCode().toUpperCase()
			, new String[]{"Mandarin or Standard Chinese"});
		capitalToLanguage.put(zh_hakka.getCode().toUpperCase(), zh_hakka);
		capitalToDescription.put(
			zh_hakka.getCode().toUpperCase()
			, new String[]{"Hakka"});
		capitalToLanguage.put(zh_min.getCode().toUpperCase(), zh_min);
		capitalToDescription.put(
			zh_min.getCode().toUpperCase()
			, new String[]{"Min","Fuzhou","Hokkien","Amoy or Taiwanese"});
		capitalToLanguage.put(zh_min_nan.getCode().toUpperCase(), zh_min_nan);
		capitalToDescription.put(
			zh_min_nan.getCode().toUpperCase()
			, new String[]{"Minnan","Hokkien","Amoy","Taiwanese","Southern Min","Southern Fujian","Hoklo","Southern Fukien","Ho-lo"});
		capitalToLanguage.put(zh_wuu.getCode().toUpperCase(), zh_wuu);
		capitalToDescription.put(
			zh_wuu.getCode().toUpperCase()
			, new String[]{"Shanghaiese or Wu"});
		capitalToLanguage.put(zh_xiang.getCode().toUpperCase(), zh_xiang);
		capitalToDescription.put(
			zh_xiang.getCode().toUpperCase()
			, new String[]{"Xiang or Hunanese"});
		capitalToLanguage.put(zh_yue.getCode().toUpperCase(), zh_yue);
		capitalToDescription.put(
			zh_yue.getCode().toUpperCase()
			, new String[]{"Cantonese"});
		capitalToLanguage = Collections.unmodifiableMap(capitalToLanguage);
		values.addAll(capitalToLanguage.values());
	}
}
