package jp.go.nict.langrid.wrapper.workflowsupport;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;

import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class MergeMorphemesAndTranslationWithPositions {
	public Morpheme[] merge(Morpheme[] morphemes
			, TranslationWithPosition[] translationWithPositions){
		return doMerge(morphemes, Arrays.asList(translationWithPositions));
	}

	public Morpheme[] doMerge(Morpheme[] morphemes
			, Collection<TranslationWithPosition> translationWithPositions){
		BitSet bs = new BitSet(morphemes.length);
		int nc = 0;
		for(TranslationWithPosition t : translationWithPositions){
			int begin = t.getStartIndex();
			int end = begin + t.getNumberOfMorphemes();
			if(begin == -1 || begin >= morphemes.length) continue;
			if(end == -1 || end >= morphemes.length) continue;
			int nb = bs.nextSetBit(begin); 
			if(nb != -1 && nb < end){
				continue;
			}
			String[] target = t.getTranslation().getTargetWords();
			if(target != null && target.length > 0){
				bs.set(begin, end);
				morphemes[begin] = new Morpheme(target[0], target[0], "other");
				for(int i = (begin + 1); i < end; i++){
					morphemes[i] = null;
					nc++;
				}
			}
		}
		Morpheme[] ret = morphemes;
		if(nc > 0){
			ret = new Morpheme[morphemes.length - nc];
			int c = 0;
			for(Morpheme m : morphemes){
				if(m != null){
					ret[c++] = m;
				}
			}
		}
		return ret;
	}
}
