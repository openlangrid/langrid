/*
 * $Id: ResourceGenerator.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import jp.go.nict.langrid.bpel.entity.BPEL;
import jp.go.nict.langrid.bpel.entity.MyRoleBinding;
import jp.go.nict.langrid.bpel.entity.WSDL;
import jp.go.nict.langrid.commons.io.StreamUtil;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 184 $
 */
class ResourceGenerator {
	/**
	 * 
	 * 
	 */
	public ResourceGenerator(){
		engine = new SimpleTemplateEngine();
	}

	/**
	 * 
	 * 
	 */
	public void generatePDD(OutputStream out, BPEL bpel, Iterable<WSDL> wsdls
			, MyRoleBinding binding)
		throws ClassNotFoundException, IOException
	{
		Template template = preparePDDTemplate();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bpel", bpel);
		map.put("wsdls", wsdls);
		map.put("binding", binding.name());
		Writer writer = StreamUtil.createUTF8Writer(out);
		template.make(map).writeTo(writer);
		writer.flush();
	}

	/**
	 * 
	 * 
	 */
	public void generateWsdlCatalog(OutputStream out, BPEL bpel, Iterable<WSDL> wsdls)
		throws ClassNotFoundException, IOException
	{
		Template template = prepareWsdlCatalogTemplate();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bpel", bpel);
		map.put("wsdls", wsdls);
		Writer writer = StreamUtil.createUTF8Writer(out);
		template.make(map).writeTo(writer);
		writer.flush();
	}

	/**
	 * 
	 * 
	 */
	private Template preparePDDTemplate()
		throws ClassNotFoundException, IOException
	{
		if(pddTemplate != null) return pddTemplate;
		try{
			pddTemplate = createTemplate(
				getClass().getResource("/templates/pdd.template.xml")
				);
			return pddTemplate;
		} catch(CompilationFailedException e){
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 
	 */
	private Template prepareWsdlCatalogTemplate()
		throws ClassNotFoundException, IOException
	{
		if(wsdlCatalogTemplate != null) return wsdlCatalogTemplate;
		try{
			wsdlCatalogTemplate = createTemplate(
				getClass().getResource("/templates/wsdlCatalog.template.xml")
				);
			return wsdlCatalogTemplate;
		} catch(CompilationFailedException e){
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 
	 */
	private Template createTemplate(URL aResource)
		throws ClassNotFoundException, IOException, CompilationFailedException
	{
		Reader r = new InputStreamReader(
			aResource.openStream(), charset.newDecoder());
		return engine.createTemplate(r);
	}

	private TemplateEngine engine;
	private Template pddTemplate;
	private Template wsdlCatalogTemplate;
	private Charset charset = Charset.forName("UTF-8");
}
