/*
 * $Id: JXTAPlatformConfig.java 320 2010-12-03 03:21:12Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.p2pgridbasis.platform.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.protocol.PeerAdvertisement;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public abstract class JXTAPlatformConfig {
	private String peerGroupName;
	private String peerGroupDescription;
	private boolean isInitPeer;
	private Map<String, AdvertisementFactory.Instantiator> advertisementType;
	private List<URL> rdvPeerURLs;
	private List<PeerAdvertisement> initRdvPeerAdv;
	private File baseDir;
	private String peerName;
	protected JXTAPlatformConfig() {}

	/**
	 * @return advertismentType
	 */
	public Map<String, AdvertisementFactory.Instantiator> getAdvertisementType() {
		return advertisementType;
	}

	/**
	 * @return peerName
	 */
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @return peerGroupName
	 */
	public String getPeerGroupName() {
		return peerGroupName;
	}

	/**
	 * @return isInitPeer
	 */
	public boolean isInitPeer() {
		return isInitPeer;
	}

	/**
	 * @return rdvPeerURLs
	 */
	public List<URL> getRdvPeerURLs() {
		return rdvPeerURLs;
	}

	/**
	 * @return peerGroupDescription
	 */
	public String getPeerGroupDescription() {
		return peerGroupDescription;
	}
	/**
	 * @param advertisementType advertisementType
	 */
	public void setAdvertisementType(
			Map<String, AdvertisementFactory.Instantiator> advertisementType) {
		this.advertisementType = advertisementType;
	}

	/**
	 * @param peerGroupDescription peerGroupDescription
	 */
	protected void setPeerGroupDescription(String peerGroupDescription) {
		this.peerGroupDescription = peerGroupDescription;
	}
	/**
	 * @param peerName  peerName
	 */
	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}
	/**
	 * @param peerGroupName peerGroupName
	 */
	protected void setPeerGroupName(String peerGroupName) {
		this.peerGroupName = peerGroupName;
	}
	/**
	 * @param rdvPeerURLs rdvPeerURLs
	 */
	public void setRdvPeerURLs(List<URL> rdvPeerURLs) {
		this.rdvPeerURLs = rdvPeerURLs;
	}

	/**
	 * @param isInitPeer isInitPeer
	 */
	protected void setInitPeer(boolean isInitPeer) {
		this.isInitPeer = isInitPeer;
	}

	/**
	 * @return initRdvPeerAdv
	 */
	public List<PeerAdvertisement> getInitRdvPeerAdvs() {
		return initRdvPeerAdv;
	}

	/**
	 * @param initRdvPeerAdv initRdvPeerAdv
	 */
	public void setInitRdvPeerAdvs(List<PeerAdvertisement> initRdvPeerAdv) {
		this.initRdvPeerAdv = initRdvPeerAdv;
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	abstract public void save() throws IOException;

}
