package jp.go.nict.langrid.custominvoke.workflowsupport;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.Analysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.ChineseAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.DefaultAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.EnglishAnalysis4TreeTagger;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.HangulAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.JapaneseAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.util.AeInvokeHelper;
import jp.go.nict.langrid.custominvoke.workflowsupport.util.StringUtil;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ConstructSourceAndMorphemesAndCodes extends AbstractCustomInvokeService {

	private static final String MORPHEME_PART_OF_SPEECH = "/morphemes/Morpheme/partOfSpeech/text()";
	private static final String MORPHEME_LEMMA = "/morphemes/Morpheme/lemma/text()";
	private static final String MORPHEME_WORD = "/morphemes/Morpheme/word/text()";
	private static final String TRANSLATION_WITH_POSITION_TRANSLATION_HEAD_WORD = "/translations/TranslationWithPosition/translation/headWord/text()";
	private static final String TRANSLATION_WITH_POSITION_START_INDEX = "/translations/TranslationWithPosition/startIndex/text()";
	private static final String TRANSLATION_WITH_POSITION_NUMBER_OF_MORPHEMES = "/translations/TranslationWithPosition/numberOfMorphemes/text()";

	public ConstructSourceAndMorphemesAndCodes() {
		super();
	}
	private static Logger logger = Logger.getLogger(ConstructSourceAndMorphemesAndCodes.class.getName());

	@SuppressWarnings("unchecked")
	public IAeWebServiceResponse invoke(IAeInvoke invokeRequest, String queryData, QName qName) {
		try {
		
			Map<?, ?> reqMsgParts = invokeRequest.getInputMessageData().getMessageData();
			String sourceLang = (String)reqMsgParts.get("sourceLang");
			Document morphemesDoc = (Document)reqMsgParts.get("morphemes");
			Document translationsDoc =(Document)reqMsgParts.get("translations");
			NodeList words = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_WORD);
			NodeList lemmas = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_LEMMA);
			NodeList parts = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_PART_OF_SPEECH);
			Morpheme[] morphemes = new Morpheme[words.getLength()];
			for (int i = 0; i < words.getLength(); i++) {
				String lemma = "";
				try {
					if (lemmas.item(i).getNodeValue() != null) {
						lemma = lemmas.item(i).getNodeValue();
					}
				} catch (NullPointerException ne) {
					lemma = words.item(i).getNodeValue();
					;
				}
				morphemes[i] = new Morpheme(words.item(i).getNodeValue(), lemma, parts.item(i).getNodeValue());
			}
			
			// エスケープ及び、形態素の再構築
			morphemes = StringUtil.escapeCharacterRestructing(morphemes);
			morphemesDoc = null;
			NodeList numbers = AeInvokeHelper.selectNodeList(translationsDoc, TRANSLATION_WITH_POSITION_NUMBER_OF_MORPHEMES);
			NodeList indexes = AeInvokeHelper.selectNodeList(translationsDoc, TRANSLATION_WITH_POSITION_START_INDEX);
			NodeList headWords = AeInvokeHelper.selectNodeList(translationsDoc, TRANSLATION_WITH_POSITION_TRANSLATION_HEAD_WORD);
			TranslationWithPosition[] translations = new TranslationWithPosition[numbers.getLength()];
			for (int i = 0; i < numbers.getLength(); i++) {
				NodeList targetNodes = AeInvokeHelper.selectNodeList(translationsDoc, "/translations/TranslationWithPosition[" + (i + 1) + "]/translation/targetWords/string/text()");
				String[] targets = new String[targetNodes.getLength()];
				for (int j = 0; j < targets.length; j++) {
					targets[j] = targetNodes.item(j).getNodeValue();
				}
				Translation translation = new Translation(headWords.item(i).getNodeValue(), targets);
				translations[i] = new TranslationWithPosition(translation, Integer.parseInt(indexes.item(i).getNodeValue()),Integer.parseInt(numbers.item(i).getNodeValue()));
			}
			translationsDoc = null;
			SourceAndMorphemesAndCodes smc = doConstructSMC(sourceLang, morphemes, translations);
//			logger.severe("++ Source after ++" + smc.getSource());			
			StringBuffer xml = new StringBuffer();
			xml.append("<constructSMCReturn xmlns:ns2=\"http://langrid.nict.go.jp/ws_1_2/workflowsupport/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:SourceAndMorphemesAndCodes\">");
			String[] rescodes = smc.getCodes();
			String[] restargetWords = smc.getTargetWords();
			morphemes = smc.getMorphemes();
			String source = smc.getSource();
			xml.append("<codes>");
			for (int i = 0; i < rescodes.length; i++) {
				xml.append("<string>");
				xml.append(rescodes[i]);
				xml.append("</string>");
			}
			xml.append("</codes>");
			xml.append("<morphemes>");
			for (int i = 0; i < morphemes.length; i++) {
				Morpheme m = morphemes[i];
				String w = m.getWord();
				String l = m.getLemma();
				w = StringUtil.sanitize(w);
				l = StringUtil.sanitize(l);
				xml.append("<Morpheme>");
				xml.append("<word>");
				xml.append(w);
				xml.append("</word>");
				xml.append("<lemma>");
				xml.append(l);
				xml.append("</lemma>");
				xml.append("<partOfSpeech>");
				xml.append(morphemes[i].getPartOfSpeech());
				xml.append("</partOfSpeech>");
				xml.append("</Morpheme>");
			}
			xml.append("</morphemes>");
			xml.append("<source>");
			xml.append(StringUtil.sanitize(source));
			xml.append("</source>");
			xml.append("<targetWords>");
			for (int i = 0; i < restargetWords.length; i++) {
				xml.append("<string>");
				xml.append(restargetWords[i]);
				xml.append("</string>");
			}
			xml.append("</targetWords>");
			xml.append("</constructSMCReturn>");
//			logger.severe(xml.toString());
			Document responseDoc = AeInvokeHelper.createDocument(xml.toString());
			Map respMsgParts = new HashMap();
			respMsgParts.put("constructSMCReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(qName, respMsgParts);
			return response;
		} catch (XPathExpressionException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (LanguageNotUniquelyDecidedException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (UnsupportedLanguageException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (AccessLimitExceededException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (NoAccessPermissionException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (NoValidEndpointsException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ServerBusyException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ServiceNotActiveException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ServiceNotFoundException e) {
			logger.severe(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected SourceAndMorphemesAndCodes doConstructSMC(String sourceLang,
			Morpheme[] morphemes, TranslationWithPosition[] translations)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
//		logger.severe("doConstructSMC start:" + DATE_FORMAT.format(new Date()));
		if (sourceLang == null || morphemes == null || translations == null) {
			throw new InvalidParameterException("sorceLang", "sourceLang is null.");
		} else if (morphemes == null) {
			throw new InvalidParameterException("morphemes", "morphemes is null.");
		} else if (translations == null) {
			translations = new TranslationWithPosition[0];
		}
		try {
			Map<Integer, TranslationWithPosition> positionMap = new HashMap<Integer, TranslationWithPosition>();
			int length = translations.length;
			for (int i = 0; i < length; i++) {
				if (translations[i] == null) continue;
				if (translations[i].getTranslation() == null) continue;
				String[] tWords = translations[i].getTranslation().getTargetWords();
				if (tWords == null || tWords.length == 0) {
					continue;
				}
				positionMap.put(Integer.valueOf(translations[i].getStartIndex()), translations[i]);
			}
			Analysis analysis = null;
			if (sourceLang.contains("ja")) {
				analysis = new JapaneseAnalysis();
			} else if (sourceLang.contains("ko")) {
				analysis = new HangulAnalysis();
			} else if (sourceLang.contains("zh")) {
				analysis = new ChineseAnalysis();
			} else if (sourceLang.contains("en")) {
				analysis = new EnglishAnalysis4TreeTagger();
			} else {
				analysis = new DefaultAnalysis();
			}
			SourceAndMorphemesAndCodes smc = analysis.doConstructSMC(morphemes, positionMap);
			// 全角アルファベット→半角アルファベット変換

			return smc;
		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new ProcessFailedException(e);
		} finally {
//			logger.severe("doConstructSMC end:" + DATE_FORMAT.format(new Date()));
		}
	}
	@Override
	protected Port getPort(Service service) {
		super.setPortTypeName("ConstructSourceAndMorphemesAndCodes");
		return service.getPort("ConstructSourceAndMorphemesAndCodes");
	}
}
