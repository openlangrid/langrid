/*
 * $Id: WorkFileUtil.java 265 2010-10-03 10:25:32Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public class WorkFileUtil {
	/**
	 * 
	 * 
	 */
	public static File getFile(String name){
		return new File(getWorkDirectory(), name);
	}

	/**
	 * 
	 * 
	 */
	public static void storeResources(Class<?> clazz, Iterable<String> resources, File directory)
			throws IOException
	{
		for(String s : resources){
			InputStream is = clazz.getResourceAsStream(s);
	
			String fname = s.replace('/', File.separatorChar);
			if(!fname.startsWith(File.separator)){
				fname = File.separator + fname;
			}
			File file = new File(directory.getAbsolutePath() + fname);
			File parent = file.getParentFile();
			if(!parent.exists() && !parent.mkdirs()){
				throw new IOException("can't make directory: " + parent);
			}
			OutputStream os = new FileOutputStream(file);

			try{
				StreamUtil.transfer(is, os);
			} finally{
				is.close();
				os.close();
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public static File getWorkDirectory(){
		try{
			ServiceContext sc = AbstractService.getCurrentServiceContext();
			File webinf = null;
			if(sc != null){
				webinf = new File(sc.getRealPath("WEB-INF"));
			} else{
				Object mc = Class.forName("org.apache.axis.MessageContext")
					.getMethod("getCurrentContext")
					.invoke(null);
				String path = (String)mc.getClass()
					.getMethod("getProperty", String.class)
					.invoke(mc, "transport.http.servletLocation");
				webinf = new File(path);
			}
			if(webinf.exists()){
				return new File(webinf, "wrapperwork");
			}
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		} catch(SecurityException e){
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(InvocationTargetException e){
			e.printStackTrace();
		} catch(NoSuchMethodException e){
			e.printStackTrace();
		} catch(NoClassDefFoundError e){
			e.printStackTrace();
		}
		return new File("wrapperwork");
	}
}
