/*
 * $Id: LanguagePairNotUniquelyDecidedException.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;

/**
 * 
 * Exception thrown when a language pair could not be singly resolved.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class LanguagePairNotUniquelyDecidedException extends InvalidParameterException {
	public LanguagePairNotUniquelyDecidedException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param aParameterName1 First language parameter name
	 * @param aParameterName2 Second language parameter name
	 * @param candidates Valid language pair candidates
	 * 
	 */
	public LanguagePairNotUniquelyDecidedException(
		String aParameterName1, String aParameterName2
		, LanguagePair[] candidates
		)
	{
		super(aParameterName1 + ", " + aParameterName2
				, "language pair not uniquely decided."
				);
		this.parameterName1 = aParameterName1;
		this.parameterName2 = aParameterName2;
		setCandidates(candidates);
	}

	/**
	 * 
	 * Returns the first parameter name.
	 * @return Name of first parameter
	 * 
	 */
	public String getParameterName1(){
		return parameterName1;
	}

	public void setParameterName1(String parameterName1) {
		this.parameterName1 = parameterName1;
	}

	/**
	 * 
	 * Returns the first parameter name.
	 * @return Name of first parameter
	 * 
	 */
	public String getParameterName2(){
		return parameterName2;
	}

	public void setParameterName2(String parameterName2) {
		this.parameterName2 = parameterName2;
	}

	/**
	 * 
	 * Gets valid language pair candidates.
	 * @return Language pair candidates
	 * 
	 */
	public String[] getCandidates(){
		return candidates;
	}

	public void setCandidates(String[] candidates){
		this.candidates = candidates;
	}

	/**
	 * 
	 * Sets language pair candidates.
	 * @param candidates Language pair candidates
	 * 
	 */
	public void setCandidates(LanguagePair[] candidates){
		this.candidates = ArrayUtil.collect(
				candidates
				, String.class, new ToStringTransformer<LanguagePair>()
				);	
	}

	@Override
	public String getMessage(){
		return buildMessage();
	}

	private String buildMessage(){
		StringBuilder b = new StringBuilder();
		b.append(parameterName1);
		b.append(",");
		b.append(parameterName2);
		b.append(": ");
		b.append("language pair not uniquely decided");
		if(candidates != null){
			b.append(" (");
			for(String p : candidates){
				b.append("(");
				b.append(p);
				b.append(")");
			}
			b.append(")");
		}
		return b.toString();
	}

	@Field(order=1)
	private String parameterName1;
	@Field(order=2)
	private String parameterName2;
	@Field(order=3)
	private String[] candidates;

	private static final long serialVersionUID = -5213129577994155698L;
}
