/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import jp.go.nict.langrid.commons.io.StreamUtil;

public class LobUtil {
	public static Blob createBlob(byte[] source)
	throws IOException{
		try{
			return new SerialBlob(source);
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	public static Blob createBlob(InputStream source)
	throws IOException, SerialException, SQLException{
		return new SerialBlob(StreamUtil.readAsBytes(source));
	}

	public static Blob createBlob(File source)
	throws IOException, SerialException, SQLException{
		InputStream is = new FileInputStream(source);
		try{
			return new SerialBlob(StreamUtil.readAsBytes(is));
		} finally{
			is.close();
		}
	}

	public static Clob createClob(char[] source) throws SerialException, SQLException{
		return new SerialClob(source);
	}

	public static Clob createClob(String source)
	throws SerialException, SQLException, IOException{
		return new SerialClob(source.toCharArray());
	}

	public static Clob createClob(InputStreamReader source)
	throws SerialException, SQLException, IOException{
		return new SerialClob(StreamUtil.readAsString(source).toCharArray());
	}
}
