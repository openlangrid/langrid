/*
 * $Id: TrustfulJSSESocketFactory.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import jp.go.nict.langrid.commons.net.ssl.SSLUtil;

import org.apache.axis.components.net.JSSESocketFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class TrustfulJSSESocketFactory extends JSSESocketFactory{
	/**
	 * 
	 * 
	 */
	public TrustfulJSSESocketFactory(Hashtable<?, ?> attributes) {
		super(attributes);
	}

	@Override
	protected void initFactory() throws IOException {
		try{
			sslFactory = SSLUtil.createTrustfulSocketFactory("TLS");
		} catch(KeyManagementException e){
			log.fatal("failed to create socket factory", e);
		} catch(NoSuchAlgorithmException e){
			log.fatal("failed to create socket factory", e);
		}
		if(sslFactory == null){
			super.initFactory();
		}
	}
}
