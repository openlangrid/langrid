/*
 * $Id: NodeData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.NodeAttribute;
import jp.go.nict.langrid.dao.entity.NodePK;
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
public class NodeData extends Data{
	public static final String _IDPrefix = "Node_";
	public static final String _dataType = "NodeData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return NodeData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new NodeData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param node
	 * @throws DataConvertException
	 */
	public NodeData(Node node) throws DataConvertException{
		super(node.getGridId(), getDataID(node, null), null, null);
		// set the updated datetime
		setLastUpdate(node.getUpdatedDateTime() != null ? node.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(node));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public NodeData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the Node.
	 * @return
	 * @throws ParseException
	 */
	public Node getNode() throws DataConvertException {
		Node node = new Node();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, node);

		List<NodeAttribute> a_list = getNodeAttribute(attr);
		if(a_list != null){
			for(NodeAttribute a:a_list)
			node.setAttribute(a);
		}
		return node;
	}

	private List<NodeAttribute> getNodeAttribute(DataAttributes attr){
		List<NodeAttribute> ret_list = new ArrayList<NodeAttribute>();
		String value = attr.getValue("attribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###Attribute###")){
			NodeAttribute a = new NodeAttribute();
			for(String str : line.split("\n")){
				//GridId
				if(str.startsWith("attribute_GridId=")){
					str = str.replaceAll("attribute_GridId=", "");
					a.setGridId(str);
				}else if(str.startsWith("attribute_Id=")){
				//NodeID
					str = str.replaceAll("attribute_Id=", "");
					a.setNodeId(str);
				}else if(str.startsWith("attribute_Name=")){
				//Name
					str = str.replaceAll("attribute_Name=", "");
					a.setName(str);
				}else if(str.startsWith("attribute_Value=")){
				//Value
					str = str.replaceAll("attribute_Value=", "");
					a.setValue(str);
				}else if(str.startsWith("attribute_CreateTime=")){
				//CreatedDateTime
					str = str.replaceAll("attribute_CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					a.setCreatedDateTime(calendar);
				}else if(str.startsWith("attribute_UpdateTime=")){
				//UpdatedDateTime
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
	 * Get the data type string.
	 * @return Data type string
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
	 * Get the DataID.
	 * @return
	 */
	public static DataID getDataID(Node data, NodePK pk){
		if(pk == null){
			pk = new NodePK(data.getGridId()
						  , data.getNodeId());
		}

		return new DataID(_IDPrefix
			+ pk.getGridId() + "_"
			+ pk.getNodeId());
	}
}
