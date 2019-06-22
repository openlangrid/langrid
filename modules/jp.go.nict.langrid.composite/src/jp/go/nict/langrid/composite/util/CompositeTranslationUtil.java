package jp.go.nict.langrid.composite.util;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

public class CompositeTranslationUtil {
	public static TranslationWithPosition[] dropInvalidEntries(
			TranslationWithPosition[] entries, Morpheme[] morphs) {
//*/
		return entries;
/*/
		if(entries == null) return null;
		List<TranslationWithPosition> r = new ArrayList<>();
		for(TranslationWithPosition twp : entries) {
			Morpheme m = morphs[twp.getStartIndex()];
			if(!m.getPartOfSpeech().startsWith("noun") &&
					!m.getPartOfSpeech().equals("unknown")) continue;
			r.add(twp);
		}
		return r.toArray(new TranslationWithPosition[] {});
//*/
	}

	public static SourceAndMorphemesAndCodes mergeSmc(
			SourceAndMorphemesAndCodes firstSmc, SourceAndMorphemesAndCodes secondSmc) {
		return new SourceAndMorphemesAndCodes(
				secondSmc.getSource()
				, secondSmc.getMorphemes()
				, ArrayUtil.append(firstSmc.getCodes(), secondSmc.getCodes())
				, ArrayUtil.append(firstSmc.getHeadWords(), secondSmc.getHeadWords())
				, ArrayUtil.append(firstSmc.getTargetWords(), secondSmc.getTargetWords())
		);
	}

	public static SourceAndMorphemesAndCodes createBackwardSmc(
			SourceAndMorphemesAndCodes smc, String transResult) {
		return new SourceAndMorphemesAndCodes(
				transResult
				, null
				, smc.getCodes()
				, smc.getTargetWords()
				, smc.getHeadWords()
				);
	}

	public static String translateUntilNocodeAppears(
			String sl, String tl, SourceAndMorphemesAndCodes joinedSmc, TranslationService trans)
	throws LanguagePairNotUniquelyDecidedException, UnsupportedLanguagePairException, AccessLimitExceededException, InvalidParameterException, NoAccessPermissionException, ProcessFailedException, NoValidEndpointsException, ServerBusyException, ServiceNotActiveException, ServiceNotFoundException {
		String fwTranslationResult = null;
		String tsrc = joinedSmc.getSource();
		String[] codes = joinedSmc.getCodes();
		String[] headWords = joinedSmc.getHeadWords();
		String[] targetWords = joinedSmc.getTargetWords();
		if(!sl.equals(tl)) {
//			System.out.println("translate: " + tsrc);
			int retryCount = 0;
			while(true) {
				fwTranslationResult = trans.translate(sl, tl, tsrc);
				int n = codes.length;
				boolean retry = false;
				for(int i = 0; i < n; i++) {
					String c = codes[i];
					if(!fwTranslationResult.contains(c)) {
						if(retryCount > 8) {
							tsrc = tsrc.replace(c, headWords[i]);
							codes = ArrayUtil.delete(codes, i);
							headWords = ArrayUtil.delete(headWords, i);
							targetWords = ArrayUtil.delete(targetWords, i);
						} else {
							String newCode = StringUtil.generateCode(joinedSmc.getHeadWords()[i], i);
							tsrc = tsrc.replace(c, newCode);
							codes[i] = newCode;
						}
						retry = true;
						retryCount++;
//						System.out.println("retry with new src: " + tsrc);
						break;
					}
				}
				if(retry) continue;
				break;
			}
		}
		if(fwTranslationResult == null){
			fwTranslationResult = tsrc;
		}
		joinedSmc.setSource(tsrc);
		joinedSmc.setCodes(codes);
		joinedSmc.setHeadWords(headWords);
		joinedSmc.setTargetWords(targetWords);
		return fwTranslationResult;
	}
}
