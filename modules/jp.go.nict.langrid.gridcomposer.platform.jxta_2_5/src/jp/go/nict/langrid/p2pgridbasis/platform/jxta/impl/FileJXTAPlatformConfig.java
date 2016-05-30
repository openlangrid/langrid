/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.p2pgridbasis.platform.jxta.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformConfig;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public class FileJXTAPlatformConfig extends JXTAPlatformConfig {
	private static final String RDV_PEER_URL = "RdvPeerURLs";
	private static final String IS_RENDEZVOUS = "IsRendezvous";
	private static final String PEER_GROUP_DESCRIPTION = "PeerGroupDescription";
	private static final String PEER_NAME = "PeerName";
	private static final String PEER_GROUP_NAME = "PeerGroupName";

	static private Logger logger = Logger.getLogger(FileJXTAPlatformConfig.class);

	private File file;

	/**
	 * 
	 * 
	 */
	public FileJXTAPlatformConfig(File file) throws FileNotFoundException, IOException, RequiredPropertyNotFoundException {
		logger.info("Platform Config File:" + file.getAbsolutePath());
		this.file = file;

		Properties prop = new Properties();
		prop.load(new FileInputStream(file));

		checkRequiredItems(prop);

		super.setPeerName(prop.getProperty(PEER_NAME));
		super.setPeerName(prop.getProperty(PEER_NAME));
		super.setPeerGroupName(prop.getProperty(PEER_GROUP_NAME));
		super.setPeerGroupDescription(prop.getProperty(PEER_GROUP_DESCRIPTION));
		super.setRdvPeerURLs(getURLs(prop.getProperty(RDV_PEER_URL).split(",")));
		super.setInitPeer(getRdvPeerURLs().isEmpty());
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformConfig#save()
	 */
	public void save() throws IOException {
		Properties prop = new Properties();
		prop.setProperty(PEER_NAME, getPeerName());
		prop.setProperty(PEER_GROUP_NAME, getPeerGroupName());
		prop.setProperty(PEER_GROUP_DESCRIPTION, getPeerGroupDescription());
		String rdvPeerUrls = "";
		for (URL url : getRdvPeerURLs()) {
			rdvPeerUrls += url.toString() + ",";
		}
		prop.setProperty(RDV_PEER_URL, rdvPeerUrls);

		prop.store(new FileOutputStream(file), "");
	}

	/**
	 * 
	 * 
	 */
	private void checkRequiredItems(Properties prop) throws RequiredPropertyNotFoundException {
		ArrayList<String> inexistentPropNames = new ArrayList<String>();

		checkRequiredItem(prop, PEER_NAME, inexistentPropNames);
		checkRequiredItem(prop, PEER_GROUP_NAME, inexistentPropNames);
		checkRequiredItem(prop, PEER_GROUP_DESCRIPTION, inexistentPropNames);
		checkRequiredItem(prop, IS_RENDEZVOUS, inexistentPropNames);
		checkRequiredItem(prop, RDV_PEER_URL, inexistentPropNames);

		if(inexistentPropNames.isEmpty() == false) {
			throw new RequiredPropertyNotFoundException(inexistentPropNames);
		}


	}

	/**
	 * 
	 * 
	 */
	private void checkRequiredItem(Properties prop, String propName, Collection<String> ret){
		if(prop.containsKey(propName) == false) {
			ret.add(propName);
		}
	}

	/**
	 * 
	 * 
	 */
	private List<URL> getURLs(String[] urlStrings) throws MalformedURLException {
		List<URL> ret = new ArrayList<URL>();
		for(String urlString : urlStrings) {
			if(urlString.length() == 0) {
				continue;
			}
			ret.add(new URL(urlString));
		}

		return ret;
	}

}
