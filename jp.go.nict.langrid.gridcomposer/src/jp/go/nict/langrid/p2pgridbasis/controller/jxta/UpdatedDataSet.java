/*
 * $Id: UpdatedDataSet.java 318 2010-12-03 03:10:29Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.controller.jxta;

import java.util.ArrayList;
import java.util.Calendar;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import net.jxta.peer.PeerID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 318 $
 */
class UpdatedDataSet {
	private Calendar latestUpdatedDate;
	private DataID dataId;
	private ArrayList<PeerID> peerIds;
	private boolean updated;

	public UpdatedDataSet(DataSummary dataSummary) {
		this(dataSummary.getLastUpdateDate(), dataSummary.getId(), false);
	}

	/**
	 * 
	 * 
	 */
	public UpdatedDataSet(PeerID peerId, DataSummary dataSummary) {
		this(dataSummary.getLastUpdateDate(), dataSummary.getId(), true);
		peerIds.add(peerId);
	}

	/**
	 * 
	 * 
	 */
	private UpdatedDataSet(Calendar latestUCalendar, DataID dataId, boolean updated) {
		this.latestUpdatedDate = latestUCalendar;
		this.dataId = dataId;
		this.updated = updated;

		peerIds = new ArrayList<PeerID>();
	}

	/**
	 * 
	 * 
	 */
	public void processSummary(PeerID peerId, DataSummary dataSummary) {
		if(!dataSummary.getId().equals(dataId)){
			throw new RuntimeException("DataID is different: " + dataSummary.getId());
		}

		if(dataSummary.getLastUpdateDate().after(latestUpdatedDate)) {
			peerIds.clear();
			peerIds.add(peerId);
			updated = true;
		} else if (dataSummary.getLastUpdateDate().equals(latestUpdatedDate)) {
			if(updated == true) {
				peerIds.add(peerId);
			} else {
				// updated == false => same timestamp
			}
		} else { // dataSummary.getLastUpdatedDate is before latestUpdatedDate
		}
	}

	/**
	 * 
	 * 
	 */
	public boolean isUpdated() {
		return updated;
	}

	/**
	 * 
	 * 
	 */
	public ArrayList<PeerID> getTargetPeerIDs() {
		return peerIds;
	}
	
	/**
	 * 
	 * 
	 */
	public DataID getDataId() {
		return dataId;
	}
}
