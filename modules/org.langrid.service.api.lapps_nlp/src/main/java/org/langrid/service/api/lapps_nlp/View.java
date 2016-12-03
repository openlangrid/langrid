package org.langrid.service.api.lapps_nlp;

public class View {
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public Annotation[] getAnnotations() {
		return annotations;
	}
	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}
	private Metadata metadata;
	private Annotation[] annotations;
}
