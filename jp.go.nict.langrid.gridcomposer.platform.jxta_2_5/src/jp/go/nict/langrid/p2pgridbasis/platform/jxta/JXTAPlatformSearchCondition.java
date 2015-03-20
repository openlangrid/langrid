/*
 * $Id: JXTAPlatformSearchCondition.java 320 2010-12-03 03:21:12Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.platform.jxta;

import net.jxta.peer.PeerID;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Takao Nakaguchi
 * @author Masato Mori
 */
public class JXTAPlatformSearchCondition {
	/**
	 * 
	 * 
	 */
	private String name;

	/**
	 * 
	 * 
	 */
	private String value;

	/**
	 * 
	 * 
	 */
	private int num;

	/**
	 * 
	 * 
	 */
	private PeerID peerID;
	
	/**
	 * 
	 * 
	 */
	public JXTAPlatformSearchCondition(String name, String value, int num, PeerID peerID) {
		this.name = name;
		this.value = value;
		this.num = num;
		this.peerID = peerID;
	}
	
	/**
	 * 
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 
	 */
	public int getNum() {
		return num;
	}
	
	/**
	 * 
	 * 
	 */
	public PeerID getPeerID() {
		return peerID;
	}

	/**
	 * 
	 * 
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "name:" + name + "\tvalue:" + value + "\tnum:" + num + "\tpeerID:" + peerID;
	}
}
