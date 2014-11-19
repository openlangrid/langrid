/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataFactory;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.RequiredAttributeNotFoundException;
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
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class DataAdv extends DataAdvertisement {
	public static final String _tagDataAdvID            = "DataAdvID";
	public static final String _tagDataID               = "DataId";
	public static final String _tagDataType             = "DataType";
	public static final String _tagGridId               = "gridId";
	public static final String _tagLastUpdateDate       = "LastUpdate";
	public static final String _tagPeerID               = "PeerID";
	public static final String _tagServiceAndNodeGridId = "serviceAndNodeGridId";
	public static final String _tagServiceGridId        = "serviceGridId";

	static private Logger logger = Logger.getLogger(DataAdv.class);

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
			return DataAdvertisement.getAdvertisementType();
		}

		/*
		 * (non-Javadoc)
		 * @see net.jxta.document.AdvertisementFactory$Instantiator#newInstance()
		 */
		public Advertisement newInstance() {
			return new DataAdv();

		}

		/*
		 * (non-Javadoc)
		 * @see net.jxta.document.AdvertisementFactory$Instantiator#newInstance(net.jxta.document.Element)
		 */
		public Advertisement newInstance(Element root) {
			try {
				return new DataAdv(root);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
		}
	};

	/**
	 * The constructor.
	 */
	public DataAdv() {
		super();
	}

	/**
	 * 
	 * 
	 */
	public DataAdv(Data data, PeerID peerID) {
		super();
		this.setData(data);
		this.setPeerID(peerID);
	}

	/**
	 * 
	 * 
	 */
	public DataAdv(Element document) {
		super();
		try {
			readAdvertisement((TextElement) document);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	private void readAdvertisement(TextElement document) throws ParseException {
		logger.trace(document);
		try {
			Enumeration elements = document.getChildren();
			DataID dataID = null;
			Calendar lastUpdateDate = null;
			String dataType = null;
			String gid = null;
			DataAttributes attributes = new DataAttributes();
			PeerID peerID = null;

			while(elements.hasMoreElements()) {
				TextElement el = (TextElement)elements.nextElement();
				String name = el.getName();
//				logger.debug(el.getName() + " : " + el.getTextValue());
				if (name.equals(_tagDataID)) {
					dataID = new DataID(el.getTextValue());
				} else if (name.equals(_tagLastUpdateDate)) {
					lastUpdateDate = CalendarUtil.decodeFromSimpleDate(el.getTextValue());
				} else if (name.equals(_tagDataType)) {
					dataType = el.getTextValue();
				} else if(name.equals(_tagPeerID)) {
					peerID = (PeerID)IDFactory.fromURI(new URI(el.getTextValue()));
				} else if (name.equals(DataAdvertisement._tagAdvertisementType)) {
					// do nothing
				} else if (name.equals(_tagDataAdvID)) {
					// do nothing
					logger.debug("readAdvertisement() : " + el.getTextValue());
				} else {
					if (name.equals(_tagGridId)) {
						gid = el.getTextValue();
					} else if (name.equals(_tagServiceGridId)) {
						gid = el.getTextValue();
					} else if (name.equals(_tagServiceAndNodeGridId)) {
						gid = el.getTextValue();
					}

					attributes.setAttribute(name, el.getTextValue());
				}
			}
			if(dataID == null || lastUpdateDate == null || dataType == null || peerID == null){
				throw new ParseException("DataID:" + dataID +
						"\tlastUpdateDate:" + lastUpdateDate +
						"\tdataType:" + dataType +
						"\tpeerID:" + peerID, 0);
			}

			super.setData(DataFactory.createData(dataType, gid, dataID, lastUpdateDate, attributes));
			super.setPeerID(peerID);
		} catch (URISyntaxException e) {
			throw new ParseException(e.getMessage(), 0);
		} catch (RequiredAttributeNotFoundException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument document =
			(StructuredTextDocument) StructuredDocumentFactory.newStructuredDocument(asMimeType,
					getAdvertisementType());
		Data data = this.getData();

//		logger.debug(asMimeType + ":" + data.getId());

		appendChild(document, _tagDataID, safeToString(data.getId().toString()));
		appendChild(document, _tagLastUpdateDate, CalendarUtil.encodeToSimpleDate(data.getLastUpdate()));
		appendChild(document, _tagDataType, data.getType());
		appendChild(document, _tagPeerID, safeToString(getPeerID()));
		appendChild(document, DataAdvertisement._tagAdvertisementType, DataAdvertisement._advertisementType);
		appendChild(document, _tagDataAdvID, safeToString(data.getId().toString()) + "_" + getPeerID().toString());

		DataAttributes attributes = data.getAttributes();
		for(String name : attributes.getKeys()) {
			this.appendChild(document, name, attributes.getValue(name));
		}
		return document;
	}

	/**
	 * 
	 * 
	 */
	private String safeToString(Object o) {
		if(o == null) {
			return "null";
		}
		return o.toString();
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
