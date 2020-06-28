/*
 * $Id: AxisUtil.java 196 2010-10-02 11:58:32Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.client.axis;

import java.util.Iterator;

import javax.xml.soap.MimeHeader;

import jp.go.nict.langrid.commons.ws.MimeHeaders;

/**
 * Apache Axis 1.x 用のユーティリティ。
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 196 $
 */
public class AxisUtil {
	public static MimeHeaders toSoapMimeHeaders(javax.xml.soap.MimeHeaders mimeHeaders) {
		MimeHeaders ret = new MimeHeaders();
		@SuppressWarnings("unchecked")
		Iterator<MimeHeader> it = mimeHeaders.getAllHeaders();
		while(it.hasNext()) {
			MimeHeader h = it.next();
			ret.addHeader(h.getName(), h.getValue());
		}
		return ret;
	}

}
