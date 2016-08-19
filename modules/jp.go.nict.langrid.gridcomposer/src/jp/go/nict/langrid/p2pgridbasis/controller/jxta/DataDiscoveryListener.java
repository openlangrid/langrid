/*
 * $Id: DataDiscoveryListener.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.DataAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataTypeNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.P2PGridDaoFactory;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class DataDiscoveryListener implements DiscoveryListener {
	/**
	 * 
	 * 
	 */
	public DataDiscoveryListener(JXTAController ctrl) {
		this.ctrl = ctrl;
	}


	/*
	 * (non-Javadoc)
	 * @see net.jxta.discovery.DiscoveryListener#discoveryEvent(net.jxta.discovery.DiscoveryEvent)
	 */
	public void discoveryEvent(DiscoveryEvent event) {
		synchronized(DataDiscoveryListener.class){
			Enumeration<String> responses = event.getResponse().getResponses();
			while (responses.hasMoreElements()) {
				Data data = null;
				try {
					data = parseResponse((String)responses.nextElement());
					if(data == null) return;
					Pair<AtomicLong, AtomicLong> c = counter.get(data.getType());
					if(c == null){
						synchronized(counter){
							c = counter.get(data.getType());
							if(c == null){
								AtomicLong discovered = new AtomicLong();
								AtomicLong updated = new AtomicLong();
								c = Pair.create(discovered, updated);
								counter.put(data.getType(), c);
							}
						}
					}
					c.getFirst().incrementAndGet();
					DataDao dao = P2PGridDaoFactory.getDataDao(data.getType());
					if(dao.updateData(data)){
						c.getSecond().incrementAndGet();
					}
				} catch (InvalidDiscoveryResponseException e) {
					logger.info(e.getMessage());
				} catch (DataTypeNotFoundException e) {
					logger.info(e.getMessage());
				} catch (UnmatchedDataTypeException e) {
					logger.info(e.getMessage());
				} catch (DataDaoException e) {
					logger.log(
							Level.INFO
							, "exception in processing discovery event for ["
							+ data.getType() + "(" + data.getId().getId() + ")]"
							, e);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private Data parseResponse(String responseElement) throws InvalidDiscoveryResponseException{
		try {
			Advertisement adv = parseService(responseElement);
			if(adv instanceof DataAdvertisement){
				DataAdvertisement dataAdv = (DataAdvertisement)adv;
				return dataAdv.getData();
			} else{
				return null;
			}
		} catch (NullPointerException e) {
			throw new InvalidDiscoveryResponseException(e);
		} catch (IOException e) {
			throw new InvalidDiscoveryResponseException(e);
		} catch (ClassCastException e) {
			throw new InvalidDiscoveryResponseException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	private Advertisement parseService(String responseElement) throws IOException{
		XMLDocument<?> xmlDocument = (XMLDocument<?>)StructuredDocumentFactory.newStructuredDocument(
				MIME_MEDIA_TYPE, new ByteArrayInputStream(responseElement.getBytes()));
		return AdvertisementFactory.newAdvertisement(xmlDocument);
	}

	private JXTAController ctrl;
	private static MimeMediaType MIME_MEDIA_TYPE = new MimeMediaType("text/xml");
	private static Map<String, Pair<AtomicLong, AtomicLong>> counter
		= new ConcurrentHashMap<String, Pair<AtomicLong, AtomicLong>>();
	private static Timer timer = new Timer(true);
	private static Logger logger = Logger.getLogger(DataDiscoveryListener.class.getName());
	static{
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				StringBuilder b = new StringBuilder();
				b.append("-- status of p2p-shared elements (" + counter.size() + ") --\n");
				if(counter.size() == 0){
					b.append("none.");
				} else{
					for(Map.Entry<String, Pair<AtomicLong, AtomicLong>> entry : 
							new TreeMap<String, Pair<AtomicLong, AtomicLong>>(counter).entrySet()){
						b.append("[").append(entry.getKey()).append("] discovered: ")
							.append(entry.getValue().getFirst().longValue())
							.append(", updated: ").append(entry.getValue().getSecond().longValue())
							.append("\n");
					}
				}
				logger.info(b.toString());
			}
			}, 1000 * 60 * 3, 1000 * 60 * 10);
	}
}
