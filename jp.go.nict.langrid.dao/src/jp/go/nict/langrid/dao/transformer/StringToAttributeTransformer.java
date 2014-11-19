/*
 * $Id:RefreshPartners.java 8164 2008-05-21 07:06:42Z nakaguchi $
 *
 * Language Grid.
 * Copyright (C) 2005-2007 NICT Language Grid Project.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jp.go.nict.langrid.dao.transformer;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.dao.entity.Attribute;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 209 $
 */
public class StringToAttributeTransformer implements Transformer<String, Attribute>{
	public Attribute transform(String value) throws TransformationException {
		String[] values = value.split("=");
		if(
				(values[0].length() == 0)
				|| (values.length == 1)
				|| (values[1].length() == 0)
				){
			throw new TransformationException(
					"invalid attribute string: " + value);
		}
		return new Attribute(values[0], values[1]);
	}
}
