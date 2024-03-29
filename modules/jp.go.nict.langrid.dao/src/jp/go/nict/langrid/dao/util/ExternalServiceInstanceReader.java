/*
 * $Id:LangridSessionFactory.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 209 $
 */
public class ExternalServiceInstanceReader {
	/**
	 * 
	 * 
	 */
	public ExternalServiceInstanceReader(InputStream instance){
		this.instance = instance;
	}

	/**
	 * 
	 * 
	 */
	public InputStream getWsdl()
		throws IOException
	{
		if(wsdl == null){
			parse();
		}
		if(wsdl == null){
			throw new IOException("wsdl file (*.wsdl) not found.");
		}
		return new ByteArrayInputStream(wsdl);
	}

	private void parse()
		throws IOException
	{
		JarInputStream is = new JarInputStream(instance);
		while(true){
			JarEntry entry = is.getNextJarEntry();
			if(entry == null) break;
			if(wsdl != null){
				throw new IOException("too many wsdl file.");
			}
			if(entry.getName().endsWith(".wsdl")){
				wsdl = StreamUtil.readAsBytes(is);
			}
		}
	}

	private byte[] wsdl;
	private InputStream instance;
}
