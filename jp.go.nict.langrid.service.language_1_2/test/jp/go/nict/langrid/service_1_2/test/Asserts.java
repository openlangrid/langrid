package jp.go.nict.langrid.service_1_2.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.test.AssertUtil;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.dependencyparser.typed.Chunk;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;
import junit.framework.Assert;

public class Asserts {
	public static void assertChunkArrayEquals(
			Chunk[] expected, Chunk[] actual)
	{
	}

	public static void assertMorphemeArrayEquals(
			Morpheme[] expected, Morpheme[] actual){
		AssertUtil.assertArrayEquals(expected, actual);
	}

	public static void assertTranslationArrayEquals(
			Translation[] expected, Translation[] actual){
		Map<String, Set<Set<String>>> headToTrans = new HashMap<String, Set<Set<String>>>();
		for(Translation t : expected){
			Set<Set<String>> trans = headToTrans.get(t.getHeadWord());
			if(trans == null){
				trans = new HashSet<Set<String>>();
				headToTrans.put(t.getHeadWord(), trans);
			}
			trans.add(new HashSet<String>(Arrays.asList(t.getTargetWords())));
		}
		for(int i = 0; i < actual.length; i++){
			Translation a = actual[i];
			Set<Set<String>> elements = headToTrans.get(a.getHeadWord());
			if(elements == null){
				Assert.fail(a + " is missing.");
			}
			Set<String> actualTargetWords = new HashSet<String>(
					Arrays.asList(a.getTargetWords()));
			Iterator<Set<String>> it = elements.iterator();
			boolean found = false;
			while(it.hasNext()){
				Set<String> e = it.next();
				if(e.equals(actualTargetWords)){
					found = true;
					it.remove();
					break;
				}
			}
			if(!found){
				Assert.fail("<" + a + "> is redundant");
			} else if(elements.size() == 0){
				headToTrans.remove(a.getHeadWord());
			}
		}
		if(headToTrans.size() > 0){
			Assert.fail("<" + headToTrans.values() + "> is missing"); 
		}
	}

	private static String getFailMessage(Object expected, Object actual){
		return "expected:<" + expected
			+ "> but was:<" + actual + ">";
	}

	private static <T> String getFailMessage(T[] expected, T[] actual){
		return "expected:<" + Arrays.toString(expected)
			+ "> but was:<" + Arrays.toString(actual) + ">";
	}
}
