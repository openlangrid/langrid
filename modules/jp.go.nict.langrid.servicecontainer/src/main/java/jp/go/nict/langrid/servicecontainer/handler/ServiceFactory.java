/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler;

import java.util.Set;

import jp.go.nict.langrid.commons.ws.ServiceContext;

/**
 * ServiceFactory creates service instance and call afterLoad
 * method of that.
 * @author Takao Nakaguchi
 *
 */
public interface ServiceFactory {
	void init(ServiceContext serviceContext, String serviceName);
	Set<Class<?>> getInterfaces();
	Object getMockService();
	Object getService();

	<T> T createService(
			ClassLoader classLoader, ServiceContext context
			, Class<T> interfaceClass);

	/**
	 * Called by ServiceFactory to notify that loading process completed.
	 * ServiceFactory must call afterLoad method of service if its instance
	 * is already created.
	 */
	void afterLoad();
}
