package jp.go.nict.langrid.custominvoke.workflowsupport;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.OperationType;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.namespace.QName;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractService;
import org.activebpel.rt.AeException;
import org.activebpel.rt.IAeConstants;
import org.activebpel.rt.IAePolicyConstants;
import org.activebpel.rt.axis.bpel.AeMessages;
import org.activebpel.rt.axis.bpel.AeService;
import org.activebpel.rt.axis.bpel.invokers.AeAxisInvokeContext;
import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.AeWSDLDefHelper;
import org.activebpel.rt.wsdl.IAeContextWSDLProvider;
import org.activebpel.rt.bpel.IAeEndpointReference;
import org.activebpel.rt.bpel.IAeFault;
import org.activebpel.rt.bpel.def.IAeBPELConstants;
import org.activebpel.rt.bpel.impl.AeEndpointReference;
import org.activebpel.rt.bpel.impl.AeFaultFactory;
import org.activebpel.rt.bpel.impl.addressing.AeAddressingHeaders;
import org.activebpel.rt.bpel.impl.addressing.IAeAddressingHeaders;
import org.activebpel.rt.bpel.impl.queue.AeInvoke;
import org.activebpel.rt.bpel.server.AeCryptoUtil;
import org.activebpel.rt.bpel.server.addressing.IAePartnerAddressing;
import org.activebpel.rt.bpel.server.deploy.IAePolicyMapper;
import org.activebpel.rt.bpel.server.engine.AeEngineFactory;
import org.activebpel.rt.bpel.server.engine.invoke.AeAddressHandlingType;
import org.activebpel.rt.util.AeUtil;
import org.activebpel.rt.util.AeXmlUtil;
import org.activebpel.rt.wsdl.def.AeBPELExtendedWSDLDef;
import org.activebpel.wsio.AeWebServiceMessageData;
import org.activebpel.wsio.IAeWebServiceEndpointReference;
import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.IAeWsAddressingConstants;
import org.activebpel.wsio.invoke.AeInvokeResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Transport;
import org.apache.axis.constants.Style;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.SOAPHeaderElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public abstract class AbstractCustomInvokeService extends AbstractService implements IAeInvokeHandler, IAePolicyConstants {

	private String _portTypeName = "";
	public AbstractCustomInvokeService() {
		;
	}
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	private static Logger _logger = Logger.getLogger(AbstractCustomInvokeService.class.getName());
	/* (non-Javadoc)
	 * @see org.activebpel.wsio.invoke.IAeInvokeHandler#handleInvoke(org.activebpel.wsio.invoke.IAeInvoke, java.lang.String)
	 */
	public IAeWebServiceResponse handleInvoke(IAeInvoke invokeRequest, String queryData) {
		AeAddressHandlingType addressHandling = AeAddressHandlingType.getByName(queryData);
		Operation operation = null;
		String portTypeNamespace = null;
		AeInvokeResponse response = new AeInvokeResponse();
		AeAxisInvokeContext invokeCtx = null;
		try {
//			Map map = invokeRequest.getInputMessageData().getMessageData();
//			for (Object o : map.keySet()) {
//				_logger.severe("+-+-+ key +-+-+" + o);
//				_logger.severe("+-+-+ value +-+-+" + map.get(o));
//			}
			IAeWebServiceEndpointReference endpointReference = ((AeInvoke)invokeRequest).getMyReference();
			Service wsdlService = getServiceObject(invokeRequest, endpointReference);
			// The service name can be null in some circumstances. Specifically, if the addressHandling type is
			// URN or URL, then we'll attempt to use the address found in the wsa:Address
			// as opposed to the soap:address in the service.
			if ( wsdlService == null && addressHandling == AeAddressHandlingType.SERVICE )
				throw new AeBusinessProcessException(AeMessages.getString("AeInvokeHandler.ERROR_0")); //$NON-NLS-1$

//			Map m = wsdlService.getPorts();
//			for (Object o : m.keySet()) {
//				_logger.severe("+++ key +++" + o);
//				_logger.severe("+++ value +++" + m.get(o));
//			}
//			_logger.severe("+++ getPortType before +++");
			PortType portType = getPortType(wsdlService, endpointReference, invokeRequest);
			operation = portType.getOperation(invokeRequest.getOperation(), null, null);
			if ( operation == null )
				throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.ERROR_MISSING_OPERATION", invokeRequest.getOperation()));
			portTypeNamespace = portType.getQName().getNamespaceURI();

			// get the implicit replyTo 
			IAeWebServiceEndpointReference replyTo = updateReplyToEndpoint(invokeRequest);

			Call call = createCall(wsdlService, operation, endpointReference, addressHandling, replyTo);

			if ( invokeRequest.isOneWay() )
				call.getOperation().setMep(OperationType.ONE_WAY);
			invokeCtx = createInvokeContext(invokeRequest, operation, response, call);
		} catch (AxisFault e) {
			_logger.severe(e.getMessage());
			setFaultOnResponse(portTypeNamespace, operation, e, response);
		} catch (Throwable t) {
			_logger.severe(t.getMessage());
			AeException.logError(t, t.getMessage());
			IAeFault fault = AeFaultFactory.getSystemErrorFault(t);
			response.setFaultData(fault.getFaultName(), null);
			response.setErrorString(t.getMessage());
			response.setErrorDetail(AeUtil.getStacktrace(t));
		}

		return invoke(invokeRequest, queryData, invokeCtx.getOperation().getOutput().getMessage().getQName());
	}

	protected abstract IAeWebServiceResponse invoke(IAeInvoke invokeRequest, String queryData, QName qName);
	/**
	 * Extract the wsa:ReplyTo <code>IAeWebServiceEndpointReference</code> from the
	 * <code>IAeInvoke</code> object and see if we need to include engine managed correlation headers.
	 * @param aInvoke
	 */
	protected IAeWebServiceEndpointReference updateReplyToEndpoint(IAeInvoke aInvoke) throws AeBusinessProcessException {
		IAeEndpointReference myRef = ((AeInvoke)aInvoke).getMyReference();
		if (myRef == null) {
			return null;
		}
		IAeEndpointReference replyTo = new AeEndpointReference();
		replyTo.setReferenceData(myRef);
		// we don't want to transmit our policy info in the replyTo
//		replyTo.getPolicies().clear();  
		return replyTo;
	}
	/**
	 * Find the matching <code>javax.wsdl.Service</code> service object.
	 * @param aInvoke
	 * @param aEndpointRef
	 */
	@SuppressWarnings("unchecked")
	protected Service getServiceObject(IAeInvoke aInvoke, IAeWebServiceEndpointReference aEndpointRef) throws AeBusinessProcessException {
		Service service = null;
//		_logger.severe("getServiceName:" + aEndpointRef.getServiceName());
		if (aEndpointRef.getServiceName() != null) {
			// they have a service name, see if we can find the wsdl for it
			AeBPELExtendedWSDLDef def = AeWSDLDefHelper.getWSDLDefinitionForService( AeEngineFactory.getCatalog(), aEndpointRef.getServiceName() );
			// if not global wsdl check the context wsdl for service
			if(def == null) {
				IAeContextWSDLProvider wsdlProvider = AeEngineFactory.getDeploymentProvider().findDeploymentPlan(aInvoke.getProcessId(), aInvoke.getProcessName());
				if (wsdlProvider != null) {
					def = AeWSDLDefHelper.getWSDLDefinitionForService(wsdlProvider, aEndpointRef.getServiceName());
				}
			}
			if (def != null) {
				service = (Service)def.getServices().get(aEndpointRef.getServiceName());
			}
			// TODO (MF) if we were given a service name but couldn't find it, we should generate a warning message.
			//       we may still be able to do the invoke but w/o the binding info we might be missing out
			//       on adding some required info like the soap action.
		} else {
			AeBPELExtendedWSDLDef def = AeWSDLDefHelper.getWSDLDefinitionForService(AeEngineFactory.getCatalog(), aInvoke.getPortType());
//			_logger.severe("============" + aInvoke.getPortType().toString());
			if(def == null) {
//				_logger.severe("=========== def == null ===============");
				IAeContextWSDLProvider wsdlProvider = AeEngineFactory.getDeploymentProvider().findDeploymentPlan(aInvoke.getProcessId(), aInvoke.getProcessName());
				if (wsdlProvider != null) {
//					_logger.severe("=========== wsdlProvider != null ===============");
//					Iterator it = wsdlProvider.getWSDLIterator();
//					while (it.hasNext()) {
//						AeResourceKey key = (AeResourceKey)it.next();
//						_logger.severe("+++ wsdlProvider location +++" + key.getLocation());
//						_logger.severe("+++ wsdlProvider typeURI +++" + key.getTypeURI());
//					}
					def = AeWSDLDefHelper.getWSDLDefinitionForService(wsdlProvider, aInvoke.getPortType());
				}
			}
//			Map m = def.getServices();
//			for (Object o : m.keySet()) {
//				_logger.severe("+++ service key +++" + o);
//				_logger.severe("+++ service value +++" + m.get(o));
//			}
			if (def != null) {
				service = (Service)def.getServices().get(aInvoke.getPortType());
			} else {
//				_logger.severe("=========== def == null2 ===============");
			}
//			
//			service = new ServiceImpl();
//			service.setQName(aInvoke.getPortType());
//			
		}
		return service;
	}

	/**
	 * Create and configure the client call object.
	 * @param aWsdlService
	 * @param aOperation
	 * @param aEndpoint
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Call createCall(Service aWsdlService, Operation aOperation,
			IAeWebServiceEndpointReference aEndpoint, AeAddressHandlingType aAddressHandlingType, IAeWebServiceEndpointReference aDefaultReplyTo)
	throws Exception  {
		String url = getEndpointUrl(aWsdlService, aEndpoint, aAddressHandlingType);
		Style requestStyle = getRequestStyle(aWsdlService, aOperation.getName(), aEndpoint);
		String soapAction = getSoapAction(aWsdlService, aOperation.getName(), aEndpoint);
		// use AeService cached client
		/**
		 * TODO: KP add a special .pdd flag to indicate that we need a whole new one-off client for particular cases
		 **/ 
		AeService service = new AeService();
		Call call = (Call)service.createCall();

		// set an empty soap service so axis doesn't bother trying to find specific one for invoke, since there won't be one
		call.setSOAPService(new SOAPService());  
		call.setTimeout(new Integer(AeEngineFactory.getEngineConfig().getWebServiceInvokeTimeout() * 1000));
		call.setTargetEndpointAddress(new URL(url));
		call.setSOAPActionURI(soapAction);
		call.setOperationName(aOperation.getName());
		call.setProperty(Call.OPERATION_STYLE_PROPERTY, requestStyle.getName());  
		// for policy assertions associated with our endpoint
		if (!aEndpoint.getPolicies().isEmpty()) {
			setupCallForPolicies(aEndpoint.getPolicies(), call);
		} else {
			setCredentialsOnCall(aEndpoint, call);
		}
		// Setup a holder for WS-Addressing info
		IAeAddressingHeaders wsaHeaders = new AeAddressingHeaders(aEndpoint.getSourceNamespace());
		wsaHeaders.setAction(soapAction);
		wsaHeaders.setTo(url);
		wsaHeaders.setReplyTo(aDefaultReplyTo);

		// Update the endpoint with WS-Addressing info
		IAePartnerAddressing pa = AeEngineFactory.getPartnerAddressing();
		IAeEndpointReference newEndpoint = pa.updateEndpointHeaders(wsaHeaders, aEndpoint);         
		// Add all wsa:ReferenceProperties to the call header, per WS-Addressing spec
		Iterator refProps = newEndpoint.getReferenceProperties().iterator();
		SOAPHeaderElement header = null;
		while (refProps.hasNext()) {
			header = new SOAPHeaderElement((Element)refProps.next());
			// special handling for credentials stored as reference properties
			// in the header
			if( header.getNamespaceURI().equals(CREDENTIALS_NAMESPACE))
				continue;
			// Axis will add duplicate actor and mustUnderstand attributes that will 
			// cause issues later if we do not remove them explicitly
			String actor = header.getAttributeNS(IAeConstants.SOAP_NAMESPACE_URI, ACTOR_ATTRIBUTE);
			if (actor != null) {
				header.removeAttribute(ACTOR_ATTRIBUTE);
				header.setActor(actor);
			}
			String mustUnderstand = header.getAttributeNS(IAeConstants.SOAP_NAMESPACE_URI, MUST_UNDERSTAND_ATTRIBUTE);
			if (mustUnderstand != null) {
				header.removeAttribute(MUST_UNDERSTAND_ATTRIBUTE);
				header.setMustUnderstand(AeUtil.toBoolean(mustUnderstand));
			}
			call.addHeader(header);
		}
		return call;
	}

	/**
	 * Extract the endpoint url string from the service.
	 * @param aWsdlService
	 * @param aEndpointRef
	 * @param aAddressHandlingType
	 * @throws AeBusinessProcessException
	 */
	@SuppressWarnings("unchecked")
	protected String getEndpointUrl(Service aWsdlService, IAeWebServiceEndpointReference aEndpointRef, AeAddressHandlingType aAddressHandlingType) throws AeBusinessProcessException {
		// get the port we are executin on the service and get the url
		String url = "";

		if (aAddressHandlingType == AeAddressHandlingType.SERVICE ) {
//			Port port = aWsdlService.getPort(aEndpointRef.getServicePort());
			Port port = aWsdlService.getPort(_portTypeName);
			if (port == null) {
				Object[] args = { aEndpointRef.getServiceName(), aEndpointRef.getServicePort() };
				throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.MissingPort", args)); //$NON-NLS-1$
			}

			List extList = port.getExtensibilityElements();
			for(int i = 0; extList != null && i < extList.size(); i++) {
				Object obj = extList.get(i);
				if ( obj instanceof UnknownExtensibilityElement ) {
					UnknownExtensibilityElement sop = (UnknownExtensibilityElement)obj;
					if ( "address".equals(sop.getElement().getLocalName()) ) {
						url = sop.getElement().getAttribute("location"); //$NON-NLS-1$
						break;
					}
				}
			}
		}
		else if ( aAddressHandlingType == AeAddressHandlingType.ADDRESS )
		{
			url = aEndpointRef.getAddress();
		}

		// in either case, send the url through the urn mapping facility to see if
		// it maps to another url.
		return AeEngineFactory.getURNResolver().getURL(url);
	}

	/**
	 * Determine the call style (RPC or Document).
	 *
	 * @param aWsdlService
	 * @param aOperationName
	 * @param aEndpointRef
	 * @throws AeBusinessProcessException
	 */
	@SuppressWarnings("unchecked")
	protected Style getRequestStyle( Service aWsdlService, String aOperationName, IAeWebServiceEndpointReference aEndpointRef) throws AeBusinessProcessException {
		if (aWsdlService == null) {
			return Style.DOCUMENT;
		}
		Style requestStyle = null;

//		Port port = aWsdlService.getPort(aEndpointRef.getServicePort());
		Port port = getPort(aWsdlService);
		Binding binding = port.getBinding();
		List extList = binding.getExtensibilityElements();
		for (int i = 0; extList != null && i < extList.size(); i++) {
			Object obj = extList.get(i);
			if (obj instanceof UnknownExtensibilityElement) {
				UnknownExtensibilityElement sop = (UnknownExtensibilityElement)obj;
				if ("binding".equals(sop.getElement().getLocalName())) {
					String style = sop.getElement().getAttribute("style");
					if (Style.DOCUMENT.getName().equals(style))
					{
						requestStyle = Style.DOCUMENT;
					}
					else if ( Style.RPC.getName().equals(style) )
					{
						requestStyle = Style.RPC;
					}
					break;
				}
			}
		}

		// find out if document or rpc and get the soap action from binding/operation
		BindingOperation bop = getBindingOperation(aWsdlService, aOperationName, aEndpointRef);
		extList = bop.getExtensibilityElements();
		for (int i = 0; extList != null && i < extList.size(); i++)
		{
			Object obj = extList.get(i);

			if ( obj instanceof UnknownExtensibilityElement )
			{
				UnknownExtensibilityElement sop = (UnknownExtensibilityElement)obj;
				if ( "operation".equals(sop.getElement().getLocalName()) ) //$NON-NLS-1$
				{
					String style = sop.getElement().getAttribute("style"); //$NON-NLS-1$
					if ( Style.DOCUMENT_STR.equals(style) )
					{
						requestStyle = Style.DOCUMENT;
					}
					else if ( Style.RPC_STR.equals(style) )
					{
						requestStyle = Style.RPC;
					}
				}
			}
		}
		return requestStyle;
	}

	/**
	 * Look for the value of a soapAction attribute. 
	 * If no action is explicitly defined in wsdl, 
	 * returns the implicit action based on the service namespace and operation name 
	 * since WS-Addressing mandates the use of a wsa:Action header
	 *  
	 * @param aWsdlService
	 * @param aOperationName
	 * @param aEndpointRef
	 * @throws AeBusinessProcessException
	 */
	@SuppressWarnings("unchecked")
	protected String getSoapAction(Service aWsdlService, String aOperationName, IAeWebServiceEndpointReference aEndpointRef) throws AeBusinessProcessException {
		String soapAction = IAeWsAddressingConstants.WSA_DEFAULT_ACTION; 
		if ( aWsdlService != null )
		{
			// get the soap action from binding/operation
			BindingOperation bop = getBindingOperation(aWsdlService, aOperationName, aEndpointRef);
			List extList = bop.getExtensibilityElements();
			for (int i = 0; extList != null && i < extList.size(); i++)
			{
				Object obj = extList.get(i);

				if ( obj instanceof UnknownExtensibilityElement )
				{
					UnknownExtensibilityElement sop = (UnknownExtensibilityElement)obj;
					if ( "operation".equals(sop.getElement().getLocalName()) ) //$NON-NLS-1$
					{
						soapAction = sop.getElement().getAttribute("soapAction"); //$NON-NLS-1$
						break;
					}
				}
			}
			// Return implicit action
			if (AeUtil.isNullOrEmpty(soapAction))
			{
				soapAction = aWsdlService.getQName().getNamespaceURI() + "/" + aOperationName; //$NON-NLS-1$
			}
		}
		return soapAction;
	}

	/**
	 * Get the <code>BindingOperation</code> from the service object.
	 * @param aWsdlService
	 * @param aOperationName
	 * @param aEndpointRef
	 * @throws AeBusinessProcessException
	 */
	protected BindingOperation getBindingOperation(Service aWsdlService, String aOperationName, IAeWebServiceEndpointReference aEndpointRef) throws AeBusinessProcessException
	{
//		Port port = aWsdlService.getPort(aEndpointRef.getServicePort());
		Port port = aWsdlService.getPort(_portTypeName);
		Binding binding = port.getBinding();
		BindingOperation operation;
		if (binding == null || (operation = binding.getBindingOperation( aOperationName, null, null)) == null)
		{
			Object[] args = { aEndpointRef.getServiceName(), aEndpointRef.getServicePort() };
			throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.MissingBinding", args)); //$NON-NLS-1$
		}
		return operation;
	}

	/**
	 * Get the <code>PortType</code> that we're invoking.
	 * @param aWsdlService
	 * @param aEndpointRef
	 * @param aInvoker
	 * @throws AeBusinessProcessException
	 */
	protected PortType getPortType(Service aWsdlService, IAeWebServiceEndpointReference aEndpointRef, IAeInvoke aInvoker) throws AeBusinessProcessException {
		PortType portType = null;
		if (aWsdlService != null) {
			// get the operation and build the body elements
//			Port port = aWsdlService.getPort(aEndpointRef.getServicePort());
//			_logger.severe("aWsdlService.getPort(ConstructSourceAndMorphemesAndCodes);");
			Port port = getPort(aWsdlService);
			if (port == null) {
//				_logger.severe("port = null.");
				Object[] args = { aEndpointRef.getServiceName().getNamespaceURI(), aEndpointRef.getServiceName().getLocalPart(), aEndpointRef.getServicePort() };
				throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.MissingPort", args)); //$NON-NLS-1$
			}
			if (port.getBinding() == null) {
				throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.MissingBinding", aEndpointRef.getServicePort())); //$NON-NLS-1$
			}
			portType = port.getBinding().getPortType();
		} else {
//			_logger.severe("aWsdlService == null");
			// TODO (RN) May want to revisit this to pass the IAeProcessDeployment instead of looking it up
			org.activebpel.rt.wsdl.IAeContextWSDLProvider wsdlProvider = AeEngineFactory.getDeploymentProvider().findDeploymentPlan(aInvoker.getProcessId(), aInvoker.getProcessName());
			if (wsdlProvider != null) {
				AeBPELExtendedWSDLDef def = AeWSDLDefHelper.getWSDLDefinitionForPortType(wsdlProvider, aInvoker.getPortType());
				if ( def != null )
					portType = def.getPortType(aInvoker.getPortType());
				else
					throw new AeBusinessProcessException(AeMessages.format("AeInvokeHandler.NoDefForPortType", aInvoker.getPortType())); //$NON-NLS-1$
			}
		}
		return portType;
	}

	/**
	 * Create the invoke context.
	 * @param aInvoke
	 * @param aOperation
	 * @param aResponse
	 * @param aCall
	 */
	protected AeAxisInvokeContext createInvokeContext(IAeInvoke aInvoke, Operation aOperation, AeInvokeResponse aResponse, Call aCall) {
		AeAxisInvokeContext ctx = new AeAxisInvokeContext();
		ctx.setInvoke(aInvoke);
		ctx.setOperation(aOperation);
		ctx.setResponse(aResponse);
		ctx.setCall(aCall);
		return ctx;
	}

	/**
	 * Sets the fault data on the response.
	 *
	 * @param aPortTypeNamespace namespace of the port type we invoked
	 * @param aOper the operation we invoked
	 * @param aAxisFault fault generated from invoke
	 * @param aResponse response object we're populating
	 */
	protected void setFaultOnResponse(String aPortTypeNamespace, Operation aOper, AxisFault aAxisFault, AeInvokeResponse aResponse) {
		AeWebServiceMessageData data = null;

		QName faultCode = aAxisFault.getFaultCode();
		Element[] details = aAxisFault.getFaultDetails();
		Element firstDetailElement = details != null && details.length > 0 ? details[0] : null;

		Fault wsdlFault = resolveToWsdlFault(aPortTypeNamespace, aOper, faultCode, firstDetailElement);

		QName faultName = null;
		if (wsdlFault != null) {
			// if we have a wsdl fault, then the faultName is the QName of the wsdl fault
			// and the data is extracted from the firstDetailElement
			data = extractMessageData(wsdlFault, firstDetailElement);
			faultName = new QName(aPortTypeNamespace, wsdlFault.getName());
		} else if ( IAeBPELConstants.BPWS_NAMESPACE_URI.equals(aAxisFault.getFaultCode().getNamespaceURI()) ||
				IAeBPELConstants.WSBPEL_2_0_NAMESPACE_URI.equals(aAxisFault.getFaultCode().getNamespaceURI())) {
			// it's a BPEL fault (probably from us)
			faultName = aAxisFault.getFaultCode();
		} else if (firstDetailElement != null && AeUtil.notNullOrEmpty(firstDetailElement.getNamespaceURI())) {
			// if we couldn't match a wsdl fault AND we have a detail element with a namespace
			// value then we'll use the element's QName as the fault name but we won't attempt
			// to extract the data since we might not have the schema for it
			faultName = AeXmlUtil.getElementType(firstDetailElement);
		} else {
			// otherwise fall back to fault code
			faultName = aAxisFault.getFaultCode();
		}

		aResponse.setFaultData(faultName, data);
		aResponse.setErrorString(aAxisFault.getFaultString());

		// we weren't able to extract a WSDL fault, convert the error details
		// into a human readable string for debugging purposes.
		if (data == null) {
			aResponse.setErrorDetail(getErrorDetail(aAxisFault));
		}
	}

	/**
	 * Attempts to resolve the AxisFault to a fault defined for the operation being invoked.
	 * We first attempt to match the faultcode to a fault name. Failing that, we'll examine
	 * the faultdetails to see if we can match the element's type to one of the operation's
	 * fault messages.
	 *
	 * @param aNamespaceUri namespace for the operation, used to identify the fault name
	 * @param aOperation wsdl operation invoked
	 * @param aFaultCode QName from the faultcode element in the fault
	 * @param aFirstDetailElement The first element within the fault details
	 */
	@SuppressWarnings("unchecked")
	protected Fault resolveToWsdlFault(String aNamespaceUri, Operation aOperation, QName aFaultCode, Element aFirstDetailElement) {
		Fault wsdlFault = null;

		Map faultsMap = aOperation.getFaults();

		// don't bother with this routine if the operation doesn't define any faults.
		if (faultsMap != null && !faultsMap.isEmpty()) {
			// step 1: determine the fault name
			// try the faultCode first since this is the cleanest place to indicate the
			// fault's name
			if (aNamespaceUri.equals(aFaultCode.getNamespaceURI())) {
				// the faultcode is in the same namespace as our port type, see if the
				// local part matches up to a fault name
				wsdlFault = aOperation.getFault(aFaultCode.getLocalPart());
			}

			// step 2: fault code didn't pan out, try comparing the first fault detail
			// element to the operation's faults
			if (wsdlFault == null && aFirstDetailElement != null) {
				wsdlFault = findMatchingWsdlFault(aFirstDetailElement, faultsMap.values());
			}
		}
		return wsdlFault;
	}

	/**
	 * Attempts to match the element to one of the declared faults for the operation
	 * invoked.
	 *
	 * WS-I Basic Profile 1.1 requires that fault parts are elements:
	 * http://www.ws-i.org/Profiles/BasicProfile-1.1-2004-08-24.html#Bindings_and_Faults
	 *
	 * The code here is a little flexible in this regard. It'll handle getting
	 * a complex type for a fault part if it has an xsi:type attribute. Otherwise,
	 * the fault detail element must be a valid schema element or we cannot
	 * reliably determine what fault we should map to.
	 *
	 *
	 * @param aFaultDetailElement The first detail element from the soap fault
	 * @param aDefinedFaultsForOperationColl Collection of faults defined for the operation invoked.
	 */
	@SuppressWarnings("unchecked")
	protected Fault findMatchingWsdlFault(Element aFaultDetailElement, Collection aDefinedFaultsForOperationColl) {
		Fault wsdlFault = null;
		boolean isType = false;

		QName type = AeXmlUtil.getElementType(aFaultDetailElement);
		if (AeUtil.isNullOrEmpty(aFaultDetailElement.getNamespaceURI())) {
			// if the element was in the empty namespace, check to see if it has an
			// xsi:type attribute. This isn't strictly allowed under ws-i basic profile
			// but is being done here since there may be a number of services that aren't
			// compliant and we should attempt to achieve interoperability where possible.
			type = AeXmlUtil.getXSIType(aFaultDetailElement);
		} else {
			// we found a schema element, set the flag to indicate such
			isType = false;
		}

		if (type != null) {
			// we found a type. walk all of the existing faults and stop at the
			// first one who has a message that matches our type.
			for (Iterator iter = aDefinedFaultsForOperationColl.iterator(); iter.hasNext();) {
				Fault possibleMatch = (Fault)iter.next();
				Message msg = possibleMatch.getMessage();
				// fault messages will only have a single part
				Map partsMap = msg.getParts();
				if ( partsMap != null && partsMap.size() == 1) {
					Part part = (Part)msg.getParts().values().iterator().next();
					// if we found a complex type, then compare its qname with the part's type='...'
					// if we found an element, then compare its qname with the part's element='...'
					if (isMatch(isType, type, part)) {
						wsdlFault = possibleMatch;
						break;
					}
				}
			}
		}
		return wsdlFault;
	}

	/**
	 * Determines if the part we're inspecting matches the type that we found in
	 * the fault details.
	 *
	 * @param aTypeFlag true if QName we found maps to a type, false if it maps to a schema element.
	 * @param aType qname of the type we're looking for
	 * @param aPart wsdl part from the fault message
	 */
	protected boolean isMatch(boolean aTypeFlag, QName aType, Part aPart)
	{
		QName partType = aTypeFlag ? aPart.getTypeName() : aPart.getElementName();
		return aType.equals(partType);
	}

	/**
	 * Converts the Element[] in Axis's faultDetail to a String for as the error
	 * detail string in our response.
	 *
	 * @param aFault
	 */
	protected String getErrorDetail(AxisFault aFault)
	{
		String errorDetail = null;
		Element[] details = aFault.getFaultDetails();
		if ( details != null )
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			for (int i = 0; i < details.length; i++)
			{
				// todo should we include the element names?
				pw.println(AeXmlUtil.getText(details[i]));
			}

			errorDetail = sw.toString();
		}
		return errorDetail;
	}

	/**
	 * Extracts the message data from an AxisFault's fault details.
	 * @param aWsdlFault
	 * @param aFirstDetailElement
	 */
	protected AeWebServiceMessageData extractMessageData(Fault aWsdlFault, Element aFirstDetailElement)
	{
		AeWebServiceMessageData data = new AeWebServiceMessageData(aWsdlFault.getMessage().getQName());
		String partName = (String)aWsdlFault.getMessage().getParts().keySet().iterator().next();
		if ( isSimpleType(aFirstDetailElement) )
		{
			data.setData(partName, AeXmlUtil.getText(aFirstDetailElement));
		}
		else
		{
			Document doc = AeXmlUtil.newDocument();
			Element details = (Element)doc.importNode(aFirstDetailElement, true);
			doc.appendChild(details);
			data.setData(partName, doc);
		}
		return data;
	}

	/**
	 * Helper method that checks to see if the passed elements data is a simple type
	 * or complex type.
	 * @param aElement The element to check the contents of.
	 */
	protected boolean isSimpleType(Element aElement)
	{
		boolean simple = false;
		// TODO Simple check for now, a complex type will have attributes and/or child elements.
		if ( AeUtil.isNullOrEmpty(aElement.getNamespaceURI()) && AeXmlUtil.getFirstSubElement(aElement) == null )
		{
			simple = true;
			if ( aElement.hasAttributes() )
			{
				NamedNodeMap attrs = aElement.getAttributes();
				for (int i = 0; i < attrs.getLength(); ++i)
				{
					String nsURI = attrs.item(i).getNamespaceURI();
					if(! IAeBPELConstants.W3C_XMLNS.equals(nsURI) &&
							! IAeBPELConstants.W3C_XML_SCHEMA_INSTANCE.equals(nsURI))
					{
						simple = false;
						break;
					}
				}
			}
		}
		return simple;
	}

	/**
	 * Extracts the credentials (if any) from the endpoint reference and sets them
	 * on the call object.
	 * @param aEndpointReference
	 * @param aCall
	 */
	protected void setCredentialsOnCall(IAeWebServiceEndpointReference aEndpointReference, 
			Call aCall)
	{
		String username = aEndpointReference.getUsername();
		if ( !AeUtil.isNullOrEmpty(username) )
		{
			aCall.setUsername(username);
		}
		String password = aEndpointReference.getPassword();
		if ( !AeUtil.isNullOrEmpty(password) )
		{
			aCall.setPassword(password);
		}
	}

	/**
	 * Get policy driven call properties.
	 * @param aPolicyList
	 */
	@SuppressWarnings("unchecked")
	protected Map getPolicyDrivenProperties( List aPolicyList ) throws AeBusinessProcessException
	{
		try {
			// Map policy assertions to call properties
			if (!AeUtil.isNullOrEmpty(aPolicyList)) 
			{
				// get the main policy mapper
				IAePolicyMapper mapper = AeEngineFactory.getPolicyMapper();
				// get Client Request properties
				return mapper.getCallProperties(aPolicyList);
			}
			else
			{
				return Collections.EMPTY_MAP;
			}
		} 
		catch (Throwable t) 
		{
			throw new AeBusinessProcessException(AeMessages.getString("AeInvokeHandler.0"),t);  //$NON-NLS-1$
		}
	}

	/**
	 * Call setup for policy assertions.
	 * @param aPolicyList
	 * @param aCall
	 */
	@SuppressWarnings("unchecked")
	protected void setupCallForPolicies( List aPolicyList, Call aCall ) throws AeException {
		try {
			// Map policy assertions to call properties
			if (!AeUtil.isNullOrEmpty(aPolicyList)) {
				Map props = getPolicyDrivenProperties(aPolicyList);
				for (Iterator it = props.keySet().iterator(); it.hasNext();) {
					String name = (String) it.next();
					if (name.equals(TAG_ASSERT_AUTH_USER))
						aCall.setUsername((String) props.get(name));
					else if (name.equals(TAG_ASSERT_AUTH_PASSWORD)) {
						String password = (String) props.get(name);
						aCall.setPassword(AeCryptoUtil.decryptString(password));
					} else if (name.equals(PARAM_TRANSPORT)) {
						Transport transport = (Transport) props.get(name);
						transport.setUrl(aCall.getTargetEndpointAddress());
						aCall.setTransport(transport);
					} else   
						aCall.setProperty(name, props.get(name));
				}
			}
		} catch (Throwable t) {
			throw new AeException(AeMessages.getString("AeInvokeHandler.0"),t);  //$NON-NLS-1$
		}
	}
	/**
	 * Creates the container for the response or null if it's a one-way
	 * @param aContext
	 */
	protected AeWebServiceMessageData createOutputMessageData(AeAxisInvokeContext aContext) {
		if (!aContext.getInvoke().isOneWay()) {
			QName outMsgQName = aContext.getOperation().getOutput().getMessage().getQName();
			return new AeWebServiceMessageData(outMsgQName); 
		}
		return null;
	}

	private static final String MUST_UNDERSTAND_ATTRIBUTE = "mustUnderstand"; //$NON-NLS-1$
	private static final String ACTOR_ATTRIBUTE = "actor"; //$NON-NLS-1$
	/** namespace we're using for identifying the credentials embedded in the endpoint properties */
	private static final String CREDENTIALS_NAMESPACE = "http://active-endpoints/endpoint-credentials"; //$NON-NLS-1$
	/**
	 * _portTypeを返す。
	 * @return _portType
	 */
	public String getPortTypeName() {
		return _portTypeName;
	}

	/**
	 * _portTypeを設定する。
	 * @param type _portType
	 */
	public void setPortTypeName(String type) {
		_portTypeName = type;
	}

	protected abstract Port getPort(Service service);
}
