package jp.go.nict.langrid.service_1_2.speech;

public class RecognizeAndTranslateResult {
	public RecognizeAndTranslateResult() {
	}
	public RecognizeAndTranslateResult(String recognized, String target) {
		this.recognized = recognized;
		this.target = target;
	}
	public String getRecognized() {
		return recognized;
	}
	public void setRecognized(String recognized) {
		this.recognized = recognized;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	private String recognized;
	private String target;
}
