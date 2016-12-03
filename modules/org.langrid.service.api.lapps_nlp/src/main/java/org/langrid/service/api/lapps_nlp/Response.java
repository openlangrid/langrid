package org.langrid.service.api.lapps_nlp;

public class Response {
	public Response() {
	}
	public Response(String discriminator, Payload payload) {
		this.discriminator = discriminator;
		this.payload = payload;
	}

	public String getDiscriminator() {
		return discriminator;
	}
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	private String discriminator;
	private Payload payload;
}
