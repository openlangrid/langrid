/*
 * $Id: ISO3166.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 * @see <a href="http://www.iso.org/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/list-en1-semic.txt">ISO3166 code list</a>
 */
public enum ISO3166 implements CountryName{

	/**
	 * "AFGHANISTAN"
	 */
	AF("AFGHANISTAN"),

	/**
	 * "ÅLAND ISLANDS"
	 */
	AX("ÅLAND ISLANDS"),

	/**
	 * "ALBANIA"
	 */
	AL("ALBANIA"),

	/**
	 * "ALGERIA"
	 */
	DZ("ALGERIA"),

	/**
	 * "AMERICAN SAMOA"
	 */
	AS("AMERICAN SAMOA"),

	/**
	 * "ANDORRA"
	 */
	AD("ANDORRA"),

	/**
	 * "ANGOLA"
	 */
	AO("ANGOLA"),

	/**
	 * "ANGUILLA"
	 */
	AI("ANGUILLA"),

	/**
	 * "ANTARCTICA"
	 */
	AQ("ANTARCTICA"),

	/**
	 * "ANTIGUA AND BARBUDA"
	 */
	AG("ANTIGUA AND BARBUDA"),

	/**
	 * "ARGENTINA"
	 */
	AR("ARGENTINA"),

	/**
	 * "ARMENIA"
	 */
	AM("ARMENIA"),

	/**
	 * "ARUBA"
	 */
	AW("ARUBA"),

	/**
	 * "AUSTRALIA"
	 */
	AU("AUSTRALIA"),

	/**
	 * "AUSTRIA"
	 */
	AT("AUSTRIA"),

	/**
	 * "AZERBAIJAN"
	 */
	AZ("AZERBAIJAN"),

	/**
	 * "BAHAMAS"
	 */
	BS("BAHAMAS"),

	/**
	 * "BAHRAIN"
	 */
	BH("BAHRAIN"),

	/**
	 * "BANGLADESH"
	 */
	BD("BANGLADESH"),

	/**
	 * "BARBADOS"
	 */
	BB("BARBADOS"),

	/**
	 * "BELARUS"
	 */
	BY("BELARUS"),

	/**
	 * "BELGIUM"
	 */
	BE("BELGIUM"),

	/**
	 * "BELIZE"
	 */
	BZ("BELIZE"),

	/**
	 * "BENIN"
	 */
	BJ("BENIN"),

	/**
	 * "BERMUDA"
	 */
	BM("BERMUDA"),

	/**
	 * "BHUTAN"
	 */
	BT("BHUTAN"),

	/**
	 * "BOLIVIA"
	 */
	BO("BOLIVIA"),

	/**
	 * "BOSNIA AND HERZEGOVINA"
	 */
	BA("BOSNIA AND HERZEGOVINA"),

	/**
	 * "BOTSWANA"
	 */
	BW("BOTSWANA"),

	/**
	 * "BOUVET ISLAND"
	 */
	BV("BOUVET ISLAND"),

	/**
	 * "BRAZIL"
	 */
	BR("BRAZIL"),

	/**
	 * "BRITISH INDIAN OCEAN TERRITORY"
	 */
	IO("BRITISH INDIAN OCEAN TERRITORY"),

	/**
	 * "BRUNEI DARUSSALAM"
	 */
	BN("BRUNEI DARUSSALAM"),

	/**
	 * "BULGARIA"
	 */
	BG("BULGARIA"),

	/**
	 * "BURKINA FASO"
	 */
	BF("BURKINA FASO"),

	/**
	 * "BURUNDI"
	 */
	BI("BURUNDI"),

	/**
	 * "CAMBODIA"
	 */
	KH("CAMBODIA"),

	/**
	 * "CAMEROON"
	 */
	CM("CAMEROON"),

	/**
	 * "CANADA"
	 */
	CA("CANADA"),

	/**
	 * "CAPE VERDE"
	 */
	CV("CAPE VERDE"),

	/**
	 * "CAYMAN ISLANDS"
	 */
	KY("CAYMAN ISLANDS"),

	/**
	 * "CENTRAL AFRICAN REPUBLIC"
	 */
	CF("CENTRAL AFRICAN REPUBLIC"),

	/**
	 * "CHAD"
	 */
	TD("CHAD"),

	/**
	 * "CHILE"
	 */
	CL("CHILE"),

	/**
	 * "CHINA"
	 */
	CN("CHINA"),

	/**
	 * "CHRISTMAS ISLAND"
	 */
	CX("CHRISTMAS ISLAND"),

	/**
	 * "COCOS (KEELING) ISLANDS"
	 */
	CC("COCOS (KEELING) ISLANDS"),

	/**
	 * "COLOMBIA"
	 */
	CO("COLOMBIA"),

	/**
	 * "COMOROS"
	 */
	KM("COMOROS"),

	/**
	 * "CONGO"
	 */
	CG("CONGO"),

	/**
	 * "CONGO", " THE DEMOCRATIC REPUBLIC OF THE"
	 */
	CD("CONGO", " THE DEMOCRATIC REPUBLIC OF THE"),

	/**
	 * "COOK ISLANDS"
	 */
	CK("COOK ISLANDS"),

	/**
	 * "COSTA RICA"
	 */
	CR("COSTA RICA"),

	/**
	 * "CÔTE D'IVOIRE"
	 */
	CI("CÔTE D'IVOIRE"),

	/**
	 * "CROATIA"
	 */
	HR("CROATIA"),

	/**
	 * "CUBA"
	 */
	CU("CUBA"),

	/**
	 * "CYPRUS"
	 */
	CY("CYPRUS"),

	/**
	 * "CZECH REPUBLIC"
	 */
	CZ("CZECH REPUBLIC"),

	/**
	 * "DENMARK"
	 */
	DK("DENMARK"),

	/**
	 * "DJIBOUTI"
	 */
	DJ("DJIBOUTI"),

	/**
	 * "DOMINICA"
	 */
	DM("DOMINICA"),

	/**
	 * "DOMINICAN REPUBLIC"
	 */
	DO("DOMINICAN REPUBLIC"),

	/**
	 * "ECUADOR"
	 */
	EC("ECUADOR"),

	/**
	 * "EGYPT"
	 */
	EG("EGYPT"),

	/**
	 * "EL SALVADOR"
	 */
	SV("EL SALVADOR"),

	/**
	 * "EQUATORIAL GUINEA"
	 */
	GQ("EQUATORIAL GUINEA"),

	/**
	 * "ERITREA"
	 */
	ER("ERITREA"),

	/**
	 * "ESTONIA"
	 */
	EE("ESTONIA"),

	/**
	 * "ETHIOPIA"
	 */
	ET("ETHIOPIA"),

	/**
	 * "FALKLAND ISLANDS (MALVINAS)"
	 */
	FK("FALKLAND ISLANDS (MALVINAS)"),

	/**
	 * "FAROE ISLANDS"
	 */
	FO("FAROE ISLANDS"),

	/**
	 * "FIJI"
	 */
	FJ("FIJI"),

	/**
	 * "FINLAND"
	 */
	FI("FINLAND"),

	/**
	 * "FRANCE"
	 */
	FR("FRANCE"),

	/**
	 * "FRENCH GUIANA"
	 */
	GF("FRENCH GUIANA"),

	/**
	 * "FRENCH POLYNESIA"
	 */
	PF("FRENCH POLYNESIA"),

	/**
	 * "FRENCH SOUTHERN TERRITORIES"
	 */
	TF("FRENCH SOUTHERN TERRITORIES"),

	/**
	 * "GABON"
	 */
	GA("GABON"),

	/**
	 * "GAMBIA"
	 */
	GM("GAMBIA"),

	/**
	 * "GEORGIA"
	 */
	GE("GEORGIA"),

	/**
	 * "GERMANY"
	 */
	DE("GERMANY"),

	/**
	 * "GHANA"
	 */
	GH("GHANA"),

	/**
	 * "GIBRALTAR"
	 */
	GI("GIBRALTAR"),

	/**
	 * "GREECE"
	 */
	GR("GREECE"),

	/**
	 * "GREENLAND"
	 */
	GL("GREENLAND"),

	/**
	 * "GRENADA"
	 */
	GD("GRENADA"),

	/**
	 * "GUADELOUPE"
	 */
	GP("GUADELOUPE"),

	/**
	 * "GUAM"
	 */
	GU("GUAM"),

	/**
	 * "GUATEMALA"
	 */
	GT("GUATEMALA"),

	/**
	 * "GUERNSEY"
	 */
	GG("GUERNSEY"),

	/**
	 * "GUINEA"
	 */
	GN("GUINEA"),

	/**
	 * "GUINEA-BISSAU"
	 */
	GW("GUINEA-BISSAU"),

	/**
	 * "GUYANA"
	 */
	GY("GUYANA"),

	/**
	 * "HAITI"
	 */
	HT("HAITI"),

	/**
	 * "HEARD ISLAND AND MCDONALD ISLANDS"
	 */
	HM("HEARD ISLAND AND MCDONALD ISLANDS"),

	/**
	 * "HOLY SEE (VATICAN CITY STATE)"
	 */
	VA("HOLY SEE (VATICAN CITY STATE)"),

	/**
	 * "HONDURAS"
	 */
	HN("HONDURAS"),

	/**
	 * "HONG KONG"
	 */
	HK("HONG KONG"),

	/**
	 * "HUNGARY"
	 */
	HU("HUNGARY"),

	/**
	 * "ICELAND"
	 */
	IS("ICELAND"),

	/**
	 * "INDIA"
	 */
	IN("INDIA"),

	/**
	 * "INDONESIA"
	 */
	ID("INDONESIA"),

	/**
	 * "IRAN", " ISLAMIC REPUBLIC OF"
	 */
	IR("IRAN", " ISLAMIC REPUBLIC OF"),

	/**
	 * "IRAQ"
	 */
	IQ("IRAQ"),

	/**
	 * "IRELAND"
	 */
	IE("IRELAND"),

	/**
	 * "ISLE OF MAN"
	 */
	IM("ISLE OF MAN"),

	/**
	 * "ISRAEL"
	 */
	IL("ISRAEL"),

	/**
	 * "ITALY"
	 */
	IT("ITALY"),

	/**
	 * "JAMAICA"
	 */
	JM("JAMAICA"),

	/**
	 * "JAPAN"
	 */
	JP("JAPAN"),

	/**
	 * "JERSEY"
	 */
	JE("JERSEY"),

	/**
	 * "JORDAN"
	 */
	JO("JORDAN"),

	/**
	 * "KAZAKHSTAN"
	 */
	KZ("KAZAKHSTAN"),

	/**
	 * "KENYA"
	 */
	KE("KENYA"),

	/**
	 * "KIRIBATI"
	 */
	KI("KIRIBATI"),

	/**
	 * "KOREA", " DEMOCRATIC PEOPLE'S REPUBLIC OF"
	 */
	KP("KOREA", " DEMOCRATIC PEOPLE'S REPUBLIC OF"),

	/**
	 * "KOREA", " REPUBLIC OF"
	 */
	KR("KOREA", " REPUBLIC OF"),

	/**
	 * "KUWAIT"
	 */
	KW("KUWAIT"),

	/**
	 * "KYRGYZSTAN"
	 */
	KG("KYRGYZSTAN"),

	/**
	 * "LAO PEOPLE'S DEMOCRATIC REPUBLIC"
	 */
	LA("LAO PEOPLE'S DEMOCRATIC REPUBLIC"),

	/**
	 * "LATVIA"
	 */
	LV("LATVIA"),

	/**
	 * "LEBANON"
	 */
	LB("LEBANON"),

	/**
	 * "LESOTHO"
	 */
	LS("LESOTHO"),

	/**
	 * "LIBERIA"
	 */
	LR("LIBERIA"),

	/**
	 * "LIBYAN ARAB JAMAHIRIYA"
	 */
	LY("LIBYAN ARAB JAMAHIRIYA"),

	/**
	 * "LIECHTENSTEIN"
	 */
	LI("LIECHTENSTEIN"),

	/**
	 * "LITHUANIA"
	 */
	LT("LITHUANIA"),

	/**
	 * "LUXEMBOURG"
	 */
	LU("LUXEMBOURG"),

	/**
	 * "MACAO"
	 */
	MO("MACAO"),

	/**
	 * "MACEDONIA", " THE FORMER YUGOSLAV REPUBLIC OF"
	 */
	MK("MACEDONIA", " THE FORMER YUGOSLAV REPUBLIC OF"),

	/**
	 * "MADAGASCAR"
	 */
	MG("MADAGASCAR"),

	/**
	 * "MALAWI"
	 */
	MW("MALAWI"),

	/**
	 * "MALAYSIA"
	 */
	MY("MALAYSIA"),

	/**
	 * "MALDIVES"
	 */
	MV("MALDIVES"),

	/**
	 * "MALI"
	 */
	ML("MALI"),

	/**
	 * "MALTA"
	 */
	MT("MALTA"),

	/**
	 * "MARSHALL ISLANDS"
	 */
	MH("MARSHALL ISLANDS"),

	/**
	 * "MARTINIQUE"
	 */
	MQ("MARTINIQUE"),

	/**
	 * "MAURITANIA"
	 */
	MR("MAURITANIA"),

	/**
	 * "MAURITIUS"
	 */
	MU("MAURITIUS"),

	/**
	 * "MAYOTTE"
	 */
	YT("MAYOTTE"),

	/**
	 * "MEXICO"
	 */
	MX("MEXICO"),

	/**
	 * "MICRONESIA", " FEDERATED STATES OF"
	 */
	FM("MICRONESIA", " FEDERATED STATES OF"),

	/**
	 * "MOLDOVA", " REPUBLIC OF"
	 */
	MD("MOLDOVA", " REPUBLIC OF"),

	/**
	 * "MONACO"
	 */
	MC("MONACO"),

	/**
	 * "MONGOLIA"
	 */
	MN("MONGOLIA"),

	/**
	 * "MONTENEGRO"
	 */
	ME("MONTENEGRO"),

	/**
	 * "MONTSERRAT"
	 */
	MS("MONTSERRAT"),

	/**
	 * "MOROCCO"
	 */
	MA("MOROCCO"),

	/**
	 * "MOZAMBIQUE"
	 */
	MZ("MOZAMBIQUE"),

	/**
	 * "MYANMAR"
	 */
	MM("MYANMAR"),

	/**
	 * "NAMIBIA"
	 */
	NA("NAMIBIA"),

	/**
	 * "NAURU"
	 */
	NR("NAURU"),

	/**
	 * "NEPAL"
	 */
	NP("NEPAL"),

	/**
	 * "NETHERLANDS"
	 */
	NL("NETHERLANDS"),

	/**
	 * "NETHERLANDS ANTILLES"
	 */
	AN("NETHERLANDS ANTILLES"),

	/**
	 * "NEW CALEDONIA"
	 */
	NC("NEW CALEDONIA"),

	/**
	 * "NEW ZEALAND"
	 */
	NZ("NEW ZEALAND"),

	/**
	 * "NICARAGUA"
	 */
	NI("NICARAGUA"),

	/**
	 * "NIGER"
	 */
	NE("NIGER"),

	/**
	 * "NIGERIA"
	 */
	NG("NIGERIA"),

	/**
	 * "NIUE"
	 */
	NU("NIUE"),

	/**
	 * "NORFOLK ISLAND"
	 */
	NF("NORFOLK ISLAND"),

	/**
	 * "NORTHERN MARIANA ISLANDS"
	 */
	MP("NORTHERN MARIANA ISLANDS"),

	/**
	 * "NORWAY"
	 */
	NO("NORWAY"),

	/**
	 * "OMAN"
	 */
	OM("OMAN"),

	/**
	 * "PAKISTAN"
	 */
	PK("PAKISTAN"),

	/**
	 * "PALAU"
	 */
	PW("PALAU"),

	/**
	 * "PALESTINIAN TERRITORY", " OCCUPIED"
	 */
	PS("PALESTINIAN TERRITORY", " OCCUPIED"),

	/**
	 * "PANAMA"
	 */
	PA("PANAMA"),

	/**
	 * "PAPUA NEW GUINEA"
	 */
	PG("PAPUA NEW GUINEA"),

	/**
	 * "PARAGUAY"
	 */
	PY("PARAGUAY"),

	/**
	 * "PERU"
	 */
	PE("PERU"),

	/**
	 * "PHILIPPINES"
	 */
	PH("PHILIPPINES"),

	/**
	 * "PITCAIRN"
	 */
	PN("PITCAIRN"),

	/**
	 * "POLAND"
	 */
	PL("POLAND"),

	/**
	 * "PORTUGAL"
	 */
	PT("PORTUGAL"),

	/**
	 * "PUERTO RICO"
	 */
	PR("PUERTO RICO"),

	/**
	 * "QATAR"
	 */
	QA("QATAR"),

	/**
	 * "REUNION"
	 */
	RE("REUNION"),

	/**
	 * "ROMANIA"
	 */
	RO("ROMANIA"),

	/**
	 * "RUSSIAN FEDERATION"
	 */
	RU("RUSSIAN FEDERATION"),

	/**
	 * "RWANDA"
	 */
	RW("RWANDA"),

	/**
	 * "SAINT BARTHÉLEMY"
	 */
	BL("SAINT BARTHÉLEMY"),

	/**
	 * "SAINT HELENA"
	 */
	SH("SAINT HELENA"),

	/**
	 * "SAINT KITTS AND NEVIS"
	 */
	KN("SAINT KITTS AND NEVIS"),

	/**
	 * "SAINT LUCIA"
	 */
	LC("SAINT LUCIA"),

	/**
	 * "SAINT MARTIN"
	 */
	MF("SAINT MARTIN"),

	/**
	 * "SAINT PIERRE AND MIQUELON"
	 */
	PM("SAINT PIERRE AND MIQUELON"),

	/**
	 * "SAINT VINCENT AND THE GRENADINES"
	 */
	VC("SAINT VINCENT AND THE GRENADINES"),

	/**
	 * "SAMOA"
	 */
	WS("SAMOA"),

	/**
	 * "SAN MARINO"
	 */
	SM("SAN MARINO"),

	/**
	 * "SAO TOME AND PRINCIPE"
	 */
	ST("SAO TOME AND PRINCIPE"),

	/**
	 * "SAUDI ARABIA"
	 */
	SA("SAUDI ARABIA"),

	/**
	 * "SENEGAL"
	 */
	SN("SENEGAL"),

	/**
	 * "SERBIA"
	 */
	RS("SERBIA"),

	/**
	 * "SEYCHELLES"
	 */
	SC("SEYCHELLES"),

	/**
	 * "SIERRA LEONE"
	 */
	SL("SIERRA LEONE"),

	/**
	 * "SINGAPORE"
	 */
	SG("SINGAPORE"),

	/**
	 * "SLOVAKIA"
	 */
	SK("SLOVAKIA"),

	/**
	 * "SLOVENIA"
	 */
	SI("SLOVENIA"),

	/**
	 * "SOLOMON ISLANDS"
	 */
	SB("SOLOMON ISLANDS"),

	/**
	 * "SOMALIA"
	 */
	SO("SOMALIA"),

	/**
	 * "SOUTH AFRICA"
	 */
	ZA("SOUTH AFRICA"),

	/**
	 * "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"
	 */
	GS("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"),

	/**
	 * "SPAIN"
	 */
	ES("SPAIN"),

	/**
	 * "SRI LANKA"
	 */
	LK("SRI LANKA"),

	/**
	 * "SUDAN"
	 */
	SD("SUDAN"),

	/**
	 * "SURINAME"
	 */
	SR("SURINAME"),

	/**
	 * "SVALBARD AND JAN MAYEN"
	 */
	SJ("SVALBARD AND JAN MAYEN"),

	/**
	 * "SWAZILAND"
	 */
	SZ("SWAZILAND"),

	/**
	 * "SWEDEN"
	 */
	SE("SWEDEN"),

	/**
	 * "SWITZERLAND"
	 */
	CH("SWITZERLAND"),

	/**
	 * "SYRIAN ARAB REPUBLIC"
	 */
	SY("SYRIAN ARAB REPUBLIC"),

	/**
	 * "TAIWAN", " PROVINCE OF CHINA"
	 */
	TW("TAIWAN", " PROVINCE OF CHINA"),

	/**
	 * "TAJIKISTAN"
	 */
	TJ("TAJIKISTAN"),

	/**
	 * "TANZANIA", " UNITED REPUBLIC OF"
	 */
	TZ("TANZANIA", " UNITED REPUBLIC OF"),

	/**
	 * "THAILAND"
	 */
	TH("THAILAND"),

	/**
	 * "TIMOR-LESTE"
	 */
	TL("TIMOR-LESTE"),

	/**
	 * "TOGO"
	 */
	TG("TOGO"),

	/**
	 * "TOKELAU"
	 */
	TK("TOKELAU"),

	/**
	 * "TONGA"
	 */
	TO("TONGA"),

	/**
	 * "TRINIDAD AND TOBAGO"
	 */
	TT("TRINIDAD AND TOBAGO"),

	/**
	 * "TUNISIA"
	 */
	TN("TUNISIA"),

	/**
	 * "TURKEY"
	 */
	TR("TURKEY"),

	/**
	 * "TURKMENISTAN"
	 */
	TM("TURKMENISTAN"),

	/**
	 * "TURKS AND CAICOS ISLANDS"
	 */
	TC("TURKS AND CAICOS ISLANDS"),

	/**
	 * "TUVALU"
	 */
	TV("TUVALU"),

	/**
	 * "UGANDA"
	 */
	UG("UGANDA"),

	/**
	 * "UKRAINE"
	 */
	UA("UKRAINE"),

	/**
	 * "UNITED ARAB EMIRATES"
	 */
	AE("UNITED ARAB EMIRATES"),

	/**
	 * "UNITED KINGDOM"
	 */
	GB("UNITED KINGDOM"),

	/**
	 * "UNITED STATES"
	 */
	US("UNITED STATES"),

	/**
	 * "UNITED STATES MINOR OUTLYING ISLANDS"
	 */
	UM("UNITED STATES MINOR OUTLYING ISLANDS"),

	/**
	 * "URUGUAY"
	 */
	UY("URUGUAY"),

	/**
	 * "UZBEKISTAN"
	 */
	UZ("UZBEKISTAN"),

	/**
	 * "VANUATU"
	 */
	VU("VANUATU"),

	/**
	 * "VENEZUELA"
	 */
	VE("VENEZUELA"),

	/**
	 * "VIET NAM"
	 */
	VN("VIET NAM"),

	/**
	 * "VIRGIN ISLANDS", " BRITISH"
	 */
	VG("VIRGIN ISLANDS", " BRITISH"),

	/**
	 * "VIRGIN ISLANDS", " U.S."
	 */
	VI("VIRGIN ISLANDS", " U.S."),

	/**
	 * "WALLIS AND FUTUNA"
	 */
	WF("WALLIS AND FUTUNA"),

	/**
	 * "WESTERN SAHARA"
	 */
	EH("WESTERN SAHARA"),

	/**
	 * "YEMEN"
	 */
	YE("YEMEN"),

	/**
	 * "ZAMBIA"
	 */
	ZM("ZAMBIA"),

	/**
	 * "ZIMBABWE"
	 */
	ZW("ZIMBABWE"),

	;

	/**
	 * 
	 * 
	 */
	ISO3166(String... descriptions){
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
	public static Collection<ISO3166> valuesCollection(){
		return values;
	}

	private List<String> descriptions;

	private static final Collection<ISO3166> values;

	static{
		values = Collections.unmodifiableCollection(
			Arrays.asList(values())
			);
	}
}
