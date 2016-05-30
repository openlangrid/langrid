/*
 * $Id:ServiceProfileParser.java 3990 2007-01-15 10:00:33Z nakaguchi $
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
package jp.go.nict.langrid.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:3990 $
 */
public abstract class PropertyFileParser {
	/**
	 * 
	 * 
	 */
	public PropertyFileParser(CharsetDecoder aDecoder){
		decoder = aDecoder;
	}

	protected interface Listener{
		/**
		 * 
		 * 
		 */
		void propertyFound(String name, String value);
	}

	/**
	 * 
	 * 
	 */
	protected void parse(InputStream inputStream, Listener listener)
		throws IOException
	{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream, decoder)
				);

		String line = null;
		int linenum = -1;
		while((line = reader.readLine()) != null){
			linenum++;
			if(line.charAt(0) == '#') continue;
			int i = line.indexOf('=');
			if(i == -1){
				logger.warning(String.format(
						"illegal profile line #%d: \"%s\""
						, linenum, line
						));
				continue;
			}
			String name = line.substring(0, i).trim();
			String value = line.substring(i + 1).trim();
			listener.propertyFound(name, value);
		}
	}

	private CharsetDecoder decoder;

	private static Logger logger = Logger.getLogger(
			PropertyFileParser.class.getName()
			);
}
