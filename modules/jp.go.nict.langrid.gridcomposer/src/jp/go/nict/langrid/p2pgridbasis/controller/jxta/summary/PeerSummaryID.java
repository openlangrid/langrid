/*
 * $Id: PeerSummaryID.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary;

import net.jxta.peer.PeerID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class PeerSummaryID {
	private PeerID peerID;
	private String summaryID;
	
	/**
	 * 
	 * 
	 */
	public PeerSummaryID(PeerID peerID, String summaryID) {
		this.peerID = peerID;
		this.summaryID = summaryID;
	}

	/**
	 * @return peerID
	 */
	public PeerID getPeerID() {
		return peerID;
	}

	/**
	 * 
	 * 
	 */
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	/**
	 * @return summaryID
	 */
	public String getSummaryID() {
		return summaryID;
	}

	/**
	 * 
	 * 
	 */
	public void setSummaryID(String summaryID) {
		this.summaryID = summaryID;
	}
	
	public String toString() {
		return this.peerID.getUniqueValue() + summaryID;
	}
	
	public boolean equals(Object target) {
		if(target == this) {
			return true;
		}
		
		if(target.getClass().equals(PeerSummaryID.class) == false) {
			return false;
		}
		
		PeerSummaryID targetId = (PeerSummaryID) target;
		return this.toString().equals(targetId.toString());
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
}
