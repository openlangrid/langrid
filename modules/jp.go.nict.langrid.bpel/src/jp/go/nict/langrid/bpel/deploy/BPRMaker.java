/*
 * $Id: BPRMaker.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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
package jp.go.nict.langrid.bpel.deploy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import jp.go.nict.langrid.bpel.entity.BPEL;
import jp.go.nict.langrid.bpel.entity.ProcessInfo;
import jp.go.nict.langrid.bpel.entity.WSDL;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class BPRMaker {
	/**
	 * 
	 * 
	 */
	public static void make(
			ProcessInfo processInfo, OutputStream outputStream)
		throws ClassNotFoundException, IOException
		, SAXException
	{
		// 
		// 
		ResourceGenerator gen = prepareGenerator();
		ByteArrayOutputStream pdd = new ByteArrayOutputStream();
		gen.generatePDD(
				pdd, processInfo.getBpel(), processInfo.getWsdls().values()
				, processInfo.getBinding()
				);
 		ByteArrayOutputStream wsdlCatalog = new ByteArrayOutputStream();
		gen.generateWsdlCatalog(wsdlCatalog, processInfo.getBpel(), processInfo.getWsdls().values());

		JarOutputStream jar = new JarOutputStream(outputStream);

		// pdd
		putEntry(jar, processInfo.getBpel().getProcessName() + ".pdd", pdd.toByteArray());

		// bpel/process.bpel
		BPEL bpelElement = processInfo.getBpel();
		putEntry(jar, "bpel/" + bpelElement.getFilename(), bpelElement.getBody());

		// WEB-INF/wsdlCatalog.xml
		putEntry(jar, "META-INF/wsdlCatalog.xml", wsdlCatalog.toByteArray());

		// wsdl/service??.wsdl
		for(WSDL wsdl : processInfo.getWsdls().values()){
			putEntry(jar, "wsdl/" + wsdl.getFilename(), wsdl.getBody());
		}

		jar.close();
	}

	/**
	 * 
	 * 
	 */
	private static void putEntry(JarOutputStream jar, String aName, byte[] data)
		throws IOException
	{
		jar.putNextEntry(new ZipEntry(aName));
		jar.write(data);
		jar.closeEntry();
	}

	/**
	 * 
	 * 
	 */
	private static ResourceGenerator prepareGenerator(){
		if(generator == null){
			generator = new ResourceGenerator();
		}
		return generator;
	}

	private static ResourceGenerator generator;
}
