/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.decorator;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Shingo Furukido
 * @author Takao Nakaguchi
 */
public class LogDecorator implements Decorator
{
    private static Logger logger = Logger.getLogger( LogDecorator.class.getName( ) ) ;
    private boolean measureProcessTime = false ;
    private ThreadLocal<MeasureContext> measureCtx = new ThreadLocal<MeasureContext>( ) ;
    private long processTimeWarningThreasholdMillis = 1000 ;

    /**
     * 
     * 
     */
    public long getProcessTimeWarningThreasholdMillis( )
    {
        return processTimeWarningThreasholdMillis ;
    }

    /**
     * 
     * 
     */
    public void setProcessTimeWarningThreasholdMillis( long processTimeWarningThreasholdMillis )
    {
        this.processTimeWarningThreasholdMillis = processTimeWarningThreasholdMillis ;
    }

    /**
     * 
     * 
     */
    public boolean getMeasureProcessTime( )
    {
        return measureProcessTime ;
    }

    /**
     * 
     * 
     */
    public void setMeasureProcessTime( boolean measureProcessTime )
    {
        this.measureProcessTime = measureProcessTime ;
    }

    /**
     * 
     * 
     */
	@Override
	public Object doDecorate( Request request, DecoratorChain chain)
	throws InvocationTargetException, IllegalArgumentException, IllegalAccessException{
		Object result ;
//        LogListener orgLogListener = chain.getLogListener( ) ;
//        chain.setLogListener( this ) ;

		processStart( request.getServiceClass( ) ) ;
		try {
			result = chain.next( request ) ;
		} finally{
			processEnd( ) ;
//            chain.setLogListener( orgLogListener ) ;
		}
		return result ;
	}

    /**
     * 
     * 
     */
    protected void processStart( Class<?> serviceClass )
    {
        if( !measureProcessTime ) return ;
        if( measureCtx.get( ) != null ) {
            logger.severe( "panic! in start to measure process time. context already set" ) ;
        }
        MeasureContext ctx = new MeasureContext( serviceClass.getName( ) ) ;
        measureCtx.set( ctx ) ;
        logger.info( ctx.formatStart( ) ) ;
    }

    /**
     * 
     * 
     */
//    @Override
//    public void log( String message )
//    {
//        processLap( message ) ;
//    }

    /**
     * 
     * 
     */
    protected long processLap( String message )
    {
        if( !measureProcessTime ) return -1 ;
        MeasureContext ctx = measureCtx.get( ) ;
        if( ctx == null ) {
            logger.severe( "panic! in lap process time. context not initialized" ) ;
            return -1 ;
        }
        ctx.appendLap( message ) ;
        return ctx.getLastLapTime( ) ;
    }

    /**
     * 
     * 
     */
    protected void processEnd( )
    {
        processEnd( null ) ;
    }

    /**
     * 
     * 
     */
    protected void processEnd( Runnable onThreasholdOver )
    {
        if( !measureProcessTime ) return ;
        MeasureContext ctx = measureCtx.get( ) ;
        if( ctx == null ) {
            logger.severe( "panic! in finish to measure process time. context not initialized" ) ;
            return ;
        }
        logger.info( ctx.formatFinish( ) ) ;
        if( ctx.getTotalTime( ) >= processTimeWarningThreasholdMillis ) {
            logger.warning( ctx.formatProcessTimeOver( ) ) ;
            if(onThreasholdOver != null) onThreasholdOver.run( ) ;
        }
        measureCtx.set( null ) ;
    }

    /**
     * 
     * 
     */
    private static class MeasureContext
    {
        public MeasureContext( String className )
        {
            header = "[" + className + "][tid:"
            + Thread.currentThread().getId() + ",time:" ;
        }

        public String formatStart( )
        {
            start = System.currentTimeMillis( ) ;
            prev = start ;
            lastLapTime = 0 ;
            laps = new StringBuffer( ) ;
            return header + start + "] processs started." ;
        }

        public void appendLap( String message )
        {
            long t = System.currentTimeMillis( ) ;
            lastLapTime = t - prev ;
            prev = t ;
            laps.append( "lap(" + lastLapTime + "ms): " + message + "\n" ) ;
        }

        public String formatFinish( )
        {
            long t = System.currentTimeMillis( ) ;
            lastLapTime = t - prev ;
            totalTime = t - start ;
            String l = laps.toString( ) ;
            laps = new StringBuffer( ) ;

            return ((l.length() > 0) ? header + t + "] all laps ---\n" + l + "---\n" : "")
            + header + t + "] finished(" + lastLapTime + "ms laps, " + totalTime + "ms total)" ;
        }

        public String formatProcessTimeOver( )
        {
            return header + "] too long process millis: " + totalTime ;
        }

        public long getLastLapTime( )
        {
            return lastLapTime ;
        }
        public long getTotalTime( )
        {
            return totalTime ;
        }

        private String header ;
        private long start ;
        private long prev ;
        private long lastLapTime ;
        private long totalTime ;
        private StringBuffer laps ;
    }
}
