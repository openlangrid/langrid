/*
 * $Id: LanguagePathNotUniquelyDecidedException.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.rpc.intf.Field;

/**
 * 
 * Exception thrown when a language path could not be singly resolved.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class LanguagePathNotUniquelyDecidedException extends InvalidParameterException {
	public LanguagePathNotUniquelyDecidedException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterNames Parameter names
	 * @param candidates Candidate lanugage paths
	 * 
	 */
	public LanguagePathNotUniquelyDecidedException(
		String[] parameterNames
		, LanguagePath[] candidates
		)
	{
		super(StringUtil.join(parameterNames, ",")
				, "language path not uniquely decided."
				);
		this.parameterNames = parameterNames;
		this.candidates = candidates;
	}

	/**
	 * 
	 * Returns an array of the parameter names making up the language path.
	 * @return Array of parameter names
	 * 
	 */
	public String[] getParameterNames(){
		return parameterNames;
	}
	
	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	/**
	 * 
	 * Gets valid language path candidates.
	 * @return Language path candidates
	 * 
	 */
	public LanguagePath[] getCandidates(){
		return candidates;
	}

	public void setCandidates(LanguagePath[] candidates) {
		this.candidates = candidates;
	}

	@Override
	public String getMessage(){
		return buildMessage();
	}

	private String buildMessage(){
		StringBuilder b = new StringBuilder();
		b.append(getParameterName());
		b.append(": ");
		b.append("language path not uniquely decided");
		if(candidates != null){
			b.append(" (");
			for(LanguagePath p : candidates){
				b.append("(");
				b.append(StringUtil.join(p.getLanguages(), ":"));
				b.append(")");
			}
			b.append(")");
		}
		return b.toString();
	}

	@Field(order=1)
	private String[] parameterNames;
	@Field(order=2)
	private LanguagePath[] candidates;

	private static final long serialVersionUID = -5213129527994155698L;
}
