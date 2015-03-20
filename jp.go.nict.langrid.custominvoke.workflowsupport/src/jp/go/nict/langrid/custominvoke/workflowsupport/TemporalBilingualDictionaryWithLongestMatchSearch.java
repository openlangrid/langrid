package jp.go.nict.langrid.custominvoke.workflowsupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.xpath.XPathExpressionException;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TemporalBilingualDictionaryWithLongestMatchSearch implements IAeInvokeHandler
{
	public TemporalBilingualDictionaryWithLongestMatchSearch(){
		searchTerm = new jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAeWebServiceResponse handleInvoke(IAeInvoke invokeRequest,
			String queryData){
		long start = start("prepare to invoke");	// ###
		long current = start;	// ###
		try {
			Map<?, ?> reqMsgParts = invokeRequest.getInputMessageData().getMessageData();
			String headLang = (String)reqMsgParts.get("headLang");
			Document morphemesDoc = (Document)reqMsgParts.get("morphemes");
			NodeList words = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/word/text()");
			NodeList lemmas = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/lemma/text()");
			NodeList parts = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/partOfSpeech/text()");
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
			morphemesDoc = null;
			Holder<Long> h = new Holder<Long>(current); // ###
			Document responseDoc = searchTemporalDictionary(headLang, reqMsgParts, h); // ###
			Map respMsgParts = new HashMap();
			respMsgParts.put("searchAllLongestMatchingTermsReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(resultQName, respMsgParts);
			current = lap(h.value, "build response done");	// ###
			return response;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			e.printStackTrace();
		} catch (InvalidLanguageTagException e) {
			e.printStackTrace();
		} finally{
			finish(start, current);	// ###
		}
		return null;
	}
	
	/**
	 * テンポラル辞書適用
	 * @param headLang
	 * @param reqMsgParts
	 * @return
	 * @throws XPathExpressionException
	 * @throws InvalidParameterException
	 * @throws ProcessFailedException
	 * @throws InvalidLanguageTagException
	 */
	public Document searchTemporalDictionary(String headLang, Map<?, ?> reqMsgParts, Holder<Long> h) throws XPathExpressionException, InvalidParameterException, ProcessFailedException, InvalidLanguageTagException {
		try {
			Document morphemesDoc = (Document)reqMsgParts.get("morphemes");
			Document temporalDictDoc =(Document)reqMsgParts.get("temporalDict");
			NodeList words = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/word/text()");
			NodeList lemmas = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/lemma/text()");
			NodeList parts = AeInvokeHelper.selectNodeList(morphemesDoc, "/morphemes/Morpheme/partOfSpeech/text()");
			Morpheme[] morphemes = new Morpheme[words.getLength()];
			for (int i = 0; i < words.getLength(); i++) {
				String lemma = "";
				try {
					if (lemmas.item(i).getNodeValue() != null) {
						lemma = lemmas.item(i).getNodeValue();
//						System.out.println(lemma);
					}
				} catch (NullPointerException ne) {
					lemma = words.item(i).getNodeValue();
					;
				}
				morphemes[i] = new Morpheme(words.item(i).getNodeValue(), lemma, parts.item(i).getNodeValue());
			}
			morphemesDoc = null;
			List<Translation> translations = new ArrayList<Translation>();
			NodeList nodes = AeInvokeHelper.selectNodeList(temporalDictDoc, "/temporalDict/Translation");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				String hw = this.getHeadWord(n);
				String tw = this.getTargetWord(n);
				if (hw != null && tw != null) {
					translations.add(new Translation(hw, new String[]{tw}));
				}
			}
			long current = h.value;
			current = lap(current, "preparation done. invoke doRepolace(..)"); //###
			Collection<TranslationWithPosition> results = searchTerm.doSearchAllLongestMatchingTerms(new Language(headLang), morphemes, translations.toArray(new Translation[]{}));
			current = lap(current, "invocation done. build response"); //###
			h.value = current;
			StringBuffer xml = new StringBuffer();
			xml.append("<searchLongestMatchingTermsReturn xmlns:aensTYPE=\"http://localhost:8080/wrapper-mock-1.2.N/services/AbstractBilingualDictionaryWithLongestMatchSearch\" xmlns:ns2=\"http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"aensTYPE:ArrayOf_tns1_TranslationWithPosition\">");
			for (TranslationWithPosition tran : results) {
				xml.append("<TranslationWithPosition>");
				xml.append("<numberOfMorphemes>");
				xml.append(tran.getNumberOfMorphemes());
				xml.append("</numberOfMorphemes>");
				xml.append("<startIndex>");
				xml.append(tran.getStartIndex());
				xml.append("</startIndex>");
				xml.append("<translation>");
				xml.append("<headWord>");
				xml.append(AeInvokeHelper.sanitize(tran.getTranslation().getHeadWord()));
				xml.append("</headWord>");
				xml.append("<targetWords>");
				xml.append("<string>");
				xml.append(AeInvokeHelper.sanitize(tran.getTranslation().getTargetWords()[0]));
				xml.append("</string>");
				xml.append("</targetWords>");
				xml.append("</translation>");
				xml.append("</TranslationWithPosition>");
			}
			xml.append("</searchLongestMatchingTermsReturn>");
			Document responseDoc = AeInvokeHelper.string2Document(xml.toString());
			return responseDoc;
		} catch (XPathExpressionException e) {
			throw e;
		} catch (InvalidParameterException e) {
			throw e;
		} catch (ProcessFailedException e) {
			throw e;
		} catch (InvalidLanguageTagException e) {
			throw e;
		}
	}
	
	/**
	 * It returns a head word
	 * @param node
	 * @return
	 */
	private String getHeadWord(Node node) {
		String content = "";
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n.getNodeName() != null && n.getNodeName().equals("headWord")) {
				content = n.getTextContent();
			}
		}
		return content;
	}
	/**
	 * It returns a target word
	 * @param node
	 * @return
	 */
	private String getTargetWord(Node node) {
		String content = "";
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n.getNodeName() != null && n.getNodeName().equals("targetWords")) {
				NodeList childs = n.getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node child = childs.item(j);
					if (child.getNodeName() != null && child.getNodeName().equals("string")) {
						content = child.getTextContent();
						break;
					}
				}
			}
		}
		return content;
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

	private jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch
			searchTerm;
	private String logHeader = "[TemporalBilingualDictionaryWithLongestMatchSearch] ";
	private boolean logEnabled = false;
	private static QName resultQName = new QName(
			"http://localhost:8080/workflow-support/services/TemporalBilingualDictionaryWithLongestMatchSearch"
			, "searchAllLongestMatchingTermsResponse"
			);
}
