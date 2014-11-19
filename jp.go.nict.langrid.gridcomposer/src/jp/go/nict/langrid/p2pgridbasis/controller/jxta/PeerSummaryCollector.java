/*
 * $Id: PeerSummaryCollector.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import java.io.IOException;
import java.util.ArrayList;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdv;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao.DataSummaryDao;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformException;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformSearchCondition;
import net.jxta.document.Advertisement;
import net.jxta.exception.PeerGroupException;

import org.apache.log4j.Logger;

/**
 * 
 * 
 */
public class PeerSummaryCollector implements Runnable {
	static private Logger logger = Logger.getLogger(PeerSummaryCollector.class);

	static private int milliSecondSearchSummaryToSearchUpdatedInfo = 2 * 60 * 1000;
	static private int milliSecondSeeachUpdatedInfoToRepublishSummary = 2 * 60 * 1000;
	static private int collectMaxNum = 100;

	private JXTAPlatform platform;
	private DataSummaryDao dataSummaryDao;
	private DataSharingStrategy strategy;

	/**
	 * 
	 * 
	 */
	public PeerSummaryCollector(JXTAPlatform platform,
			DataSummaryDao summaryElementDao,
			DataSharingStrategy strategy) throws PeerGroupException {
		this.platform = platform;
		this.dataSummaryDao = summaryElementDao;
		this.strategy = strategy;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!Thread.interrupted()) {
				try {
					searchRemotePeerSummaryAdvertisement();
					Thread.sleep(milliSecondSearchSummaryToSearchUpdatedInfo);

					searchUpdatedServiceAdvertisements();
					Thread.sleep(milliSecondSeeachUpdatedInfoToRepublishSummary);
				} catch (IOException e){
					logger.fatal(e.getMessage());
				} catch (JXTAPlatformException e) {
					logger.fatal(e.getMessage());
				}
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 
	 * 
	 */
	private void searchRemotePeerSummaryAdvertisement() throws JXTAPlatformException {
		this.platform.searchAdvertisements(new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._hostedSummary + "*", collectMaxNum, null));
		this.platform.searchAdvertisements(new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._customSummary + "*", collectMaxNum, null));
		this.platform.searchAdvertisements(new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._logSummary    + "*", collectMaxNum, null));
		this.platform.searchAdvertisements(new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._stateSummary  + "*", collectMaxNum, null));
	}

	/**
	 * 
	 * 
	 */
	private void searchUpdatedServiceAdvertisements() throws IOException, JXTAPlatformException{
		ArrayList<PeerSummary> remotePeerSummaries   = new ArrayList<PeerSummary>();
		ArrayList<PeerSummary> remoteLogSummaries    = new ArrayList<PeerSummary>();
		ArrayList<PeerSummary> remoteStateSummaries  = new ArrayList<PeerSummary>();
		Advertisement[] b_advs = this.platform.getAdvertisements(
				new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._hostedSummary + "*", collectMaxNum, null));
		Advertisement[] c_advs = this.platform.getAdvertisements(
				new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._customSummary + "*", collectMaxNum, null));
		Advertisement[] l_advs = this.platform.getAdvertisements(
				new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._logSummary    + "*", collectMaxNum, null));
		Advertisement[] s_advs = this.platform.getAdvertisements(
				new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagSummaryID, PeerSummaryAdvertisement._stateSummary  + "*", collectMaxNum, null));

		logger.debug("Summary : " + this.platform.getAdvertisements(new JXTAPlatformSearchCondition(PeerSummaryAdvertisement._tagAdvertisementType, PeerSummaryAdvertisement._advertisementType, collectMaxNum, null)).length);
		logger.debug("hostSummary : " + b_advs.length);
		for(Advertisement adv : b_advs){
			logger.debug("   ID : " + adv.getID());
			this.platform.localPublish(adv);
		}
		logger.debug("custSummary : " + c_advs.length);
		for(Advertisement adv : c_advs){
			logger.debug("   ID : " + adv.getID());
			this.platform.localPublish(adv);
		}
		logger.debug("logSummary : " + l_advs.length);
		for(Advertisement adv : l_advs){
			logger.debug("   ID : " + adv.getID());
			this.platform.localPublish(adv, 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24);
		}
		logger.debug("stateSummary : " + s_advs.length);
		for(Advertisement adv : s_advs){
			logger.debug("   ID : " + adv.getID());
			this.platform.localPublish(adv);
		}

		for(Advertisement adv : b_advs) {
			if (adv instanceof PeerSummaryAdv) {
				PeerSummaryAdv advertisement = (PeerSummaryAdv) adv;
				if(advertisement.getPeerSummary().getId().getPeerID().equals(this.platform.getPeerID()) == false) {
					remotePeerSummaries.add(advertisement.getPeerSummary());
				}
			} else {
				logger.error("Not a peer summary: " + adv.getClass());
			}
		}
		for(Advertisement adv : c_advs) {
			if (adv instanceof PeerSummaryAdv) {
				PeerSummaryAdv advertisement = (PeerSummaryAdv) adv;
				if(advertisement.getPeerSummary().getId().getPeerID().equals(this.platform.getPeerID()) == false) {
					remotePeerSummaries.add(advertisement.getPeerSummary());
				}
			} else {
				logger.error("Not a peer summary: " + adv.getClass());
			}
		}

		for(Advertisement adv : l_advs) {
			if (adv instanceof PeerSummaryAdv) {
				PeerSummaryAdv advertisement = (PeerSummaryAdv) adv;
				if(advertisement.getPeerSummary().getId().getPeerID().equals(this.platform.getPeerID()) == false) {
					remoteLogSummaries.add(advertisement.getPeerSummary());
				}
			} else {
				logger.error("Not a peer summary: " + adv.getClass());
			}
		}

		for(Advertisement adv : s_advs) {
			if (adv instanceof PeerSummaryAdv) {
				PeerSummaryAdv advertisement = (PeerSummaryAdv) adv;
				if(advertisement.getPeerSummary().getId().getPeerID().equals(this.platform.getPeerID()) == false) {
					remoteStateSummaries.add(advertisement.getPeerSummary());
				}
			} else {
				logger.error("Not a peer summary: " + adv.getClass());
			}
		}

		strategy.shareData(this.dataSummaryDao.getAllDataSummaries(), remotePeerSummaries.toArray(new PeerSummary[0]));
		strategy.shareData(this.dataSummaryDao.getAllDataSummaries(), remoteLogSummaries.toArray(new PeerSummary[0]));
		strategy.shareData(this.dataSummaryDao.getAllDataSummaries(), remoteStateSummaries.toArray(new PeerSummary[0]));
	}
}
