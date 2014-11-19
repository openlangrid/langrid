/*
 * $Id: FullySynchronizeStrategy.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.DataAdv;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformException;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformSearchCondition;
import net.jxta.peer.PeerID;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class FullySynchronizeStrategy implements DataSharingStrategy {
	static private Logger logger = Logger.getLogger(FullySynchronizeStrategy.class);

	private JXTAPlatform platform;

	/**
	 * 
	 * 
	 */
	public FullySynchronizeStrategy(JXTAPlatform platform) {
		this.platform = platform;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.jxta.DataSharingStrategy#shareData(jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary[], jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary[])
	 */
	public void shareData(DataSummary[] localDataSummaries, PeerSummary[] remotePeerSummaries) {
		logger.info(String.format(
				"shareData. %d local summaries, %d remote summaries."
				, localDataSummaries.length, remotePeerSummaries.length
				));
		ArrayList<UpdatedDataSet> targetPeerIdMap =
			findUpdatedData(localDataSummaries, remotePeerSummaries);
		getUpdatedData(targetPeerIdMap);
	}

	/**
	 * 
	 * 
	 */
	private ArrayList<UpdatedDataSet> findUpdatedData(DataSummary[] localDataSummaries, PeerSummary[] remotePeerSummaries) {
		Map<DataID, UpdatedDataSet> updatedDataMap = new HashMap<DataID, UpdatedDataSet>();

		for(DataSummary dataSummary : localDataSummaries) {
			updatedDataMap.put(dataSummary.getId(), new UpdatedDataSet(dataSummary));
		}

		for (PeerSummary peerSummary : remotePeerSummaries) {
			PeerID peerId = peerSummary.getId().getPeerID();
			for (DataSummary dataSummary : peerSummary.getDataSummaries()) {
				if (updatedDataMap.containsKey(dataSummary.getId()) == true) {
					updatedDataMap.get(dataSummary.getId()).processSummary(peerId, dataSummary);
				} else {
					updatedDataMap.put(dataSummary.getId(), new UpdatedDataSet(peerId, dataSummary));
				}
			}
		}

		ArrayList<UpdatedDataSet> ret = new ArrayList<UpdatedDataSet>();
		for(DataID dataId : updatedDataMap.keySet()) {
			UpdatedDataSet data = updatedDataMap.get(dataId);
			if(data.isUpdated() == true) {
				ret.add(data);
			}
		}

		return ret;
	}

	/**
	 * 
	 * 
	 */
	private void getUpdatedData(ArrayList<UpdatedDataSet> targetPeerIdMap) {
		Random rand = new Random();
		try {
			for (UpdatedDataSet updatedDataSet : targetPeerIdMap) {
				ArrayList<PeerID> targets = updatedDataSet.getTargetPeerIDs();
				DataID dataId = updatedDataSet.getDataId();
				if(targets.size() > 0) {
					PeerID peerId = targets.get(rand.nextInt(targets.size()));
					String advId = dataId.toString() + "_" + peerId.toString();
					JXTAPlatformSearchCondition con = new JXTAPlatformSearchCondition(DataAdv._tagDataAdvID, advId, 10, peerId);
					platform.searchAdvertisements(con);
				}
			}
		} catch (JXTAPlatformException e) {
			throw new RuntimeException("JXTA hasn't started yet.");
		}
	}
}
