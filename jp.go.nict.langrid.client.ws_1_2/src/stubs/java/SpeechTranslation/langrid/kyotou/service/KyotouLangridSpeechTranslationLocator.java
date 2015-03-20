/**
 * KyotouLangridSpeechTranslationLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package SpeechTranslation.langrid.kyotou.service;

public class KyotouLangridSpeechTranslationLocator extends org.apache.axis.client.Service implements SpeechTranslation.langrid.kyotou.service.KyotouLangridSpeechTranslation {

    public KyotouLangridSpeechTranslationLocator() {
    }


    public KyotouLangridSpeechTranslationLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public KyotouLangridSpeechTranslationLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // SpeechTranslationのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for SpeechTranslation)
    private java.lang.String SpeechTranslation_address = "http://landev.nict.go.jp/service_manager/invoker/kyotou.langrid:SpeechTranslation";

    public java.lang.String getSpeechTranslationAddress() {
        return SpeechTranslation_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String SpeechTranslationWSDDServiceName = "SpeechTranslation";

    public java.lang.String getSpeechTranslationWSDDServiceName() {
        return SpeechTranslationWSDDServiceName;
    }

    public void setSpeechTranslationWSDDServiceName(java.lang.String name) {
        SpeechTranslationWSDDServiceName = name;
    }

    public SpeechTranslation.langrid.kyotou.service.AxisSpeechTranslationHandler getSpeechTranslation() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SpeechTranslation_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSpeechTranslation(endpoint);
    }

    public SpeechTranslation.langrid.kyotou.service.AxisSpeechTranslationHandler getSpeechTranslation(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SpeechTranslation.langrid.kyotou.service.SpeechTranslationSoapBindingStub _stub = new SpeechTranslation.langrid.kyotou.service.SpeechTranslationSoapBindingStub(portAddress, this);
            _stub.setPortName(getSpeechTranslationWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSpeechTranslationEndpointAddress(java.lang.String address) {
        SpeechTranslation_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SpeechTranslation.langrid.kyotou.service.AxisSpeechTranslationHandler.class.isAssignableFrom(serviceEndpointInterface)) {
                SpeechTranslation.langrid.kyotou.service.SpeechTranslationSoapBindingStub _stub = new SpeechTranslation.langrid.kyotou.service.SpeechTranslationSoapBindingStub(new java.net.URL(SpeechTranslation_address), this);
                _stub.setPortName(getSpeechTranslationWSDDServiceName());
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
        if ("SpeechTranslation".equals(inputPortName)) {
            return getSpeechTranslation();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("servicegrid:service:kyotou.langrid:SpeechTranslation", "kyotou.langrid:SpeechTranslation");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("servicegrid:service:kyotou.langrid:SpeechTranslation", "SpeechTranslation"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SpeechTranslation".equals(portName)) {
            setSpeechTranslationEndpointAddress(address);
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
