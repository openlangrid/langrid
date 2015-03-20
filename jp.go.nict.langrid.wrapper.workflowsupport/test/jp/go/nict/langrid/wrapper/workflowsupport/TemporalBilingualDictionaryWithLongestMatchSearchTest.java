package jp.go.nict.langrid.wrapper.workflowsupport;

import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import org.junit.Assert;

import org.junit.Test;

public class TemporalBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void test_empty() throws Throwable{
		Assert.assertEquals(
				0,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
					"ja",
					new Morpheme[]{},
					new Translation[]{}).length
				);
	}

	@Test
	public void test() throws Throwable{
		Assert.assertEquals(
				1,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
					"ja",
					new Morpheme[]{
							new Morpheme("こんにちは", "こんにちは", "other"),
							new Morpheme("世界", "世界", "noun.other")
					},
					new Translation[]{
							new Translation("世界", new String[]{"world"})
					}
					).length
				);
	}

	@Test
	public void test_emptyTargets() throws Throwable{
		String lang = "ja";
		Morpheme[] morphs = {
				new Morpheme("こんにちは", "こんにちは", "other"),
				new Morpheme("世界", "世界", "noun.other")
		};
		Assert.assertEquals(
				0,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
						lang, morphs, new Translation[]{
								new Translation("世界", null)
						}
						).length
				);
		Assert.assertEquals(
				0,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
						lang, morphs, new Translation[]{
								new Translation("世界", new String[]{})
						}
						).length
				);
		Assert.assertEquals(
				0,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
						lang, morphs, new Translation[]{
								new Translation("世界", new String[]{null})
						}
						).length
				);
		Assert.assertEquals(
				0,
				new TemporalBilingualDictionaryWithLongestMatchSearch().searchAllLongestMatchingTerms(
						lang, morphs, new Translation[]{
								new Translation("世界", new String[]{""})
						}
						).length
				);
	}
}
