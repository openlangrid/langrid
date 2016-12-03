package org.langrid.service.api.lapps_nlp;

public class Request {
	public Request() {
	}
	public Request(String discriminator, String payload) {
		this.discriminator = discriminator;
		this.payload = payload;
	}

	public String getDiscriminator() {
		return discriminator;
	}
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}

	private String discriminator;
	private String payload;
}
