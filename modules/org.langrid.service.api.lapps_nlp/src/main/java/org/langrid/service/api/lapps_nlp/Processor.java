package org.langrid.service.api.lapps_nlp;

public interface Processor {
	String getMetadata();

	String execute(String arg);
}
