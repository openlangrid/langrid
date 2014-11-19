import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.EnglishAnalysis4TreeTagger;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import junit.framework.TestCase;


public class EnglishAnalysis4TreeTaggerTest extends TestCase {

	public void testDoConstructSMC() {
		EnglishAnalysis4TreeTagger english = new EnglishAnalysis4TreeTagger();
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
		morphemes[12] = new Morpheme("I", "I", "noun");
		morphemes[13] = new Morpheme("climbed", "climb", "noun");
		morphemes[14] = new Morpheme("Mt", "Mt", "noun");
		morphemes[15] = new Morpheme(".", ".", "noun");
		morphemes[16] = new Morpheme("Fuji", "Fuji", "noun");
		morphemes[17] = new Morpheme(".", ".", "noun");
		try {
			SourceAndMorphemesAndCodes smc = english.doConstructSMC(StringUtil.escapeCharacterRestructing(morphemes), positionMap);
			System.out.println(smc.getSource());
			Morpheme[] results = smc.getMorphemes();
			for (Morpheme m : results) {
				System.out.println(m.getWord());
			}
		} catch (LanguageNotUniquelyDecidedException e) {
			e.printStackTrace();
		} catch (UnsupportedLanguageException e) {
			e.printStackTrace();
		} catch (AccessLimitExceededException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (NoAccessPermissionException e) {
			e.printStackTrace();
		} catch (NoValidEndpointsException e) {
			e.printStackTrace();
		} catch (ProcessFailedException e) {
			e.printStackTrace();
		} catch (ServerBusyException e) {
			e.printStackTrace();
		} catch (ServiceNotActiveException e) {
			e.printStackTrace();
		} catch (ServiceNotFoundException e) {
			e.printStackTrace();
		}
	}

}
