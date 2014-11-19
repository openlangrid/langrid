/*
 * $Id: LanguageNotUniquelyDecidedException.java 491 2012-05-24 02:50:11Z t-nakaguchi $
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


/**
 * 
 * Exception thrown when a language candidate could not be singly resolved.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 491 $
 */
public class LanguageNotUniquelyDecidedException extends InvalidParameterException{
	public LanguageNotUniquelyDecidedException() {
	}

	/**
	 * 
	 * Constructor.
	 * @param parameterName Name of the parameter that took the language expression
	 * @param candidates List of candidate languages
	 * 
	 */
	public LanguageNotUniquelyDecidedException(
		String parameterName, String[] candidates
		)
	{
		super(parameterName, "language not uniquely decided");
		this.candidates = candidates;
	}

	/**
	 * 
	 * Returns valid language candidates.
	 * @return Language candidates
	 * 
	 */
	public String[] getCandidates(){
		return candidates;
	}

	public void setCandidates(String[] candidates) {
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
		b.append("language not uniquely decided");
		if(candidates != null){
			b.append(" (");
			for(String l : candidates){
				b.append(" ");
				b.append(l);
			}
			b.append(")");
		}
		return b.toString();
	}

	private String[] candidates;

	private static final long serialVersionUID = 8919868768224036704L;
}
