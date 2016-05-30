/*
 * $Id: PeerSummaryAdv.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummaryID;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.apache.log4j.Logger;


/**
 * 
 * 
 *
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class PeerSummaryAdv extends PeerSummaryAdvertisement {
	public static final String _tagDataSummaries = "DataSummaries";
	public static final String _tagPeerId = "PeerId";
	public static final String _tagLastUpdateDate = "Date";
	public static final String _tagDataId = "Id";
	private static Logger logger = Logger.getLogger(PeerSummaryAdv.class);

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements net.jxta.document.AdvertisementFactory.Instantiator {
		/*
		 * (non-Javadoc)
		 * @see net.jxta.document.AdvertisementFactory$Instantiator#getAdvertisementType()
		 */
		public String getAdvertisementType() {
			return PeerSummaryAdvertisement.getAdvertisementType();
		}

		public Advertisement newInstance() {
			return new PeerSummaryAdv();

		}

		public Advertisement newInstance(Element root) {
			try {
				return new PeerSummaryAdv(root);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * The constructor.
	 */
	public PeerSummaryAdv() {
		super();
	}

	/**
	 * 
	 * 
	 */
	public PeerSummaryAdv(Element document) {
		super();
		try {
			readAdvertisement((TextElement) document);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * The Constructor.
	 * @param peerSummary
	 */
	public PeerSummaryAdv(PeerSummary peerSummary) {
		super();
		this.setPeerSummary(peerSummary);
	}

	/**
	 * 
	 * 
	 */
	private void readAdvertisement(TextElement element) throws ParseException{
		try {
			Enumeration enu = element.getChildren();
			String peerId = null;
			String summaryID = null;
			DataSummary[] dataSummaries = null;
			while (enu.hasMoreElements()) {
				TextElement el = (TextElement)enu.nextElement();
				String name = el.getName();
				if (name.equals(_tagPeerId)) {
					peerId = el.getTextValue();
				} else if (name.equals(PeerSummaryAdvertisement._tagSummaryID)) {
					summaryID = el.getTextValue();
				} else if (name.equals(_tagDataSummaries)) {
					dataSummaries = this.readDataSummaries(el.getTextValue());
				}
			}

			if(peerId == null || dataSummaries == null) {
				throw new ParseException("peerId:" + peerId + "\tdataSummaries:" + dataSummaries, 0);
			}


			PeerSummaryID peerSummaryId = new PeerSummaryID((PeerID)IDFactory.fromURI(new URI(peerId)), summaryID);
			PeerSummary peerSummary = new PeerSummary(peerSummaryId, dataSummaries);

			logger.info("DataSummaries:" + dataSummaries.length + " / Id:" + peerSummary.getId());

			this.setPeerSummary(peerSummary);
		} catch (URISyntaxException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}


	Object calLock = new Object();
	/**
	 * 
	 * 
	 */
	private DataSummary[] readDataSummaries(String dataSummariesString) throws ParseException{
		try {
			String xml = "<Root><DataSummaries>" + dataSummariesString + "</DataSummaries></Root>";
			StructuredDocument document = StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"), new StringReader(xml));
			ArrayList<DataSummary> dataSummaries = new ArrayList<DataSummary>();
			Enumeration enu = document.getChildren();
			while (enu.hasMoreElements()) {
				TextElement ele = (TextElement) enu.nextElement();
				Enumeration enu_inn = ele.getChildren();
				DataID dataId = null;
				Calendar lastUpdateDate = null;
				while (enu_inn.hasMoreElements()) {
					TextElement el = (TextElement) enu_inn.nextElement();
					String name = el.getName();
					if (name.equals(PeerSummaryAdv._tagDataId)) {
						dataId = new DataID(el.getTextValue());
					} else if (name.equals(_tagLastUpdateDate)) {
						lastUpdateDate = CalendarUtil.decodeFromSimpleDate(el.getTextValue());
					}
				}

				if(dataId == null || lastUpdateDate == null) {
					throw new ParseException("dataId:" + dataId + "\tlastUpdateDate:" + lastUpdateDate, 0);
				}

				DataSummary dataSummary = new DataSummary(dataId, lastUpdateDate);
				dataSummaries.add(dataSummary);
			}

			return dataSummaries.toArray(new DataSummary[0]);
		} catch (IOException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument document =
			(StructuredTextDocument) StructuredDocumentFactory.newStructuredDocument(asMimeType,
					getAdvertisementType());
		PeerSummary peerSummary = this.getPeerSummary();

//		logger.debug(asMimeType + ":" + peerSummary.getId());

		this.appendChild(document, _tagPeerId, peerSummary.getId().getPeerID().toString());
		this.writeDataSummaries(document, peerSummary.getDataSummaries());
		this.appendChild(document, PeerSummaryAdvertisement._tagAdvertisementType, PeerSummaryAdvertisement._advertisementType);
		this.appendChild(document, PeerSummaryAdvertisement._tagSummaryID, peerSummary.getId().getSummaryID());
		return document;
	}

	/**
	 * 
	 * 
	 */
	private void writeDataSummaries(StructuredDocument document, DataSummary[] dataSummaries) {
		String ret = "";
		for(DataSummary dataSummary : dataSummaries) {
			ret += "<Data>" +
					"<"  + PeerSummaryAdv._tagDataId + ">" +
						dataSummary.getId() +
					"</" + PeerSummaryAdv._tagDataId + ">" +
					"<"  + PeerSummaryAdv._tagLastUpdateDate + ">" +
						CalendarUtil.encodeToSimpleDate(dataSummary.getLastUpdateDate()) +
					"</" + PeerSummaryAdv._tagLastUpdateDate + "></Data>";
		}

		this.appendChild(document, _tagDataSummaries, ret);
	}

	/**
	 * 
	 * 
	 */
	private void appendChild(StructuredDocument document, String name, String value) {
		Element element = document.createElement(name, value);
		document.appendChild(element);

	}
}
