package org.langrid.service.api.lapps_nlp;

public interface DataSource {
	String getMetadata();

	String execute(String arg);
}
