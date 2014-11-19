/*
 * $Id: Services_1_2.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.testresource;

import static jp.go.nict.langrid.testresource.InstanceType.BPEL;
import static jp.go.nict.langrid.testresource.InstanceType.EXTERNAL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.testresource.loader.BeanPropertyReader;

/**
 * プロパティファイル及びインスタンスを定義する列挙型。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1162 $
 */
public enum Services_1_2 implements Services {
	/**
	 * クロスランゲージ翻訳サービス。
	 */
	CLWT("ws_1_2/CLWT.prop", "ws_1_2/CLWT.ler", EXTERNAL),

	/**
	 * JArabic辞書サービス。
	 */
	JARABIC("ws_1_2/JArabic.prop"),

	/**
	 * JServer翻訳サービス。
	 */
	JSERVER("ws_1_2/JServer.prop", "ws_1_2/JServer.ler", EXTERNAL),

	/**
	 * JServerを使った折り返し翻訳。
	 */
	JSERVER_BACKTRANS(
			"ws_1_2/JServerBacktranslation.prop",
			"ws_1_2/JServerBacktranslation.lbr",
			BPEL
	),

	/**
	 * 複数言語対応JServer/CLWT翻訳。
	 */
	JSERVERJSERVER_JA2HOP(
			"ws_1_2/JServerJServer_ja2hop.prop"
			, "ws_1_2/JServerJServer_ja2hop.lbr"
			, BPEL
	),

	/**
	 * 京大JServer
	 */
	KYOTOU_JSERVER("ws_1_2/KyotoUJServer.prop", "ws_1_2/KyotoUJServer.ler", EXTERNAL),

	/**
	 * モック隣接応答対サービス。
	 */
	MOCK_ADJACENCYPAIR("ws_1_2/AdjacencyPair.prop", "ws_1_2/AdjacencyPair.ler", EXTERNAL),

	/**
	 * モック対訳辞書サービス。
	 */
	MOCK_BILINGUALDICTIONARY("ws_1_2/BilingualDictionary.prop", "ws_1_2/BilingualDictionary.ler", EXTERNAL),

	/**
	 * モック辞書サービス。
	 */
	MOCK_DICTIONARY("ws_1_2/Dictionary.prop", "ws_1_2/Dictionary.ler", EXTERNAL),

	/**
	 * モック翻訳サービス。
	 */
	MOCK_MORPHOLOGICALANALYSIS("ws_1_2/MorphologicalAnalysis.prop", "ws_1_2/MorphologicalAnalysis.ler", EXTERNAL),

	/**
	 * モック多ホップ翻訳サービス。
	 */
	MOCK_MULTIHOPTRANSLATION("ws_1_2/MultihopTranslation.prop", "ws_1_2/MultihopTranslation.ler", EXTERNAL),
	/**
	 * モック対訳サービス。
	 */
	MOCK_PARALLELTEXT("ws_1_2/ParallelText.prop", "ws_1_2/ParallelText.ler", EXTERNAL),

	/**
	 * モック言い換えサービス。
	 */
	MOCK_PARAPHRASE("ws_1_2/Paraphrase.prop", "ws_1_2/Paraphrase.ler", EXTERNAL),

	/**
	 * モック類似度計算サービス。
	 */
	MOCK_SIMILARITYCALCULATION("ws_1_2/SimilarityCalculation.prop", "ws_1_2/SimilarityCalculation.ler", EXTERNAL),

	/**
	 * モック翻訳サービス。
	 */
	MOCK_TRANSLATION("ws_1_2/Translation.prop", "ws_1_2/Translation.ler", EXTERNAL),

	/**
	 * モック翻訳サービス。長時間無応答。
	 */
	MOCK_TRANSLATION_LONGTIMENORESPONSE(
			"ws_1_2/TranslationLongTimeNoResponse.prop"
			, "ws_1_2/TranslationLongTimeNoResponse.wsdl", EXTERNAL),

	/**
	 * モック翻訳サービス。
	 */
	MOCK_TRANSLATION8079("ws_1_2/Translation8079.prop", "ws_1_2/Translation8079.ler", EXTERNAL),

	/**
	 * MockTranslationを使った折り返し翻訳。
	 */
	MOCKTRANSLATION_BACKTRANS(
			"ws_1_2/TranslationBacktrans.prop",
			"ws_1_2/TranslationBacktrans.lbr",
			BPEL
	),

	/**
	 * MockTranslationを使った折り返し翻訳。ポート8079にアクセスする。
	 */
	MOCKTRANSLATION_BACKTRANS8079(
			"ws_1_2/TranslationBacktrans8079.prop",
			"ws_1_2/TranslationBacktrans8079.lbr",
			BPEL
	),
	/**
	 * 複数言語対応形態素解析。
	 */
	MULTILINGUALMORPHOLOGICALANALYSIS(
			"ws_1_2/MultilingualMorphologicalAnalysis.prop"
			, "ws_1_2/MultilingualMorphologicalAnalysis.lbr"
			, BPEL
	),
	/**
	 * 複数言語対応JServer/CLWT翻訳。
	 */
	MULTILINGUALTRANSLATIONJSERVER(
			"ws_1_2/MultilingualTranslationJServer.prop"
			, "ws_1_2/MultilingualTranslationJServer.lbr"
			, BPEL
	),

	/**
	 * 抽象折り返し翻訳サービス。
	 */
	ABSTRACTBACKTRANSLATION("ws_1_2/abstract/Backtranslation.prop"
	, "ws_1_2/abstract/Backtranslation.xml", EXTERNAL),

	/**
	 * 抽象対訳辞書翻訳サービス。
	 */
	ABSTRACTBILINGUALDICTIONARY("ws_1_2/abstract/BilingualDictionary.prop"
	, "ws_1_2/abstract/BilingualDictionary.xml", EXTERNAL),

	/**
	 * 抽象辞書サービス。
	 */
	ABSTRACTDICTIONARY("ws_1_2/abstract/Dictionary.prop"
	, "ws_1_2/abstract/Dictionary.xml", EXTERNAL),

	/**
	 * 抽象抽出対応対訳辞書サービス。
	 */
	ABSTRACTBILINGUALDICTIONARYHEADWORDSEXTRACTION(
			"ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction.prop"
			, "ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction.xml"
			, EXTERNAL),

	/**
	 * 抽象抽出対応対訳辞書サービス2。
	 */
	ABSTRACTBILINGUALDICTIONARYHEADWORDSEXTRACTION2(
			"ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction2.prop"
			, "ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction2.xml"
			, EXTERNAL),

	/**
	 * 抽象抽出対応対訳辞書サービス3。
	 */
	ABSTRACTBILINGUALDICTIONARYHEADWORDSEXTRACTION3(
			"ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction3.prop"
			, "ws_1_2/abstract/BilingualDictionaryHeadwordsExtraction3.xml", EXTERNAL
			),

	/**
	 * 抽象形態素解析サービス。
	 */
	ABSTRACTMORPHOLOGICALANALYSIS(
			"ws_1_2/abstract/MorphologicalAnalysis.prop"
			, "ws_1_2/abstract/MorphologicalAnalysis.xml"
			, EXTERNAL),

	/**
	 * 抽象形態素解析サービス2。
	 */
	ABSTRACTMORPHOLOGICALANALYSIS2(
			"ws_1_2/abstract/MorphologicalAnalysis2.prop"
			, "ws_1_2/abstract/MorphologicalAnalysis2.xml"
			, EXTERNAL),

	/**
	 * 抽象形態素解析サービス3。
	 */
	ABSTRACTMORPHOLOGICALANALYSIS3(
			"ws_1_2/abstract/MorphologicalAnalysis3.prop"
			, "ws_1_2/abstract/MorphologicalAnalysis3.xml"
			, EXTERNAL),

	/**
	 * 抽象多ホップ翻訳サービス。
	 */
//	ABSTRACTMULTIHOPTRANSLATION(
//			"ws_1_2/abstract/MultihopTranslation.prop"
//			, "ws_1_2/abstract/MultihopTranslation.xml", EXTERNAL),

	/**
	 * 抽象用例対訳サービス。
	 */
//	ABSTRACTPARALLELTEXT(
//			"ws_1_2/abstract/ParallelText.prop"
//			, "ws_1_2/abstract/ParallelText.xml", EXTERNAL),

	/**
	 * 抽象言い換えサービス。
	 */
//	ABSTRACTPARAPHRASE(
//			"ws_1_2/abstract/Paraphrase.prop"
//			, "ws_1_2/abstract/Paraphrase.xml", EXTERNAL),

	/**
	 * 抽象類似度計算サービス。
	 */
//
//			"ws_1_2/abstract/SimilarityCalculation.prop"
//			, "ws_1_2/abstract/SimilarityCalculation.xml", EXTERNAL),

	/**
	 * 抽象翻訳サービス。
	 */
	ABSTRACTTRANSLATION(
			"ws_1_2/abstract/Translation.prop"
			, "ws_1_2/abstract/Translation.xml", EXTERNAL),

	/**
	 * 専門翻訳サービス。
	 */
	SPECIALIZEDTRANSLATION(
			"ws_1_2/SpecializedTranslation/SpecializedTranslation.prop"
			, "ws_1_2/SpecializedTranslation/contents", BPEL),

	/**
	 * 最適化版辞書翻訳。翻訳インタフェース/一時辞書有り翻訳インタフェース共用
	 */
	TRANSLATIONCOMBINEDWITHBILINGUALDICTIONARYWITHLONGESTMATCHSEARCH(
			"ws_1_2/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch.prop"
			, "ws_1_2/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch/contents", BPEL),

	/**
	 * 最適化版辞書翻訳。及び、ハイライト機能翻訳インタフェース/一時辞書有り翻訳インタフェース共用
	 */
	TRANSLATIONCOMBINEDWITHBILINGUALDICTIONARYWITHLONGESTMATCHSEARCHMARKINGREPLACEDWORDS(
			"ws_1_2/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchMarkingReplacedWords/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchMarkingReplacedWords.prop"
			, "ws_1_2/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchMarkingReplacedWords/contents", BPEL),
	;

	private static Logger logger = Logger.getLogger(Services_1_2.class.getName());

	private static void invoke(Object instance, String methodName
			, Class<?>[] parameterTypes, Object... parameters){
		try{
			Method m = instance.getClass().getMethod(methodName, parameterTypes);
			m.invoke(instance, parameters);
		} catch(IllegalAccessException e){
			logger.log(
					Level.WARNING
					, "exception while invoking setInstance."
					, e);
		} catch(InvocationTargetException e){
			logger.log(
					Level.WARNING
					, "exception while invoking setInstance."
					, e);
		} catch(NoSuchMethodException e){
		}
	}

	private ResourceLoader instanceLoader;

	private InstanceType instanceType;

	private ResourceLoader propertyLoader;

	private Services_1_2(String propertyPath){
		this.propertyLoader = new ResourceLoader(propertyPath);
		this.instanceType = EXTERNAL;
	}

	private Services_1_2(String propertyPath, String instancePath, InstanceType instanceType){
		this.propertyLoader = new ResourceLoader(propertyPath);
		if(instanceType.equals(EXTERNAL) && !instancePath.endsWith("ler")){
			this.instanceLoader = new LERedResourceLoader(instancePath);
		} else if(instanceType.equals(BPEL) && !instancePath.endsWith("lbr")){
			this.instanceLoader = new LBRedResourceLoader(instancePath);
		} else{
			this.instanceLoader = new ResourceLoader(instancePath);
		}
		this.instanceType = instanceType;
	}

	private Services_1_2(String dir, String propertyPath, String instancePath
			, InstanceType instanceType){
		this.propertyLoader = new ResourceLoader(dir + "/" + propertyPath);
		this.instanceLoader = new LBRedResourceLoader(dir + "/" + instancePath);
		this.instanceType = instanceType;
	}

		public InstanceType getInstanceType(){
		return instanceType;
	}

	public String getInstancePath() {
		if(instanceLoader == null){
			return "";
		} else{
			return instanceLoader.getPath();
		}
	}

	public InputStream loadInstance() throws IOException{
		if(instanceLoader == null){
			return new ByteArrayInputStream(new byte[]{});
		} else{
			return instanceLoader.load();
		}
	}

	public String getPropertyPath() {
		return propertyLoader.getPath();
	}

	public InputStream loadProperties() throws IOException{
		return propertyLoader.load();
	}

	/**
	 * 指定されたbeanにプロパティをロードする。
	 * instancePathが設定されている場合、setInstanceメソッドにinstanceのバイナリが、
	 * setInstanceSizeメソッドにinstanceのサイズが渡される。
	 * 各々のメソッドを持たない場合、単に無視される。
	 * @param <T> beanのクラス
	 * @param bean プロパティを読み込むbean
	 * @param beanUtils プロパティの設定に使用するBeanUtilsBean
	 * @param tuplePropertyNames タプルとして記述されているプロパティ名
	 * @return bean
	 * @throws IOException 入力に失敗した
	 */
	public <T> T loadTo(T bean, Set<String> tuplePropertyNames)
	throws IOException{
		T b = new BeanPropertyReader(CharsetUtil.newUTF8Decoder())
			.read(propertyLoader.load(), bean, tuplePropertyNames);
		if(instanceLoader != null){
			byte[] instance = StreamUtil.readAsBytes(instanceLoader.load());
			invoke(bean, "setInstance", new Class<?>[]{int.class}, instance);
			invoke(bean, "setInstanceSize", new Class<?>[]{int.class}, instance.length);
		} else{
			invoke(bean, "setInstance", new Class<?>[]{byte[].class}, new byte[]{});
			invoke(bean, "setInstanceSize", new Class<?>[]{int.class}, 0);
		}
		invoke(
				bean, "setInstanceType", new Class<?>[]{String.class}
				, instanceType.name());
		return b;
	}
}
