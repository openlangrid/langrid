package jp.go.nict.langrid.language;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class LanguageTest {
	@Test
	public void test() throws Throwable{
		Assert.assertTrue(LangridLanguageTags.values().contains(LangridLanguageTags.zh_HK));
	}

	@Test
	public void test_zhhk() throws Throwable{
		Assert.assertEquals("繁体中国語(香港)", LangridLanguageTags.zh_HK.getLocalizedName(Locale.JAPAN));
		Assert.assertEquals("Chinese(Traditional, Hong kong SAR)", LangridLanguageTags.zh_HK.getLocalizedName(Locale.ENGLISH));
		Assert.assertEquals("繁体中国語", LangridLanguageTags.zh_TW.getLocalizedName(Locale.JAPAN));
		Assert.assertEquals("Chinese(Traditional)", LangridLanguageTags.zh_TW.getLocalizedName(Locale.ENGLISH));
	}
}
