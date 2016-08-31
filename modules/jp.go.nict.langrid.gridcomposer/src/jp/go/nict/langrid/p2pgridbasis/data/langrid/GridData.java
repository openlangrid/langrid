/*
 * $Id: GridData.java 401 2011-08-25 01:11:16Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.GridAttribute;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class GridData extends Data{
	public static final String _IDPrefix = "Grid_";
	public static final String _dataType = "GridData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return GridData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new GridData(null, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param grid
	 * @throws DataConvertException
	 */
	public GridData(Grid grid) throws DataConvertException{
		super(null, getDataID(grid), null, null);
		// Set the updated datetime.
		setLastUpdate(grid.getUpdatedDateTime() != null ? grid.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(grid));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate last updated date
	 * @param attributes attribute
	 */
	public GridData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(null, id, lastUpdate, attributes);
	}

	/**
	 * Get the Grid.
	 * @return
	 * @throws ParseException
	 */
	public Grid getGrid() throws DataConvertException {
		Grid grid = new Grid();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, grid);

		List<GridAttribute> a_list = getGridAttribute(attr);
		if(a_list != null){
			for(GridAttribute a:a_list)
				grid.setAttribute(a);
		}

		return grid;
	}

	private List<GridAttribute> getGridAttribute(DataAttributes attr){
		List<GridAttribute> ret_list = new ArrayList<GridAttribute>();
		String value = attr.getValue("attribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###Attribute###")){
			GridAttribute a = new GridAttribute();
			for(String str : line.split("\n")){
				//GridId
				if(str.startsWith("attribute_GridId=")){
					str = str.replaceAll("attribute_GridId=", "");
					a.setGridId(str);
				}else if(str.startsWith("attribute_Name=")){
				//Name
					str = str.replaceAll("attribute_Name=", "");
					a.setName(str);
				}else if(str.startsWith("attribute_Value=")){
				//Value
					str = str.replaceAll("attribute_Value=", "");
					a.setValue(str);
				}else if(str.startsWith("attribute_CreateTime=")){
				//createdDateTime
					str = str.replaceAll("attribute_CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					a.setCreatedDateTime(calendar);
				}else if(str.startsWith("attribute_UpdateTime=")){
				//updatedDateTime
					str = str.replaceAll("attribute_UpdateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					a.setUpdatedDateTime(calendar);
				}
			}
			ret_list.add(a);
		}
		return ret_list;
	}

	/**
	 * Get the data type.
	 * @return data type string
	 */
	static public String getDataType() {
		return _dataType;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.data.Data#getType()
	 */
	public String getType() {
		return getDataType();
	}

	/**
	 * Get the DataID
	 * @return
	 */
	public static DataID getDataID(Grid data){
		return new DataID(_IDPrefix
			+ data.getGridId());
	}

	/**
	 * Get the DataID
	 * @return
	 */
	public static DataID getDataID(String gridId){
		return new DataID(_IDPrefix
			+ gridId);
	}
}
