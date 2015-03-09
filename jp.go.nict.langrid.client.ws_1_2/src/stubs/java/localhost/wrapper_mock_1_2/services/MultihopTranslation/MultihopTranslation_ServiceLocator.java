/**
 * MultihopTranslation_ServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.wrapper_mock_1_2.services.MultihopTranslation;

public class MultihopTranslation_ServiceLocator extends org.apache.axis.client.Service implements localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslation_Service {

    public MultihopTranslation_ServiceLocator() {
    }


    public MultihopTranslation_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MultihopTranslation_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // MultihopTranslationのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for MultihopTranslation)
    private java.lang.String MultihopTranslation_address = "http://localhost:8080/langrid-1.2/invoker/MultihopTranslation";

    public java.lang.String getMultihopTranslationAddress() {
        return MultihopTranslation_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String MultihopTranslationWSDDServiceName = "MultihopTranslation";

    public java.lang.String getMultihopTranslationWSDDServiceName() {
        return MultihopTranslationWSDDServiceName;
    }

    public void setMultihopTranslationWSDDServiceName(java.lang.String name) {
        MultihopTranslationWSDDServiceName = name;
    }

    public localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslation_PortType getMultihopTranslation() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MultihopTranslation_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMultihopTranslation(endpoint);
    }

    public localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslation_PortType getMultihopTranslation(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslationSoapBindingStub _stub = new localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslationSoapBindingStub(portAddress, this);
            _stub.setPortName(getMultihopTranslationWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMultihopTranslationEndpointAddress(java.lang.String address) {
        MultihopTranslation_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslation_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslationSoapBindingStub _stub = new localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslationSoapBindingStub(new java.net.URL(MultihopTranslation_address), this);
                _stub.setPortName(getMultihopTranslationWSDDServiceName());
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
        if ("MultihopTranslation".equals(inputPortName)) {
            return getMultihopTranslation();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/MultihopTranslation", "MultihopTranslation");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/MultihopTranslation", "MultihopTranslation"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MultihopTranslation".equals(portName)) {
            setMultihopTranslationEndpointAddress(address);
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
