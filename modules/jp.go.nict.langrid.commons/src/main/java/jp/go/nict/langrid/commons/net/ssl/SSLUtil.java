/*
 * $Id: SSLUtil.java 950 2013-09-05 04:13:51Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.net.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 950 $
 */
public class SSLUtil {
	/**
	 * 
	 * 
	 */
	public static ServerSocketFactory createServerSocketFactoryFromKeyStore(
			String sslType, InputStream is, char[] password)
		throws CertificateException, IOException, NoSuchAlgorithmException
		, KeyManagementException, KeyStoreException, UnrecoverableKeyException
	{
		SSLContext context = SSLContext.getInstance(sslType);
		KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
		store.load(is, password);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(store, password);
		context.init(kmf.getKeyManagers(), null, null);
		return context.getServerSocketFactory();
	}

	/**
	 * 
	 * 
	 */
	public static SSLSocketFactory createTrustfulSocketFactory(String sslType)
		throws NoSuchAlgorithmException, KeyManagementException
	{
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(
				X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(
				X509Certificate[] certs, String authType) {
			}
			}};
		SSLContext context = SSLContext.getInstance(sslType);
		context.init(null, trustAllCerts, null);
		return context.getSocketFactory();
	}

	public static HostnameVerifier createAnonymousVerifier(){
		return new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
	}

	public static void setupForUntrustedSSLCert() throws KeyManagementException, NoSuchAlgorithmException{
		HttpsURLConnection.setDefaultSSLSocketFactory(SSLUtil.createTrustfulSocketFactory("SSL"));
		HttpsURLConnection.setDefaultHostnameVerifier(SSLUtil.createAnonymousVerifier());
	}
}
