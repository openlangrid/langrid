/*
 * $Id: OverUseLimitNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class OverUseLimitNotFoundException extends DaoException {
	/**
	 * 
	 * 
	 */
	public OverUseLimitNotFoundException(
			String gridId, Period period, LimitType limitType)
	{
		super("OverUseLimit[gridId:" + gridId + ",period:" + period + ",limitType:" + limitType + "] not found.");
		this.gridId = gridId;
		this.period = period;
		this.limitType = limitType;
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
	public Period getPeriod() {
		return period;
	}
	/**
	 * 
	 * 
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}
	/**
	 * 
	 * 
	 */
	public LimitType getLimitType() {
		return limitType;
	}
	/**
	 * 
	 * 
	 */
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}

	private String gridId;
	private Period period;
	private LimitType limitType;

	private static final long serialVersionUID = -1008765667419715163L;
}
