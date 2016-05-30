/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.workflowsupport;

public class CodeAndWords {
	public CodeAndWords() {
	}
	public CodeAndWords(String code, String sourceWord, String targetWord) {
		this.code = code;
		this.sourceWord = sourceWord;
		this.targetWord = targetWord;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSourceWord() {
		return sourceWord;
	}
	public void setSourceWord(String sourceWord) {
		this.sourceWord = sourceWord;
	}
	public String getTargetWord() {
		return targetWord;
	}
	public void setTargetWord(String targetWord) {
		this.targetWord = targetWord;
	}

	private String code;
	private String sourceWord;
	private String targetWord;
}
