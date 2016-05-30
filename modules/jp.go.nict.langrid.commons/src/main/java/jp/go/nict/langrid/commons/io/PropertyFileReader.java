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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class PropertyFileReader extends PropertyFileParser{
	/**
	 * 
	 * 
	 */
	public PropertyFileReader(CharsetDecoder decoder) {
		super(decoder);
	}

	/**
	 * 
	 * 
	 */
	public Map<String, String> read(InputStream stream)
		throws IOException
	{
		final Map<String, String> props = new HashMap<String, String>();
		parse(stream, new Listener(){
			public void propertyFound(String name, String value) {
				props.put(name, value);
			}
		});
		return props;
	}
}
