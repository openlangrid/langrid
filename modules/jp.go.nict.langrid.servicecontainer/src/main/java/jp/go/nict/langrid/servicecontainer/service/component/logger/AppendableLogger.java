/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2015 The Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.service.component.logger;

import java.io.IOException;

import jp.go.nict.langrid.servicecontainer.service.component.Logger;

public class AppendableLogger implements Logger{
	public AppendableLogger(Appendable a) {
		this.a = a;
	}

	@Override
	public void log(String message) throws IOException {
		a.append(message);
		a.append(lineSeparator);
	}

	private Appendable a;
	private static String lineSeparator;
	static{
		String ls = System.getProperty("line.separator");
		if(ls == null) ls = "\n";
		lineSeparator = ls;
	}
}
