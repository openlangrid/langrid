/*
 * $Id:PlatformUtil.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate.platform;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ResourceFinder;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class PlatformUtil {
	/**
	 * 
	 * 
	 */
	public static URL getHibernateResource(Class<?> loaderClass){
		String config = System.getProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				);
		if(config != null){
			File f = new File(config);
			if(f.exists()){
				try{
					return f.toURI().toURL();
				} catch(MalformedURLException e){
					logger.log(Level.WARNING, "failed to load \"" + config + "\" because it contains" +
							" invalid character for url."
							);
				}
			}
		} else{
			config = "hibernate.cfg.xml";
		}
		return ResourceFinder.find(loaderClass, config);
	}

	/**
	 * 
	 * 
	 */
	public static SQLExpressions getSQLFunctions(){
		return postgreSQLExpressions;
/*
		if(jp.go.nict.langrid.util.PlatformUtil.isMacOSX() || jp.go.nict.langrid.util.PlatformUtil.isWindows()){
			return derbyExpressions;
		} else{
			return postgreSQLExpressions;
		}
 */
	}

//	private static SQLExpressions derbyExpressions = new DerbyExpressions();
	private static SQLExpressions postgreSQLExpressions = new PostgreSQLExpressions();
	private static Logger logger = Logger.getLogger(PlatformUtil.class.getName());
}
