/*
 * $Id: BlobConverter.java 318 2010-12-03 03:10:29Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid.converter;

import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.dao.util.LobUtil;

import org.apache.axis.encoding.Base64;
import org.apache.commons.beanutils.Converter;

public class BlobConverter implements Converter {
	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		}

		try {
			if (String.class.equals(type)){
				if (value instanceof Blob){
					return StreamUtil.readAsString(
							((Blob)value).getBinaryStream()
							, decoder);
				} else {
					return value.toString();
				}
			} else if(Blob.class.isAssignableFrom(type) && String.class.isInstance(value)) {
				String str = (String)value;
				return LobUtil.createBlob(Base64.decode(str));
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "failed to convert value.", e);
			return null;
		} catch (IOException e) {
			logger.log(Level.WARNING, "failed to convert value.", e);
			return null;
		}
		return null;
	}

	private static CharsetDecoder decoder = CharsetUtil.newUTF8Decoder();
	private static Logger logger = Logger.getLogger(BlobConverter.class.getName());
}
