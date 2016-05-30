/*
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

import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.dao.entity.UserPK;
import jp.go.nict.langrid.dao.entity.UserRole;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.RequiredAttributeNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class UserData extends Data {
	public static final String _dataType = "UserData";
	public static final String _IDPrefix = "User_";


	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {
		public String getDataType() {
			return UserData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) throws RequiredAttributeNotFoundException {
			return new UserData(gid, dataID, lastUpdateDate, attributes);
		}
	}

	/**
	 * The constructor.
	 * @param user
	 * @throws DataConvertException
	 */
	public UserData (User user) throws DataConvertException{
		super(user.getGridId(), getDataID(user, null), null, null);
		// Set the udpated datetime.
		Calendar lastUpdateDate = user.getUpdatedDateTime() != null ? user.getUpdatedDateTime() : Data.DEFAULT_DATE;
		this.setLastUpdate(lastUpdateDate);
		this.setAttributes(ConvertUtil.encode(user));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataID
	 * @param lastUpdate
	 * @param attributes
	 * @throws RequiredAttributeNotFoundException
	 */
	public UserData(String gid, DataID dataID, Calendar lastUpdate, DataAttributes attributes) throws RequiredAttributeNotFoundException {
		super(gid, dataID, lastUpdate, attributes);
	}

	/**
	 * Get the User
	 * @return
	 * @throws ParseException
	 */
	public User getUser() throws ParseException {
		try {
			User user = new User();
			DataAttributes attr = getAttributes();
			ConvertUtil.decode(attr, user);

			//UserRole
			String value = attr.getValue("roles");
			for(String r : value.split(",")){
				if(r.startsWith("roleName=")){
					r = r.replaceAll("]", "");
					r = r.replaceAll("roleName=", "");
					user.getRoles().add(new UserRole(user.getGridId(), user.getUserId(), r));
				}
			}

			//ableToCallServices
			value = attr.getValue("ableToCallServices");
			if(value.equalsIgnoreCase("false")){
				user.setCanCallServices(false);
			}

			List<UserAttribute> a_list = getUserAttribute(attr);
			if(a_list != null){
				for(UserAttribute a:a_list)
				user.setAttribute(a);
			}

			return user;
		} catch (DataConvertException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 *
	 * @param attr
	 * @return
	 */
	private List<UserAttribute> getUserAttribute(DataAttributes attr){
		List<UserAttribute> ret_list = new ArrayList<UserAttribute>();
		String value = attr.getValue("attribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###Attribute###")){
			UserAttribute a = new UserAttribute();
			for(String str : line.split("\n")){
				//GridId
				if(str.startsWith("attribute_GridId=")){
					str = str.replaceAll("attribute_GridId=", "");
					a.setGridId(str);
				}else if(str.startsWith("attribute_Id=")){
				//UserId
					str = str.replaceAll("attribute_Id=", "");
					a.setUserId(str);
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
		return _dataType;
	}

	/**
	 * Get the DataID
	 * @return
	 */
	public static DataID getDataID(User data, UserPK pk){
		if(pk == null){
			pk = new UserPK(data.getGridId()
						  , data.getUserId());
		}

		return new DataID(_IDPrefix
			+ pk.getGridId() + "_"
			+ pk.getUserId());
	}
}
