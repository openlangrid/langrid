package jp.go.nict.langrid.client.soap.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import jp.go.nict.langrid.client.soap.io.SoapResponseParser;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.lang.ClassResource;
import jp.go.nict.langrid.commons.lang.ClassResourceLoader;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

import org.junit.Assert;
import org.junit.Test;

public class SoapRequestParserTest{
	@Test
	public void test_translationService() throws Exception{
		InputStream is = new ByteArrayInputStream(translationService_res);
		try{
			Trio<Collection<RpcHeader>, RpcFault, String> ret = SoapResponseParser.parseSoapResponse(
					String.class, "translate", is, new Converter());
			Assert.assertEquals("こんにちは", ret.getThird());
		} finally{
			is.close();
		}
	}

	@Test
	public void test_morphologicalAnalysisService() throws Exception{
		InputStream is = new ByteArrayInputStream(morphologicalAnalysisService_res);
		try{
			Trio<Collection<RpcHeader>, RpcFault, Morpheme[]> ret = SoapResponseParser.parseSoapResponse(
					Morpheme[].class, "analyze", is, new Converter());
			for(Morpheme m : ret.getThird()){
				System.out.println(m);
			}
		} finally{
			is.close();
		}
	}

	@ClassResource(path="TranslationService.res")
	private byte[] translationService_res;
	@ClassResource(path="MorphologicalAnalysisService.res")
	private byte[] morphologicalAnalysisService_res;
	{
		try {
			ClassResourceLoader.load(this);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
