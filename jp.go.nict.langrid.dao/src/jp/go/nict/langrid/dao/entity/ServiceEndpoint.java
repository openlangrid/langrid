/*
 * $Id:Endpoint.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import jp.go.nict.langrid.commons.util.CalendarUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(ServiceEndpointPK.class)
public class ServiceEndpoint
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public ServiceEndpoint(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceEndpoint(
			String gridId, String serviceId, String protocolId
			, URL url, String authUserName, String authPassword
			, boolean enabled
			){
		this.gridId = gridId;
		this.serviceId = serviceId;
		this.protocolId = protocolId;
		this.url = url.toString();
		this.authUserName = authUserName;
		this.authPassword = authPassword;
		this.enabled = enabled;
	}

	/**
	 * 
	 * 
	 */
	public ServiceEndpoint(String gridId, String serviceId
			, String protocolId, URL url, boolean enabled){
		this.gridId = gridId;
		this.serviceId = serviceId;
		this.protocolId = protocolId;
		this.url = url.toString();
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		ReflectionToStringBuilder b = new ReflectionToStringBuilder(this);
		b.setExcludeFieldNames(new String[]{"createdDateTime", "updatedDateTime", "disabledByErrorDate"});
		b.append("createdDateTime", getCreatedDateTime() != null ?
				CalendarUtil.formatToDefault(getCreatedDateTime()) :
				null);
		b.append("updatedDateTime", getUpdatedDateTime() != null ?
				CalendarUtil.formatToDefault(getUpdatedDateTime()) :
				null);
		b.append("disabledByErrorDate", getDisabledByErrorDate() != null ?
				CalendarUtil.formatToDefault(getDisabledByErrorDate()) :
					null);
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceId(){
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getProtocolId(){
		return protocolId;
	}

	/**
	 * 
	 * 
	 */
	public void setProtocolId(String protocolId){
		this.protocolId = protocolId;
	}

	/**
	 * 
	 * 
	 */
	public URL getUrl() {
		try{
			return new URL(url);
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void setUrl(URL url) {
		this.url = url.toString();
	}

	/**
	 * 
	 * 
	 */
	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}

	/**
	 * 
	 * 
	 */
	public String getAuthUserName() {
		return authUserName;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	/**
	 * 
	 * 
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * 
	 * 
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * 
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getDisabledByErrorDate(){
		if(disabledByErrorDate == null) return null;
		else return (Calendar)disabledByErrorDate.clone();
	}

	/**
	 * 
	 * 
	 */
	public void setDisabledByErrorDate(Calendar date){
		disabledByErrorDate = date;
	}

	/**
	 * 呼び出しエラー時の例外メッセージを取得する。
	 * @return 例外メッセージ
	 */
	public String getDisableReason(){
		return disableReason;
	}

	/**
	 * 
	 * 
	 */
	public void setDisableReason(String reason){
		this.disableReason = reason;
	}

	/**
	 * 10回平均の応答時間を取得する。
	 * @return 応答時間
	 */
	public long getAveResponseMillis(){
		return aveResponseMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setAveResponseMillis(long aveResponseMillis){
		this.aveResponseMillis = aveResponseMillis;
	}

	/**
	 * 呼び出し成功回数の概算を取得する。
	 * @return 呼び出し成功回数の概算
	 */
	public long getExperience(){
		return experience;
	}

	/**
	 * 
	 * 
	 */
	public void setExperience(long experience){
		this.experience = experience;
	}

	@Id
	private String gridId;
	@Id
	private String serviceId;
	@Id
	private String protocolId;
	@Id
	@Type(type="text")
	private String url;
	private String authUserName = "";
	private String authPassword = "";
	private boolean enabled;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar disabledByErrorDate;
	@Type(type="text")
	private String disableReason;
	//@Transient
	private long aveResponseMillis = 0;
	//@Transient
	private long experience = 0;

	private static final long serialVersionUID = 4079302617418498428L;
}
