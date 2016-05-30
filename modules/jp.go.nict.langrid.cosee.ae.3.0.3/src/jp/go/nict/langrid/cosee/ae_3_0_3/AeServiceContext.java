/*
 * $Id: AeServiceContext.java 260 2010-10-03 09:49:40Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 2 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.cosee.ae_3_0_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.ws.axis.AxisServiceContext;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 260 $
 */
public class AeServiceContext extends AxisServiceContext{
	public String getInitParameter(String param){
		prepare();
		return properties.getProperty(param);
	}

	private static void prepare(){
		if(properties == null){
			File prop = new File(
					new File(
							new File(System.getProperty("catalina.home"))
							, "bpr"
							)
					, PROPERTY_FILE);
			logger.info("load settings from " + prop);
			properties = new Properties();
			try{
				properties.load(new FileInputStream(prop));
			} catch(IOException e){
				logger.log(Level.SEVERE, PROPERTY_FILE + " not found.");
			}
		}
	}

	private static String PROPERTY_FILE = "langrid.ae.properties";
	private static Properties properties;
	private static Logger logger = Logger.getLogger(
			AeServiceContext.class.getName());
}
