/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.service.composite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.servicecontainer.service.AbstractService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class AbstractCompositeService
extends AbstractService{
	public AbstractCompositeService() {
	}

	public AbstractCompositeService(Class<?> holderClass){
		this.holderClass = holderClass;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = Level.parse(logLevel).intValue();
	}

	public Iterable<Pair<Invocation, Class<?>>> invocations(){
		if(holderClass == null){
			return new ArrayList<Pair<Invocation,Class<?>>>();
		}
		Set<Pair<Invocation, Class<?>>> ret = new TreeSet<Pair<Invocation, Class<?>>>(
				new Comparator<Pair<Invocation, Class<?>>>() {
					@Override
					public int compare(Pair<Invocation, Class<?>> o1,
							Pair<Invocation, Class<?>> o2) {
						return o1.getFirst().name().compareTo(o2.getFirst().name());
					}
				});
		for(Field f : holderClass.getDeclaredFields()){
			Invocation iv = f.getAnnotation(Invocation.class);
			if(iv == null) continue;
			ret.add(new Pair<Invocation, Class<?>>(iv, f.getType()));
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	protected <T> T loadServices() throws ServiceLoadingFailedException{
		if(holderClass == null) return null;
		ComponentServiceFactory factory = getComponentServiceFactory();
		try {
			Object o = holderClass.newInstance();
			for(Field f : holderClass.getDeclaredFields()){
				Invocation iv = f.getAnnotation(Invocation.class);
				if(iv == null) continue;
				f.setAccessible(true);
				Object s = factory.getService(iv.name(), f.getType());
				if(s != null){
					f.set(o, s);
				} else{
					if(iv.required()){
						throw new ServiceLoadingFailedException(iv.name(), iv.name() + " is not binded.");
					}
				}
			}
			return (T)o;
		} catch (InstantiationException e) {
			throw new ServiceLoadingFailedException(e);
		} catch (IllegalAccessException e) {
			throw new ServiceLoadingFailedException(e);
		}
	}

	public void log(String message){
		info(message);
	}

	public void log(String format, Object... args){
		info(format, args);
	}

	public void info(String message){
		if(logLevel >= Level.INFO.intValue())
			logger.info(message);
	}

	public void info(String format, Object... args){
		if(logLevel >= Level.INFO.intValue())
			logger.info(String.format(format, args));
	}

	public void warning(String message, Throwable t){
		if(logLevel >= Level.WARNING.intValue())
			logger.log(Level.WARNING, message, t);
	}

	public void severe(String message, Throwable t){
		if(logLevel >= Level.SEVERE.intValue())
			logger.log(Level.SEVERE, message, t);
	}

	private int logLevel = Level.WARNING.intValue();
	private Class<?> holderClass;
	private static Logger logger = Logger.getLogger(AbstractCompositeService.class.getName());
}
