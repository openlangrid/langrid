/*
 * $Id: ISO639_1.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
package jp.go.nict.langrid.language;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ISO639: 2 letter codes。
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 * @see <a href="http://www.loc.gov/standards/iso639-2/ISO-639-2_utf-8.txt">ISO639.2 code list</a>
 */
public enum ISO639_1 implements LanguageTag{

	/**
	 * "Afar"
	 */
	AA("Afar"),

	/**
	 * "Abkhazian"
	 */
	AB("Abkhazian"),

	/**
	 * "Afrikaans"
	 */
	AF("Afrikaans"),

	/**
	 * "Akan"
	 */
	AK("Akan"),

	/**
	 * "Albanian"
	 */
	SQ("Albanian"),

	/**
	 * "Amharic"
	 */
	AM("Amharic"),

	/**
	 * "Arabic"
	 */
	AR("Arabic"),

	/**
	 * "Aragonese"
	 */
	AN("Aragonese"),

	/**
	 * "Armenian"
	 */
	HY("Armenian"),

	/**
	 * "Assamese"
	 */
	AS("Assamese"),

	/**
	 * "Avaric"
	 */
	AV("Avaric"),

	/**
	 * "Avestan"
	 */
	AE("Avestan"),

	/**
	 * "Aymara"
	 */
	AY("Aymara"),

	/**
	 * "Azerbaijani"
	 */
	AZ("Azerbaijani"),

	/**
	 * "Bashkir"
	 */
	BA("Bashkir"),

	/**
	 * "Bambara"
	 */
	BM("Bambara"),

	/**
	 * "Basque"
	 */
	EU("Basque"),

	/**
	 * "Belarusian"
	 */
	BE("Belarusian"),

	/**
	 * "Bengali"
	 */
	BN("Bengali"),

	/**
	 * "Bihari"
	 */
	BH("Bihari"),

	/**
	 * "Bislama"
	 */
	BI("Bislama"),

	/**
	 * "Bosnian"
	 */
	BS("Bosnian"),

	/**
	 * "Breton"
	 */
	BR("Breton"),

	/**
	 * "Bulgarian"
	 */
	BG("Bulgarian"),

	/**
	 * "Burmese"
	 */
	MY("Burmese"),

	/**
	 * "Catalan", "Valencian"
	 */
	CA("Catalan", "Valencian"),

	/**
	 * "Chamorro"
	 */
	CH("Chamorro"),

	/**
	 * "Chechen"
	 */
	CE("Chechen"),

	/**
	 * "Chinese"
	 */
	ZH("Chinese"),

	/**
	 * "Church Slavic", "Old Slavonic", "Church Slavonic", "Old Bulgarian", "Old Church Slavonic"
	 */
	CU("Church Slavic", "Old Slavonic", "Church Slavonic", "Old Bulgarian", "Old Church Slavonic"),

	/**
	 * "Chuvash"
	 */
	CV("Chuvash"),

	/**
	 * "Cornish"
	 */
	KW("Cornish"),

	/**
	 * "Corsican"
	 */
	CO("Corsican"),

	/**
	 * "Cree"
	 */
	CR("Cree"),

	/**
	 * "Czech"
	 */
	CS("Czech"),

	/**
	 * "Danish"
	 */
	DA("Danish"),

	/**
	 * "Divehi", "Dhivehi", "Maldivian"
	 */
	DV("Divehi", "Dhivehi", "Maldivian"),

	/**
	 * "Dutch", "Flemish"
	 */
	NL("Dutch", "Flemish"),

	/**
	 * "Dzongkha"
	 */
	DZ("Dzongkha"),

	/**
	 * "English"
	 */
	EN("English"),

	/**
	 * "Esperanto"
	 */
	EO("Esperanto"),

	/**
	 * "Estonian"
	 */
	ET("Estonian"),

	/**
	 * "Ewe"
	 */
	EE("Ewe"),

	/**
	 * "Faroese"
	 */
	FO("Faroese"),

	/**
	 * "Fijian"
	 */
	FJ("Fijian"),

	/**
	 * "Finnish"
	 */
	FI("Finnish"),

	/**
	 * "French"
	 */
	FR("French"),

	/**
	 * "Western Frisian"
	 */
	FY("Western Frisian"),

	/**
	 * "Fulah"
	 */
	FF("Fulah"),

	/**
	 * "Georgian"
	 */
	KA("Georgian"),

	/**
	 * "German"
	 */
	DE("German"),

	/**
	 * "Gaelic", "Scottish Gaelic"
	 */
	GD("Gaelic", "Scottish Gaelic"),

	/**
	 * "Irish"
	 */
	GA("Irish"),

	/**
	 * "Galician"
	 */
	GL("Galician"),

	/**
	 * "Manx"
	 */
	GV("Manx"),

	/**
	 * "Greek, Modern (1453-)"
	 */
	EL("Greek, Modern (1453-)"),

	/**
	 * "Guarani"
	 */
	GN("Guarani"),

	/**
	 * "Gujarati"
	 */
	GU("Gujarati"),

	/**
	 * "Haitian", "Haitian Creole"
	 */
	HT("Haitian", "Haitian Creole"),

	/**
	 * "Hausa"
	 */
	HA("Hausa"),

	/**
	 * "Hebrew"
	 */
	HE("Hebrew"),

	/**
	 * "Herero"
	 */
	HZ("Herero"),

	/**
	 * "Hindi"
	 */
	HI("Hindi"),

	/**
	 * "Hiri Motu"
	 */
	HO("Hiri Motu"),

	/**
	 * "Croatian"
	 */
	HR("Croatian"),

	/**
	 * "Hungarian"
	 */
	HU("Hungarian"),

	/**
	 * "Igbo"
	 */
	IG("Igbo"),

	/**
	 * "Icelandic"
	 */
	IS("Icelandic"),

	/**
	 * "Ido"
	 */
	IO("Ido"),

	/**
	 * "Sichuan Yi", "Nuosu"
	 */
	II("Sichuan Yi", "Nuosu"),

	/**
	 * "Inuktitut"
	 */
	IU("Inuktitut"),

	/**
	 * "Interlingue", "Occidental"
	 */
	IE("Interlingue", "Occidental"),

	/**
	 * "Interlingua (International Auxiliary Language Association)"
	 */
	IA("Interlingua (International Auxiliary Language Association)"),

	/**
	 * "Indonesian"
	 */
	ID("Indonesian"),

	/**
	 * "Inupiaq"
	 */
	IK("Inupiaq"),

	/**
	 * "Italian"
	 */
	IT("Italian"),

	/**
	 * "Javanese"
	 */
	JV("Javanese"),

	/**
	 * "Japanese"
	 */
	JA("Japanese"),

	/**
	 * "Kalaallisut", "Greenlandic"
	 */
	KL("Kalaallisut", "Greenlandic"),

	/**
	 * "Kannada"
	 */
	KN("Kannada"),

	/**
	 * "Kashmiri"
	 */
	KS("Kashmiri"),

	/**
	 * "Kanuri"
	 */
	KR("Kanuri"),

	/**
	 * "Kazakh"
	 */
	KK("Kazakh"),

	/**
	 * "Central Khmer"
	 */
	KM("Central Khmer"),

	/**
	 * "Kikuyu", "Gikuyu"
	 */
	KI("Kikuyu", "Gikuyu"),

	/**
	 * "Kinyarwanda"
	 */
	RW("Kinyarwanda"),

	/**
	 * "Kirghiz", "Kyrgyz"
	 */
	KY("Kirghiz", "Kyrgyz"),

	/**
	 * "Komi"
	 */
	KV("Komi"),

	/**
	 * "Kongo"
	 */
	KG("Kongo"),

	/**
	 * "Korean"
	 */
	KO("Korean"),

	/**
	 * "Kuanyama", "Kwanyama"
	 */
	KJ("Kuanyama", "Kwanyama"),

	/**
	 * "Kurdish"
	 */
	KU("Kurdish"),

	/**
	 * "Lao"
	 */
	LO("Lao"),

	/**
	 * "Latin"
	 */
	LA("Latin"),

	/**
	 * "Latvian"
	 */
	LV("Latvian"),

	/**
	 * "Limburgan", "Limburger", "Limburgish"
	 */
	LI("Limburgan", "Limburger", "Limburgish"),

	/**
	 * "Lingala"
	 */
	LN("Lingala"),

	/**
	 * "Lithuanian"
	 */
	LT("Lithuanian"),

	/**
	 * "Luxembourgish", "Letzeburgesch"
	 */
	LB("Luxembourgish", "Letzeburgesch"),

	/**
	 * "Luba-Katanga"
	 */
	LU("Luba-Katanga"),

	/**
	 * "Ganda"
	 */
	LG("Ganda"),

	/**
	 * "Macedonian"
	 */
	MK("Macedonian"),

	/**
	 * "Marshallese"
	 */
	MH("Marshallese"),

	/**
	 * "Malayalam"
	 */
	ML("Malayalam"),

	/**
	 * "Maori"
	 */
	MI("Maori"),

	/**
	 * "Marathi"
	 */
	MR("Marathi"),

	/**
	 * "Malay"
	 */
	MS("Malay"),

	/**
	 * "Malagasy"
	 */
	MG("Malagasy"),

	/**
	 * "Maltese"
	 */
	MT("Maltese"),

	/**
	 * "Mongolian"
	 */
	MN("Mongolian"),

	/**
	 * "Nauru"
	 */
	NA("Nauru"),

	/**
	 * "Navajo", "Navaho"
	 */
	NV("Navajo", "Navaho"),

	/**
	 * "Ndebele, South", "South Ndebele"
	 */
	NR("Ndebele, South", "South Ndebele"),

	/**
	 * "Ndebele, North", "North Ndebele"
	 */
	ND("Ndebele, North", "North Ndebele"),

	/**
	 * "Ndonga"
	 */
	NG("Ndonga"),

	/**
	 * "Nepali"
	 */
	NE("Nepali"),

	/**
	 * "Norwegian Nynorsk", "Nynorsk, Norwegian"
	 */
	NN("Norwegian Nynorsk", "Nynorsk, Norwegian"),

	/**
	 * "Bokmål, Norwegian", "Norwegian Bokmål"
	 */
	NB("Bokmål, Norwegian", "Norwegian Bokmål"),

	/**
	 * "Norwegian"
	 */
	NO("Norwegian"),

	/**
	 * "Chichewa", "Chewa", "Nyanja"
	 */
	NY("Chichewa", "Chewa", "Nyanja"),

	/**
	 * "Occitan (post 1500)", "Provençal"
	 */
	OC("Occitan (post 1500)", "Provençal"),

	/**
	 * "Ojibwa"
	 */
	OJ("Ojibwa"),

	/**
	 * "Oriya"
	 */
	OR("Oriya"),

	/**
	 * "Oromo"
	 */
	OM("Oromo"),

	/**
	 * "Ossetian", "Ossetic"
	 */
	OS("Ossetian", "Ossetic"),

	/**
	 * "Panjabi", "Punjabi"
	 */
	PA("Panjabi", "Punjabi"),

	/**
	 * "Persian"
	 */
	FA("Persian"),

	/**
	 * "Pali"
	 */
	PI("Pali"),

	/**
	 * "Polish"
	 */
	PL("Polish"),

	/**
	 * "Portuguese"
	 */
	PT("Portuguese"),

	/**
	 * "Pushto", "Pashto"
	 */
	PS("Pushto", "Pashto"),

	/**
	 * "Quechua"
	 */
	QU("Quechua"),

	/**
	 * "Romansh"
	 */
	RM("Romansh"),

	/**
	 * "Romanian", "Moldavian", "Moldovan"
	 */
	RO("Romanian", "Moldavian", "Moldovan"),

	/**
	 * "Rundi"
	 */
	RN("Rundi"),

	/**
	 * "Russian"
	 */
	RU("Russian"),

	/**
	 * "Sango"
	 */
	SG("Sango"),

	/**
	 * "Sanskrit"
	 */
	SA("Sanskrit"),

	/**
	 * "Sinhala", "Sinhalese"
	 */
	SI("Sinhala", "Sinhalese"),

	/**
	 * "Slovak"
	 */
	SK("Slovak"),

	/**
	 * "Slovenian"
	 */
	SL("Slovenian"),

	/**
	 * "Northern Sami"
	 */
	SE("Northern Sami"),

	/**
	 * "Samoan"
	 */
	SM("Samoan"),

	/**
	 * "Shona"
	 */
	SN("Shona"),

	/**
	 * "Sindhi"
	 */
	SD("Sindhi"),

	/**
	 * "Somali"
	 */
	SO("Somali"),

	/**
	 * "Sotho, Southern"
	 */
	ST("Sotho, Southern"),

	/**
	 * "Spanish", "Castilian"
	 */
	ES("Spanish", "Castilian"),

	/**
	 * "Sardinian"
	 */
	SC("Sardinian"),

	/**
	 * "Serbian"
	 */
	SR("Serbian"),

	/**
	 * "Swati"
	 */
	SS("Swati"),

	/**
	 * "Sundanese"
	 */
	SU("Sundanese"),

	/**
	 * "Swahili"
	 */
	SW("Swahili"),

	/**
	 * "Swedish"
	 */
	SV("Swedish"),

	/**
	 * "Tahitian"
	 */
	TY("Tahitian"),

	/**
	 * "Tamil"
	 */
	TA("Tamil"),

	/**
	 * "Tatar"
	 */
	TT("Tatar"),

	/**
	 * "Telugu"
	 */
	TE("Telugu"),

	/**
	 * "Tajik"
	 */
	TG("Tajik"),

	/**
	 * "Tagalog"
	 */
	TL("Tagalog"),

	/**
	 * "Thai"
	 */
	TH("Thai"),

	/**
	 * "Tibetan"
	 */
	BO("Tibetan"),

	/**
	 * "Tigrinya"
	 */
	TI("Tigrinya"),

	/**
	 * "Tonga (Tonga Islands)"
	 */
	TO("Tonga (Tonga Islands)"),

	/**
	 * "Tswana"
	 */
	TN("Tswana"),

	/**
	 * "Tsonga"
	 */
	TS("Tsonga"),

	/**
	 * "Turkmen"
	 */
	TK("Turkmen"),

	/**
	 * "Turkish"
	 */
	TR("Turkish"),

	/**
	 * "Twi"
	 */
	TW("Twi"),

	/**
	 * "Uighur", "Uyghur"
	 */
	UG("Uighur", "Uyghur"),

	/**
	 * "Ukrainian"
	 */
	UK("Ukrainian"),

	/**
	 * "Urdu"
	 */
	UR("Urdu"),

	/**
	 * "Uzbek"
	 */
	UZ("Uzbek"),

	/**
	 * "Venda"
	 */
	VE("Venda"),

	/**
	 * "Vietnamese"
	 */
	VI("Vietnamese"),

	/**
	 * "Volapük"
	 */
	VO("Volapük"),

	/**
	 * "Welsh"
	 */
	CY("Welsh"),

	/**
	 * "Walloon"
	 */
	WA("Walloon"),

	/**
	 * "Wolof"
	 */
	WO("Wolof"),

	/**
	 * "Xhosa"
	 */
	XH("Xhosa"),

	/**
	 * "Yiddish"
	 */
	YI("Yiddish"),

	/**
	 * "Yoruba"
	 */
	YO("Yoruba"),

	/**
	 * "Zhuang", "Chuang"
	 */
	ZA("Zhuang", "Chuang"),

	/**
	 * "Zulu"
	 */
	ZU("Zulu"),

	/**
	 * "Moldavian"
	 */
	MO("Moldavian"),

	/**
	 * "Serbo-Croatian"
	 */
	SH("Serbo-Croatian"),

	;

	/**
	 * 
	 * 
	 */
	ISO639_1(String... descriptions){
		this.descriptions = Collections.unmodifiableList(
			Arrays.asList(descriptions)
			);
	}

	/**
	 * 
	 * 
	 */
	public String getCode(){
		return name();
	}

	/**
	 * 
	 * 
	 */
	public String getDescription(){
		return descriptions.get(0);
	}

	/**
	 * 
	 * 
	 */
	public List<String> getDescriptions(){
		return descriptions;
	}

	/**
	 * 
	 * 
	 */
	public static Collection<ISO639_1> valuesCollection(){
		return values;
	}

	private List<String> descriptions;

	private static final Collection<ISO639_1> values;

	static{
		values = Collections.unmodifiableCollection(
			Arrays.asList(values())
			);
	}
}
