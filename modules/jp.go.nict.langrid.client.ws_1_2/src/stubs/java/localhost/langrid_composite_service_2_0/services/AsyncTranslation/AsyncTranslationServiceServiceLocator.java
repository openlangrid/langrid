/**
 * AsyncTranslationServiceServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_composite_service_2_0.services.AsyncTranslation;

public class AsyncTranslationServiceServiceLocator extends org.apache.axis.client.Service implements localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationServiceService {

    public AsyncTranslationServiceServiceLocator() {
    }


    public AsyncTranslationServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AsyncTranslationServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // AsyncTranslationのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for AsyncTranslation)
    private java.lang.String AsyncTranslation_address = "http://localhost:8080/langrid-composite-service-2.0/services/AsyncTranslation";

    public java.lang.String getAsyncTranslationAddress() {
        return AsyncTranslation_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String AsyncTranslationWSDDServiceName = "AsyncTranslation";

    public java.lang.String getAsyncTranslationWSDDServiceName() {
        return AsyncTranslationWSDDServiceName;
    }

    public void setAsyncTranslationWSDDServiceName(java.lang.String name) {
        AsyncTranslationWSDDServiceName = name;
    }

    public localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationService getAsyncTranslation() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AsyncTranslation_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAsyncTranslation(endpoint);
    }

    public localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationService getAsyncTranslation(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationSoapBindingStub _stub = new localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationSoapBindingStub(portAddress, this);
            _stub.setPortName(getAsyncTranslationWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAsyncTranslationEndpointAddress(java.lang.String address) {
        AsyncTranslation_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationService.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationSoapBindingStub _stub = new localhost.langrid_composite_service_2_0.services.AsyncTranslation.AsyncTranslationSoapBindingStub(new java.net.URL(AsyncTranslation_address), this);
                _stub.setPortName(getAsyncTranslationWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("インターフェースに対するスタブの実装がありません: / [en]-(There is no stub implementation for the interface:)  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("AsyncTranslation".equals(inputPortName)) {
            return getAsyncTranslation();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/langrid-composite-service-2.0/services/AsyncTranslation", "AsyncTranslationServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/langrid-composite-service-2.0/services/AsyncTranslation", "AsyncTranslation"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AsyncTranslation".equals(portName)) {
            setAsyncTranslationEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" 未知のポートに対してはエンドポイントのアドレスをセットできません / [en]-(Cannot set Endpoint Address for Unknown Port)" + portName);
        }
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
