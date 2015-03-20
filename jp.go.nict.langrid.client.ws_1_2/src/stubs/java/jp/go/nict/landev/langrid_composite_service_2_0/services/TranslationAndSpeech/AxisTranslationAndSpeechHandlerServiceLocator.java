/**
 * AxisTranslationAndSpeechHandlerServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech;

public class AxisTranslationAndSpeechHandlerServiceLocator extends org.apache.axis.client.Service implements jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.AxisTranslationAndSpeechHandlerService {

    public AxisTranslationAndSpeechHandlerServiceLocator() {
    }


    public AxisTranslationAndSpeechHandlerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AxisTranslationAndSpeechHandlerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // TranslationAndSpeechのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for TranslationAndSpeech)
    private java.lang.String TranslationAndSpeech_address = "http://landev.nict.go.jp/langrid-composite-service-2.0/services/TranslationAndSpeech";

    public java.lang.String getTranslationAndSpeechAddress() {
        return TranslationAndSpeech_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String TranslationAndSpeechWSDDServiceName = "TranslationAndSpeech";

    public java.lang.String getTranslationAndSpeechWSDDServiceName() {
        return TranslationAndSpeechWSDDServiceName;
    }

    public void setTranslationAndSpeechWSDDServiceName(java.lang.String name) {
        TranslationAndSpeechWSDDServiceName = name;
    }

    public jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.AxisTranslationAndSpeechHandler getTranslationAndSpeech() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TranslationAndSpeech_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTranslationAndSpeech(endpoint);
    }

    public jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.AxisTranslationAndSpeechHandler getTranslationAndSpeech(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.TranslationAndSpeechSoapBindingStub _stub = new jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.TranslationAndSpeechSoapBindingStub(portAddress, this);
            _stub.setPortName(getTranslationAndSpeechWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTranslationAndSpeechEndpointAddress(java.lang.String address) {
        TranslationAndSpeech_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.AxisTranslationAndSpeechHandler.class.isAssignableFrom(serviceEndpointInterface)) {
                jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.TranslationAndSpeechSoapBindingStub _stub = new jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech.TranslationAndSpeechSoapBindingStub(new java.net.URL(TranslationAndSpeech_address), this);
                _stub.setPortName(getTranslationAndSpeechWSDDServiceName());
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
        if ("TranslationAndSpeech".equals(inputPortName)) {
            return getTranslationAndSpeech();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://landev.nict.go.jp/langrid-composite-service-2.0/services/TranslationAndSpeech", "AxisTranslationAndSpeechHandlerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://landev.nict.go.jp/langrid-composite-service-2.0/services/TranslationAndSpeech", "TranslationAndSpeech"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TranslationAndSpeech".equals(portName)) {
            setTranslationAndSpeechEndpointAddress(address);
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
