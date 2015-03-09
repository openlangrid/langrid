/**
 * MultihopTranslationWithTemporalDictionaryServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary;

public class MultihopTranslationWithTemporalDictionaryServiceLocator extends org.apache.axis.client.Service implements localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionaryService {

    public MultihopTranslationWithTemporalDictionaryServiceLocator() {
    }


    public MultihopTranslationWithTemporalDictionaryServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MultihopTranslationWithTemporalDictionaryServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // MultihopTranslationWithTemporalDictionaryのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for MultihopTranslationWithTemporalDictionary)
    private java.lang.String MultihopTranslationWithTemporalDictionary_address = "http://localhost:8080/wrapper-mock-1.2/services/MultihopTranslationWithTemporalDictionary";

    public java.lang.String getMultihopTranslationWithTemporalDictionaryAddress() {
        return MultihopTranslationWithTemporalDictionary_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String MultihopTranslationWithTemporalDictionaryWSDDServiceName = "MultihopTranslationWithTemporalDictionary";

    public java.lang.String getMultihopTranslationWithTemporalDictionaryWSDDServiceName() {
        return MultihopTranslationWithTemporalDictionaryWSDDServiceName;
    }

    public void setMultihopTranslationWithTemporalDictionaryWSDDServiceName(java.lang.String name) {
        MultihopTranslationWithTemporalDictionaryWSDDServiceName = name;
    }

    public localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionary getMultihopTranslationWithTemporalDictionary() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MultihopTranslationWithTemporalDictionary_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMultihopTranslationWithTemporalDictionary(endpoint);
    }

    public localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionary getMultihopTranslationWithTemporalDictionary(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionarySoapBindingStub _stub = new localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionarySoapBindingStub(portAddress, this);
            _stub.setPortName(getMultihopTranslationWithTemporalDictionaryWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMultihopTranslationWithTemporalDictionaryEndpointAddress(java.lang.String address) {
        MultihopTranslationWithTemporalDictionary_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionary.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionarySoapBindingStub _stub = new localhost.wrapper_mock_1_2.services.MultihopTranslationWithTemporalDictionary.MultihopTranslationWithTemporalDictionarySoapBindingStub(new java.net.URL(MultihopTranslationWithTemporalDictionary_address), this);
                _stub.setPortName(getMultihopTranslationWithTemporalDictionaryWSDDServiceName());
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
        if ("MultihopTranslationWithTemporalDictionary".equals(inputPortName)) {
            return getMultihopTranslationWithTemporalDictionary();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/MultihopTranslationWithTemporalDictionary", "MultihopTranslationWithTemporalDictionaryService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/MultihopTranslationWithTemporalDictionary", "MultihopTranslationWithTemporalDictionary"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MultihopTranslationWithTemporalDictionary".equals(portName)) {
            setMultihopTranslationWithTemporalDictionaryEndpointAddress(address);
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
