package jp.go.nict.langrid.custominvoke.workflowsupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jp.go.nict.langrid.custominvoke.workflowsupport.util.AeInvokeHelper;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.Scanner;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.ScannerFactory;

public class TemporalBilingualDictionaryWithLongestMatchSearch
extends AbstractCustomInvokeService
{
	public TemporalBilingualDictionaryWithLongestMatchSearch() {
//		logger.severe("+++ construct +++");
		super.setPortTypeName("TemporalBilingualDictionaryWithLongestMatchSearch");
	}
	
	public Collection<TranslationWithPosition> doSearchAllLongestMatchingTerms(
			Language language, Morpheme[] morphemes, Translation[] translations) throws InvalidParameterException, ProcessFailedException {
		Collection<TranslationWithPosition> result = new ArrayList<TranslationWithPosition>();
		if (translations == null || translations.length == 0) {
			return result;
		}
		Scanner scanner = ScannerFactory.getInstance(language, Scanner.TYPE_LONGEST_MATCH);
		for (int i = 0; i < morphemes.length; i++) {
			int ret = scanner.doScan(language, i, morphemes, translations, result);
			if (ret != -1) {
				i = ++ret;
			}
		}
		return result;
	}

	/**
	 * 最終更新日付を返す
	 * @return 更新日付
	 * @throws ProcessFailedException
	 */
	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro("$Date: 2010-10-03 18:49:40 +0900 (Sun, 03 Oct 2010) $");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IAeWebServiceResponse invoke(IAeInvoke invokeRequest,
			String queryData, QName qName) {
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
			Document responseDoc = searchTemporalDictionary(headLang, reqMsgParts);
			Map respMsgParts = new HashMap();
			respMsgParts.put("searchAllLongestMatchingTermsReturn", responseDoc);
			IAeWebServiceResponse response = AeInvokeHelper.createOutputMessage(qName, respMsgParts);
			return response;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			e.printStackTrace();
		} catch (InvalidLanguageTagException e) {
			e.printStackTrace();
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
	public Document searchTemporalDictionary(String headLang, Map<?, ?> reqMsgParts) throws XPathExpressionException, InvalidParameterException, ProcessFailedException, InvalidLanguageTagException {
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
						System.out.println(lemma);
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
			Collection<TranslationWithPosition> results = doSearchAllLongestMatchingTerms(new Language(headLang), morphemes, translations.toArray(new Translation[]{}));
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

	@Override
	protected Port getPort(Service service) {
		super.setPortTypeName("TemporalBilingualDictionaryWithLongestMatchSearch");
		Port port = service.getPort("TemporalBilingualDictionaryWithLongestMatchSearch");
		return port;
	}
	private static Logger logger = Logger.getLogger(TemporalBilingualDictionaryWithLongestMatchSearch.class.getName());

}
