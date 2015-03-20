package net.jxta.impl.endpoint.servlethttp;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointService;
import net.jxta.endpoint.MessageReceiver;
import net.jxta.endpoint.MessengerEvent;
import net.jxta.endpoint.MessengerEventListener;
import net.jxta.exception.PeerGroupException;


public class CatalinaHttpMessageReceiver implements MessageReceiver {

    /**
     *  Log4j logger
     **/
    private final static transient Logger LOG = Logger.getLogger(CatalinaHttpMessageReceiver.class.getName());

    /**
     * The ServletHttpTransport that created this MessageReceiver.
     **/
    final ServletHttpTransport servletHttpTransport;

    /**
     * The public addresses for the this transport.
     **/
    private final List publicAddresses;

    /**
     * The listener to invoke when making an incoming messenger.
     **/
    private MessengerEventListener messengerEventListener;

    public CatalinaHttpMessageReceiver(ServletHttpTransport servletHttpTransport,
            List publicAddresses) throws PeerGroupException {
        this.servletHttpTransport = servletHttpTransport;
        this.publicAddresses = publicAddresses;

        if (LOG.isLoggable(Level.INFO)) {
            StringBuffer configInfo = new StringBuffer("Configuring HTTP Servlet Message Transport : " + servletHttpTransport.assignedID );
            LOG.info(configInfo.toString());
        }
    }

    
    synchronized void start() throws PeerGroupException {
    	instance = this;

        messengerEventListener = servletHttpTransport.getEndpointService().addMessageTransport(this);
        
        if (messengerEventListener == null) {
            throw new PeerGroupException("Transport registration refused");
        }
        
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info( "HTTP Servlet Transport started.");
        }
    }
    
    synchronized void stop() {
        servletHttpTransport.getEndpointService().removeMessageTransport(this);
        messengerEventListener = null;
        
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info( "HTTP Servlet Transport stopped.");
        }
    }

    /**
     * {@inheritDoc}
     **/
    boolean messengerReadyEvent(HttpServletMessenger newMessenger, EndpointAddress connAddr) {
        MessengerEventListener temp = messengerEventListener;
        
        if( null != temp ) {
            return temp.messengerReady(new MessengerEvent(this, newMessenger, connAddr));
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     **/
    public Iterator getPublicAddresses() {
        return Collections.unmodifiableList(publicAddresses).iterator();
    }

    /**
     * {@inheritDoc}
     **/
    public String getProtocolName() {
        return servletHttpTransport.HTTP_PROTOCOL_NAME;
    }

    /**
     * {@inheritDoc}
     **/
    public EndpointService getEndpointService() {
        return servletHttpTransport.getEndpointService();
    }
    
    /**
     * {@inheritDoc}
     **/
    public Object transportControl(Object operation, Object Value) {
        return null;
    }

    ServletHttpTransport getServletHttpTransport() {
        return servletHttpTransport;
    }
    
    static private CatalinaHttpMessageReceiver instance = null;
    static public CatalinaHttpMessageReceiver getMessageReceiver() {
    	return instance;
    }
}
