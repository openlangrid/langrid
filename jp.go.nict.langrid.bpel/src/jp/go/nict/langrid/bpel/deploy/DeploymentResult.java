/*
 * $Id: DeploymentResult.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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
package jp.go.nict.langrid.bpel.deploy;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class DeploymentResult {
	/**
	 * 
	 * 
	 */
	public int getSummaryErrorCount() {
		return summaryErrorCount;
	}

	/**
	 * 
	 * 
	 */
	public void setSummaryErrorCount(int summaryErrorCount) {
		this.summaryErrorCount = summaryErrorCount;
	}

	/**
	 * 
	 * tSummary/@numWarningsの値
	 */
	public int getSummaryWarningCount() {
		return summaryWarningCount;
	}

	/**
	 * 
	 * 
	 */
	public void setSummaryWarningCount(int summaryWarningCount) {
		this.summaryWarningCount = summaryWarningCount;
	}

	/**
	 * 
	 * 
	 */
	public String getSummaryMessages() {
		return summaryMessages;
	}

	/**
	 * 
	 * 
	 */
	public void setSummaryMessages(String summaryMessages) {
		this.summaryMessages = summaryMessages;
	}

	/**
	 * 
	 * 
	 */
	public int getDeploymentErrorCount() {
		return deploymentErrorCount;
	}

	/**
	 * 
	 * 
	 */
	public void setDeploymentErrorCount(int deploymentErrorCount) {
		this.deploymentErrorCount = deploymentErrorCount;
	}

	/**
	 * 
	 * 
	 */
	public int getDeploymentWarningCount() {
		return deploymentWarningCount;
	}

	/**
	 * 
	 * 
	 */
	public void setDeploymentWarningCount(int deploymentWarningCount) {
		this.deploymentWarningCount = deploymentWarningCount;
	}

	/**
	 * 
	 * 
	 */
	public String getDeploymentLog() {
		return deploymentLog;
	}

	/**
	 * 
	 * 
	 */
	public void setDeploymentLog(String deploymentLog) {
		this.deploymentLog = deploymentLog;
	}

	/**
	 * 
	 * 
	 */
	public boolean isDeployed() {
		return deployed;
	}

	/**
	 * 
	 * 
	 */
	public void setDeployed(boolean deployed) {
		this.deployed = deployed;
	}

	/**
	 * 
	 * 
	 */
	public String getPddFilename() {
		return pddFilename;
	}

	/**
	 * 
	 * 
	 */
	public void setPddFilename(String pddFilename) {
		this.pddFilename = pddFilename;
	}

	/**
	 * 
	 * 
	 */
	public String getSourceString() {
		return sourceString;
	}

	/**
	 * 
	 * 
	 */
	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}

	private int summaryErrorCount;
	private int summaryWarningCount;
	private String summaryMessages;
	private int deploymentErrorCount;
	private int deploymentWarningCount;
	private String deploymentLog;
	private boolean deployed;
	private String pddFilename;
	private String sourceString;
}
