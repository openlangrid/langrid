/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.translationselection;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class SelectionResult
implements Serializable {
	public SelectionResult() {
	}

	public SelectionResult(EstimationResult[] estimationResults,
			int selectedIndex) {
		this.estimationResults = estimationResults;
		this.selectedIndex = selectedIndex;
	}

	/**
	 * 
	 * 
	 */
	public EstimationResult[] getEstimationResults() {
		return estimationResults;
	}

	/**
	 * 
	 * 
	 */
	public void setEstimationResults(EstimationResult[] estimationResults) {
		this.estimationResults = estimationResults;
	}

	/**
	 * 
	 * 
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * 
	 * 
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	@Field(order=1)
	private EstimationResult [] estimationResults;
	@Field(order=2)
	private int selectedIndex;

	private static final long serialVersionUID = -6068376239100445741L;
}
