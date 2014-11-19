import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Holder;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import jp.go.nict.langrid.custominvoke.workflowsupport.AeInvokeHelper;
import jp.go.nict.langrid.custominvoke.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import junit.framework.TestCase;


public class TemporalBilingualDictionaryWithLongestMatchSearchTest extends
		TestCase {

	public TemporalBilingualDictionaryWithLongestMatchSearchTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSearchTemporalDictionary() {
		TemporalBilingualDictionaryWithLongestMatchSearch temporal = new TemporalBilingualDictionaryWithLongestMatchSearch();
		String xml = ""
				+ "<temporalDict xmlns:aensTYPE=\"http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/\" xmlns:ns4=\"http://langrid.nict.go.jp/process/binding/tree\""
				+ " xmlns:ns5=\"http://translation.ws_1_2.wrapper.langrid.nict.go.jp\""
				+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"aensTYPE:ArrayOf_Translation\">"
				+ "<Translation>"
				+ "	<headWord></headWord>"
				+ "	<targetWords><string>Eng1</string>"
				+ "	</targetWords>"
				+ "</Translation>"
				+ "<Translation>"
				+ "	<headWord>store</headWord>"
				+ "	<targetWords>"
				+ "		<string>Eng2</string>"
				+ "		<string>Eng2-2</string>"
				+ "	</targetWords>"
				+ "</Translation>"
				+ "<Translation>"
				+ "	<headWord>close</headWord>"
				+ "	<targetWords>"
				+ "		<string>Q&amp;A</string>"
				+ "	</targetWords>"
				+ "</Translation>"
				+ "</temporalDict>";
		Document doc = null;
		doc = AeInvokeHelper.string2Document(xml);
		Map<String, Document> m = new HashMap<String, Document>();
		m.put("temporalDict", doc);
		String xml2 = ""
			+ "   <morphemes xmlns:aensTYPE=\"http://localhost:8080/wrapper-mock-1.2/services/AbstractMorphologicalAnalysis\" xmlns:ns2=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"aensTYPE:ArrayOf_tns1_Morpheme\">"
			+ "      <Morpheme xmlns:ns3=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>the</lemma>"
			+ "         <partOfSpeech>other</partOfSpeech>"
			+ "         <word>The</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns4=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>store</lemma>"
			+ "         <partOfSpeech>noun.common</partOfSpeech>"
			+ "         <word>store</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns7=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>close</lemma>"
			+ "         <partOfSpeech>verb</partOfSpeech>"
			+ "         <word>closes</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns6=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>at</lemma>"
			+ "         <partOfSpeech>other</partOfSpeech>"
			+ "         <word>at</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns8=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>twenty</lemma>"
			+ "         <partOfSpeech>other</partOfSpeech>"
			+ "         <word>twenty</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns9=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>o'clock</lemma>"
			+ "         <partOfSpeech>adverb</partOfSpeech>"
			+ "         <word>o'clock</word>"
			+ "      </Morpheme>"
			+ "      <Morpheme xmlns:ns5=\"http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/\">"
			+ "         <lemma>.</lemma>"
			+ "         <partOfSpeech>other</partOfSpeech>"
			+ "         <word>.</word>"
			+ "      </Morpheme>"
			+ "   </morphemes>"
			+ "";

		Document doc2 = null;
		doc2 = AeInvokeHelper.string2Document(xml2);
		m.put("morphemes", doc2);
		Document resultDoc;
		try {
			resultDoc = temporal.searchTemporalDictionary("ja", m, new Holder<Long>());
			System.out.println(AeInvokeHelper.document2String(resultDoc));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			e.printStackTrace();
		} catch (InvalidLanguageTagException e) {
			e.printStackTrace();
		}
	}

}
