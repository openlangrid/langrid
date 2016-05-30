package jp.go.nict.langrid.service_1_2.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.conceptdictionary.typed.Concept;
import jp.go.nict.langrid.service_1_2.conceptdictionary.typed.ConceptualRelation;
import jp.go.nict.langrid.service_1_2.conceptdictionary.typed.Gloss;
import jp.go.nict.langrid.service_1_2.conceptdictionary.typed.Lemma;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LanguageServiceClientTestCase extends TestCase{
	public static Concept concept(
			String conceptId, PartOfSpeech partOfSpeech
			, Lemma[] synset, Gloss[] glosses
			, ConceptualRelation[] relation){
		return new Concept(
				"urn:langrid:edr:concept:" + partOfSpeech.getExpression()
				+ ":" + conceptId, partOfSpeech
				, synset, glosses, relation);
	}

	public static Lemma[] synsets(
			Language language, String... headWords){
		return ArrayUtil.collect(
				headWords, Lemma.class
				, new StringToLemmaTransformer(language) 
				);
	}

	public static Gloss[] glosses(
			Language language, String... glosses){
		return ArrayUtil.collect(
				glosses, Gloss.class
				, new StringToGlossTransformer(language) 
				);
	}

	public static ConceptualRelation[] relations(ConceptualRelation... relations){
		return relations;
	}

	private static class StringToLemmaTransformer
	implements Transformer<String, Lemma>{
		public StringToLemmaTransformer(Language language){
			this.language = language;
		}
		public Lemma transform(String value) throws TransformationException {
			return new Lemma(value, language);
		}
		private Language language;
	}

	private static class StringToGlossTransformer
	implements Transformer<String, Gloss>{
		public StringToGlossTransformer(Language language){
			this.language = language;
		}
		public Gloss transform(String value) throws TransformationException {
			return new Gloss(value, language);
		}
		private Language language;
	}

	public static void assertConceptArrayEquals(
			Concept[] expected, Concept[] actual)
	throws Exception
	{
		Map<String, ConceptForCompare> idToConcept = new HashMap<String, ConceptForCompare>();
		for(Concept c : expected){
			idToConcept.put(c.getConceptId(), new ConceptForCompare(c));
		}
		for(Concept a : actual){
			ConceptForCompare e = idToConcept.get(a.getConceptId());
			if(e == null){
				Assert.fail(a + " is redundant.");
			}
			Assert.assertEquals(e, new ConceptForCompare(a));
			idToConcept.remove(a.getConceptId());
		}
		if(idToConcept.size() > 0){
			Assert.fail("<" + idToConcept.values() + "> is missing"); 
		}
	}

	private static class ConceptForCompare{
		public ConceptForCompare(Concept concept){
			this.conceptId = concept.getConceptId();
			this.partOfSpeech = concept.getPartOfSpeech();
			this.synsets.addAll(Arrays.asList(concept.getSynset()));
			this.glosses.addAll(Arrays.asList(concept.getGlosses()));
			this.relations.addAll(Arrays.asList(concept.getRelations()));
		}
		public boolean equals(Object value){
			return EqualsBuilder.reflectionEquals(this, value);
		}
		public String toString(){
			return ToStringBuilder.reflectionToString(this);
		}
		public int hashCode(){
			return HashCodeBuilder.reflectionHashCode(this);
		}
		private String conceptId;
		private PartOfSpeech partOfSpeech;
		private Set<Lemma> synsets = new HashSet<Lemma>();
		private Set<Gloss> glosses = new HashSet<Gloss>();
		private Set<ConceptualRelation> relations = new HashSet<ConceptualRelation>();
	}
}
