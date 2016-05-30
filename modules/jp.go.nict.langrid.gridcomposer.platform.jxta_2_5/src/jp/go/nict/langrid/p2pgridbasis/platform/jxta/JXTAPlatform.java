/*
 * $Id: JXTAPlatform.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;

import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public interface JXTAPlatform {
	/**
	 * 
	 * 
	 */
	NetworkManager getNetworkManager();

	/**
	 * 
	 * 
	 */
	void start(String sourceGridId, boolean hosted, Collection<URL> seedUrls
			, DiscoveryListener discoveryListener) throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void publish(Advertisement advertisement) throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void publish(Advertisement advertisement, long lifetimeMillis, long exirationMillis)
	throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void localPublish(Advertisement adv) throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void localPublish(Advertisement adv, long lifetimeMillis, long exirationMillis)
	throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void searchAdvertisements(JXTAPlatformSearchCondition searchCondition) throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	Advertisement[] getAdvertisements(JXTAPlatformSearchCondition searchCondition) throws JXTAPlatformException;

	/**
	 * 
	 * 
	 */
	void shutdown();

	/**
	 * 
	 * 
	 */
	PeerID getPeerID();

	/**
	 * 
	 * 
	 */
	void clearCache();

	/**
	 * 
	 * 
	 */
	void peers_r();

	/**
	 * 
	 * 
	 */
	void rdvstatus(boolean verbose);

	/**
	 * 
	 * 
	 */
	void showStatus(PrintStream stream, boolean verbose);

	/**
	 * 
	 * 
	 */
	PeerGroup getPeerGroup();

	/**
	 * 
	 * 
	 */
	boolean isRdv();
}
