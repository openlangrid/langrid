package jp.go.nict.langrid.wrapper.workflowsupport;


public class ConstructSPForMTResult {
	public TaggedPart[] getParts() {
		return parts;
	}
	public void setParts(TaggedPart[] parts) {
		this.parts = parts;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private String text;
	private TaggedPart[] parts;

}
