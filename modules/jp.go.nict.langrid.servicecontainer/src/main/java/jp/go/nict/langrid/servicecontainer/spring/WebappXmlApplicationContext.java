/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.spring;

import jp.go.nict.langrid.commons.ws.ServiceContext;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class WebappXmlApplicationContext extends FileSystemXmlApplicationContext{
	public WebappXmlApplicationContext(ServiceContext context, String path){
		this.context = context;
		setConfigLocation(path);
		refresh();
	}

	@Override
	protected Resource getResourceByPath(String path) {
		return new FileSystemResource(context.getRealPath(path));
	}

	private ServiceContext context;
}
