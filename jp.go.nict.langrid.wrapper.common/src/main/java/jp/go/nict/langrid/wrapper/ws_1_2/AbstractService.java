/*
 * $Id: AbstractService.java 507 2012-05-24 04:34:29Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.transformer.PassthroughTransformer;
import jp.go.nict.langrid.commons.transformer.StringToBooleanTransformer;
import jp.go.nict.langrid.commons.transformer.StringToIntegerTransformer;
import jp.go.nict.langrid.commons.transformer.StringToLongTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.ws.LocalServiceContext;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.transformer.LanguageToCodeStringTransformer;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 507 $
 */
public abstract class AbstractService
extends jp.go.nict.langrid.servicecontainer.service.AbstractService{
	public static ServiceContext getCurrentServiceContext(){
		return currentServiceContext.get();
	}

	public static void setCurrentServiceContext(ServiceContext context){
		currentServiceContext.set(context);
	}

	public ServiceContext getServiceContext(){
		if(serviceContext != null){
			return serviceContext;
		}
		ServiceContext sc = currentServiceContext.get();
		if(sc != null){
			return sc;
		}
		return new LocalServiceContext();
	}

	private static ThreadLocal<ServiceContext> currentServiceContext
			= new ThreadLocal<ServiceContext>();
	private static ThreadLocal<LogListener> logListener
			= new ThreadLocal<LogListener>();

	/**
	 * 
	 * 
	 */
	public void setLogListener(LogListener logListener){
		AbstractService.logListener.set(logListener);
	}

	/**
	 * 
	 * 
	 */
	public LogListener getLogListener(){
		return logListener.get();
	}

	/**
	 * 
	 * 
	 */
	public void log(String message){
		LogListener l = logListener.get();
		if(l == null) return;
		l.log(message);
	}

	/**
	 * 
	 * 
	 */
	public AbstractService(){
		init();
	}

	/**
	 * 
	 * 
	 */
	public AbstractService(ServiceContext serviceContext){
		this.serviceContext = serviceContext;
		init();
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public void setMaxWaitMillisForExternalProcess(
			int maxWaitMillisForExternalProcess) {
		this.maxWaitMillisForExternalProcess = maxWaitMillisForExternalProcess;
	}

	public void setMaxWaitMillisForThread(int maxWaitMillisForThread) {
		this.maxWaitMillisForThread = maxWaitMillisForThread;
	}

	public void setEliminateDuplicates(boolean eliminateDuplicates) {
		this.eliminateDuplicates = eliminateDuplicates;
	}

	public void setDoubleSearchResults(boolean doubleSearchResults) {
		this.doubleSearchResults = doubleSearchResults;
	}

	public void setMeasureProcessTime(boolean measureProcessTime) {
		this.measureProcessTime = measureProcessTime;
	}

	public void setProcessTimeWarningThreasholdMillis(
			long processTimeWarningThreasholdMillis) {
		this.processTimeWarningThreasholdMillis = processTimeWarningThreasholdMillis;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getLastUpdate()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	{
		return doGetLastUpdate();
	}

	protected int getMaxWaitMillisForExternalProcess(){
		return maxWaitMillisForExternalProcess;
	}

	protected int getMaxResults(){
		return maxResults;
	}

	protected boolean getEliminateDuplicates(){
		return eliminateDuplicates;
	}

	protected boolean getDoDoubleSearch(){
		return doubleSearchResults;
	}

	/**
	 * 
	 * 
	 */
	protected void checkStartupException()
	throws ProcessFailedException{
		if(startupException != null){
			throw startupException;
		}
	}

	protected void setStartupException(Exception exception){
		this.startupException = new ProcessFailedException(
				"service has exception on startup.", exception);
		logger.log(Level.SEVERE, "exception occurred on startup.", exception);
	}

	/**
	 * 
	 * 
	 */
	protected File getWorkDirectory(){
		return new File(getServiceContext().getRealPath("WEB-INF/work/" + getClass().getName()));
	}

	/**
	 * 
	 * 
	 */
	protected File getWorkFile(String directoryName){
		return new File(getWorkDirectory(), directoryName);
	}

	/**
	 * 
	 * 
	 */
	protected ParameterContext getInitParameters(){
		return new ServiceContextParameterContext(getServiceContext());
	}

	protected boolean getInitParameterBoolean(String parameterName, boolean defaultValue){
		return getInitParameterUsingTransformer(
				parameterName, defaultValue
				, new StringToBooleanTransformer()
				);
	}

	protected int getInitParameterInt(String parameterName, int defaultValue){
		return getInitParameterUsingTransformer(
				parameterName, defaultValue
				, new StringToIntegerTransformer()
				);
	}

	protected long getInitParameterLong(String parameterName, long defaultValue){
		return getInitParameterUsingTransformer(
				parameterName, defaultValue
				, new StringToLongTransformer()
				);
	}

	protected String getInitParameterString(String parameterName, String defaultValue){
		return getInitParameterUsingTransformer(
				parameterName, defaultValue
				, new PassthroughTransformer<String>()
				);
	}

	protected String getInitParameter(String parameterName){
		return getServiceContext().getInitParameter(parameterName);
	}

	protected <T> T getInitParameterUsingTransformer(
			String parameterName, T defaultValue
			, Transformer<String, T> transformer)
	{
		String v = getInitParameter(parameterName);
		if(v == null){
			return defaultValue;
		}
		try{
			return transformer.transform(v);
		} catch(TransformationException e){
			return defaultValue;
		}
	}

	/**
	 * 
	 * 
	 */
	protected MimeHeaders getRequestMimeHeaders(){
		return getServiceContext().getRequestMimeHeaders();
	}

	protected jp.go.nict.langrid.service_1_2.LanguagePair[] convertLanguagePairs(
			Collection<LanguagePair> pairs){
		jp.go.nict.langrid.service_1_2.LanguagePair[] p =
			new jp.go.nict.langrid.service_1_2.LanguagePair[pairs.size()];
		Iterator<LanguagePair> it = pairs.iterator();
		for(int i = 0; i < pairs.size(); i++){
			LanguagePair pair = it.next();
			p[i] = new jp.go.nict.langrid.service_1_2.LanguagePair(
					pair.getSource().getCode()
					, pair.getTarget().getCode()
			);
		}
		return p;
	}

	protected String[] convertLanguages(
			Collection<Language> languages){
		return ArrayUtil.collect(
				languages.toArray(new Language[]{})
				, new LanguageToCodeStringTransformer()
				);
	}

	protected void acquireSemaphore()
	throws ProcessFailedException{
		if(maxThreads <= 0) return;
		if(currentSemaphore.get() != null){
			logger.severe("panic! in treating semaphore. review acquireSemaphore/releaseSemaphore call" +
					" of service " + getClass().getName());
		}
		Class<? extends AbstractService> clazz = getClass();
		Semaphore s = semaphores.get(clazz);
		if(s == null){
			synchronized(clazz){
				s = semaphores.get(clazz);
				if(s == null){
					s = new Semaphore(maxThreads, true);
					semaphores.put(clazz, s);
				}
			}
		}
		if(s != null){
			try{
				if(!s.tryAcquire(maxWaitMillisForThread, TimeUnit.MILLISECONDS)){
					throw new ProcessFailedException("server busy.");
				}
			} catch(InterruptedException e){
				throw new ProcessFailedException(e);
			}
			currentSemaphore.set(s);
		}
	}

	protected void releaseSemaphore(){
		if(maxThreads <= 0) return;
		Semaphore s = currentSemaphore.get();
		if(s != null){
			s.release();
			currentSemaphore.set(null);
		}
	}

	protected void processStart(){
		if(!measureProcessTime) return;
		if(measureCtx.get() != null){
			logger.severe("panic! in start to measure process time. context already set");
		}
		MeasureContext ctx = new MeasureContext(getClass().getName());
		measureCtx.set(ctx);
		logger.info(ctx.formatStart());
	}

	protected long processLap(String message){
		if(!measureProcessTime) return -1;
		MeasureContext ctx = measureCtx.get();
		if(ctx == null){
			logger.severe("panic! in lap process time. context not initialized");
			return -1;
		}
		ctx.appendLap(message);
		return ctx.getLastLapTime();
	}

	protected void processEnd(){
		processEnd(null);
	}

	protected void processEnd(Runnable onThreasholdOver){
		if(!measureProcessTime) return;
		MeasureContext ctx = measureCtx.get();
		if(ctx == null){
			logger.severe("panic! in finish to measure process time. context not initialized");
			return;
		}
		logger.info(ctx.formatFinish());
		if(ctx.getTotalTime() >= processTimeWarningThreasholdMillis){
			logger.warning(ctx.formatProcessTimeOver());
			if(onThreasholdOver != null) onThreasholdOver.run();
		}
		measureCtx.set(null);
	}

	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro(
				"$Date: 2012-05-24 13:34:29 +0900 (Thu, 24 May 2012) $"
				);
	}

	protected Calendar parseDateMacro(String value)
	throws ProcessFailedException{
		Calendar c = ClassUtil.getLastModified(getClass());
		if(c != null) return c;
		try{
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ZZZZ").parse(
					value.substring(7, 32)
					);
			c = Calendar.getInstance();
			c.setTime(date);
			return c;
		} catch(ParseException e){
			throw new ProcessFailedException(e);
		}
	}

	private static class MeasureContext{
		public MeasureContext(String className){
			header = "[" + className + "][tid:"
				+ Thread.currentThread().getId() + ",time:";
		}
		public String formatStart(){
			start = System.currentTimeMillis();
			prev = start;
			lastLapTime = 0;
			laps = new StringBuffer();
			return header + start + "] processs started.";
		}
		public void appendLap(String message){
			long t = System.currentTimeMillis();
			lastLapTime = t - prev;
			prev = t;
			laps.append("lap(" + lastLapTime + "ms): " + message + "\n");
		}
		public String formatFinish(){
			long t = System.currentTimeMillis();
			lastLapTime = t - prev;
			totalTime = t - start;
			String l = laps.toString();
			laps = new StringBuffer();
			return ((l.length() > 0) ? header + t + "] all laps ---\n" + l + "---\n" : "")
				+ header + t + "] finished(" + lastLapTime + "ms laps, " + totalTime + "ms total)";
		}
		public String formatProcessTimeOver(){
			return header + "] too long process millis: " + totalTime;
		}
		public long getLastLapTime(){
			return lastLapTime;
		}
		public long getTotalTime(){
			return totalTime;
		}
		private String header;
		private long start;
		private long prev;
		private long lastLapTime;
		private long totalTime;
		private StringBuffer laps;
	}

	private ThreadLocal<MeasureContext> measureCtx = new ThreadLocal<MeasureContext>();

	private void init(){
		maxThreads = getInitParameterInt(
				"langrid.maxThreads", 10);
		maxWaitMillisForThread = getInitParameterInt(
				"langrid.maxWaitMillisForThread", 10000);
		maxWaitMillisForExternalProcess = getInitParameterInt(
				"langrid.maxWaitMillisForExternalProcess", 10000);
		maxResults = getInitParameterInt(
				"langrid.maxResults", 1000);
		eliminateDuplicates = getInitParameterBoolean(
				"langrid.eliminateDuplicates", false);
		doubleSearchResults = getInitParameterBoolean(
				"langrid.doubleSearchResults", false);
		measureProcessTime = getInitParameterBoolean(
				"langrid.measureProcessTime", false);
		processTimeWarningThreasholdMillis = getInitParameterLong(
				"langrid.processTimeWarningThreasholdMillis", 1000);
	}

	protected static final List<Language> EMPTY_LANGUAGES;
	protected static final List<LanguagePair> EMPTY_LANGUAGEPAIRS;
	protected static final List<LanguagePath> EMPTY_LANGUAGEPATHS;
	protected static final Set<MatchingMethod> MINIMUM_MATCHINGMETHODS;
	protected static final Set<MatchingMethod> ALL_MATCHINGMETHODS;

	private ServiceContext serviceContext;
	private ProcessFailedException startupException;
	private int maxThreads;
	private int maxWaitMillisForThread;
	private int maxWaitMillisForExternalProcess;
	private int maxResults;
	private boolean eliminateDuplicates;
	private boolean doubleSearchResults;
	private boolean measureProcessTime;
	private long processTimeWarningThreasholdMillis;
	private static ThreadLocal<Semaphore> currentSemaphore
		= new ThreadLocal<Semaphore>();
	private static Map<Class<? extends AbstractService>, Semaphore> semaphores
		= Collections.synchronizedMap(new HashMap<Class<? extends AbstractService>, Semaphore>());
	private static Logger logger = Logger.getLogger(AbstractService.class.getName());

	static{
		List<Language> languages = new ArrayList<Language>();
		EMPTY_LANGUAGES = Collections.unmodifiableList(languages);
		List<LanguagePair> languagePairs = new ArrayList<LanguagePair>();
		EMPTY_LANGUAGEPAIRS = Collections.unmodifiableList(languagePairs);
		List<LanguagePath> languagePaths = new ArrayList<LanguagePath>();
		EMPTY_LANGUAGEPATHS = Collections.unmodifiableList(languagePaths);
		MINIMUM_MATCHINGMETHODS = Collections.unmodifiableSet(new HashSet<MatchingMethod>(
				Arrays.asList(
						MatchingMethod.COMPLETE, MatchingMethod.PARTIAL
						, MatchingMethod.PREFIX, MatchingMethod.SUFFIX
						)
				));
		ALL_MATCHINGMETHODS = Collections.unmodifiableSet(new HashSet<MatchingMethod>(
				Arrays.asList(MatchingMethod.values())
				));
	}
}
