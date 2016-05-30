/*
 * $Id: PeerSummaryAdvertisement.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary;
import net.jxta.document.Advertisement;
import net.jxta.id.ID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public abstract class PeerSummaryAdvertisement extends Advertisement {

	private PeerSummary peerSummary;

	public static final String _tagAdvertisementType = "AdvertisementType";
	public static final String _advertisementType    = "PeerSummaryAdvertisement";
	public static final String _tagSummaryID  = "SummaryID";
	public static final String _customSummary = "_CustomSummary";
	public static final String _hostedSummary = "_HostedSummary";
	public static final String _logSummary    = "_LogSummary";
	public static final String _stateSummary  = "_StateSummary";

	@Override
	public ID getID() {
		return new PeerSummaryAdvertisementID(peerSummary.getId());
	}

	public PeerSummary getPeerSummary() {
		return peerSummary;
	}

	public void setPeerSummary(PeerSummary peerSummary) {
		this.peerSummary = peerSummary;
	}

	@Override
	public String[] getIndexFields() {
		return new String[]{_tagAdvertisementType, _tagSummaryID};
	}

	static public String getAdvertisementType() {
		return _advertisementType;
	}
}
