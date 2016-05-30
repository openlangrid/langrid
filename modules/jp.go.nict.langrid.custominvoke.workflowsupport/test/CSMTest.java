import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.custominvoke.workflowsupport.ConstructSourceAndMorphemesAndCodes;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import junit.framework.TestCase;


public class CSMTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testDoConstructSMC() {
		Map<Integer, TranslationWithPosition> positionMap = new HashMap<Integer, TranslationWithPosition>();
		Morpheme[] morphemes = new Morpheme[18];
		morphemes[0] = new Morpheme("I", "I", "noun");
		morphemes[1] = new Morpheme("&#39", "&#39", "noun");
		morphemes[2] = new Morpheme(";", ";", "noun");
		morphemes[3] = new Morpheme("m", "be", "noun");
		morphemes[4] = new Morpheme("peacefully", "peacefully", "noun");
		morphemes[5] = new Morpheme(".", ".", "noun");
		morphemes[6] = new Morpheme("I", "I", "noun");
		morphemes[7] = new Morpheme("'m", "be", "noun");
		morphemes[8] = new Morpheme("sorry", "sorry", "noun");
		morphemes[9] = new Morpheme(".", ".", "noun");
		morphemes[10] = new Morpheme("&quot", "&quot", "noun");
		morphemes[11] = new Morpheme(";", ";", "noun");
		morphemes[12] = new Morpheme("NICT", "NICT", "noun");
		morphemes[13] = new Morpheme("&quot", "&quot", "noun");
		morphemes[14] = new Morpheme(";", ";", "noun");
		morphemes[15] = new Morpheme("is", "be", "noun");
		morphemes[16] = new Morpheme("happy", "happy", "noun");
		morphemes[17] = new Morpheme(".", ".", "noun");
//		ConstructSourceAndMorphemesAndCodes csm = new ConstructSourceAndMorphemesAndCodes();
	}

}
