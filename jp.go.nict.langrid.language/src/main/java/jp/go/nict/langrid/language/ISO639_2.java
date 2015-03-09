/*
 * $Id: ISO639_2.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ISO639: 3 letter codes。
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 * @see <a href="http://www.loc.gov/standards/iso639-2/ISO-639-2_utf-8.txt">ISO639.2 code list</a>
 */
public enum ISO639_2 implements LanguageTag{

	/**
	 * "Afar", AAR, AA
	 */
	AAR("AAR", null, ISO639_1.AA, "Afar"),

	/**
	 * "Abkhazian", ABK, AB
	 */
	ABK("ABK", null, ISO639_1.AB, "Abkhazian"),

	/**
	 * "Achinese", ACE
	 */
	ACE("ACE", null, null, "Achinese"),

	/**
	 * "Acoli", ACH
	 */
	ACH("ACH", null, null, "Acoli"),

	/**
	 * "Adangme", ADA
	 */
	ADA("ADA", null, null, "Adangme"),

	/**
	 * "Adyghe", "Adygei", ADY
	 */
	ADY("ADY", null, null, "Adyghe", "Adygei"),

	/**
	 * "Afro-Asiatic (Other)", AFA
	 */
	AFA("AFA", null, null, "Afro-Asiatic (Other)"),

	/**
	 * "Afrihili", AFH
	 */
	AFH("AFH", null, null, "Afrihili"),

	/**
	 * "Afrikaans", AFR, AF
	 */
	AFR("AFR", null, ISO639_1.AF, "Afrikaans"),

	/**
	 * "Ainu", AIN
	 */
	AIN("AIN", null, null, "Ainu"),

	/**
	 * "Akan", AKA, AK
	 */
	AKA("AKA", null, ISO639_1.AK, "Akan"),

	/**
	 * "Akkadian", AKK
	 */
	AKK("AKK", null, null, "Akkadian"),

	/**
	 * "Albanian", ALB/SQI, SQ
	 */
	ALB("ALB", "SQI", ISO639_1.SQ, "Albanian"),

	/**
	 * "Albanian", ALB/SQI, SQ
	 */
	SQI("ALB", "SQI", ISO639_1.SQ, "Albanian"),

	/**
	 * "Aleut", ALE
	 */
	ALE("ALE", null, null, "Aleut"),

	/**
	 * "Algonquian languages", ALG
	 */
	ALG("ALG", null, null, "Algonquian languages"),

	/**
	 * "Southern Altai", ALT
	 */
	ALT("ALT", null, null, "Southern Altai"),

	/**
	 * "Amharic", AMH, AM
	 */
	AMH("AMH", null, ISO639_1.AM, "Amharic"),

	/**
	 * "English, Old (ca.450-1100)", ANG
	 */
	ANG("ANG", null, null, "English, Old (ca.450-1100)"),

	/**
	 * "Angika", ANP
	 */
	ANP("ANP", null, null, "Angika"),

	/**
	 * "Apache languages", APA
	 */
	APA("APA", null, null, "Apache languages"),

	/**
	 * "Arabic", ARA, AR
	 */
	ARA("ARA", null, ISO639_1.AR, "Arabic"),

	/**
	 * "Official Aramaic (700-300 BCE)", "Imperial Aramaic (700-300 BCE)", ARC
	 */
	ARC("ARC", null, null, "Official Aramaic (700-300 BCE)", "Imperial Aramaic (700-300 BCE)"),

	/**
	 * "Aragonese", ARG, AN
	 */
	ARG("ARG", null, ISO639_1.AN, "Aragonese"),

	/**
	 * "Armenian", ARM/HYE, HY
	 */
	ARM("ARM", "HYE", ISO639_1.HY, "Armenian"),

	/**
	 * "Armenian", ARM/HYE, HY
	 */
	HYE("ARM", "HYE", ISO639_1.HY, "Armenian"),

	/**
	 * "Mapudungun", "Mapuche", ARN
	 */
	ARN("ARN", null, null, "Mapudungun", "Mapuche"),

	/**
	 * "Arapaho", ARP
	 */
	ARP("ARP", null, null, "Arapaho"),

	/**
	 * "Artificial (Other)", ART
	 */
	ART("ART", null, null, "Artificial (Other)"),

	/**
	 * "Arawak", ARW
	 */
	ARW("ARW", null, null, "Arawak"),

	/**
	 * "Assamese", ASM, AS
	 */
	ASM("ASM", null, ISO639_1.AS, "Assamese"),

	/**
	 * "Asturian", "Bable", "Leonese", "Asturleonese", AST
	 */
	AST("AST", null, null, "Asturian", "Bable", "Leonese", "Asturleonese"),

	/**
	 * "Athapascan languages", ATH
	 */
	ATH("ATH", null, null, "Athapascan languages"),

	/**
	 * "Australian languages", AUS
	 */
	AUS("AUS", null, null, "Australian languages"),

	/**
	 * "Avaric", AVA, AV
	 */
	AVA("AVA", null, ISO639_1.AV, "Avaric"),

	/**
	 * "Avestan", AVE, AE
	 */
	AVE("AVE", null, ISO639_1.AE, "Avestan"),

	/**
	 * "Awadhi", AWA
	 */
	AWA("AWA", null, null, "Awadhi"),

	/**
	 * "Aymara", AYM, AY
	 */
	AYM("AYM", null, ISO639_1.AY, "Aymara"),

	/**
	 * "Azerbaijani", AZE, AZ
	 */
	AZE("AZE", null, ISO639_1.AZ, "Azerbaijani"),

	/**
	 * "Banda languages", BAD
	 */
	BAD("BAD", null, null, "Banda languages"),

	/**
	 * "Bamileke languages", BAI
	 */
	BAI("BAI", null, null, "Bamileke languages"),

	/**
	 * "Bashkir", BAK, BA
	 */
	BAK("BAK", null, ISO639_1.BA, "Bashkir"),

	/**
	 * "Baluchi", BAL
	 */
	BAL("BAL", null, null, "Baluchi"),

	/**
	 * "Bambara", BAM, BM
	 */
	BAM("BAM", null, ISO639_1.BM, "Bambara"),

	/**
	 * "Balinese", BAN
	 */
	BAN("BAN", null, null, "Balinese"),

	/**
	 * "Basque", BAQ/EUS, EU
	 */
	BAQ("BAQ", "EUS", ISO639_1.EU, "Basque"),

	/**
	 * "Basque", BAQ/EUS, EU
	 */
	EUS("BAQ", "EUS", ISO639_1.EU, "Basque"),

	/**
	 * "Basa", BAS
	 */
	BAS("BAS", null, null, "Basa"),

	/**
	 * "Baltic (Other)", BAT
	 */
	BAT("BAT", null, null, "Baltic (Other)"),

	/**
	 * "Beja", "Bedawiyet", BEJ
	 */
	BEJ("BEJ", null, null, "Beja", "Bedawiyet"),

	/**
	 * "Belarusian", BEL, BE
	 */
	BEL("BEL", null, ISO639_1.BE, "Belarusian"),

	/**
	 * "Bemba", BEM
	 */
	BEM("BEM", null, null, "Bemba"),

	/**
	 * "Bengali", BEN, BN
	 */
	BEN("BEN", null, ISO639_1.BN, "Bengali"),

	/**
	 * "Berber (Other)", BER
	 */
	BER("BER", null, null, "Berber (Other)"),

	/**
	 * "Bhojpuri", BHO
	 */
	BHO("BHO", null, null, "Bhojpuri"),

	/**
	 * "Bihari", BIH, BH
	 */
	BIH("BIH", null, ISO639_1.BH, "Bihari"),

	/**
	 * "Bikol", BIK
	 */
	BIK("BIK", null, null, "Bikol"),

	/**
	 * "Bini", "Edo", BIN
	 */
	BIN("BIN", null, null, "Bini", "Edo"),

	/**
	 * "Bislama", BIS, BI
	 */
	BIS("BIS", null, ISO639_1.BI, "Bislama"),

	/**
	 * "Siksika", BLA
	 */
	BLA("BLA", null, null, "Siksika"),

	/**
	 * "Bantu (Other)", BNT
	 */
	BNT("BNT", null, null, "Bantu (Other)"),

	/**
	 * "Bosnian", BOS, BS
	 */
	BOS("BOS", null, ISO639_1.BS, "Bosnian"),

	/**
	 * "Braj", BRA
	 */
	BRA("BRA", null, null, "Braj"),

	/**
	 * "Breton", BRE, BR
	 */
	BRE("BRE", null, ISO639_1.BR, "Breton"),

	/**
	 * "Batak languages", BTK
	 */
	BTK("BTK", null, null, "Batak languages"),

	/**
	 * "Buriat", BUA
	 */
	BUA("BUA", null, null, "Buriat"),

	/**
	 * "Buginese", BUG
	 */
	BUG("BUG", null, null, "Buginese"),

	/**
	 * "Bulgarian", BUL, BG
	 */
	BUL("BUL", null, ISO639_1.BG, "Bulgarian"),

	/**
	 * "Burmese", BUR/MYA, MY
	 */
	BUR("BUR", "MYA", ISO639_1.MY, "Burmese"),

	/**
	 * "Burmese", BUR/MYA, MY
	 */
	MYA("BUR", "MYA", ISO639_1.MY, "Burmese"),

	/**
	 * "Blin", "Bilin", BYN
	 */
	BYN("BYN", null, null, "Blin", "Bilin"),

	/**
	 * "Caddo", CAD
	 */
	CAD("CAD", null, null, "Caddo"),

	/**
	 * "Central American Indian (Other)", CAI
	 */
	CAI("CAI", null, null, "Central American Indian (Other)"),

	/**
	 * "Galibi Carib", CAR
	 */
	CAR("CAR", null, null, "Galibi Carib"),

	/**
	 * "Catalan", "Valencian", CAT, CA
	 */
	CAT("CAT", null, ISO639_1.CA, "Catalan", "Valencian"),

	/**
	 * "Caucasian (Other)", CAU
	 */
	CAU("CAU", null, null, "Caucasian (Other)"),

	/**
	 * "Cebuano", CEB
	 */
	CEB("CEB", null, null, "Cebuano"),

	/**
	 * "Celtic (Other)", CEL
	 */
	CEL("CEL", null, null, "Celtic (Other)"),

	/**
	 * "Chamorro", CHA, CH
	 */
	CHA("CHA", null, ISO639_1.CH, "Chamorro"),

	/**
	 * "Chibcha", CHB
	 */
	CHB("CHB", null, null, "Chibcha"),

	/**
	 * "Chechen", CHE, CE
	 */
	CHE("CHE", null, ISO639_1.CE, "Chechen"),

	/**
	 * "Chagatai", CHG
	 */
	CHG("CHG", null, null, "Chagatai"),

	/**
	 * "Chinese", CHI/ZHO, ZH
	 */
	CHI("CHI", "ZHO", ISO639_1.ZH, "Chinese"),

	/**
	 * "Chinese", CHI/ZHO, ZH
	 */
	ZHO("CHI", "ZHO", ISO639_1.ZH, "Chinese"),

	/**
	 * "Chuukese", CHK
	 */
	CHK("CHK", null, null, "Chuukese"),

	/**
	 * "Mari", CHM
	 */
	CHM("CHM", null, null, "Mari"),

	/**
	 * "Chinook jargon", CHN
	 */
	CHN("CHN", null, null, "Chinook jargon"),

	/**
	 * "Choctaw", CHO
	 */
	CHO("CHO", null, null, "Choctaw"),

	/**
	 * "Chipewyan", "Dene Suline", CHP
	 */
	CHP("CHP", null, null, "Chipewyan", "Dene Suline"),

	/**
	 * "Cherokee", CHR
	 */
	CHR("CHR", null, null, "Cherokee"),

	/**
	 * "Church Slavic", "Old Slavonic", "Church Slavonic", "Old Bulgarian", "Old Church Slavonic", CHU, CU
	 */
	CHU("CHU", null, ISO639_1.CU, "Church Slavic", "Old Slavonic", "Church Slavonic", "Old Bulgarian", "Old Church Slavonic"),

	/**
	 * "Chuvash", CHV, CV
	 */
	CHV("CHV", null, ISO639_1.CV, "Chuvash"),

	/**
	 * "Cheyenne", CHY
	 */
	CHY("CHY", null, null, "Cheyenne"),

	/**
	 * "Chamic languages", CMC
	 */
	CMC("CMC", null, null, "Chamic languages"),

	/**
	 * "Coptic", COP
	 */
	COP("COP", null, null, "Coptic"),

	/**
	 * "Cornish", COR, KW
	 */
	COR("COR", null, ISO639_1.KW, "Cornish"),

	/**
	 * "Corsican", COS, CO
	 */
	COS("COS", null, ISO639_1.CO, "Corsican"),

	/**
	 * "Creoles and pidgins, English based (Other)", CPE
	 */
	CPE("CPE", null, null, "Creoles and pidgins, English based (Other)"),

	/**
	 * "Creoles and pidgins, French-based (Other)", CPF
	 */
	CPF("CPF", null, null, "Creoles and pidgins, French-based (Other)"),

	/**
	 * "Creoles and pidgins, Portuguese-based (Other)", CPP
	 */
	CPP("CPP", null, null, "Creoles and pidgins, Portuguese-based (Other)"),

	/**
	 * "Cree", CRE, CR
	 */
	CRE("CRE", null, ISO639_1.CR, "Cree"),

	/**
	 * "Crimean Tatar", "Crimean Turkish", CRH
	 */
	CRH("CRH", null, null, "Crimean Tatar", "Crimean Turkish"),

	/**
	 * "Creoles and pidgins (Other)", CRP
	 */
	CRP("CRP", null, null, "Creoles and pidgins (Other)"),

	/**
	 * "Kashubian", CSB
	 */
	CSB("CSB", null, null, "Kashubian"),

	/**
	 * "Cushitic (Other)", CUS
	 */
	CUS("CUS", null, null, "Cushitic (Other)"),

	/**
	 * "Czech", CZE/CES, CS
	 */
	CZE("CZE", "CES", ISO639_1.CS, "Czech"),

	/**
	 * "Czech", CZE/CES, CS
	 */
	CES("CZE", "CES", ISO639_1.CS, "Czech"),

	/**
	 * "Dakota", DAK
	 */
	DAK("DAK", null, null, "Dakota"),

	/**
	 * "Danish", DAN, DA
	 */
	DAN("DAN", null, ISO639_1.DA, "Danish"),

	/**
	 * "Dargwa", DAR
	 */
	DAR("DAR", null, null, "Dargwa"),

	/**
	 * "Land Dayak languages", DAY
	 */
	DAY("DAY", null, null, "Land Dayak languages"),

	/**
	 * "Delaware", DEL
	 */
	DEL("DEL", null, null, "Delaware"),

	/**
	 * "Slave (Athapascan)", DEN
	 */
	DEN("DEN", null, null, "Slave (Athapascan)"),

	/**
	 * "Dogrib", DGR
	 */
	DGR("DGR", null, null, "Dogrib"),

	/**
	 * "Dinka", DIN
	 */
	DIN("DIN", null, null, "Dinka"),

	/**
	 * "Divehi", "Dhivehi", "Maldivian", DIV, DV
	 */
	DIV("DIV", null, ISO639_1.DV, "Divehi", "Dhivehi", "Maldivian"),

	/**
	 * "Dogri", DOI
	 */
	DOI("DOI", null, null, "Dogri"),

	/**
	 * "Dravidian (Other)", DRA
	 */
	DRA("DRA", null, null, "Dravidian (Other)"),

	/**
	 * "Lower Sorbian", DSB
	 */
	DSB("DSB", null, null, "Lower Sorbian"),

	/**
	 * "Duala", DUA
	 */
	DUA("DUA", null, null, "Duala"),

	/**
	 * "Dutch, Middle (ca.1050-1350)", DUM
	 */
	DUM("DUM", null, null, "Dutch, Middle (ca.1050-1350)"),

	/**
	 * "Dutch", "Flemish", DUT/NLD, NL
	 */
	DUT("DUT", "NLD", ISO639_1.NL, "Dutch", "Flemish"),

	/**
	 * "Dutch", "Flemish", DUT/NLD, NL
	 */
	NLD("DUT", "NLD", ISO639_1.NL, "Dutch", "Flemish"),

	/**
	 * "Dyula", DYU
	 */
	DYU("DYU", null, null, "Dyula"),

	/**
	 * "Dzongkha", DZO, DZ
	 */
	DZO("DZO", null, ISO639_1.DZ, "Dzongkha"),

	/**
	 * "Efik", EFI
	 */
	EFI("EFI", null, null, "Efik"),

	/**
	 * "Egyptian (Ancient)", EGY
	 */
	EGY("EGY", null, null, "Egyptian (Ancient)"),

	/**
	 * "Ekajuk", EKA
	 */
	EKA("EKA", null, null, "Ekajuk"),

	/**
	 * "Elamite", ELX
	 */
	ELX("ELX", null, null, "Elamite"),

	/**
	 * "English", ENG, EN
	 */
	ENG("ENG", null, ISO639_1.EN, "English"),

	/**
	 * "English, Middle (1100-1500)", ENM
	 */
	ENM("ENM", null, null, "English, Middle (1100-1500)"),

	/**
	 * "Esperanto", EPO, EO
	 */
	EPO("EPO", null, ISO639_1.EO, "Esperanto"),

	/**
	 * "Estonian", EST, ET
	 */
	EST("EST", null, ISO639_1.ET, "Estonian"),

	/**
	 * "Ewe", EWE, EE
	 */
	EWE("EWE", null, ISO639_1.EE, "Ewe"),

	/**
	 * "Ewondo", EWO
	 */
	EWO("EWO", null, null, "Ewondo"),

	/**
	 * "Fang", FAN
	 */
	FAN("FAN", null, null, "Fang"),

	/**
	 * "Faroese", FAO, FO
	 */
	FAO("FAO", null, ISO639_1.FO, "Faroese"),

	/**
	 * "Fanti", FAT
	 */
	FAT("FAT", null, null, "Fanti"),

	/**
	 * "Fijian", FIJ, FJ
	 */
	FIJ("FIJ", null, ISO639_1.FJ, "Fijian"),

	/**
	 * "Filipino", "Pilipino", FIL
	 */
	FIL("FIL", null, null, "Filipino", "Pilipino"),

	/**
	 * "Finnish", FIN, FI
	 */
	FIN("FIN", null, ISO639_1.FI, "Finnish"),

	/**
	 * "Finno-Ugrian (Other)", FIU
	 */
	FIU("FIU", null, null, "Finno-Ugrian (Other)"),

	/**
	 * "Fon", FON
	 */
	FON("FON", null, null, "Fon"),

	/**
	 * "French", FRE/FRA, FR
	 */
	FRE("FRE", "FRA", ISO639_1.FR, "French"),

	/**
	 * "French", FRE/FRA, FR
	 */
	FRA("FRE", "FRA", ISO639_1.FR, "French"),

	/**
	 * "French, Middle (ca.1400-1600)", FRM
	 */
	FRM("FRM", null, null, "French, Middle (ca.1400-1600)"),

	/**
	 * "French, Old (842-ca.1400)", FRO
	 */
	FRO("FRO", null, null, "French, Old (842-ca.1400)"),

	/**
	 * "Northern Frisian", FRR
	 */
	FRR("FRR", null, null, "Northern Frisian"),

	/**
	 * "Eastern Frisian", FRS
	 */
	FRS("FRS", null, null, "Eastern Frisian"),

	/**
	 * "Western Frisian", FRY, FY
	 */
	FRY("FRY", null, ISO639_1.FY, "Western Frisian"),

	/**
	 * "Fulah", FUL, FF
	 */
	FUL("FUL", null, ISO639_1.FF, "Fulah"),

	/**
	 * "Friulian", FUR
	 */
	FUR("FUR", null, null, "Friulian"),

	/**
	 * "Ga", GAA
	 */
	GAA("GAA", null, null, "Ga"),

	/**
	 * "Gayo", GAY
	 */
	GAY("GAY", null, null, "Gayo"),

	/**
	 * "Gbaya", GBA
	 */
	GBA("GBA", null, null, "Gbaya"),

	/**
	 * "Germanic (Other)", GEM
	 */
	GEM("GEM", null, null, "Germanic (Other)"),

	/**
	 * "Georgian", GEO/KAT, KA
	 */
	GEO("GEO", "KAT", ISO639_1.KA, "Georgian"),

	/**
	 * "Georgian", GEO/KAT, KA
	 */
	KAT("GEO", "KAT", ISO639_1.KA, "Georgian"),

	/**
	 * "German", GER/DEU, DE
	 */
	GER("GER", "DEU", ISO639_1.DE, "German"),

	/**
	 * "German", GER/DEU, DE
	 */
	DEU("GER", "DEU", ISO639_1.DE, "German"),

	/**
	 * "Geez", GEZ
	 */
	GEZ("GEZ", null, null, "Geez"),

	/**
	 * "Gilbertese", GIL
	 */
	GIL("GIL", null, null, "Gilbertese"),

	/**
	 * "Gaelic", "Scottish Gaelic", GLA, GD
	 */
	GLA("GLA", null, ISO639_1.GD, "Gaelic", "Scottish Gaelic"),

	/**
	 * "Irish", GLE, GA
	 */
	GLE("GLE", null, ISO639_1.GA, "Irish"),

	/**
	 * "Galician", GLG, GL
	 */
	GLG("GLG", null, ISO639_1.GL, "Galician"),

	/**
	 * "Manx", GLV, GV
	 */
	GLV("GLV", null, ISO639_1.GV, "Manx"),

	/**
	 * "German, Middle High (ca.1050-1500)", GMH
	 */
	GMH("GMH", null, null, "German, Middle High (ca.1050-1500)"),

	/**
	 * "German, Old High (ca.750-1050)", GOH
	 */
	GOH("GOH", null, null, "German, Old High (ca.750-1050)"),

	/**
	 * "Gondi", GON
	 */
	GON("GON", null, null, "Gondi"),

	/**
	 * "Gorontalo", GOR
	 */
	GOR("GOR", null, null, "Gorontalo"),

	/**
	 * "Gothic", GOT
	 */
	GOT("GOT", null, null, "Gothic"),

	/**
	 * "Grebo", GRB
	 */
	GRB("GRB", null, null, "Grebo"),

	/**
	 * "Greek, Ancient (to 1453)", GRC
	 */
	GRC("GRC", null, null, "Greek, Ancient (to 1453)"),

	/**
	 * "Greek, Modern (1453-)", GRE/ELL, EL
	 */
	GRE("GRE", "ELL", ISO639_1.EL, "Greek, Modern (1453-)"),

	/**
	 * "Greek, Modern (1453-)", GRE/ELL, EL
	 */
	ELL("GRE", "ELL", ISO639_1.EL, "Greek, Modern (1453-)"),

	/**
	 * "Guarani", GRN, GN
	 */
	GRN("GRN", null, ISO639_1.GN, "Guarani"),

	/**
	 * "Swiss German", "Alemannic", "Alsatian", GSW
	 */
	GSW("GSW", null, null, "Swiss German", "Alemannic", "Alsatian"),

	/**
	 * "Gujarati", GUJ, GU
	 */
	GUJ("GUJ", null, ISO639_1.GU, "Gujarati"),

	/**
	 * "Gwich'in", GWI
	 */
	GWI("GWI", null, null, "Gwich'in"),

	/**
	 * "Haida", HAI
	 */
	HAI("HAI", null, null, "Haida"),

	/**
	 * "Haitian", "Haitian Creole", HAT, HT
	 */
	HAT("HAT", null, ISO639_1.HT, "Haitian", "Haitian Creole"),

	/**
	 * "Hausa", HAU, HA
	 */
	HAU("HAU", null, ISO639_1.HA, "Hausa"),

	/**
	 * "Hawaiian", HAW
	 */
	HAW("HAW", null, null, "Hawaiian"),

	/**
	 * "Hebrew", HEB, HE
	 */
	HEB("HEB", null, ISO639_1.HE, "Hebrew"),

	/**
	 * "Herero", HER, HZ
	 */
	HER("HER", null, ISO639_1.HZ, "Herero"),

	/**
	 * "Hiligaynon", HIL
	 */
	HIL("HIL", null, null, "Hiligaynon"),

	/**
	 * "Himachali", HIM
	 */
	HIM("HIM", null, null, "Himachali"),

	/**
	 * "Hindi", HIN, HI
	 */
	HIN("HIN", null, ISO639_1.HI, "Hindi"),

	/**
	 * "Hittite", HIT
	 */
	HIT("HIT", null, null, "Hittite"),

	/**
	 * "Hmong", HMN
	 */
	HMN("HMN", null, null, "Hmong"),

	/**
	 * "Hiri Motu", HMO, HO
	 */
	HMO("HMO", null, ISO639_1.HO, "Hiri Motu"),

	/**
	 * "Croatian", HRV, HR
	 */
	HRV("HRV", null, ISO639_1.HR, "Croatian"),

	/**
	 * "Upper Sorbian", HSB
	 */
	HSB("HSB", null, null, "Upper Sorbian"),

	/**
	 * "Hungarian", HUN, HU
	 */
	HUN("HUN", null, ISO639_1.HU, "Hungarian"),

	/**
	 * "Hupa", HUP
	 */
	HUP("HUP", null, null, "Hupa"),

	/**
	 * "Iban", IBA
	 */
	IBA("IBA", null, null, "Iban"),

	/**
	 * "Igbo", IBO, IG
	 */
	IBO("IBO", null, ISO639_1.IG, "Igbo"),

	/**
	 * "Icelandic", ICE/ISL, IS
	 */
	ICE("ICE", "ISL", ISO639_1.IS, "Icelandic"),

	/**
	 * "Icelandic", ICE/ISL, IS
	 */
	ISL("ICE", "ISL", ISO639_1.IS, "Icelandic"),

	/**
	 * "Ido", IDO, IO
	 */
	IDO("IDO", null, ISO639_1.IO, "Ido"),

	/**
	 * "Sichuan Yi", "Nuosu", III, II
	 */
	III("III", null, ISO639_1.II, "Sichuan Yi", "Nuosu"),

	/**
	 * "Ijo languages", IJO
	 */
	IJO("IJO", null, null, "Ijo languages"),

	/**
	 * "Inuktitut", IKU, IU
	 */
	IKU("IKU", null, ISO639_1.IU, "Inuktitut"),

	/**
	 * "Interlingue", "Occidental", ILE, IE
	 */
	ILE("ILE", null, ISO639_1.IE, "Interlingue", "Occidental"),

	/**
	 * "Iloko", ILO
	 */
	ILO("ILO", null, null, "Iloko"),

	/**
	 * "Interlingua (International Auxiliary Language Association)", INA, IA
	 */
	INA("INA", null, ISO639_1.IA, "Interlingua (International Auxiliary Language Association)"),

	/**
	 * "Indic (Other)", INC
	 */
	INC("INC", null, null, "Indic (Other)"),

	/**
	 * "Indonesian", IND, ID
	 */
	IND("IND", null, ISO639_1.ID, "Indonesian"),

	/**
	 * "Indo-European (Other)", INE
	 */
	INE("INE", null, null, "Indo-European (Other)"),

	/**
	 * "Ingush", INH
	 */
	INH("INH", null, null, "Ingush"),

	/**
	 * "Inupiaq", IPK, IK
	 */
	IPK("IPK", null, ISO639_1.IK, "Inupiaq"),

	/**
	 * "Iranian (Other)", IRA
	 */
	IRA("IRA", null, null, "Iranian (Other)"),

	/**
	 * "Iroquoian languages", IRO
	 */
	IRO("IRO", null, null, "Iroquoian languages"),

	/**
	 * "Italian", ITA, IT
	 */
	ITA("ITA", null, ISO639_1.IT, "Italian"),

	/**
	 * "Javanese", JAV, JV
	 */
	JAV("JAV", null, ISO639_1.JV, "Javanese"),

	/**
	 * "Lojban", JBO
	 */
	JBO("JBO", null, null, "Lojban"),

	/**
	 * "Japanese", JPN, JA
	 */
	JPN("JPN", null, ISO639_1.JA, "Japanese"),

	/**
	 * "Judeo-Persian", JPR
	 */
	JPR("JPR", null, null, "Judeo-Persian"),

	/**
	 * "Judeo-Arabic", JRB
	 */
	JRB("JRB", null, null, "Judeo-Arabic"),

	/**
	 * "Kara-Kalpak", KAA
	 */
	KAA("KAA", null, null, "Kara-Kalpak"),

	/**
	 * "Kabyle", KAB
	 */
	KAB("KAB", null, null, "Kabyle"),

	/**
	 * "Kachin", "Jingpho", KAC
	 */
	KAC("KAC", null, null, "Kachin", "Jingpho"),

	/**
	 * "Kalaallisut", "Greenlandic", KAL, KL
	 */
	KAL("KAL", null, ISO639_1.KL, "Kalaallisut", "Greenlandic"),

	/**
	 * "Kamba", KAM
	 */
	KAM("KAM", null, null, "Kamba"),

	/**
	 * "Kannada", KAN, KN
	 */
	KAN("KAN", null, ISO639_1.KN, "Kannada"),

	/**
	 * "Karen languages", KAR
	 */
	KAR("KAR", null, null, "Karen languages"),

	/**
	 * "Kashmiri", KAS, KS
	 */
	KAS("KAS", null, ISO639_1.KS, "Kashmiri"),

	/**
	 * "Kanuri", KAU, KR
	 */
	KAU("KAU", null, ISO639_1.KR, "Kanuri"),

	/**
	 * "Kawi", KAW
	 */
	KAW("KAW", null, null, "Kawi"),

	/**
	 * "Kazakh", KAZ, KK
	 */
	KAZ("KAZ", null, ISO639_1.KK, "Kazakh"),

	/**
	 * "Kabardian", KBD
	 */
	KBD("KBD", null, null, "Kabardian"),

	/**
	 * "Khasi", KHA
	 */
	KHA("KHA", null, null, "Khasi"),

	/**
	 * "Khoisan (Other)", KHI
	 */
	KHI("KHI", null, null, "Khoisan (Other)"),

	/**
	 * "Central Khmer", KHM, KM
	 */
	KHM("KHM", null, ISO639_1.KM, "Central Khmer"),

	/**
	 * "Khotanese", "Sakan", KHO
	 */
	KHO("KHO", null, null, "Khotanese", "Sakan"),

	/**
	 * "Kikuyu", "Gikuyu", KIK, KI
	 */
	KIK("KIK", null, ISO639_1.KI, "Kikuyu", "Gikuyu"),

	/**
	 * "Kinyarwanda", KIN, RW
	 */
	KIN("KIN", null, ISO639_1.RW, "Kinyarwanda"),

	/**
	 * "Kirghiz", "Kyrgyz", KIR, KY
	 */
	KIR("KIR", null, ISO639_1.KY, "Kirghiz", "Kyrgyz"),

	/**
	 * "Kimbundu", KMB
	 */
	KMB("KMB", null, null, "Kimbundu"),

	/**
	 * "Konkani", KOK
	 */
	KOK("KOK", null, null, "Konkani"),

	/**
	 * "Komi", KOM, KV
	 */
	KOM("KOM", null, ISO639_1.KV, "Komi"),

	/**
	 * "Kongo", KON, KG
	 */
	KON("KON", null, ISO639_1.KG, "Kongo"),

	/**
	 * "Korean", KOR, KO
	 */
	KOR("KOR", null, ISO639_1.KO, "Korean"),

	/**
	 * "Kosraean", KOS
	 */
	KOS("KOS", null, null, "Kosraean"),

	/**
	 * "Kpelle", KPE
	 */
	KPE("KPE", null, null, "Kpelle"),

	/**
	 * "Karachay-Balkar", KRC
	 */
	KRC("KRC", null, null, "Karachay-Balkar"),

	/**
	 * "Karelian", KRL
	 */
	KRL("KRL", null, null, "Karelian"),

	/**
	 * "Kru languages", KRO
	 */
	KRO("KRO", null, null, "Kru languages"),

	/**
	 * "Kurukh", KRU
	 */
	KRU("KRU", null, null, "Kurukh"),

	/**
	 * "Kuanyama", "Kwanyama", KUA, KJ
	 */
	KUA("KUA", null, ISO639_1.KJ, "Kuanyama", "Kwanyama"),

	/**
	 * "Kumyk", KUM
	 */
	KUM("KUM", null, null, "Kumyk"),

	/**
	 * "Kurdish", KUR, KU
	 */
	KUR("KUR", null, ISO639_1.KU, "Kurdish"),

	/**
	 * "Kutenai", KUT
	 */
	KUT("KUT", null, null, "Kutenai"),

	/**
	 * "Ladino", LAD
	 */
	LAD("LAD", null, null, "Ladino"),

	/**
	 * "Lahnda", LAH
	 */
	LAH("LAH", null, null, "Lahnda"),

	/**
	 * "Lamba", LAM
	 */
	LAM("LAM", null, null, "Lamba"),

	/**
	 * "Lao", LAO, LO
	 */
	LAO("LAO", null, ISO639_1.LO, "Lao"),

	/**
	 * "Latin", LAT, LA
	 */
	LAT("LAT", null, ISO639_1.LA, "Latin"),

	/**
	 * "Latvian", LAV, LV
	 */
	LAV("LAV", null, ISO639_1.LV, "Latvian"),

	/**
	 * "Lezghian", LEZ
	 */
	LEZ("LEZ", null, null, "Lezghian"),

	/**
	 * "Limburgan", "Limburger", "Limburgish", LIM, LI
	 */
	LIM("LIM", null, ISO639_1.LI, "Limburgan", "Limburger", "Limburgish"),

	/**
	 * "Lingala", LIN, LN
	 */
	LIN("LIN", null, ISO639_1.LN, "Lingala"),

	/**
	 * "Lithuanian", LIT, LT
	 */
	LIT("LIT", null, ISO639_1.LT, "Lithuanian"),

	/**
	 * "Mongo", LOL
	 */
	LOL("LOL", null, null, "Mongo"),

	/**
	 * "Lozi", LOZ
	 */
	LOZ("LOZ", null, null, "Lozi"),

	/**
	 * "Luxembourgish", "Letzeburgesch", LTZ, LB
	 */
	LTZ("LTZ", null, ISO639_1.LB, "Luxembourgish", "Letzeburgesch"),

	/**
	 * "Luba-Lulua", LUA
	 */
	LUA("LUA", null, null, "Luba-Lulua"),

	/**
	 * "Luba-Katanga", LUB, LU
	 */
	LUB("LUB", null, ISO639_1.LU, "Luba-Katanga"),

	/**
	 * "Ganda", LUG, LG
	 */
	LUG("LUG", null, ISO639_1.LG, "Ganda"),

	/**
	 * "Luiseno", LUI
	 */
	LUI("LUI", null, null, "Luiseno"),

	/**
	 * "Lunda", LUN
	 */
	LUN("LUN", null, null, "Lunda"),

	/**
	 * "Luo (Kenya and Tanzania)", LUO
	 */
	LUO("LUO", null, null, "Luo (Kenya and Tanzania)"),

	/**
	 * "Lushai", LUS
	 */
	LUS("LUS", null, null, "Lushai"),

	/**
	 * "Macedonian", MAC/MKD, MK
	 */
	MAC("MAC", "MKD", ISO639_1.MK, "Macedonian"),

	/**
	 * "Macedonian", MAC/MKD, MK
	 */
	MKD("MAC", "MKD", ISO639_1.MK, "Macedonian"),

	/**
	 * "Madurese", MAD
	 */
	MAD("MAD", null, null, "Madurese"),

	/**
	 * "Magahi", MAG
	 */
	MAG("MAG", null, null, "Magahi"),

	/**
	 * "Marshallese", MAH, MH
	 */
	MAH("MAH", null, ISO639_1.MH, "Marshallese"),

	/**
	 * "Maithili", MAI
	 */
	MAI("MAI", null, null, "Maithili"),

	/**
	 * "Makasar", MAK
	 */
	MAK("MAK", null, null, "Makasar"),

	/**
	 * "Malayalam", MAL, ML
	 */
	MAL("MAL", null, ISO639_1.ML, "Malayalam"),

	/**
	 * "Mandingo", MAN
	 */
	MAN("MAN", null, null, "Mandingo"),

	/**
	 * "Maori", MAO/MRI, MI
	 */
	MAO("MAO", "MRI", ISO639_1.MI, "Maori"),

	/**
	 * "Maori", MAO/MRI, MI
	 */
	MRI("MAO", "MRI", ISO639_1.MI, "Maori"),

	/**
	 * "Austronesian (Other)", MAP
	 */
	MAP("MAP", null, null, "Austronesian (Other)"),

	/**
	 * "Marathi", MAR, MR
	 */
	MAR("MAR", null, ISO639_1.MR, "Marathi"),

	/**
	 * "Masai", MAS
	 */
	MAS("MAS", null, null, "Masai"),

	/**
	 * "Malay", MAY/MSA, MS
	 */
	MAY("MAY", "MSA", ISO639_1.MS, "Malay"),

	/**
	 * "Malay", MAY/MSA, MS
	 */
	MSA("MAY", "MSA", ISO639_1.MS, "Malay"),

	/**
	 * "Moksha", MDF
	 */
	MDF("MDF", null, null, "Moksha"),

	/**
	 * "Mandar", MDR
	 */
	MDR("MDR", null, null, "Mandar"),

	/**
	 * "Mende", MEN
	 */
	MEN("MEN", null, null, "Mende"),

	/**
	 * "Irish, Middle (900-1200)", MGA
	 */
	MGA("MGA", null, null, "Irish, Middle (900-1200)"),

	/**
	 * "Mi'kmaq", "Micmac", MIC
	 */
	MIC("MIC", null, null, "Mi'kmaq", "Micmac"),

	/**
	 * "Minangkabau", MIN
	 */
	MIN("MIN", null, null, "Minangkabau"),

	/**
	 * "Uncoded languages", MIS
	 */
	MIS("MIS", null, null, "Uncoded languages"),

	/**
	 * "Mon-Khmer (Other)", MKH
	 */
	MKH("MKH", null, null, "Mon-Khmer (Other)"),

	/**
	 * "Malagasy", MLG, MG
	 */
	MLG("MLG", null, ISO639_1.MG, "Malagasy"),

	/**
	 * "Maltese", MLT, MT
	 */
	MLT("MLT", null, ISO639_1.MT, "Maltese"),

	/**
	 * "Manchu", MNC
	 */
	MNC("MNC", null, null, "Manchu"),

	/**
	 * "Manipuri", MNI
	 */
	MNI("MNI", null, null, "Manipuri"),

	/**
	 * "Manobo languages", MNO
	 */
	MNO("MNO", null, null, "Manobo languages"),

	/**
	 * "Mohawk", MOH
	 */
	MOH("MOH", null, null, "Mohawk"),

	/**
	 * "Mongolian", MON, MN
	 */
	MON("MON", null, ISO639_1.MN, "Mongolian"),

	/**
	 * "Mossi", MOS
	 */
	MOS("MOS", null, null, "Mossi"),

	/**
	 * "Multiple languages", MUL
	 */
	MUL("MUL", null, null, "Multiple languages"),

	/**
	 * "Munda languages", MUN
	 */
	MUN("MUN", null, null, "Munda languages"),

	/**
	 * "Creek", MUS
	 */
	MUS("MUS", null, null, "Creek"),

	/**
	 * "Mirandese", MWL
	 */
	MWL("MWL", null, null, "Mirandese"),

	/**
	 * "Marwari", MWR
	 */
	MWR("MWR", null, null, "Marwari"),

	/**
	 * "Mayan languages", MYN
	 */
	MYN("MYN", null, null, "Mayan languages"),

	/**
	 * "Erzya", MYV
	 */
	MYV("MYV", null, null, "Erzya"),

	/**
	 * "Nahuatl languages", NAH
	 */
	NAH("NAH", null, null, "Nahuatl languages"),

	/**
	 * "North American Indian", NAI
	 */
	NAI("NAI", null, null, "North American Indian"),

	/**
	 * "Neapolitan", NAP
	 */
	NAP("NAP", null, null, "Neapolitan"),

	/**
	 * "Nauru", NAU, NA
	 */
	NAU("NAU", null, ISO639_1.NA, "Nauru"),

	/**
	 * "Navajo", "Navaho", NAV, NV
	 */
	NAV("NAV", null, ISO639_1.NV, "Navajo", "Navaho"),

	/**
	 * "Ndebele, South", "South Ndebele", NBL, NR
	 */
	NBL("NBL", null, ISO639_1.NR, "Ndebele, South", "South Ndebele"),

	/**
	 * "Ndebele, North", "North Ndebele", NDE, ND
	 */
	NDE("NDE", null, ISO639_1.ND, "Ndebele, North", "North Ndebele"),

	/**
	 * "Ndonga", NDO, NG
	 */
	NDO("NDO", null, ISO639_1.NG, "Ndonga"),

	/**
	 * "Low German", "Low Saxon", "German, Low", "Saxon, Low", NDS
	 */
	NDS("NDS", null, null, "Low German", "Low Saxon", "German, Low", "Saxon, Low"),

	/**
	 * "Nepali", NEP, NE
	 */
	NEP("NEP", null, ISO639_1.NE, "Nepali"),

	/**
	 * "Nepal Bhasa", "Newari", NEW
	 */
	NEW("NEW", null, null, "Nepal Bhasa", "Newari"),

	/**
	 * "Nias", NIA
	 */
	NIA("NIA", null, null, "Nias"),

	/**
	 * "Niger-Kordofanian (Other)", NIC
	 */
	NIC("NIC", null, null, "Niger-Kordofanian (Other)"),

	/**
	 * "Niuean", NIU
	 */
	NIU("NIU", null, null, "Niuean"),

	/**
	 * "Norwegian Nynorsk", "Nynorsk, Norwegian", NNO, NN
	 */
	NNO("NNO", null, ISO639_1.NN, "Norwegian Nynorsk", "Nynorsk, Norwegian"),

	/**
	 * "Bokmål, Norwegian", "Norwegian Bokmål", NOB, NB
	 */
	NOB("NOB", null, ISO639_1.NB, "Bokmål, Norwegian", "Norwegian Bokmål"),

	/**
	 * "Nogai", NOG
	 */
	NOG("NOG", null, null, "Nogai"),

	/**
	 * "Norse, Old", NON
	 */
	NON("NON", null, null, "Norse, Old"),

	/**
	 * "Norwegian", NOR, NO
	 */
	NOR("NOR", null, ISO639_1.NO, "Norwegian"),

	/**
	 * "N'Ko", NQO
	 */
	NQO("NQO", null, null, "N'Ko"),

	/**
	 * "Pedi", "Sepedi", "Northern Sotho", NSO
	 */
	NSO("NSO", null, null, "Pedi", "Sepedi", "Northern Sotho"),

	/**
	 * "Nubian languages", NUB
	 */
	NUB("NUB", null, null, "Nubian languages"),

	/**
	 * "Classical Newari", "Old Newari", "Classical Nepal Bhasa", NWC
	 */
	NWC("NWC", null, null, "Classical Newari", "Old Newari", "Classical Nepal Bhasa"),

	/**
	 * "Chichewa", "Chewa", "Nyanja", NYA, NY
	 */
	NYA("NYA", null, ISO639_1.NY, "Chichewa", "Chewa", "Nyanja"),

	/**
	 * "Nyamwezi", NYM
	 */
	NYM("NYM", null, null, "Nyamwezi"),

	/**
	 * "Nyankole", NYN
	 */
	NYN("NYN", null, null, "Nyankole"),

	/**
	 * "Nyoro", NYO
	 */
	NYO("NYO", null, null, "Nyoro"),

	/**
	 * "Nzima", NZI
	 */
	NZI("NZI", null, null, "Nzima"),

	/**
	 * "Occitan (post 1500)", "Provençal", OCI, OC
	 */
	OCI("OCI", null, ISO639_1.OC, "Occitan (post 1500)", "Provençal"),

	/**
	 * "Ojibwa", OJI, OJ
	 */
	OJI("OJI", null, ISO639_1.OJ, "Ojibwa"),

	/**
	 * "Oriya", ORI, OR
	 */
	ORI("ORI", null, ISO639_1.OR, "Oriya"),

	/**
	 * "Oromo", ORM, OM
	 */
	ORM("ORM", null, ISO639_1.OM, "Oromo"),

	/**
	 * "Osage", OSA
	 */
	OSA("OSA", null, null, "Osage"),

	/**
	 * "Ossetian", "Ossetic", OSS, OS
	 */
	OSS("OSS", null, ISO639_1.OS, "Ossetian", "Ossetic"),

	/**
	 * "Turkish, Ottoman (1500-1928)", OTA
	 */
	OTA("OTA", null, null, "Turkish, Ottoman (1500-1928)"),

	/**
	 * "Otomian languages", OTO
	 */
	OTO("OTO", null, null, "Otomian languages"),

	/**
	 * "Papuan (Other)", PAA
	 */
	PAA("PAA", null, null, "Papuan (Other)"),

	/**
	 * "Pangasinan", PAG
	 */
	PAG("PAG", null, null, "Pangasinan"),

	/**
	 * "Pahlavi", PAL
	 */
	PAL("PAL", null, null, "Pahlavi"),

	/**
	 * "Pampanga", "Kapampangan", PAM
	 */
	PAM("PAM", null, null, "Pampanga", "Kapampangan"),

	/**
	 * "Panjabi", "Punjabi", PAN, PA
	 */
	PAN("PAN", null, ISO639_1.PA, "Panjabi", "Punjabi"),

	/**
	 * "Papiamento", PAP
	 */
	PAP("PAP", null, null, "Papiamento"),

	/**
	 * "Palauan", PAU
	 */
	PAU("PAU", null, null, "Palauan"),

	/**
	 * "Persian, Old (ca.600-400 B.C.)", PEO
	 */
	PEO("PEO", null, null, "Persian, Old (ca.600-400 B.C.)"),

	/**
	 * "Persian", PER/FAS, FA
	 */
	PER("PER", "FAS", ISO639_1.FA, "Persian"),

	/**
	 * "Persian", PER/FAS, FA
	 */
	FAS("PER", "FAS", ISO639_1.FA, "Persian"),

	/**
	 * "Philippine (Other)", PHI
	 */
	PHI("PHI", null, null, "Philippine (Other)"),

	/**
	 * "Phoenician", PHN
	 */
	PHN("PHN", null, null, "Phoenician"),

	/**
	 * "Pali", PLI, PI
	 */
	PLI("PLI", null, ISO639_1.PI, "Pali"),

	/**
	 * "Polish", POL, PL
	 */
	POL("POL", null, ISO639_1.PL, "Polish"),

	/**
	 * "Pohnpeian", PON
	 */
	PON("PON", null, null, "Pohnpeian"),

	/**
	 * "Portuguese", POR, PT
	 */
	POR("POR", null, ISO639_1.PT, "Portuguese"),

	/**
	 * "Prakrit languages", PRA
	 */
	PRA("PRA", null, null, "Prakrit languages"),

	/**
	 * "Provençal, Old (to 1500)", PRO
	 */
	PRO("PRO", null, null, "Provençal, Old (to 1500)"),

	/**
	 * "Pushto", "Pashto", PUS, PS
	 */
	PUS("PUS", null, ISO639_1.PS, "Pushto", "Pashto"),

	/**
	 * "Quechua", QUE, QU
	 */
	QUE("QUE", null, ISO639_1.QU, "Quechua"),

	/**
	 * "Rajasthani", RAJ
	 */
	RAJ("RAJ", null, null, "Rajasthani"),

	/**
	 * "Rapanui", RAP
	 */
	RAP("RAP", null, null, "Rapanui"),

	/**
	 * "Rarotongan", "Cook Islands Maori", RAR
	 */
	RAR("RAR", null, null, "Rarotongan", "Cook Islands Maori"),

	/**
	 * "Romance (Other)", ROA
	 */
	ROA("ROA", null, null, "Romance (Other)"),

	/**
	 * "Romansh", ROH, RM
	 */
	ROH("ROH", null, ISO639_1.RM, "Romansh"),

	/**
	 * "Romany", ROM
	 */
	ROM("ROM", null, null, "Romany"),

	/**
	 * "Romanian", "Moldavian", "Moldovan", RUM/RON, RO
	 */
	RUM("RUM", "RON", ISO639_1.RO, "Romanian", "Moldavian", "Moldovan"),

	/**
	 * "Romanian", "Moldavian", "Moldovan", RUM/RON, RO
	 */
	RON("RUM", "RON", ISO639_1.RO, "Romanian", "Moldavian", "Moldovan"),

	/**
	 * "Rundi", RUN, RN
	 */
	RUN("RUN", null, ISO639_1.RN, "Rundi"),

	/**
	 * "Aromanian", "Arumanian", "Macedo-Romanian", RUP
	 */
	RUP("RUP", null, null, "Aromanian", "Arumanian", "Macedo-Romanian"),

	/**
	 * "Russian", RUS, RU
	 */
	RUS("RUS", null, ISO639_1.RU, "Russian"),

	/**
	 * "Sandawe", SAD
	 */
	SAD("SAD", null, null, "Sandawe"),

	/**
	 * "Sango", SAG, SG
	 */
	SAG("SAG", null, ISO639_1.SG, "Sango"),

	/**
	 * "Yakut", SAH
	 */
	SAH("SAH", null, null, "Yakut"),

	/**
	 * "South American Indian (Other)", SAI
	 */
	SAI("SAI", null, null, "South American Indian (Other)"),

	/**
	 * "Salishan languages", SAL
	 */
	SAL("SAL", null, null, "Salishan languages"),

	/**
	 * "Samaritan Aramaic", SAM
	 */
	SAM("SAM", null, null, "Samaritan Aramaic"),

	/**
	 * "Sanskrit", SAN, SA
	 */
	SAN("SAN", null, ISO639_1.SA, "Sanskrit"),

	/**
	 * "Sasak", SAS
	 */
	SAS("SAS", null, null, "Sasak"),

	/**
	 * "Santali", SAT
	 */
	SAT("SAT", null, null, "Santali"),

	/**
	 * "Sicilian", SCN
	 */
	SCN("SCN", null, null, "Sicilian"),

	/**
	 * "Scots", SCO
	 */
	SCO("SCO", null, null, "Scots"),

	/**
	 * "Selkup", SEL
	 */
	SEL("SEL", null, null, "Selkup"),

	/**
	 * "Semitic (Other)", SEM
	 */
	SEM("SEM", null, null, "Semitic (Other)"),

	/**
	 * "Irish, Old (to 900)", SGA
	 */
	SGA("SGA", null, null, "Irish, Old (to 900)"),

	/**
	 * "Sign Languages", SGN
	 */
	SGN("SGN", null, null, "Sign Languages"),

	/**
	 * "Shan", SHN
	 */
	SHN("SHN", null, null, "Shan"),

	/**
	 * "Sidamo", SID
	 */
	SID("SID", null, null, "Sidamo"),

	/**
	 * "Sinhala", "Sinhalese", SIN, SI
	 */
	SIN("SIN", null, ISO639_1.SI, "Sinhala", "Sinhalese"),

	/**
	 * "Siouan languages", SIO
	 */
	SIO("SIO", null, null, "Siouan languages"),

	/**
	 * "Sino-Tibetan (Other)", SIT
	 */
	SIT("SIT", null, null, "Sino-Tibetan (Other)"),

	/**
	 * "Slavic (Other)", SLA
	 */
	SLA("SLA", null, null, "Slavic (Other)"),

	/**
	 * "Slovak", SLO/SLK, SK
	 */
	SLO("SLO", "SLK", ISO639_1.SK, "Slovak"),

	/**
	 * "Slovak", SLO/SLK, SK
	 */
	SLK("SLO", "SLK", ISO639_1.SK, "Slovak"),

	/**
	 * "Slovenian", SLV, SL
	 */
	SLV("SLV", null, ISO639_1.SL, "Slovenian"),

	/**
	 * "Southern Sami", SMA
	 */
	SMA("SMA", null, null, "Southern Sami"),

	/**
	 * "Northern Sami", SME, SE
	 */
	SME("SME", null, ISO639_1.SE, "Northern Sami"),

	/**
	 * "Sami languages (Other)", SMI
	 */
	SMI("SMI", null, null, "Sami languages (Other)"),

	/**
	 * "Lule Sami", SMJ
	 */
	SMJ("SMJ", null, null, "Lule Sami"),

	/**
	 * "Inari Sami", SMN
	 */
	SMN("SMN", null, null, "Inari Sami"),

	/**
	 * "Samoan", SMO, SM
	 */
	SMO("SMO", null, ISO639_1.SM, "Samoan"),

	/**
	 * "Skolt Sami", SMS
	 */
	SMS("SMS", null, null, "Skolt Sami"),

	/**
	 * "Shona", SNA, SN
	 */
	SNA("SNA", null, ISO639_1.SN, "Shona"),

	/**
	 * "Sindhi", SND, SD
	 */
	SND("SND", null, ISO639_1.SD, "Sindhi"),

	/**
	 * "Soninke", SNK
	 */
	SNK("SNK", null, null, "Soninke"),

	/**
	 * "Sogdian", SOG
	 */
	SOG("SOG", null, null, "Sogdian"),

	/**
	 * "Somali", SOM, SO
	 */
	SOM("SOM", null, ISO639_1.SO, "Somali"),

	/**
	 * "Songhai languages", SON
	 */
	SON("SON", null, null, "Songhai languages"),

	/**
	 * "Sotho, Southern", SOT, ST
	 */
	SOT("SOT", null, ISO639_1.ST, "Sotho, Southern"),

	/**
	 * "Spanish", "Castilian", SPA, ES
	 */
	SPA("SPA", null, ISO639_1.ES, "Spanish", "Castilian"),

	/**
	 * "Sardinian", SRD, SC
	 */
	SRD("SRD", null, ISO639_1.SC, "Sardinian"),

	/**
	 * "Sranan Tongo", SRN
	 */
	SRN("SRN", null, null, "Sranan Tongo"),

	/**
	 * "Serbian", SRP, SR
	 */
	SRP("SRP", null, ISO639_1.SR, "Serbian"),

	/**
	 * "Serer", SRR
	 */
	SRR("SRR", null, null, "Serer"),

	/**
	 * "Nilo-Saharan (Other)", SSA
	 */
	SSA("SSA", null, null, "Nilo-Saharan (Other)"),

	/**
	 * "Swati", SSW, SS
	 */
	SSW("SSW", null, ISO639_1.SS, "Swati"),

	/**
	 * "Sukuma", SUK
	 */
	SUK("SUK", null, null, "Sukuma"),

	/**
	 * "Sundanese", SUN, SU
	 */
	SUN("SUN", null, ISO639_1.SU, "Sundanese"),

	/**
	 * "Susu", SUS
	 */
	SUS("SUS", null, null, "Susu"),

	/**
	 * "Sumerian", SUX
	 */
	SUX("SUX", null, null, "Sumerian"),

	/**
	 * "Swahili", SWA, SW
	 */
	SWA("SWA", null, ISO639_1.SW, "Swahili"),

	/**
	 * "Swedish", SWE, SV
	 */
	SWE("SWE", null, ISO639_1.SV, "Swedish"),

	/**
	 * "Classical Syriac", SYC
	 */
	SYC("SYC", null, null, "Classical Syriac"),

	/**
	 * "Syriac", SYR
	 */
	SYR("SYR", null, null, "Syriac"),

	/**
	 * "Tahitian", TAH, TY
	 */
	TAH("TAH", null, ISO639_1.TY, "Tahitian"),

	/**
	 * "Tai (Other)", TAI
	 */
	TAI("TAI", null, null, "Tai (Other)"),

	/**
	 * "Tamil", TAM, TA
	 */
	TAM("TAM", null, ISO639_1.TA, "Tamil"),

	/**
	 * "Tatar", TAT, TT
	 */
	TAT("TAT", null, ISO639_1.TT, "Tatar"),

	/**
	 * "Telugu", TEL, TE
	 */
	TEL("TEL", null, ISO639_1.TE, "Telugu"),

	/**
	 * "Timne", TEM
	 */
	TEM("TEM", null, null, "Timne"),

	/**
	 * "Tereno", TER
	 */
	TER("TER", null, null, "Tereno"),

	/**
	 * "Tetum", TET
	 */
	TET("TET", null, null, "Tetum"),

	/**
	 * "Tajik", TGK, TG
	 */
	TGK("TGK", null, ISO639_1.TG, "Tajik"),

	/**
	 * "Tagalog", TGL, TL
	 */
	TGL("TGL", null, ISO639_1.TL, "Tagalog"),

	/**
	 * "Thai", THA, TH
	 */
	THA("THA", null, ISO639_1.TH, "Thai"),

	/**
	 * "Tibetan", TIB/BOD, BO
	 */
	TIB("TIB", "BOD", ISO639_1.BO, "Tibetan"),

	/**
	 * "Tibetan", TIB/BOD, BO
	 */
	BOD("TIB", "BOD", ISO639_1.BO, "Tibetan"),

	/**
	 * "Tigre", TIG
	 */
	TIG("TIG", null, null, "Tigre"),

	/**
	 * "Tigrinya", TIR, TI
	 */
	TIR("TIR", null, ISO639_1.TI, "Tigrinya"),

	/**
	 * "Tiv", TIV
	 */
	TIV("TIV", null, null, "Tiv"),

	/**
	 * "Tokelau", TKL
	 */
	TKL("TKL", null, null, "Tokelau"),

	/**
	 * "Klingon", "tlhIngan-Hol", TLH
	 */
	TLH("TLH", null, null, "Klingon", "tlhIngan-Hol"),

	/**
	 * "Tlingit", TLI
	 */
	TLI("TLI", null, null, "Tlingit"),

	/**
	 * "Tamashek", TMH
	 */
	TMH("TMH", null, null, "Tamashek"),

	/**
	 * "Tonga (Nyasa)", TOG
	 */
	TOG("TOG", null, null, "Tonga (Nyasa)"),

	/**
	 * "Tonga (Tonga Islands)", TON, TO
	 */
	TON("TON", null, ISO639_1.TO, "Tonga (Tonga Islands)"),

	/**
	 * "Tok Pisin", TPI
	 */
	TPI("TPI", null, null, "Tok Pisin"),

	/**
	 * "Tsimshian", TSI
	 */
	TSI("TSI", null, null, "Tsimshian"),

	/**
	 * "Tswana", TSN, TN
	 */
	TSN("TSN", null, ISO639_1.TN, "Tswana"),

	/**
	 * "Tsonga", TSO, TS
	 */
	TSO("TSO", null, ISO639_1.TS, "Tsonga"),

	/**
	 * "Turkmen", TUK, TK
	 */
	TUK("TUK", null, ISO639_1.TK, "Turkmen"),

	/**
	 * "Tumbuka", TUM
	 */
	TUM("TUM", null, null, "Tumbuka"),

	/**
	 * "Tupi languages", TUP
	 */
	TUP("TUP", null, null, "Tupi languages"),

	/**
	 * "Turkish", TUR, TR
	 */
	TUR("TUR", null, ISO639_1.TR, "Turkish"),

	/**
	 * "Altaic (Other)", TUT
	 */
	TUT("TUT", null, null, "Altaic (Other)"),

	/**
	 * "Tuvalu", TVL
	 */
	TVL("TVL", null, null, "Tuvalu"),

	/**
	 * "Twi", TWI, TW
	 */
	TWI("TWI", null, ISO639_1.TW, "Twi"),

	/**
	 * "Tuvinian", TYV
	 */
	TYV("TYV", null, null, "Tuvinian"),

	/**
	 * "Udmurt", UDM
	 */
	UDM("UDM", null, null, "Udmurt"),

	/**
	 * "Ugaritic", UGA
	 */
	UGA("UGA", null, null, "Ugaritic"),

	/**
	 * "Uighur", "Uyghur", UIG, UG
	 */
	UIG("UIG", null, ISO639_1.UG, "Uighur", "Uyghur"),

	/**
	 * "Ukrainian", UKR, UK
	 */
	UKR("UKR", null, ISO639_1.UK, "Ukrainian"),

	/**
	 * "Umbundu", UMB
	 */
	UMB("UMB", null, null, "Umbundu"),

	/**
	 * "Undetermined", UND
	 */
	UND("UND", null, null, "Undetermined"),

	/**
	 * "Urdu", URD, UR
	 */
	URD("URD", null, ISO639_1.UR, "Urdu"),

	/**
	 * "Uzbek", UZB, UZ
	 */
	UZB("UZB", null, ISO639_1.UZ, "Uzbek"),

	/**
	 * "Vai", VAI
	 */
	VAI("VAI", null, null, "Vai"),

	/**
	 * "Venda", VEN, VE
	 */
	VEN("VEN", null, ISO639_1.VE, "Venda"),

	/**
	 * "Vietnamese", VIE, VI
	 */
	VIE("VIE", null, ISO639_1.VI, "Vietnamese"),

	/**
	 * "Volapük", VOL, VO
	 */
	VOL("VOL", null, ISO639_1.VO, "Volapük"),

	/**
	 * "Votic", VOT
	 */
	VOT("VOT", null, null, "Votic"),

	/**
	 * "Wakashan languages", WAK
	 */
	WAK("WAK", null, null, "Wakashan languages"),

	/**
	 * "Walamo", WAL
	 */
	WAL("WAL", null, null, "Walamo"),

	/**
	 * "Waray", WAR
	 */
	WAR("WAR", null, null, "Waray"),

	/**
	 * "Washo", WAS
	 */
	WAS("WAS", null, null, "Washo"),

	/**
	 * "Welsh", WEL/CYM, CY
	 */
	WEL("WEL", "CYM", ISO639_1.CY, "Welsh"),

	/**
	 * "Welsh", WEL/CYM, CY
	 */
	CYM("WEL", "CYM", ISO639_1.CY, "Welsh"),

	/**
	 * "Sorbian languages", WEN
	 */
	WEN("WEN", null, null, "Sorbian languages"),

	/**
	 * "Walloon", WLN, WA
	 */
	WLN("WLN", null, ISO639_1.WA, "Walloon"),

	/**
	 * "Wolof", WOL, WO
	 */
	WOL("WOL", null, ISO639_1.WO, "Wolof"),

	/**
	 * "Kalmyk", "Oirat", XAL
	 */
	XAL("XAL", null, null, "Kalmyk", "Oirat"),

	/**
	 * "Xhosa", XHO, XH
	 */
	XHO("XHO", null, ISO639_1.XH, "Xhosa"),

	/**
	 * "Yao", YAO
	 */
	YAO("YAO", null, null, "Yao"),

	/**
	 * "Yapese", YAP
	 */
	YAP("YAP", null, null, "Yapese"),

	/**
	 * "Yiddish", YID, YI
	 */
	YID("YID", null, ISO639_1.YI, "Yiddish"),

	/**
	 * "Yoruba", YOR, YO
	 */
	YOR("YOR", null, ISO639_1.YO, "Yoruba"),

	/**
	 * "Yupik languages", YPK
	 */
	YPK("YPK", null, null, "Yupik languages"),

	/**
	 * "Zapotec", ZAP
	 */
	ZAP("ZAP", null, null, "Zapotec"),

	/**
	 * "Blissymbols", "Blissymbolics", "Bliss", ZBL
	 */
	ZBL("ZBL", null, null, "Blissymbols", "Blissymbolics", "Bliss"),

	/**
	 * "Zenaga", ZEN
	 */
	ZEN("ZEN", null, null, "Zenaga"),

	/**
	 * "Zhuang", "Chuang", ZHA, ZA
	 */
	ZHA("ZHA", null, ISO639_1.ZA, "Zhuang", "Chuang"),

	/**
	 * "Zande languages", ZND
	 */
	ZND("ZND", null, null, "Zande languages"),

	/**
	 * "Zulu", ZUL, ZU
	 */
	ZUL("ZUL", null, ISO639_1.ZU, "Zulu"),

	/**
	 * "Zuni", ZUN
	 */
	ZUN("ZUN", null, null, "Zuni"),

	/**
	 * "No linguistic content", "Not applicable", ZXX
	 */
	ZXX("ZXX", null, null, "No linguistic content", "Not applicable"),

	/**
	 * "Zaza", "Dimili", "Dimli", "Kirdki", "Kirmanjki", "Zazaki", ZZA
	 */
	ZZA("ZZA", null, null, "Zaza", "Dimili", "Dimli", "Kirdki", "Kirmanjki", "Zazaki"),

	;

	/**
	 * 
	 * 
	 */
	ISO639_2(String bibliographicCode, String terminologyCode
		, ISO639_1 twoLetterCode, String... descriptions)
	{
		this.bibliographicCode = bibliographicCode;
		this.terminologyCode = terminologyCode;
		this.twoLetterCode = twoLetterCode;
		this.descriptions = Collections.unmodifiableList(
			Arrays.asList(descriptions)
			);
	}

	/**
	 * 
	 * 
	 */
	public String getCode(){
		if(twoLetterCode != null){
			return twoLetterCode.name();
		} else if(terminologyCode != null){
			return terminologyCode;
		} else{
			return name();
		}
	}

	/**
	 * 
	 * 
	 */
	public String getBibliographicCode(){
		return bibliographicCode;
	}

	/**
	 * 
	 * 
	 */
	public String getTerminologyCode(){
		return terminologyCode;
	}

	/**
	 * 
	 * 
	 */
	public ISO639_1 getTwoLetterCode(){
		return twoLetterCode;
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
	public static Collection<ISO639_2> valuesCollection(){
		return values;
	}

	private String terminologyCode;
	private String bibliographicCode;
	private ISO639_1 twoLetterCode;
	private List<String> descriptions;

	private static final Collection<ISO639_2> values;

	static{
		values = Collections.unmodifiableCollection(
			Arrays.asList(values())
			);
	}
}
