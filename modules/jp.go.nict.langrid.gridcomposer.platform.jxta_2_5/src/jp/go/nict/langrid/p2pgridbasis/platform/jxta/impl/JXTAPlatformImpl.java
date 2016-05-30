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
package jp.go.nict.langrid.p2pgridbasis.platform.jxta.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformConfig;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformException;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformSearchCondition;
import jp.go.nict.langrid.p2pgridbasis.platform.jxta.util.JXTAPlatformUtil;
import net.jxta.credential.AuthenticationCredential;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.AdvertisementFactory.Instantiator;
import net.jxta.document.MimeMediaType;
import net.jxta.exception.PeerGroupException;
import net.jxta.exception.ProtocolNotSupportedException;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.rendezvous.RendezVousServiceInterface;
import net.jxta.impl.rendezvous.rpv.PeerView;
import net.jxta.impl.rendezvous.rpv.PeerViewElement;
import net.jxta.membership.Authenticator;
import net.jxta.membership.MembershipService;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.rendezvous.RendezvousEvent;
import net.jxta.rendezvous.RendezvousListener;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public class JXTAPlatformImpl implements JXTAPlatform {
	private static final String JXTA_DIR = "jxta";
	private static Logger logger = Logger.getLogger(JXTAPlatformImpl.class);
	private static final int _wait_group_discovery_millsec = 20000;

	protected static final String INITNODESDIR = "initnodes";
	protected static MimeMediaType mimeMediaType = new MimeMediaType("text/xml");
	protected PeerGroup netPeerGroup;
	protected PeerGroup langridPeerGroup;
	protected PeerGroup workingPeerGroup;
	protected JXTAPlatformConfig config;
	protected boolean running;
	protected boolean isRdv;
	private NetworkManager networkManager;

	/**
	 * 
	 * 
	 */
	public JXTAPlatformImpl(JXTAPlatformConfig config) {
		this.config = config;
		this.netPeerGroup = null;
		this.running = false;
	}

	@Override
	public NetworkManager getNetworkManager(){
		return networkManager;
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#start()
	 */
	public void start(
			String sourceGridId, boolean hosted, Collection<URL> seedUrls
			, DiscoveryListener discoveryListener) throws JXTAPlatformException{
		if(running == true) {
			throw new JXTAPlatformException("jxta platform is running");
		}

		try {
			registerAdvertisementInstances(config.getAdvertisementType());

			startJXTA(sourceGridId, hosted, seedUrls);

			String peerGroupName = config.getPeerGroupName();
			PeerGroupID pid = IDFactory.newPeerGroupID(peerGroupName.getBytes());
			if (config.isInitPeer()) {
				workingPeerGroup = langridPeerGroup = createPeerGroup(
						netPeerGroup, pid, peerGroupName, config.getPeerGroupDescription());
			} else {
				workingPeerGroup = langridPeerGroup = discoverPeerGroup(netPeerGroup, pid);
			}
			workingPeerGroup.getDiscoveryService().addDiscoveryListener(discoveryListener);
			joinGroupNullMembership(workingPeerGroup);

			running = true;

			startUpdater();
		} catch (PeerGroupException e) {
			throw new JXTAPlatformException(e);
		}
	}

	static private final int TRIAL_NUM = 1000;

	/**
	 * 
	 * 
	 */
	protected PeerGroup discoverPeerGroup(PeerGroup peerGroup, PeerGroupID pid) throws JXTAPlatformException {
		logger.debug("search peer group:");
		try {
			for(int i = 0; i < TRIAL_NUM; i++) {
				peerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 10);
				peerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.GROUP, null, null, 10);
				peerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10);
				logger.info("searching a peer group advertisement.");
				Thread.sleep(_wait_group_discovery_millsec);
				rdvstatus(true);
				Enumeration<Advertisement> enu = peerGroup.getDiscoveryService().getLocalAdvertisements(DiscoveryService.GROUP, null, null);
				while (enu.hasMoreElements()) {
					PeerGroupAdvertisement pga = (PeerGroupAdvertisement) enu.nextElement();
					logger.info("peer group advertisement found: " + pga.getName());
					if(pga.getPeerGroupID().equals(pid)) {
						logger.info(pga);
						return peerGroup.newGroup(pga);
					}else{
						logger.info(pga.getPeerGroupID());
						logger.info(pid.toString());
					}
				}
			}
			throw new JXTAPlatformException("langrid group is not found");
		} catch (InterruptedException e) {
			throw new JXTAPlatformException(e);
		} catch (IOException e) {
			throw new JXTAPlatformException(e);
		} catch (PeerGroupException e) {
			throw new JXTAPlatformException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	protected PeerGroup createPeerGroup(PeerGroup peerGroup, PeerGroupID pid, String peerGroupName, String peerGroupDescription) throws JXTAPlatformException {
		// 
		// 
		try {
			ModuleImplAdvertisement implAdv = (ModuleImplAdvertisement) peerGroup.getAllPurposePeerGroupImplAdvertisement(); //getImplAdvertisement();

			PeerGroup pg = peerGroup.newGroup(pid, implAdv, peerGroupName, peerGroupDescription);

			pg.publishGroup(peerGroupName, peerGroupDescription);
			pg.getDiscoveryService().publish(pg.getPeerGroupAdvertisement());
			pg.getDiscoveryService().remotePublish(pg.getPeerGroupAdvertisement());

			final String gn = peerGroupName;
			pg.getRendezVousService().addListener(new RendezvousListener() {
				@Override
				public void rendezvousEvent(RendezvousEvent event) {
					logger.info("rendezvousEvent for " + gn + ": " + event);
				}
			});
			pg.getRendezVousService().startRendezVous();

			logger.debug(pg.getPeerGroupAdvertisement());
			logger.debug(pg.getMembershipService());

			return pg;
		} catch (Exception e) {
			throw new JXTAPlatformException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void joinGroupNullMembership(PeerGroup grp) throws JXTAPlatformException{
		try {
			MembershipService membership = grp.getMembershipService();
			AuthenticationCredential authCred = new AuthenticationCredential(grp, null, null);
			Authenticator auth = membership.apply(authCred);

			logger.debug(auth.getClass());
			if (auth.isReadyForJoin()) {
				logger.info("join group:" + grp.getPeerGroupName());
				logger.debug(membership.getClass());
				membership.join(auth);
			} else {
				logger.info("auth is not ready for join");
			}
		} catch (ProtocolNotSupportedException e) {
			throw new JXTAPlatformException(e);
		} catch (PeerGroupException e) {
			throw new JXTAPlatformException(e);
		}
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#publish(net.jxta.document.Advertisement)
	 */
	public void publish(Advertisement advertisement) throws JXTAPlatformException {
		publish(advertisement, DiscoveryService.INFINITE_LIFETIME, DiscoveryService.NO_EXPIRATION);
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#publish(net.jxta.document.Advertisement)
	 */
	public void publish(Advertisement advertisement, long lifetimeMillis, long expirationMillis) throws JXTAPlatformException {
		logger.debug(advertisement);
		checkPlatformRunning();
		long start = System.currentTimeMillis();
		try{
			logger.debug(workingPeerGroup.getPeerGroupName());
			workingPeerGroup.getDiscoveryService().publish(advertisement, lifetimeMillis, expirationMillis);
			workingPeerGroup.getDiscoveryService().remotePublish(advertisement, expirationMillis);
		} catch (IOException e) {
			throw new JXTAPlatformException(e);
		} finally{
			countUpPublish(System.currentTimeMillis() - start);
		}
	}
	
	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#localLPblish(net.jxta.document.Advertisement)
	 */
	public void localPublish(Advertisement adv) throws JXTAPlatformException {
		localPublish(adv, DiscoveryService.INFINITE_LIFETIME, DiscoveryService.NO_EXPIRATION);
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#localLPblish(net.jxta.document.Advertisement)
	 */
	public void localPublish(Advertisement adv, long lifetimeMillis, long expirationMillis) throws JXTAPlatformException {
		long start = System.currentTimeMillis();
		try {
			workingPeerGroup.getDiscoveryService().publish(adv, lifetimeMillis, expirationMillis);
		} catch (IOException e) {
			throw new JXTAPlatformException(e);
		} finally{
			countUpLocalPublish(System.currentTimeMillis() - start);
		}
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#searchAdvertisements(jp.go.nict.langrid.p2pgridbasis.platform.jxta.SearchCondition)
	 */
	public void searchAdvertisements(JXTAPlatformSearchCondition cond) throws JXTAPlatformException{
		checkPlatformRunning();
		Enumeration<ID> enu = workingPeerGroup.getRendezVousService().getConnectedPeers();
		while(enu.hasMoreElements()) {
			logger.debug(enu.nextElement());
		}
		if(cond.getPeerID() == null) {
			workingPeerGroup.getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.ADV, cond.getName(), cond.getValue(), cond.getNum());
		} else {
			workingPeerGroup.getDiscoveryService().getRemoteAdvertisements(cond.getPeerID().toString(),
					DiscoveryService.ADV, cond.getName(), cond.getValue(), cond.getNum());
		}
	}

	/* (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatformInterface#getAdvertisements(jp.go.nict.langrid.p2pgridbasis.platform.jxta.SearchCondition)
	 */
	public Advertisement[] getAdvertisements(JXTAPlatformSearchCondition cond) throws JXTAPlatformException {
		try {
			Enumeration<Advertisement> enu = workingPeerGroup.getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, cond.getName(), cond.getValue());
			ArrayList<Advertisement> advs = new ArrayList<Advertisement>();
			while(enu.hasMoreElements()) {
				advs.add(enu.nextElement());
			}
			return advs.toArray(new Advertisement[0]);
		} catch (IOException e) {
			throw new JXTAPlatformException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkPlatformRunning() throws JXTAPlatformException{
		if(this.running == false) {
			throw new JXTAPlatformException("jxta platform is not running");
		}
	}

	public void shutdown() {
		localAdvUpdater.interrupt();
		langridPeerGroup.stopApp();
		netPeerGroup.stopApp();
	}

	public PeerID getPeerID() {
		return workingPeerGroup.getPeerID();
	}

	public void clearCache() {
		try {
			Enumeration<Advertisement> advertisements = workingPeerGroup.getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, null, null);
			while(advertisements.hasMoreElements()) {
				workingPeerGroup.getDiscoveryService().flushAdvertisement(advertisements.nextElement());
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	protected void registerAdvertisementInstances(Map<String, Instantiator> instances) {
		for(String typeName : instances.keySet()) {
			AdvertisementFactory.registerAdvertisementInstance(typeName, instances.get(typeName));
		}
	}

	private Thread localAdvUpdater;
	protected void startUpdater() {
		localAdvUpdater = new Thread() {
			public void run() {
				try {
					while(!Thread.interrupted()){
						peers_r();
//						rdvstatus(true);
						Thread.sleep(_wait_group_discovery_millsec);
					}
				} catch (InterruptedException e) {
					logger.warn(e.getMessage());
				}
			}
		};
		localAdvUpdater.start();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform#rdvstatus(boolean)
	 */
	public void rdvstatus(boolean verbose) {
		if(logger.isDebugEnabled()) {
			this.rdvstatus(System.err, verbose, netPeerGroup);
			this.rdvstatus(System.err, verbose, workingPeerGroup);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.platform.jxta.JXTAPlatform#peers_r()
	 */
	public void peers_r() {
		netPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 10);
		netPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.GROUP, null, null, 10);
		netPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10);
		workingPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 10);
		workingPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.GROUP, null, null, 10);
		workingPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10);
	}

	@SuppressWarnings("deprecation")
	public void rdvstatus(PrintStream stream, boolean verbose, PeerGroup peerGroup) {
		RendezVousService rdv = peerGroup.getRendezVousService();

		if( null == rdv ) {
			stream.println( "No Rendezvous Service in group " );
			return;
		}

		stream.println( "-----------------------------" );
		stream.println( "Rendezvous Status : " + peerGroup.getPeerGroupName());
		stream.println( "---" );

		stream.println( "Current configuration : " + rdv.getRendezVousStatus() );

		RendezVousServiceInterface stdRdv = null;
		net.jxta.impl.rendezvous.StdRendezVousService stdRdvProvider = null;
		if( rdv.isRendezVous()){
			if( rdv instanceof net.jxta.impl.rendezvous.RendezVousServiceInterface ) {
				stdRdv = (RendezVousServiceInterface) rdv;

				if( null != stdRdv ) {
					net.jxta.impl.rendezvous.RendezVousServiceProvider provider = stdRdv.getRendezvousProvider();

					if( provider instanceof net.jxta.impl.rendezvous.StdRendezVousService ) {
						stdRdvProvider = (net.jxta.impl.rendezvous.StdRendezVousService) provider;
					}
				}

				PeerView rpv = stdRdv.getPeerView();
				PeerViewElement self = rpv.getSelf();
				if( verbose ) {
					stream.println( "\t" + self.getRdvAdvertisement().getPeerID() );
				}
				stream.println( "\t" + self );

				PeerViewElement pves[] = (PeerViewElement[]) rpv.getView().toArray( new PeerViewElement[0] );

				stream.println( "Peer View : " );
				if( pves.length > 0 ) {
					for( int eachPVE = pves.length -1; eachPVE >= 0; eachPVE-- ) {
						if( verbose ) {
							stream.println( "\t" + pves[eachPVE].getRdvAdvertisement().getPeerID() );
						}
						stream.println( "\t" + pves[eachPVE] );

						if( rdv.isRendezVous() ) {
							if( pves[eachPVE] == rpv.getUpPeer() ) {
								stream.println( "\t(UP)" );
							} else if( pves[eachPVE] == rpv.getDownPeer() ) {
								stream.println( "\t(DOWN)" );
							} else {
								stream.println( "" );
							}
						} else {
							stream.println( "" );
						}
					}
				} else {
					stream.println( "\t[None]" );
				}
			}
		}else{
			Enumeration<?> rdvs = rdv.getConnectedRendezVous();

			stream.println( "Rendezvous Connections :" );
			if ( !rdvs.hasMoreElements() ) {
				stream.println( "\t[None]" );

			} else {
				while ( rdvs.hasMoreElements() ) {
					try {
						ID connection = (PeerID) rdvs.nextElement();
						if( verbose ) {
							stream.println( "\t" + connection );
						}
						if( null != stdRdvProvider ) {
							stream.println( "\t" + stdRdvProvider.getPeerConnection( connection ) );
						} else {
							String peerName = connection.toString();
							stream.println( "\t" + peerName );
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}

			Enumeration<?> rmRdvs = rdv.getDisconnectedRendezVous();

			stream.println( "Rendezvous Disconnections :" );
			if ( !rmRdvs.hasMoreElements() ) {
				stream.println( "\t[None]" );
			} else {
				while ( rmRdvs.hasMoreElements() ) {
					try {
						ID connection = (PeerID) rmRdvs.nextElement();
						stream.println( "\t" );
						if( verbose ) {
							stream.println( "\t" + connection );
						}
						String peerName = connection.toString();
						stream.println( "\t" + peerName );
					} catch ( Exception e ) {
						logger.debug( "failed", e );
					}
				}

			}
		}

		// no need to display clients if the peer is not a rendezvous for
		// the group
		if( rdv.isRendezVous() ) {
			Enumeration<?> clients = rdv.getConnectedPeers();

			stream.println( "Rendezvous Client Connections :" );
			if ( !clients.hasMoreElements() ) {
				stream.println( "\t[None]" );

			} else {
				while ( clients.hasMoreElements() ) {
					try {
						ID connection = (PeerID) clients.nextElement();
						if( verbose ) {
							stream.println( "\t" + connection );
						}
						if( null != stdRdvProvider ) {
							stream.println( "\t" + stdRdvProvider.getPeerConnection( connection ) );
						} else {
							String peerName = connection.toString();
							stream.println( "\t" + peerName );
						}
					} catch ( Exception e ) {
						stream.println( "failed with " + e );
					}
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	protected void startJXTA(String gridId, boolean hosted, Collection<URL> seedUrls) throws PeerGroupException {
		try {
			URI storeHome = new File(config.getBaseDir(), JXTA_DIR).toURI();
			logger.info("basedir:" + config.getBaseDir().getAbsolutePath());
			NetworkConfigurator configurator = null;
			if(hosted){
				// RDV configuration(with httpOutgoing = true)
				networkManager = new NetworkManager(ConfigMode.RENDEZVOUS, config.getPeerName(), storeHome);
				configurator = networkManager.getConfigurator();
				configurator.setName(config.getPeerName());
				configurator.setDescription("RDV node");
				configurator.setHttpEnabled(true);
				configurator.setHttpIncoming(true);
				configurator.setHttpOutgoing(true);
				configurator.setTcpEnabled(false);
				configurator.setUseMulticast(false);
				configurator.setHttpPort(80);
			} else{
				// EDGE Configuration
				networkManager = new NetworkManager(ConfigMode.EDGE, config.getPeerName(), storeHome);
				configurator = networkManager.getConfigurator();
				configurator.setName(config.getPeerName());
				configurator.setDescription("EDGE node");
				configurator.setHttpEnabled(true);
				configurator.setHttpIncoming(false);
				configurator.setHttpOutgoing(true);
				configurator.setTcpEnabled(false);
				configurator.setUseMulticast(false);
			}
			for(URL url : config.getRdvPeerURLs()){
				configurator.addSeedRendezvous(new URI(url.toString() + "#" + gridId));
			}
			for(URL url : seedUrls){
				configurator.addSeedRendezvous(new URI(url.toString() + "#" + gridId));
			}
			configurator.save();

			netPeerGroup = networkManager.startNetwork();
			final String gn = netPeerGroup.getPeerGroupName();
			netPeerGroup.getRendezVousService().addListener(new RendezvousListener() {
				@Override
				public void rendezvousEvent(RendezvousEvent event) {
					logger.info("rendezvousEvent for " + gn + ": " + event);
				}
			});

			logger.info("waitForRendezvousConnection...");
			if(networkManager.waitForRendezvousConnection(0)){
				logger.info("Connect Rendezvous");
			}

			isRdv = netPeerGroup.isRendezvous();
			workingPeerGroup = netPeerGroup;
			JXTAPlatformUtil.setPeerGroup(workingPeerGroup);

			logger.info(netPeerGroup.getPeerAdvertisement());
			logger.debug(""+ netPeerGroup.getPeerID());
			logger.debug(""+ netPeerGroup.getPeerName());
			logger.debug(""+ netPeerGroup.getPeerGroupName());

			configurator.save();
		} catch (IOException e) {
			throw new PeerGroupException(e);
		} catch (URISyntaxException e) {
			throw new PeerGroupException(e);
//		} catch (CertificateException e) {
//			e.printStackTrace();
		}
	}

	public void showStatus(PrintStream stream, boolean verbose) {
		this.rdvstatus(stream, verbose, netPeerGroup);
		this.rdvstatus(stream, verbose, workingPeerGroup);
		this.peerAdv(stream);
	}

	private void peerAdv(PrintStream stream) {
		stream.println(this.netPeerGroup.getPeerAdvertisement().toString());
		stream.println(this.workingPeerGroup.getPeerAdvertisement().toString());
	}

	public PeerGroup getPeerGroup() {
		return this.workingPeerGroup;
	}

	@Override
	public boolean isRdv() {
		return isRdv;
	}

	private synchronized void countUpPublish(long millis){
		pubCount.incrementAndGet();
		pubTotalMillis.addAndGet((int)millis);
	}
	private synchronized void countUpLocalPublish(long millis){
		pubLocalCount.incrementAndGet();
		pubLocalTotalMillis.addAndGet((int)millis);
	}
	private AtomicInteger pubCount = new AtomicInteger();
	private AtomicInteger pubTotalMillis = new AtomicInteger();
	private AtomicInteger pubLocalCount = new AtomicInteger();
	private AtomicInteger pubLocalTotalMillis = new AtomicInteger();

	{
		new Timer(true).schedule(new TimerTask(){
			@Override
			public void run() {
				synchronized(JXTAPlatformImpl.this){
					long pc = pubCount.get();
					long ptm = pubTotalMillis.get();
					long plc = pubLocalCount.get();
					long pltm = pubLocalTotalMillis.get();
					java.util.logging.Logger.getAnonymousLogger().warning(String.format(
							"publish: %d times, %d millis ave." +
							"  local publicsh: %d times, %d millis ave."
							, pc, pc > 0 ? ptm / pc : 0
							, plc, plc > 0 ? pltm / plc : 0
							));
					pubCount.set(0);
					pubTotalMillis.set(0);
					pubLocalCount.set(0);
					pubLocalTotalMillis.set(0);
				}
			}
		}, 1000 * 60 * 3, 1000 * 60 * 10);
	}
}
