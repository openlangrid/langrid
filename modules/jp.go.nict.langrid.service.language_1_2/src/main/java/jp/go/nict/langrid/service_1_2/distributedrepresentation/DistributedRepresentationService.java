package jp.go.nict.langrid.service_1_2.distributedrepresentation;

public interface DistributedRepresentationService {
	double toVector(String language, String word);
	String[] getNearestWord(double vector, int maxWords);
}
