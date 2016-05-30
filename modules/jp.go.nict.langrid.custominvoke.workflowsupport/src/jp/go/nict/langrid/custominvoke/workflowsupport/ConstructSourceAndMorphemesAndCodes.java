package jp.go.nict.langrid.custominvoke.workflowsupport;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

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
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractService;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ConstructSourceAndMorphemesAndCodes
implements IAeInvokeHandler{
	public ConstructSourceAndMorphemesAndCodes() {
		AbstractService.setCurrentServiceContext(new AeServiceContext());
		constructSmc = new jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes();
	}
	private static Logger logger = Logger.getLogger(ConstructSourceAndMorphemesAndCodes.class.getName());

	@SuppressWarnings("unchecked")
	public IAeWebServiceResponse handleInvoke(IAeInvoke invokeRequest, String queryData) {
		long start = start("prepare to invoke"); //###
		long current = start; //###
		try {
			Map<?, ?> reqMsgParts = invokeRequest.getInputMessageData().getMessageData();
			String sourceLang = (String)reqMsgParts.get("sourceLang");
			Document morphemesDoc = (Document)reqMsgParts.get("morphemes");
			Document translationsDoc =(Document)reqMsgParts.get("translations");
			NodeList words = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_WORD);
			NodeList lemmas = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_LEMMA);
			NodeList parts = AeInvokeHelper.selectNodeList(morphemesDoc, MORPHEME_PART_OF_SPEECH);
			Morpheme[] morphemes = new Morpheme[words.getLength()];
//			logger.severe("++ Morphemes ++" + AeInvokeHelper.printNode(morphemesDoc));
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
//			logger.severe("++ Morphemes Length ++" + morphemes.length);
//			logger.severe("++ Morphemes Word ++" + morphemes[0].getWord());
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
			current = lap(current, "preparation done. invoke doConstructSMC(..)"); //###
			SourceAndMorphemesAndCodes smc = constructSmc.doConstructSMC(sourceLang, morphemes, translations);
			current = lap(current, "invocation done. build response"); //###
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
				w = AeInvokeHelper.sanitize(w);
				l = AeInvokeHelper.sanitize(l);
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
			xml.append(AeInvokeHelper.sanitize(source));
			xml.append("</source>");
			xml.append("<targetWords>");
			for (int i = 0; i < restargetWords.length; i++) {
				xml.append("<string>");
				xml.append(AeInvokeHelper.sanitize(restargetWords[i]));
				xml.append("</string>");
			}
			xml.append("</targetWords>");
			xml.append("</constructSMCReturn>");
//			logger.severe(xml.toString());
			Document responseDoc = AeInvokeHelper.string2Document(xml.toString());
			Map respMsgParts = new HashMap();
			respMsgParts.put("constructSMCReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(resultQName, respMsgParts);
			current = lap(current, "build response done");	// ###
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
		} finally{
			finish(start, current);	// ###
		}
		return null;
	}

	private long start(String message){
		if(!logEnabled) return 0;
		System.out.println(logHeader + "started. " + message);
		return System.currentTimeMillis();
	}

	private long lap(long current, String message){
		if(!logEnabled) return 0;
		long d = System.currentTimeMillis() - current;
		System.out.println(logHeader + "lap(" + d + "ms laps): " + message);
		return System.currentTimeMillis();
	}

	private void finish(long start, long current){
		if(!logEnabled) return;
		long f = System.currentTimeMillis();
		long dt = f - start;
		long dl = f - current;
		System.out.println(logHeader + "finished(" + dl + "ms laps, " + dt + "ms total)");
	}

	private jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes constructSmc;
	private String logHeader = "[ConstructSourceAndMorphemesAndCodes] ";
	private boolean logEnabled = false;

	private static final String MORPHEME_PART_OF_SPEECH = "/morphemes/Morpheme/partOfSpeech/text()";
	private static final String MORPHEME_LEMMA = "/morphemes/Morpheme/lemma/text()";
	private static final String MORPHEME_WORD = "/morphemes/Morpheme/word/text()";
//	private static final String TRANSLATION_WITH_POSITION_TRANSLATION_TARGET_WORDS = "/translations/TranslationWithPosition/translation/targetWords/string/text()";
	private static final String TRANSLATION_WITH_POSITION_TRANSLATION_HEAD_WORD = "/translations/TranslationWithPosition/translation/headWord/text()";
	private static final String TRANSLATION_WITH_POSITION_START_INDEX = "/translations/TranslationWithPosition/startIndex/text()";
	private static final String TRANSLATION_WITH_POSITION_NUMBER_OF_MORPHEMES = "/translations/TranslationWithPosition/numberOfMorphemes/text()";
	private static final QName resultQName = new QName(
				"http://localhost:8080/workflow-support/services/ConstructSourceAndMorphemesAndCodes"
				, "constructSMCResponse");
}
