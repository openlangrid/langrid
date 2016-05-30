package jp.go.nict.langrid.service_1_2.test;

import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.dependencyparser.typed.Chunk;
import jp.go.nict.langrid.service_1_2.dependencyparser.typed.Dependency;
import jp.go.nict.langrid.service_1_2.dependencyparser.typed.DependencyLabel;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

public class Builder {
	public static Chunk chunk(
			String chunkId, Morpheme[] morphemes, Dependency dependency)
	{
		return new Chunk(chunkId, morphemes, dependency);
	}

	public static Dependency dependency(
			DependencyLabel label, String headChunkId)
	{
		return new Dependency(label, headChunkId);
	}

	public static Morpheme morpheme(String word, String lemma, PartOfSpeech pos){
		return new Morpheme(word, lemma, pos);
	}

	public static Translation translation(String headWord, String... targetWords){
		return new Translation(headWord, targetWords);
	}

	public static void printChunkDefinition(Chunk[] chunks){
		for(Chunk c : chunks){
			System.out.print("chunk(\"");
			System.out.print(c.getChunkId());
			System.out.println("\", array(");
			printMorpemeDefinition(c.getMorphemes());
			System.out.println(")");
			System.out.print(", dependency(");
			System.out.print(c.getDependency().getLabel().name());
			System.out.print(", \"");
			System.out.print(c.getDependency().getHeadChunkId());
			System.out.println("\")),");
		}
	}

	public static void printMorpemeDefinition(Morpheme[] morphs){
		for(Morpheme m : morphs){
			System.out.print("morpheme(\"");
			System.out.print(m.getWord());
			System.out.print("\", \"");
			System.out.print(m.getLemma());
			System.out.print("\", ");
			System.out.print(m.getPartOfSpeech().name());
			System.out.println("),");
		}
	}

	public static void printTranslationDefinition(Translation[] translations){
		for(Translation m : translations){
			System.out.print("translation(\"");
			System.out.print(m.getHeadWord());
			System.out.print("\"");
			for(String s : m.getTargetWords()){
				System.out.print(", \"");
				System.out.print(s);
				System.out.print("\"");
			}
			System.out.println("),");
		}
	}
}
