/*
 * $Id: ProcessAnalyzer.java 429 2011-12-20 07:12:19Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.bpel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import jp.go.nict.langrid.bpel.entity.BPEL;
import jp.go.nict.langrid.bpel.entity.BPELVersion;
import jp.go.nict.langrid.bpel.entity.EndpointReference;
import jp.go.nict.langrid.bpel.entity.PartnerLink;
import jp.go.nict.langrid.bpel.entity.PartnerLinkType;
import jp.go.nict.langrid.bpel.entity.Port;
import jp.go.nict.langrid.bpel.entity.ProcessInfo;
import jp.go.nict.langrid.bpel.entity.Role;
import jp.go.nict.langrid.bpel.entity.Service;
import jp.go.nict.langrid.bpel.entity.WSDL;
import jp.go.nict.langrid.commons.dom.DocumentUtil;
import jp.go.nict.langrid.commons.dom.NamespaceContextImpl;
import jp.go.nict.langrid.commons.dom.NodeUtil;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.ws.Constants;
import jp.go.nict.langrid.dao.util.BPELServiceInstanceReader;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 429 $
 */
public class ProcessAnalyzer {
	/**
	 * 
	 * 
	 */
	public static ProcessInfo analyze(
		BPELServiceInstanceReader reader)
		throws IOException, MalformedURLException, SAXException
		, ProcessAnalysisException, URISyntaxException
	{
		ProcessInfo pi = new ProcessInfo();

		byte[] bpelBody = null;
		InputStream is = reader.getBpel();
		try{
			bpelBody = StreamUtil.readAsBytes(reader.getBpel());
		} finally{
			is.close();
		}
	
		int n = reader.getWsdlCount();
		byte[][] wsdlBodies = new byte[n][];
		for(int i = 0; i < n; i++){
			InputStream w = reader.getWsdl(i);
			try{
				wsdlBodies[i] = StreamUtil.readAsBytes(w);
			} finally{
				w.close();
			}
		}

		BPEL bpel;
		try{
			bpel = analyzeBPEL(bpelBody);
		} catch(XPathExpressionException e){
			throw new ProcessAnalysisException(e);
		}

		HashMap<URI, WSDL> wsdls = new HashMap<URI,WSDL>();
		for(byte[] wsdlBody : wsdlBodies){
			WSDL wsdl = analyzeWsdl(wsdlBody);
			wsdls.put(wsdl.getTargetNamespace(), wsdl);
		}

		try{
			resolve(bpel, wsdls);
		} catch(ProcessAnalysisException e){
			logger.log(Level.WARNING, "BPEL Resolve error: ", e);
			StringBuilder b = new StringBuilder();
			b.append("service contains...\n");
			b.append("bpel: ");
			b.append(bpel.getFilename());
			b.append("  ");
			b.append(bpel.getTargetNamespace());
			b.append("\n");
			for(WSDL w : wsdls.values()){
				b.append("wsdl: ");
				b.append(w.getFilename());
				b.append("  ");
				b.append(w.getTargetNamespace());
			}
			logger.warning(b.toString());
			throw e;
		}

		pi.setBpel(bpel);
		pi.setWsdls(wsdls);
		for(PartnerLink pl : bpel.getPartnerLinks()){
			String role = pl.getPartnerRole();
			if(role == null) continue;
			WSDL w = wsdls.get(new URI(pl.getPartnerLinkType().getNamespaceURI()));
			if(w == null) continue;
			PartnerLinkType plt = w.getPlinks().get(pl.getPartnerLinkType().getLocalPart());
			if(plt == null) continue;
			Role r = plt.getRoles().get(role);
			if(r == null) continue;
			pi.getPartnerLinks().put(
					r.getPortTypeName().getNamespaceURI()
					, pl);
		}

		return pi;
	}

	/**
	 * 
	 * 
	 */
	public static BPEL analyzeBPEL(byte[] body)
		throws SAXException, URISyntaxException, XPathExpressionException
	{
		Document doc;
		try {
			doc = builder.parse(new ByteArrayInputStream(body));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Node root = doc.getDocumentElement();
		NamespaceContextImpl nsc = new NamespaceContextImpl();
		addNamespaceMappings(nsc, root.getAttributes());
		String rootNs = root.getNamespaceURI();
		nsc.addMapping("_", rootNs);
		XPath xpath = DocumentUtil.getDefaultXPath();
		xpath.setNamespaceContext(nsc);
		String processName = NodeUtil.getAttribute(root, "name");
		BPEL bpel = new BPEL();
		bpel.setBody(body);
		if(rootNs.equals(bpel4ws_1_1_ns)){
			bpel.setBpelVersion(BPELVersion.BPEL4WS_1_1);
		} else if(rootNs.equals(wsbpel_2_0_ns)){
			bpel.setBpelVersion(BPELVersion.WSBPEL_2_0);
		} else{
			bpel.setBpelVersion(BPELVersion.UNKNOWN);
		}
		bpel.setTargetNamespace(new URI(
				xpath.evaluate("/_:process/@targetNamespace", root)
			));
		bpel.setProcessName(processName);
		bpel.setFilename(processName + BPEL_EXTENSION);
		ArrayList<PartnerLink> links = new ArrayList<PartnerLink>();
		NodeList list = (NodeList)xpath.evaluate(
				"/_:process/_:partnerLinks/_:partnerLink"
				, root, XPathConstants.NODESET);
		for(int i = 0; i < list.getLength(); i++){
			Node node = list.item(i);
			PartnerLink pl = new PartnerLink(nsc, node);
			links.add(pl);
		}
		bpel.setPartnerLinks(links);
		return bpel;
	}

	/**
	 * 
	 * 
	 */
	public static WSDL analyzeWsdl(byte[] body)
		throws MalformedURLException, SAXException, URISyntaxException
	{
		JXPathContext context = JXPathContext.newContext(
			new DOMParser().parseXML(new ByteArrayInputStream(body))
			);
		context.registerNamespace("_", Constants.WSDL_URI);
		context.registerNamespace("wsdlsoap", Constants.WSDLSOAP_URI);
		context.registerNamespace("plnk1", Constants.PLNK_URI);
		context.registerNamespace("plnk2", Constants.PLNK_URI_2);

		WSDL wsdl = new WSDL();
		wsdl.setBody(body);
		wsdl.setTargetNamespace(new URI(
			context.getValue("/_:definitions/@targetNamespace").toString()
			));

		// 
		// 
		HashMap<String, PartnerLinkType> links
				= new HashMap<String, PartnerLinkType>();
		{	Iterator<?> pli = context.iteratePointers("/_:definitions/plnk1:partnerLinkType");
			while (pli.hasNext()) {
				JXPathContext pltc = JXPathContext.newContext(
					context, ((Pointer) pli.next()).getNode()
					);
				pltc.registerNamespace("plnk1", Constants.PLNK_URI);
				PartnerLinkType plt = new PartnerLinkType();
				plt.setName(pltc.getValue("/@name").toString());
	
				HashMap<String, Role> roles = new HashMap<String, Role>();
				Iterator<?> ri = pltc.iteratePointers("/plnk1:role");
				while (ri.hasNext()) {
					JXPathContext rc = JXPathContext.newContext(
						pltc, ((Pointer) ri.next()).getNode()
						);
					rc.registerNamespace("plnk1", Constants.PLNK_URI);
					Role role = new Role();
					role.setName(rc.getValue("/@name").toString());
					role.setPortType(toQName(
							rc, rc.getValue("/plnk1:portType/@name").toString()
							));
					roles.put(role.getName(), role);
				}
				plt.setRoles(roles);
				links.put(plt.getName(), plt);
			}
		}
		{	Iterator<?> pli = context.iteratePointers("/_:definitions/plnk2:partnerLinkType");
			while (pli.hasNext()) {
				JXPathContext pltc = JXPathContext.newContext(
					context, ((Pointer) pli.next()).getNode()
					);
				pltc.registerNamespace("plnk2", Constants.PLNK_URI_2);
				PartnerLinkType plt = new PartnerLinkType();
				plt.setName(pltc.getValue("/@name").toString());

				HashMap<String, Role> roles = new HashMap<String, Role>();
				Iterator<?> ri = pltc.iteratePointers("/plnk2:role");
				while (ri.hasNext()) {
					JXPathContext rc = JXPathContext.newContext(
						pltc, ((Pointer) ri.next()).getNode()
						);
					Role role = new Role();
					role.setName(rc.getValue("/@name").toString());
					role.setPortType(toQName(rc, rc.getValue("/@portType").toString()));
					roles.put(role.getName(), role);
				}
				plt.setRoles(roles);
				links.put(plt.getName(), plt);
			}
		}
		wsdl.setPlinks(links);
		
		{   // definitions/portTypeの解析
			HashMap<String, EndpointReference> portTypeToEndpointReference
				= new HashMap<String,EndpointReference>();
			Iterator<?> pti = context.iteratePointers("/_:definitions/_:portType");
			while (pti.hasNext()) {
				JXPathContext ptc = JXPathContext.newContext(
					context, ((Pointer) pti.next()).getNode()
					);
				portTypeToEndpointReference.put(
					ptc.getValue("/@name").toString()
					, null
					);
			}
			wsdl.setPortTypeToEndpointReference(portTypeToEndpointReference);
		}

		{   // definitions/bindingの解析
			HashMap<String, QName> bindingTypes = new HashMap<String, QName>();
			Iterator<?> bi = context.iteratePointers("/_:definitions/_:binding");
			while (bi.hasNext()) {
				JXPathContext bc = JXPathContext.newContext(
					context, ((Pointer) bi.next()).getNode()
					);
				bindingTypes.put(
					bc.getValue("/@name").toString()
					, toQName(bc, bc.getValue("/@type").toString())
					);
			}
			wsdl.setBindingTypes(bindingTypes);
		}

		String firstServiceName = null;
		{   // definitions/serviceの解析
			HashMap<String, Service> services = new HashMap<String, Service>();
			Iterator<?> si = context.iteratePointers("/_:definitions/_:service");
			while (si.hasNext()) {
				JXPathContext sc = JXPathContext.newContext(
					context, ((Pointer) si.next()).getNode()
					);
				sc.registerNamespace("_", Constants.WSDL_URI);
				String serviceName = sc.getValue("/@name").toString();

				Service s = new Service();
				s.setName(serviceName);
				if(firstServiceName == null){
					firstServiceName = serviceName;
				}

				// portの解析
				HashMap<String, Port> ports = new HashMap<String,Port>();
				Iterator<?> pi = sc.iteratePointers("/_:port");
				while (pi.hasNext()) {
					JXPathContext pc = JXPathContext.newContext(
						sc, ((Pointer) pi.next()).getNode()
						);
					pc.registerNamespace("wsdlsoap", Constants.WSDLSOAP_URI);
					Port p = new Port();
					p.setName(pc.getValue("/@name").toString());
					p.setBinding(
						toQName(pc, pc.getValue("/@binding").toString())
						);
					p.setAddress(new URL(
						pc.getValue("/wsdlsoap:address/@location").toString()
						));
					ports.put(p.getName(), p);
				}
				s.setPorts(ports);

				services.put(s.getName(), s);
			}
			wsdl.setServices(services);
		}

		if(firstServiceName == null){
			wsdl.setFilename("unknown" + WSDL_EXTENSION);
		} else{
			wsdl.setFilename(firstServiceName + WSDL_EXTENSION);
		}
		return wsdl;
	}

	/**
	 * 
	 * 
	 */
	public static void resolve(BPEL bpel, Map<URI, WSDL> wsdls)
		throws ProcessAnalysisException, URISyntaxException
	{
		// EndpointReferenceの解決
		for(WSDL w : wsdls.values()){
			for(Service s : w.getServices().values()){
				for(Port p : s.getPorts().values()){
					EndpointReference epr = new EndpointReference();
					epr.setServiceName(new QName(
							w.getTargetNamespace().toString()
							, s.getName()
							));
					epr.setServicePortName(p.getName());
					epr.setAddress(p.getAddress());

					QName binding = p.getBinding();
					WSDL bw = wsdls.get(new URI(binding.getNamespaceURI()));
					assert bw != null;
					QName bindingType = bw.getBindingTypes().get(
						binding.getLocalPart()
						);
					WSDL btw = wsdls.get(new URI(bindingType.getNamespaceURI()));
					assert btw != null;
					Map<String, EndpointReference> map
						=  btw.getPortTypeToEndpointReference();
					if(map == null){
						map = new HashMap<String,EndpointReference>();
						btw.setPortTypeToEndpointReference(map);
					}
					map.put(p.getName(), epr);
				}
			}
		}
		Map<QName, EndpointReference> portTypeQNameToEndpointReference
				= new HashMap<QName, EndpointReference>();
		Map<QName, PartnerLinkType> qnameToPartnerLinkType
				= new HashMap<QName, PartnerLinkType>();
		Map<QName, QName> bindingNameToServiceName
				= new HashMap<QName, QName>();
		for(WSDL w : wsdls.values()){
			String tns = w.getTargetNamespace().toString();
			for(Map.Entry<String, EndpointReference> e : w.getPortTypeToEndpointReference().entrySet()){
				portTypeQNameToEndpointReference.put(
						new QName(tns, e.getKey())
						, e.getValue());
			}
			for(Map.Entry<String, PartnerLinkType> e : w.getPlinks().entrySet()){
				qnameToPartnerLinkType.put(
						new QName(tns, e.getKey())
						, e.getValue());
			}
			for(Service s : w.getServices().values()){
				for(Port p : s.getPorts().values()){
					bindingNameToServiceName.put(p.getBinding()
							, new QName(tns, s.getName()));
				}
			}
		}
		Map<QName, QName> portTypeNameToServiceName
			= new HashMap<QName, QName>();
		for(WSDL w : wsdls.values()){
			String tns = w.getTargetNamespace().toString();
			for(Map.Entry<String, QName> e : w.getBindingTypes().entrySet()){
				portTypeNameToServiceName.put(
						e.getValue(), bindingNameToServiceName.get(
								new QName(tns, e.getKey()))
								);
			}
		}

		// PartnerLink -> ServiceName
		// PartnerLink -> EndpointReference
		// の解決
		for(PartnerLink p : bpel.getPartnerLinks()){
			// PartnerLink -> wsdlのplink:partnerLinkType
			QName pltName = p.getPartnerLinkType();
			PartnerLinkType plt = qnameToPartnerLinkType.get(pltName);
			if(plt == null){
				throw new ProcessAnalysisException(
						"couldn't find partner link type for: " + pltName
						);
			}

			// myRole処理
			String myRole = p.getMyRole();
			if(myRole != null){
				Role role = plt.getRoles().get(myRole);
				if(role == null){
					throw new ProcessAnalysisException("The my role \"" + myRole
							+ "\" for partner link name \"" + plt.getName() + "\" is not found.");
				}

				QName portTypeName = role.getPortTypeName();
				QName serviceName = portTypeNameToServiceName.get(
						portTypeName);
				if(serviceName != null){
					p.setService(serviceName.getLocalPart());
				} else{
					// 存在しない場合プロセス名で代用
					p.setService(bpel.getProcessName());
				}
			}

			// partnerRole処理
			String partnerRole = p.getPartnerRole();
			if(partnerRole != null){
				Role role = plt.getRoles().get(partnerRole);
				if(role == null){
					throw new ProcessAnalysisException("The partner role \"" + partnerRole
							+ "\" for partner link name \"" + plt.getName() + "\" is not found.");
				}

				QName portTypeName = role.getPortTypeName();
				EndpointReference epr = portTypeQNameToEndpointReference.get(
						portTypeName);
				if(epr == null){
					throw new ProcessAnalysisException(
							"The EndpointReference for port type \""
							+ portTypeName + "\" is not found."
							);
				}
				p.setEndpointReference(epr);
			}
		}
	}

	private static void addNamespaceMappings(
			NamespaceContextImpl nsc, NamedNodeMap attrs)
	{
		for(int i = 0; i < attrs.getLength(); i++){
			Attr attr = (Attr)attrs.item(i);
			String[] names = attr.getName().split(":", 2);
			if(names.length == 2){
				if(names[0].equals("xmlns")){
					nsc.addMapping(names[1], attr.getValue());
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	static QName toQName(JXPathContext aContext, String aValue){
		String[] names = aValue.split(":");
		if(names.length == 2){
			String uri = aContext.getNamespaceURI(names[0]);
			return new QName(uri, names[1], names[0]);
		} else if(names.length == 1){
			return new QName(names[0]);
		} else{
			throw new IllegalArgumentException(String.format(
				"%s is not valid qualified name.", aValue));
		}
	}

	static final String BPEL_EXTENSION = ".bpel";
	static final String BPEL_ENCODING = "UTF-8";
	static final String WSDL_EXTENSION = ".wsdl";
	static final String WSDL_ENCODING = "UTF-8";
	private static DocumentBuilder builder;
	private static final String bpel4ws_1_1_ns =
		"http://schemas.xmlsoap.org/ws/2003/03/business-process/";
	private static final String wsbpel_2_0_ns =
		"http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	private static Logger logger = Logger.getLogger(ProcessAnalyzer.class.getName());
	static{
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		try {
			builder = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
}
