/*
 * $Id: RdvPeer.java 955 2013-10-09 04:26:29Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.jxta.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.p2pgridbasis.platform.jxta.util.JXTAPlatformUtil;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.XMLDocument;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.document.LiteXMLElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;

/**
 * Servlet implementation class for Servlet: ListRdvPeer
 *
 */
public class RdvPeer extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5697591065415303668L;

	private static final String RDVvalue = "true";
	private static final String RDVtag = "Rdv";
	private static final String RENDEZVOUS_SERVICE_ID_URI = "urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000605";	
	private static ID RENDEZVOUS_SERVICE_ID;
	static {
		try {
			RENDEZVOUS_SERVICE_ID = IDFactory.fromURI(new URI(RENDEZVOUS_SERVICE_ID_URI));
		} catch (URISyntaxException e) {
			assert false : "ランデブーサービスIDの生成に失敗しました";
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PeerGroup peerGroup = JXTAPlatformUtil.getPeerGroup();
		if(peerGroup == null){
			throw new ServletException("JXTA didn't initialized. Please check the log for the Exception occurred during startup.");
		}

		ArrayList<PeerAdvertisement> peerAdvs = new ArrayList<PeerAdvertisement>();
		DiscoveryService ds = peerGroup.getDiscoveryService();
		if(ds == null){
			throw new ServletException("discovery service is null.");
		}
		Enumeration<Advertisement> advs = ds.getLocalAdvertisements(DiscoveryService.PEER, null, null);
		while (advs.hasMoreElements()) {
			PeerAdvertisement peerAdv = (PeerAdvertisement) advs.nextElement();
			if (isRendezvousPeer(peerAdv)) {
				peerAdvs.add(peerAdv);
			}
		}

		if(peerAdvs.size() == 0) {
			throw new ServletException("ランデブーピアが見つかりませんでした");
		}

		response.getWriter().println(peerAdvs.get(new Random().nextInt(peerAdvs.size())));
	}  	

	/**
	 * ランデブーピアのアドバタイズメントかどうかを判断する
	 * @param peerAdv 判断するピアアドバタイズメント
	 * @return ランデブーピアかどうか
	 */
	private boolean isRendezvousPeer(PeerAdvertisement peerAdv) {
		XMLDocument<?> document = (XMLDocument<?>)peerAdv.getServiceParam(RENDEZVOUS_SERVICE_ID);
		if(document == null) {
			return false;
		}
		Enumeration<?> enu = document.getChildren(RDVtag);
		return enu.hasMoreElements() && ((LiteXMLElement)enu.nextElement()).getTextValue().equals(RDVvalue);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}   	  	    
}
