/*
 * $Id: FrontEnd.java 597 2012-12-03 05:02:41Z t-nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.frontend;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 597 $
 */
public class FrontEnd {
	public static FrontEnd getInstance(){
		return frontEnd.get();
	}

	/**
	 * 
	 * 
	 */
	public List<Preprocess> getPreprocesses(){
		return preprocesses;
	}

	/**
	 * 
	 * 
	 */
	public void setPreprocesses(List<Preprocess> preprocesses){
		this.preprocesses = preprocesses;
	}

	/**
	 * 
	 * 
	 */
	public List<Postprocess> getPostprocesses(){
		return postprocesses;
	}

	/**
	 * 
	 * 
	 */
	public void setPostprocesses(List<Postprocess> postprocesses){
		this.postprocesses = postprocesses;
	}

	/**
	 * 
	 * 
	 */
	public List<LogProcess> getLogProcesses(){
		return logProcesses;
	}

	/**
	 * 
	 * 
	 */
	public void setLogProcesses(List<LogProcess> logProcesses){
		this.logProcesses = logProcesses;
	}

	/**
	 * 
	 * 
	 */
	public void preprocess(ProcessContext context, MimeHeaders requestMimeHeaders)
	throws AccessLimitExceededException, NoAccessPermissionException, SystemErrorException	{
		for(Preprocess p : preprocesses){
			p.process(context, requestMimeHeaders);
		}
	}

	/**
	 * 
	 * 
	 */
	public void postprocess(
			ProcessContext context, int responseBytes
			)
	throws AccessLimitExceededException
	, SystemErrorException
	{
		for(Postprocess p : postprocesses){
			p.process(context, responseBytes);
		}
	}

	/**
	 * 
	 * 
	 */
	public void logProcess(
			ProcessContext context, LogInfo logInfo
			, String faultCode, String faultString
			, boolean errorInPreprocess
			)
	throws SystemErrorException{
		for(LogProcess p : logProcesses){
			p.process(context, logInfo, faultCode, faultString, errorInPreprocess);
		}
	}

	/**
	 * 
	 * 
	 */
	public static LogInfo createLogInfo(
			HttpServletRequest request, InputStream responseBody
			, long responseMillis, int responseCode, int responseBytes
			, String protocolId){
		String fromAddress = request.getRemoteAddr();
		String fromHost = request.getRemoteHost();
		if(fromAddress.equals("127.0.0.1")){
			// localhostからのリクエストではHTTPHEADER_FROMADDRESSの内容を確認する。
			String fromAddressHeader =
				request.getHeader(LangridConstants.HTTPHEADER_FROMADDRESS);
			if(fromAddressHeader != null){
				fromAddress = fromAddressHeader;
				fromHost = fromAddressHeader;
			}
		}
		int callNest = 0;
		String callNestString = request.getHeader(LangridConstants.HTTPHEADER_CALLNEST);
		if(callNestString != null && callNestString.length() > 0){
			try{
				callNest = Integer.parseInt(callNestString);
			} catch(NumberFormatException e){
				logger.warning("The value of call nest header is not a number: " + callNestString);
			}
		}
		String callTree = "";
		try{
			callTree = getCallTree(protocolId, responseBody);
		} catch(IOException e){
		}
		return new LogInfo(
				fromAddress, fromHost
				, Calendar.getInstance()
				, request.getRequestURI()
				, request.getContentLength()
				, responseMillis
				, responseCode, responseBytes
				, protocolId
				, request.getHeader("Referrer")
				, request.getHeader("User-Agent")
				, callNest, callTree,
				request.getHeader(LangridConstants.HTTPHEADER_SERVICEINVOCATION_USERPARAM)
				);
	}

	/**
	 * 
	 * 
	 */
	public static LogInfo createJavaCallLogInfo(
			MimeHeaders requestMimeHeaders
			, String serviceId, int requestBytes
			, long responseMillis, int responseCode, int responseBytes
			, String protocolId){
		int callNest = 0;
		String callNestString = MimeHeadersUtil.getJoinedAndDecodedValue(requestMimeHeaders, LangridConstants.HTTPHEADER_CALLNEST);
		if(callNestString != null && callNestString.length() > 0){
			try{
				callNest = Integer.parseInt(callNestString);
			} catch(NumberFormatException e){
				logger.warning("The value of call nest header is not a number: " + callNestString);
			}
		}
		return new LogInfo(
				"127.0.0.1", "localhost"
				, Calendar.getInstance()
				, "http:/" + serviceId, requestBytes
				, responseMillis
				, responseCode, responseBytes
				, protocolId
				, "", ""
				, callNest, "",
				"");
	}

	static String getCallTree(String protocolId, InputStream body)
	throws IOException{
		if(body.available() == 0) return "";
		if(protocolId == null || protocolId.equals(Protocols.SOAP_RPCENCODED)){
			return getSoapRpcencodedCallTree(body);
		} else if(protocolId.equals(Protocols.PROTOBUF_RPC)){
			return getProtobufRpcCallTree(body);
		} else if(protocolId.equals(Protocols.JSON_RPC)){
			return getJsonRpcCallTree(body);
		}
		return "";
	}

	private static String getSoapRpcencodedCallTree(InputStream body)
	throws IOException{
		Matcher m = soapct.matcher(StreamUtil.readAsString(body, CharsetUtil.newUTF8Decoder()));
		if(!m.find()) return "";
		if(m.groupCount() != 3) return "";
		return StringEscapeUtils.unescapeHtml(m.group(2));
	}
	
	private static String getProtobufRpcCallTree(InputStream body)
	throws IOException{
		CodedInputStream cis = CodedInputStream.newInstance(body);
		/*
static final int WIRETYPE_VARINT           = 0;
static final int WIRETYPE_FIXED64          = 1;
static final int WIRETYPE_LENGTH_DELIMITED = 2;
static final int WIRETYPE_START_GROUP      = 3;
static final int WIRETYPE_END_GROUP        = 4;
static final int WIRETYPE_FIXED32          = 5;

static final int TAG_TYPE_BITS = 3;
static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;
		*/
		int tag;
		while((tag = cis.readTag()) != 0){
			int fn = WireFormat.getTagFieldNumber(tag);
			if(fn != 1){
				cis.skipField(tag);
				continue;
			}
			int size = cis.readRawVarint32();
			cis.setSizeLimit(size);
			String namespace = null, value = null;
			int tag1 = cis.readTag();
			int fn1 = WireFormat.getTagFieldNumber(tag1);
			if(fn1 == 1){
				namespace = cis.readString();
			} else if(fn1 == 2){
				value = cis.readString();
			} else{
				cis.skipField(tag1);
			}
			int tag2 = cis.readTag();
			int fn2 = WireFormat.getTagFieldNumber(tag2);
			if(fn2 == 1){
				namespace = cis.readString();
			} else if(fn2 == 2){
				value = cis.readString();
			} else{
				cis.skipField(tag2);
			}
			while(cis.getBytesUntilLimit() > 0){
				if((tag = cis.readTag()) != 0) cis.skipField(tag);
			}
			cis.resetSizeCounter();
			if(namespace == null || value == null) continue;
			if(namespace.equals(LangridConstants.ACTOR_SERVICE_CALLTREE)){
				return value;
			}
		}
		return "";
	}

	private static String getJsonRpcCallTree(InputStream body)
	throws IOException{
		try(Scanner s = new Scanner(body, "UTF-8")){
			String tok = s.findInLine(
					"\"name\":\"calltree\",\"namespace\":\""
					+ StringEscapeUtils.escapeJavaScript(LangridConstants.ACTOR_SERVICE_CALLTREE)
					+ "\",\"value\":\"(\\[[a-zA-Z0-9_\\[\\]\":,\\.\\{\\}\\\\]+\\])\"");
			if(tok != null){
				return StringEscapeUtils.unescapeJavaScript(s.match().group(1));
			}
			return "";
		}
	}

	private static Pattern soapct = Pattern.compile("<(\\w+:)?calltree[^>]+>([^<]+)</(\\w+:)?calltree>");
	private static ThreadLocal<FrontEnd> frontEnd = new ThreadLocal<FrontEnd>(){
		protected FrontEnd initialValue() {
			BeanFactory f = new XmlBeanFactory(new ClassPathResource("/frontend.xml"));
			try{
				return (FrontEnd)f.getBean("frontend");
			} catch(NoSuchBeanDefinitionException e){
				logger.log(Level.WARNING, "failed to load FrontEnd.", e);
				throw new RuntimeException(e);
			}
		}
	};

	private List<Preprocess> preprocesses = new ArrayList<Preprocess>();
	private List<Postprocess> postprocesses = new ArrayList<Postprocess>();
	private List<LogProcess> logProcesses = new ArrayList<LogProcess>();
	private static Logger logger = Logger.getLogger(FrontEnd.class.getName());
}
