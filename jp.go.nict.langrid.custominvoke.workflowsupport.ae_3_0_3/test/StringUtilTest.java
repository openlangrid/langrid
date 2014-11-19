import jp.go.nict.langrid.custominvoke.workflowsupport.util.StringUtil;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import junit.framework.TestCase;


public class StringUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testEscapeCharacterRestructing() {
		Morpheme[] morphemes = new Morpheme[28];
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
		morphemes[18] = new Morpheme("Koyama", "Koyama", "noun");
		morphemes[19] = new Morpheme("&", "&", "noun");
		morphemes[20] = new Morpheme("#", "#", "noun");
		morphemes[21] = new Morpheme("39", "39", "noun");
		morphemes[22] = new Morpheme(";", ";", "noun");
		morphemes[23] = new Morpheme("HomePage", "HomePage", "noun");
		morphemes[24] = new Morpheme("&", ".", "noun");
		morphemes[25] = new Morpheme("quot", "&quot", "noun");
		morphemes[26] = new Morpheme(";", ";", "noun");
		morphemes[27] = new Morpheme("I&#39;m peacefully.", "I&#39;m peacefully.", "other");
		Morpheme[] results = StringUtil.escapeCharacterRestructing(morphemes);
		for (Morpheme m : results) {
			System.out.println(m.getWord());
		}

	}

	public void testMarkingWord() {
		String str = "Hello World!";
		for (int i = 0; i < 10; i++)
		System.out.println(StringUtil.markingWord(str, i));
	}
}
