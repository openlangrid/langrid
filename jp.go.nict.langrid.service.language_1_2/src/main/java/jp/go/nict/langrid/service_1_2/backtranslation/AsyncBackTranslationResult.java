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
package jp.go.nict.langrid.service_1_2.backtranslation;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/backtranslation/")
public class AsyncBackTranslationResult {
	/**
	 * The constructor.
	 */
	public AsyncBackTranslationResult() {
	}

	/**
	 * The constructor.
	 * @param finished true if asynchronous translation has done.
	 * @param results current results
	 */
	public AsyncBackTranslationResult(boolean finished, BackTranslationResult[] results) {
		super();
		this.finished = finished;
		this.results = results;
	}

	/**
	 * Returns finished.
	 * @return finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets the finished.
	 * @param finished finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Returns the current results. 
	 * @return
	 */
	public BackTranslationResult[] getResults() {
		return results;
	}

	/**
	 * Sets the current results.
	 * @param results current results.
	 */
	public void setResults(BackTranslationResult[] results) {
		this.results = results;
	}

	@Field(order=1)
	private boolean finished;
	@Field(order=2)
	private BackTranslationResult[] results;
}
