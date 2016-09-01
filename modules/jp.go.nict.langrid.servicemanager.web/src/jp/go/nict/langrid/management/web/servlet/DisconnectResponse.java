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
package jp.go.nict.langrid.management.web.servlet;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class DisconnectResponse {
	public DisconnectResponse() {
	}

	public DisconnectResponse(boolean disconnected){
		this.isDisconnect = disconnected;
	}

	/**
	 * 
	 * 
	 */
	public boolean isDisconnect() {
		return isDisconnect;
	}
	
	public void setDisconnect(boolean isDisconnect) {
		this.isDisconnect = isDisconnect;
	}
	
	private boolean isDisconnect;
}
