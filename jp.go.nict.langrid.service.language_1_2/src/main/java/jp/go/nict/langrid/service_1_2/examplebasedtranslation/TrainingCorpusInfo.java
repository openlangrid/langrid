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
package jp.go.nict.langrid.service_1_2.examplebasedtranslation;

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class TrainingCorpusInfo
implements Serializable{
	/**
	 * The constructor.
	 */
	public TrainingCorpusInfo() {
	}

	/**
	 * 
	 * 
	 */
	public TrainingCorpusInfo(String corpusId, String sourceLang,
			String targetLang, int count, float progress, Calendar lastUpdate,
			String status) {
		this.corpusId = corpusId;
		this.sourceLang = sourceLang;
		this.targetLang = targetLang;
		this.count = count;
		this.progress = progress;
		this.lastUpdate = lastUpdate;
		this.status = status;
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
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * Returns the corpus id.
	 * @return the corpus id
	 */
	public String getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(String corpusId) {
		this.corpusId = corpusId;
	}

	public String getSourceLang() {
		return sourceLang;
	}

	public void setSourceLang(String sourceLang) {
		this.sourceLang = sourceLang;
	}

	public String getTargetLang() {
		return targetLang;
	}

	public void setTargetLang(String targetLang) {
		this.targetLang = targetLang;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Field(order=1)
	private String corpusId;
	@Field(order=2)
	private String sourceLang;
	@Field(order=3)
	private String targetLang;
	@Field(order=4)
	private int count;
	@Field(order=5)
	private float progress;
	@Field(order=6)
	private Calendar lastUpdate;
	@Field(order=7)
	private String status;

	private static final long serialVersionUID = -2179327831368113899L;
}

