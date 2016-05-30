/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.io;

import java.io.File;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import jp.go.nict.langrid.commons.io.FileResolver;

/**
 * @author Takao Nakaguchi
 *
 */
public class ServletContextFileResolver implements FileResolver {
	public ServletContextFileResolver(ServletContext context){
		this.context = context;
	}

	@Override
	public File resolve(String pathElement) {
		if(pathElement.startsWith("/") || Pattern.matches("[a-zA-Z]:", pathElement)){
			return new File(pathElement);
		} else{
			return new File(context.getRealPath(pathElement));
		}
	}

	private ServletContext context;
}
