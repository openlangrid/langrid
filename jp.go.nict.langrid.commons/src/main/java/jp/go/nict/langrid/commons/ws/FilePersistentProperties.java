/*
 * $Id: FilePersistentProperties.java 1183 2014-04-10 13:59:44Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class FilePersistentProperties {
	/**
	 * 
	 * 
	 */
	public FilePersistentProperties(File propertyFile){
		this.propertyFile = propertyFile;
	}

	/**
	 * 
	 * 
	 */
	public String getProperty(String name){
		synchronized(properties){
			try{
				return prepareProperties().getProperty(name);
			} catch(IOException e){
				logger.log(Level.SEVERE, "can't load properties", e);
				return null;
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void setProperty(String name, String value){
		synchronized(properties){
			Properties props = new Properties();
			try{
				props = loadProperties(propertyFile);
			} catch(IOException e){
				logger.log(Level.SEVERE, "can't load properties", e);
				return;
			}
			props.setProperty(name, value);
			try{
				storeProperties(props, propertyFile);
			} catch(IOException e){
				logger.log(Level.SEVERE, "can't store properties", e);
				return;
			}
		}
	}

	private Properties prepareProperties()
	throws IOException
	{
		Properties props = properties.get(propertyFile);
		if(props != null) return props;
		props = loadProperties(propertyFile);
		properties.put(propertyFile, props);
		return props;
	}


	private static Properties loadProperties(File file)
	throws FileNotFoundException, IOException
	{
		Properties props = new Properties();
		if(file.exists()){
			InputStream is = new FileInputStream(file);
			props.load(is);
			is.close();
		} else{
			file.createNewFile();
		}
		return props;
	}

	private static void storeProperties(Properties props, File file)
	throws FileNotFoundException, IOException
	{
		OutputStream os = new FileOutputStream(file);
		props.store(os, "persitent properties");
		os.close();
		properties.put(file, props);
	}

	private File propertyFile;
	private static Map<File, Properties> properties
			= new ConcurrentHashMap<File, Properties>();
	private static Logger logger = Logger.getLogger(
			FilePersistentProperties.class.getName());
}
