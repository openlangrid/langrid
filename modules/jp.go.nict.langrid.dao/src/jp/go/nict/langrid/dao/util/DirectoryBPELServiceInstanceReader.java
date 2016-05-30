/*
 * $Id:LangridSessionFactory.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class DirectoryBPELServiceInstanceReader
implements BPELServiceInstanceReader{
	/**
	 * 
	 * 
	 */
	public DirectoryBPELServiceInstanceReader(File directory){
		for(File f : directory.listFiles()){
			if(f.getName().endsWith(".wsdl")){
				wsdls.add(f);
			} else if(f.getName().endsWith(".bpel")){
				bpel = f;
			}
		}
	}

	public InputStream getBpel() throws IOException{
		return new FileInputStream(bpel);
	}

	public int getWsdlCount(){
		return wsdls.size();
	}

	public InputStream getWsdl(int index) throws IOException{
		return new FileInputStream(wsdls.get(index));
	}

	private List<File> wsdls = new ArrayList<File>();
	private File bpel;
}
