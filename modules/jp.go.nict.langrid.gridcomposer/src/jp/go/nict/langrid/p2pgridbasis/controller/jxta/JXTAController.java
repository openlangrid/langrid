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
package jp.go.nict.langrid.p2pgridbasis.controller.jxta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.encoding.Base64;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerSearchCondition;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.DataAdv;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.DataAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdv;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao.DataSummaryAlreadyExistsException;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao.DataSummaryDao;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao.DataSummaryNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao.MemoryDataSummaryDao;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummary;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummaryID;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataTypeNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.P2PGridDaoFactory;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLogData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessRightData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessStateData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DomainData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.FederationData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.GridData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NewsData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NodeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.OverUseLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ProtocolData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.TemporaryUserData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.UserData;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformConfig;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformException;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformSearchCondition;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.impl.FileJXTAPlatformConfig;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.impl.JXTAPlatformImpl;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.impl.RequiredPropertyNotFoundException;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory.Instantiator;
import net.jxta.exception.PeerGroupException;
import net.jxta.impl.rendezvous.RendezVousServiceImpl;
import net.jxta.impl.rendezvous.RendezVousServiceInterface;
import net.jxta.peergroup.PeerGroup;
import net.jxta.rendezvous.RendezVousService;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class JXTAController implements P2PGridController {
	private JXTAController(ServiceContext context) throws ControllerException {
		this.selfGridId = context.getSelfGridId();
		this.selfNodeId = context.getSelfNodeId();
		try {
			File configFile = new File(baseDir, "Config.properties");
			JXTAPlatformConfig config = new FileJXTAPlatformConfig(configFile);

			Map<String, Instantiator> instantiators = new HashMap<String, Instantiator>();
			instantiators.put(DataAdvertisement.getAdvertisementType(), new DataAdv.Instantiator());
			instantiators.put(PeerSummaryAdvertisement.getAdvertisementType(), new PeerSummaryAdv.Instantiator());
			config.setAdvertisementType(instantiators);

			config.setBaseDir(baseDir);

			platform = new JXTAPlatformImpl(config);
			baseSummaryDao   = new MemoryDataSummaryDao();
			custSummaryDao   = new MemoryDataSummaryDao();
			hostSummaryDao   = new MemoryDataSummaryDao();
			logSummaryDao    = new MemoryDataSummaryDao();
			stateSummaryDao  = new MemoryDataSummaryDao();

			selfGridId = context.getSelfGridId();
			if(selfGridId == null){
				throw new ControllerException("The parameter \"langrid.node.gridId\" must not be null.");
			}
			activeBpelServicesUrl = context.getInitParameter(
					"langrid.activeBpelServicesUrl");
			if(activeBpelServicesUrl == null){
				throw new ControllerException("A parameter langrid.activeBpelServicesUrl is required.");
			}
			activeBpelDeployBinding = context.getInitParameter(
					"langrid.activeBpelDeployBinding");
			if(activeBpelDeployBinding == null){
				activeBpelDeployBinding = "RPC";
			}
		} catch (FileNotFoundException e) {
			throw new ControllerException(e);
		} catch (IOException e) {
			throw new ControllerException(e);
		} catch (RequiredPropertyNotFoundException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#collect(jp.go.nict.langrid.p2pgridbasis.controller.SearchCondition)
	 */
	public Data[] collect(ControllerSearchCondition con) throws ControllerException {
		try {
			JXTAPlatformSearchCondition cond = new JXTAPlatformSearchCondition(con.getName(), con.getValue(), con.getNum(), null);
			this.platform.searchAdvertisements(cond);

			Thread.sleep(con.getDataCollectWaitMillis());

			Advertisement[] advs = this.platform.getAdvertisements(cond);
			ArrayList<Data> data = new ArrayList<Data>();

			for(Advertisement adv : advs) {
				if (adv instanceof DataAdvertisement) {
					DataAdvertisement dadv = (DataAdvertisement) adv;
					if(dadv.getData().getType().equals(con.getType())) {
						data.add(dadv.getData());
					}
				}
			}

			return data.toArray(new Data[0]);
		} catch (InterruptedException e) {
			throw new ControllerException(e);
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	@Override
	public void addSeedUri(URI uri){
		RendezVousService s = platform.getNetworkManager().getNetPeerGroup().getRendezVousService();
		if(s instanceof RendezVousServiceImpl){
			((RendezVousServiceImpl)s).getPeerView().addSeed(uri);
		} else if(s instanceof RendezVousServiceInterface){
			((RendezVousServiceInterface)s).getPeerView().addSeed(uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#publish(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void publish(Data data) throws ControllerException {
		try {
			if(checkHosted(data.getGridId())){
				DataAdvertisement dataAdv = new DataAdv(data, platform.getPeerID());
				platform.publish(dataAdv);
				baseSummaryAdd(data);
				custSummaryAdd(data);
				summaryPublishSet(PeerSummaryAdv._customSummary);
			}else{
				baseSummaryAdd(data);
			}
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#stateDataPublish(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void stateDataPublish(Data data) throws ControllerException {
		try {
			if(checkHosted(data.getGridId())){
				DataAdvertisement dataAdv = new DataAdv(data, platform.getPeerID());
				platform.publish(dataAdv);

				stateSummaryAdd(data);
			}else{
				baseSummaryAdd(data);
			}
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#logDataPublish(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void logDataPublish(Data data) throws ControllerException {
		try {
			if(checkHosted(data.getGridId())){
				DataAdvertisement dataAdv = new DataAdv(data, platform.getPeerID());
				platform.publish(dataAdv, 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24);

				logSummaryAdd(data);
				summaryPublishSet(PeerSummaryAdv._logSummary);
			}else{
				baseSummaryAdd(data);
			}
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#summaryPublish(String)
	 */
	synchronized public void summaryPublish(String tag) throws ControllerException {
		try {
			final int AdvMax = 100; //summary1件当たりのデータ数
			int i = 0;
			int j = 0;
			int k = 0;
			DataSummary[] dataSummaries;

			if(tag == PeerSummaryAdv._hostedSummary){
				dataSummaries = hostSummaryDao.getAllDataSummaries();
				k = hostedSummarySequentialNumber;
			}else if(tag == PeerSummaryAdv._customSummary){
				dataSummaries = custSummaryDao.getAllDataSummaries();
				summaryTimer = null;
			}else if(tag == PeerSummaryAdv._logSummary){
				dataSummaries = logSummaryDao.getAllDataSummaries();
				logTimer = null;
			}else if(tag == PeerSummaryAdv._stateSummary){
				dataSummaries = stateSummaryDao.getAllDataSummaries();
			}else{
				logger.severe(tag + " : is don't use!!");
				return;
			}

			while(j < dataSummaries.length){
				int size = dataSummaries.length - j > AdvMax ? AdvMax:dataSummaries.length - j;
				DataSummary[] dataSummaries_buff = new DataSummary[size];

				for(i = 0; j < dataSummaries.length; i++, j++){
					dataSummaries_buff[i] = dataSummaries[j];

					if(i >= AdvMax-1){
						j++;
						break;
					}
				}
				PeerSummary peerSummary = new PeerSummary(new PeerSummaryID(platform.getPeerID(), tag + String.valueOf(++k)), dataSummaries_buff);
				PeerSummaryAdvertisement peerSummaryAdv = new PeerSummaryAdv(peerSummary);
				platform.publish(peerSummaryAdv);
			}
			if(tag == PeerSummaryAdv._hostedSummary){
				hostedSummarySequentialNumber = k;
			}
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#baseSummaryAdd(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void baseSummaryAdd(Data data) throws ControllerException {
		try {
			baseSummaryDao.deleteDataSummary(data.getId());
		} catch (DataSummaryNotFoundException e) {
			// Do nothing
		}

		try {
			baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
		} catch (DataSummaryAlreadyExistsException e) {
			assert false : "Daoへのデータサマリの追加に失敗した";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#custSummaryAdd(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized private void custSummaryAdd(Data data) throws ControllerException {
		try {
			custSummaryDao.deleteDataSummary(data.getId());
		} catch (DataSummaryNotFoundException e) {
			// Do nothing
		}

		try {
			custSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
		} catch (DataSummaryAlreadyExistsException e) {
			assert false : "Daoへのデータサマリの追加に失敗した";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#logSummaryAdd(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void logSummaryAdd(Data data) throws ControllerException {
		logSummaryDao.addLogSummary(new DataSummary(data.getId(), data.getLastUpdate()));
		baseSummaryDao.addLogSummary(new DataSummary(data.getId(), data.getLastUpdate()));
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#stateSummaryAdd(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public void stateSummaryAdd(Data data) throws ControllerException {
		try {
			stateSummaryDao.deleteDataSummary(data.getId());
			baseSummaryDao.deleteDataSummary(data.getId());
		} catch (DataSummaryNotFoundException e) {
			// Do nothing
		}

		try {
			stateSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
			baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
		} catch (DataSummaryAlreadyExistsException e) {
			assert false : "Daoへのデータサマリの追加に失敗した";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#custSummaryPublishSet(String)
	 */
	synchronized public void summaryPublishSet(String tag) throws ControllerException {
		if(tag == PeerSummaryAdv._customSummary){
			if(summaryTimer == null){
				summaryTask  = new summaryTimerTask(this, tag);
				summaryTimer = new Timer(true);
				summaryTimer.schedule(summaryTask, 60 * 1000);
			}
		}else if(tag == PeerSummaryAdv._logSummary){
			if(logTimer == null){
				logTask  = new summaryTimerTask(this, tag);
				logTimer = new Timer(true);
				logTimer.schedule(logTask, 60 * 1000);
			}
		}else{
			logger.severe(tag + " : is not defined!!");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#removeAccessLogSummary()
	 */
	public void removeAccessLogSummary() {
		try {
			boolean isDelete = false;
			long nowTimeMillis  = Calendar.getInstance().getTimeInMillis();

			for(DataSummary ds : logSummaryDao.getAllDataSummaries()){
				if(ds.getId().toString().startsWith(AccessLogData._IDPrefix)){
					long dataTimeMillis = ds.getLastUpdateDate().getTimeInMillis();

					if((nowTimeMillis - dataTimeMillis) > DayMills){
						logSummaryDao.deleteDataSummary(ds.getId());
						baseSummaryDao.deleteDataSummary(ds.getId());
						isDelete = true;
					}
				}
			}

			if(isDelete){
				summaryPublish(PeerSummaryAdv._logSummary);
			}
		} catch (DataSummaryNotFoundException e) {
			// Do nothing
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#removeAccessStateSummary()
	 */
	public void removeAccessStateSummary() {
		try {
			boolean isDelete = false;
			long nowTimeMillis  = Calendar.getInstance().getTimeInMillis();

			for(DataSummary ds : stateSummaryDao.getAllDataSummaries()){
				if(ds.getId().toString().startsWith(AccessStateData._IDPrefix)){
					long dataTimeMillis = ds.getLastUpdateDate().getTimeInMillis();

					if((nowTimeMillis - dataTimeMillis) > DayMills){
						stateSummaryDao.deleteDataSummary(ds.getId());
						baseSummaryDao.deleteDataSummary(ds.getId());
						isDelete = true;
					}
				}
			}

			if(isDelete){
				summaryPublish(PeerSummaryAdv._stateSummary);
			}
		} catch (DataSummaryNotFoundException e) {
			// Do nothing
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#revoke(jp.go.nict.langrid.p2pgridbasis.data.DataID)
	 */
	public void revoke(DataID id) throws ControllerException, DataNotFoundException {
		try {
			Data data = getData(id);
			data.setLastUpdate(Calendar.getInstance());
			data.getAttributes().setAttribute(IS_DELETED, "true");

			publish(data);
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	private boolean checkHosted(String gid) {
		if(gid == null){
			//To always do publish about "Grid" and "Federation", true is returned.
			return true;
		}

		try {
			return DaoFactory.createInstance().createGridDao().getGrid(gid).isHosted();
		} catch (GridNotFoundException e) {
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	private Data getData(DataID id) throws DataNotFoundException, JXTAPlatformException{
		Advertisement[] advs = platform.getAdvertisements(new JXTAPlatformSearchCondition(DataAdv._tagDataID, id.toString(), 10000, null));
		Data data = null;
		for(Advertisement adv : advs) {
			if (adv instanceof DataAdvertisement) {
				DataAdvertisement dadv = (DataAdvertisement) adv;
				if (dadv.getPeerID().equals(platform.getPeerID())) {
					data = (Data)dadv.getData();
				}
			}
		}

		if(data == null) {
			throw new DataNotFoundException(id);
		}

		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#shutdown()
	 */
	public void shutdown() {
		platform.shutdown();
		if(collectorThread != null){
			collectorThread.interrupt();
			collectorThread = null;
		}
		if(logTimer != null){
			logTimer.cancel();
			logTimer = null;
		}
		if(deleteTimer != null){
			deleteTimer.cancel();
			deleteTimer = null;
		}
		if(summaryTimer != null){
			summaryTimer.cancel();
			summaryTimer = null;
		}
		removeEntityListener();
	}

	private void setEntityListener(){
		try {
			P2PGridDaoFactory.getDataDao("AccessLimitData").setEntityListener();
			P2PGridDaoFactory.getDataDao("AccessLogData").setEntityListener();
			P2PGridDaoFactory.getDataDao("AccessRightData").setEntityListener();
			P2PGridDaoFactory.getDataDao("AccessStateData").setEntityListener();
			P2PGridDaoFactory.getDataDao("DomainData").setEntityListener();
			P2PGridDaoFactory.getDataDao("FederationData").setEntityListener();
			P2PGridDaoFactory.getDataDao("GridData").setEntityListener();
			P2PGridDaoFactory.getDataDao("NewsData").setEntityListener();
			P2PGridDaoFactory.getDataDao("NodeData").setEntityListener();
			P2PGridDaoFactory.getDataDao("OverUseLimitData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ProtocolData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceTypeData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceMetaAttributeData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceTypeData").setEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceMetaAttributeData").setEntityListener();
			P2PGridDaoFactory.getDataDao("TemporaryUserData").setEntityListener();
			P2PGridDaoFactory.getDataDao("UserData").setEntityListener();
		} catch (DataTypeNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void removeEntityListener(){
		try {
			P2PGridDaoFactory.getDataDao("AccessLimitData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("AccessLogData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("AccessRightData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("AccessStateData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("DomainData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("FederationData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("GridData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("NewsData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("NodeData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("OverUseLimitData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ProtocolData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceTypeData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ResourceMetaAttributeData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceTypeData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("ServiceMetaAttributeData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("TemporaryUserData").removeEntityListener();
			P2PGridDaoFactory.getDataDao("UserData").removeEntityListener();
		} catch (DataTypeNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#hostSummaryCreate(String)
	 */
	public boolean hostSummaryCreate(String gridId) throws ControllerException {
		if(!platform.isRdv()){
			return false;
		}
		StringBuilder b = new StringBuilder("--- publishing host entities of " + gridId + " ---\n");
		try{
			DaoFactory factory = DaoFactory.createInstance();
			factory.getDaoContext().beginTransaction();
			int c = 0;
			//User
			c = 0;
			for (User user : factory.createUserDao().dumpAllUsers(gridId)) {
				UserData data = new UserData(user);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("User: " + c + "\n");

			//TemporaryUser
			c = 0;
			for (TemporaryUser tempUser : factory.createTemporaryUserDao().listAllUsers(gridId)) {
				TemporaryUserData data = new TemporaryUserData(tempUser);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("TemporaryUser: " + c + "\n");

			//AccessRight
			c = 0;
			for (AccessRight accessRight : factory.createAccessRightDao().listAccessRights(gridId)) {
				AccessRightData data = new AccessRightData(accessRight);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("AccessRight: " + c + "\n");

			//Service
			c = 0;
			for (Service service : factory.createServiceDao().listAllServices(gridId)) {
				ServiceData data = new ServiceData(service);
				//Instance
				InputStream is = factory.createServiceDao().getServiceInstance(gridId, service.getServiceId());
				if(is != null){
					try {
						data.getAttributes().setAttribute(
								"instance"
								, Base64.encode(StreamUtil.readAsBytes(is))
								);
					} catch (IOException e) {
						throw new ControllerException(e);
					}
				}

				//Wsdl
				is = factory.createServiceDao().getServiceWsdl(gridId, service.getServiceId());
				if(is != null){
					try {
						data.getAttributes().setAttribute(
								"wsdl"
								, Base64.encode(StreamUtil.readAsBytes(is))
								);
					} catch (IOException e) {
						throw new ControllerException(e);
					}
				}

				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("Service: " + c + "\n");

			//News
			c = 0;
			for (News news : factory.createNewsDao().listNews(gridId)) {
				NewsData data = new NewsData(news);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("News: " + c + "\n");

			//Node
			c = 0;
			for (Node node : factory.createNodeDao().listAllNodes(gridId)) {
				NodeData data = new NodeData(node);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("Node: " + c + "\n");

			//AccessLimit
			c = 0;
			for (AccessLimit limit : factory.createAccessLimitDao().listAccessLimits(gridId)) {
				AccessLimitData data = new AccessLimitData(limit);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("AccessLimit: " + c + "\n");

			//OverUseLimit
			c = 0;
			for (OverUseLimit limit : factory.createOverUseLimitDao().listOverUseLimits(gridId, new Order[]{})) {
				OverUseLimitData data = new OverUseLimitData(limit);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("OverUseLimit: " + c + "\n");

			//Resource
			c = 0;
			for (Resource resource : factory.createResourceDao().listAllResources(gridId)) {
				ResourceData data = new ResourceData(resource);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("Resource: " + c + "\n");

			//AccessState
			c = 0;
			for (AccessStat state : factory.createAccessStateDao().listAccessStatsNewerThanOrEqualsTo(gridId, CalendarUtil.createBeginningOfDay(Calendar.getInstance()))) {
				AccessStateData data = new AccessStateData(state);
				baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				stateSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			b.append("AccessStat: " + c + "\n");

			//AccessLog
/*			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			logger.info("--- publishing host AccessLogs.");
			c = 0;
			for (AccessLog log : factory.createAccessLogDao().listAccessLogsNewerThanOrEqualsTo(
					gridId, cal)) {
				AccessLogData data = new AccessLogData(log);
				baseSummaryDao.addLogSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				logSummaryDao.addLogSummary(new DataSummary(data.getId(), data.getLastUpdate()));
				platform.publish(new DataAdv(data, platform.getPeerID()));
				c++;
			}
			logger.info("--- " + c + " entities done.");
*/		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		} catch (DaoException e) {
			throw new ControllerException(e);
		} catch (DataSummaryAlreadyExistsException e) {
			throw new ControllerException(e);
		} catch (DataConvertException e) {
			throw new ControllerException(e);
		} catch (ControllerException e) {
			throw new ControllerException(e);
		}
		logger.info(b.toString());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#baseSummaryCreate()
	 */
	public void baseSummaryCreate() throws ControllerException {
		try
		{
			DaoFactory factory = DaoFactory.createInstance();
			factory.getDaoContext().beginTransaction();

			List<Grid> gridList = factory.createGridDao().listAllGrids();

			int c = 0;
			for(Grid grid : gridList){
				String gridId = grid.getGridId();
				if(platform.isRdv() && grid.isHosted()){
					hostSummaryCreate(gridId);
				}else{
/*					StringBuilder b = new StringBuilder(
							"--- publishing non-hosted entities of " + gridId + ". ---\n");
					//User
					c = 0;
					for (User user : factory.createUserDao().dumpAllUsers(gridId)) {
						UserData data = new UserData(user);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("User: " + c + "\n");

					//TemporaryUser
					c = 0;
					for (TemporaryUser tempUser : factory.createTemporaryUserDao().listAllUsers(gridId)) {
						TemporaryUserData data = new TemporaryUserData(tempUser);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("Temporary: " + c + "\n");

					//AccessRight
					c = 0;
					for (AccessRight accessRight : factory.createAccessRightDao().listAccessRights(gridId)) {
						AccessRightData data = new AccessRightData(accessRight);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("AccessRight: " + c + "\n");

					//Service
					c = 0;
					for (Service service : factory.createServiceDao().listAllServices(gridId)) {
						ServiceData data = new ServiceData(service);

						//Instance
						InputStream is = factory.createServiceDao().getServiceInstance(gridId, service.getServiceId());
						if(is != null){
							try {
								data.getAttributes().setAttribute(
										"instance"
										, Base64.encode(StreamUtil.readAsBytes(is))
										);
							} catch (IOException e) {
								throw new ControllerException(e);
							}
						}

						//Wsdl
						is = factory.createServiceDao().getServiceWsdl(gridId, service.getServiceId());
						if(is != null){
							try {
								data.getAttributes().setAttribute(
										"wsdl"
										, Base64.encode(StreamUtil.readAsBytes(is))
										);
							} catch (IOException e) {
								throw new ControllerException(e);
							}
						}
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("Service: " + c + "\n");

					//News
					c = 0;
					for (News news : factory.createNewsDao().listNews(gridId)) {
						NewsData data = new NewsData(news);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("News: " + c + "\n");

					//Node
					c = 0;
					for (Node node : factory.createNodeDao().listAllNodes(gridId)) {
						NodeData data = new NodeData(node);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("Node: " + c + "\n");

					//AccessLimit
					c = 0;
					for (AccessLimit limit : factory.createAccessLimitDao().listAccessLimits(gridId)) {
						AccessLimitData data = new AccessLimitData(limit);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("AccessLimit: " + c + "\n");

					//OverUseLimit
					c = 0;
					for (OverUseLimit limit : factory.createOverUseLimitDao().listOverUseLimits(gridId, new Order[]{})) {
						OverUseLimitData data = new OverUseLimitData(limit);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("OverUseLimit: " + c + "\n");

					//Resource
					c = 0;
					for (Resource resource : factory.createResourceDao().listAllResources(gridId)) {
						ResourceData data = new ResourceData(resource);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("Resource: " + c + "\n");

					//AccessState
					c = 0;
					for (AccessStat state : factory.createAccessStateDao().listAccessStatsNewerThanOrEqualsTo(gridId, CalendarUtil.createBeginningOfDay(Calendar.getInstance()))) {
						AccessStateData data = new AccessStateData(state);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("AccessStat: " + c + "\n");

					//AccessLog
					c = 0;
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -1);
					for (AccessLog log : factory.createAccessLogDao().listAccessLogsNewerThanOrEqualsTo(
							gridId, cal)) {
						AccessLogData data = new AccessLogData(log);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
					b.append("AccessLog: " + c + "\n");

					logger.info(b.toString());
*/				}
			}

			if(platform.isRdv()){
				StringBuilder b = new StringBuilder("--- publishing domain entities of "
						+ getSelfGridId() + " ---\n");
				//Grid
				c = 0;
				for (Grid grid : gridList){
					GridData data = new GridData(grid);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("Grid: " + c + "\n");

				//Federation
				c = 0;
				for (Federation federation : factory.createFederationDao().list()) {
					FederationData data = new FederationData(federation);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("Federation: " + c + "\n");

				//Domain
				c = 0;
				for (Domain domain : factory.createDomainDao().listAllDomains()) {
					DomainData data = new DomainData(domain);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("Domain: " + c + "\n");

				//Protocol
				c = 0;
				for (Protocol protocol : factory.createProtocolDao().listAllProtocols()) {
					ProtocolData data = new ProtocolData(protocol);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("Protocol: " + c + "\n");

				//ResourceMetaAttribute
				c = 0;
				for (ResourceMetaAttribute attr : factory.createResourceTypeDao().listAllResourceMetaAttributes()) {
					ResourceMetaAttributeData data = new ResourceMetaAttributeData(attr);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("ResourceMetaAttribute: " + c + "\n");

				//ResourceType
				c = 0;
				for (ResourceType resourceType : factory.createResourceTypeDao().listAllResourceTypes()) {
					ResourceTypeData data = new ResourceTypeData(resourceType);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("ResourceType: " + c + "\n");

				//ServiceMetaAttribute
				c = 0;
				for (ServiceMetaAttribute attr : factory.createServiceTypeDao().listAllServiceMetaAttributes()) {
					ServiceMetaAttributeData data = new ServiceMetaAttributeData(attr);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("ServiceMetaAttribute: " + c + "\n");

				//ServiceType
				c = 0;
				for (ServiceType serviceType : factory.createServiceTypeDao().listAllServiceTypes()) {
					ServiceTypeData data = new ServiceTypeData(serviceType);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					hostSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					platform.publish(new DataAdv(data, platform.getPeerID()));
					c++;
				}
				b.append("ServiceType: " + c + "\n");
				logger.info(b.toString());

				//RdvNodeのみ既存データのSummaryを送出
				logger.info("--- publishing summeries of hosted elements.");
				summaryPublish(PeerSummaryAdv._hostedSummary);
				summaryPublish(PeerSummaryAdv._logSummary);
				summaryPublish(PeerSummaryAdv._stateSummary);
			}else{
				//Grid
				logger.info("--- publishing edge Grids.");
				c = 0;
				for (Grid grid : gridList){
					GridData data = new GridData(grid);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//Domain
				logger.info("--- publishing edge Domains.");
				c = 0;
				for (Domain domain : factory.createDomainDao().listAllDomains()) {
					DomainData data = new DomainData(domain);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//Federation
				logger.info("--- publishing edge Federations.");
				c = 0;
				for (Federation federation : factory.createFederationDao().list()) {
					FederationData data = new FederationData(federation);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//Protocol
				logger.info("--- publishing edge Protocols.");
				c = 0;
				for (Protocol protocol : factory.createProtocolDao().listAllProtocols()) {
					ProtocolData data = new ProtocolData(protocol);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//ResourceMetaAttribute
				logger.warning("--- publishing edge ResourceMetaAttributes.");
				c = 0;
				for (ResourceMetaAttribute attr : factory.createResourceTypeDao().listAllResourceMetaAttributes()) {
					ResourceMetaAttributeData data = new ResourceMetaAttributeData(attr);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//ResourceType
				logger.info("--- publishing edge ResourceTypes.");
				c = 0;
				for (ResourceType resourceType : factory.createResourceTypeDao().listAllResourceTypes()) {
					ResourceTypeData data = new ResourceTypeData(resourceType);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//ServiceMetaAttribute
				logger.info("--- publishing edge ServiceMetaAttributes.");
				c = 0;
				for (ServiceMetaAttribute attr : factory.createServiceTypeDao().listAllServiceMetaAttributes()) {
					ServiceMetaAttributeData data = new ServiceMetaAttributeData(attr);
					baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
					c++;
				}
				logger.info("--- " + c + " entities done.");

				//ServiceType
				logger.info("--- publishing edge ServiceTypes.");
				c = 0;
				for(Domain d : factory.createDomainDao().listAllDomains(selfGridId)){
					for (ServiceType serviceType : factory.createServiceTypeDao().listAllServiceTypes(d.getDomainId())) {
						ServiceTypeData data = new ServiceTypeData(serviceType);
						baseSummaryDao.addDataSummary(new DataSummary(data.getId(), data.getLastUpdate()));
						c++;
					}
				}
				logger.info("--- " + c + " entities done.");
			}
			logger.info("--- done.");

			factory.getDaoContext().commitTransaction();
		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		} catch (DaoException e) {
			throw new ControllerException(e);
		} catch (DataSummaryAlreadyExistsException e) {
			throw new ControllerException(e);
		} catch (DataConvertException e) {
			throw new ControllerException(e);
		} catch (ControllerException e) {
			throw new ControllerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController#start()
	 */
	@Override
	public void start() throws ControllerException {
		try {
			DaoFactory factory = DaoFactory.createInstance();
			DaoContext c = factory.getDaoContext();
			c.beginTransaction();
			try{
				GridDao gdao = factory.createGridDao();
				List<URL> urls = new ArrayList<URL>();
				Grid self = gdao.getGrid(selfGridId);
				if(self.isHosted()){
					FederationDao fdao = factory.createFederationDao();
					for(Federation f : fdao.listFederationsFrom(selfGridId)){
						if(f.isRequesting() || !f.isConnected()) continue;
						Grid g = null;
						try{
							g = gdao.getGrid(f.getTargetGridId());
						} catch(GridNotFoundException e){
							logger.log(Level.SEVERE, "federated grid not found: " + f.getTargetGridId(), e);
						}
						if(g == null) continue;
						if(g.isHosted()) continue;
						String gu = g.getUrl();
						if(gu == null || gu.trim().length() == 0) continue;
						URL gurl = new URL(gu);
						urls.add(new URL(
								gurl.getProtocol() + "://"
								+ f.getTargetGridUserId() + ":" + f.getTargetGridAccessToken()
								+ "@" + gurl.getHost()
								+ ((gurl.getPort() != -1) ? ":" + gurl.getPort() : "")
								+ gurl.getFile()
								+ (gurl.getFile().endsWith("/") ? "" : "/")
								+ "RdvPeer"
								));
					}
				} else{
					NodeDao ndao = factory.createNodeDao();
					Node n = ndao.getNode(selfGridId, selfNodeId);
					URL gurl = new URL(self.getUrl());
					urls.add(new URL(
							gurl.getProtocol() + "://"
							+ n.getOwnerUserId() + ":" + n.getAccessToken()
							+ "@" + gurl.getHost()
							+ ((gurl.getPort() != -1) ? ":" + gurl.getPort() : "")
							+ gurl.getFile()
							+ (gurl.getFile().endsWith("/") ? "" : "/")
							+ "RdvPeer"
							));
				}
				platform.start(selfGridId, self.isHosted(), urls, new DataDiscoveryListener(this));
			} catch(MalformedURLException e){
				throw new ControllerException(e);
			} finally{
				c.commitTransaction();
			}

			P2PGridDaoFactory.setupDataDaoMap(platform.getPeerID(), activeBpelServicesUrl, activeBpelDeployBinding);

			baseSummaryCreate();
			setEntityListener();

			this.collector = new PeerSummaryCollector(platform, baseSummaryDao, new FullySynchronizeStrategy(platform));
			this.collectorThread = new Thread(collector);
			this.collectorThread.start();

			deleteTask  = new deleteTimerTask(this);
			deleteTimer = new Timer(true);
			deleteTimer.schedule(deleteTask, deleteTimerMills, deleteTimerMills);

		} catch (JXTAPlatformException e) {
			throw new ControllerException(e);
		} catch (DaoException e) {
			throw new ControllerException(e);
		} catch (PeerGroupException e) {
			throw new ControllerException(e);
		}
	}

	@Override
	public void showStatus(PrintStream stream) {
		this.platform.showStatus(stream, true);
	}

	@Override
	public String getSelfGridId() {
		return selfGridId;
	}

	public PeerGroup getPeerGroup() {
		return this.platform.getPeerGroup();
	}

	public static void setBaseDir(File baseDir) {
		JXTAController.baseDir = baseDir;
	}

	public static synchronized void setUpInstance(ServiceContext context)
	throws ControllerException{
		instance = new JXTAController(context);
	}

	public static synchronized P2PGridController getInstance()
	throws ControllerException {
		if (instance == null) {
			throw new ControllerException("JXTAController hasn't be collectly initialized.");
		}
		return instance;
	}

	private JXTAPlatform platform;
	private DataSummaryDao baseSummaryDao;
	private DataSummaryDao custSummaryDao;
	private DataSummaryDao hostSummaryDao;
	private DataSummaryDao logSummaryDao;
	private DataSummaryDao stateSummaryDao;
	private PeerSummaryCollector collector;
	private Thread collectorThread;
	private String selfGridId;
	private String selfNodeId;
	private String activeBpelServicesUrl;
	private String activeBpelDeployBinding;
	private static final String IS_DELETED = "IsDeleted";
	private static P2PGridController instance;
	private static int hostedSummarySequentialNumber = 0;
	private static File baseDir;
	private static Timer summaryTimer;
	private static TimerTask summaryTask;
	private static Timer logTimer;
	private static TimerTask logTask;
	private static Timer deleteTimer;
	private static TimerTask deleteTask;
	private static final long DayMills = 24 * 60 * 60 * 1000;
	private static final long deleteTimerMills = 10 * 60 * 1000; //AccessLog削除間隔ミリ秒
	static private Logger logger = Logger.getLogger(JXTAController.class.getName());
}

/**
 * SummaryAdv送出用タイマー
 */
class summaryTimerTask extends TimerTask{
	/**
	 * コンストラクタ
	 * @param ctrl
	 * @param tag
	 */
	public summaryTimerTask(JXTAController ctrl, String tag) {
		this.ctrl = ctrl;
		this.tag  = tag;
	}

	public void run() {
		try {
			ctrl.summaryPublish(tag);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	private JXTAController ctrl;
	private String tag;
}

/**
 * AccessLog/AccessState削除用タイマー
 */
class deleteTimerTask extends TimerTask{
	/**
	 * コンストラクタ
	 * @param dao
	 * @param context
	 */
	public deleteTimerTask(JXTAController ctrl) {
		this.ctrl = ctrl;
	}

	public void run() {
		ctrl.removeAccessLogSummary();
//		ctrl.removeAccessStateSummary();
	}
	private JXTAController ctrl;

}
