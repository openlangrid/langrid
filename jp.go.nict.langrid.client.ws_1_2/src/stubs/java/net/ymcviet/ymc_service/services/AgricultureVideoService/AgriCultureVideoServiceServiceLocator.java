/**
 * AgriCultureVideoServiceServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package net.ymcviet.ymc_service.services.AgricultureVideoService;

public class AgriCultureVideoServiceServiceLocator extends org.apache.axis.client.Service implements net.ymcviet.ymc_service.services.AgricultureVideoService.AgriCultureVideoServiceService {

    public AgriCultureVideoServiceServiceLocator() {
    }


    public AgriCultureVideoServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AgriCultureVideoServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // AgricultureVideoServiceのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for AgricultureVideoService)
    private java.lang.String AgricultureVideoService_address = "http://ymcviet.net/ymc-service/services/AgricultureVideoService";

    public java.lang.String getAgricultureVideoServiceAddress() {
        return AgricultureVideoService_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String AgricultureVideoServiceWSDDServiceName = "AgricultureVideoService";

    public java.lang.String getAgricultureVideoServiceWSDDServiceName() {
        return AgricultureVideoServiceWSDDServiceName;
    }

    public void setAgricultureVideoServiceWSDDServiceName(java.lang.String name) {
        AgricultureVideoServiceWSDDServiceName = name;
    }

    public net.ymcviet.ymc_service.services.AgricultureVideoService.AgriCultureVideoService getAgricultureVideoService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AgricultureVideoService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAgricultureVideoService(endpoint);
    }

    public net.ymcviet.ymc_service.services.AgricultureVideoService.AgriCultureVideoService getAgricultureVideoService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.ymcviet.ymc_service.services.AgricultureVideoService.AgricultureVideoServiceSoapBindingStub _stub = new net.ymcviet.ymc_service.services.AgricultureVideoService.AgricultureVideoServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAgricultureVideoServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAgricultureVideoServiceEndpointAddress(java.lang.String address) {
        AgricultureVideoService_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.ymcviet.ymc_service.services.AgricultureVideoService.AgriCultureVideoService.class.isAssignableFrom(serviceEndpointInterface)) {
                net.ymcviet.ymc_service.services.AgricultureVideoService.AgricultureVideoServiceSoapBindingStub _stub = new net.ymcviet.ymc_service.services.AgricultureVideoService.AgricultureVideoServiceSoapBindingStub(new java.net.URL(AgricultureVideoService_address), this);
                _stub.setPortName(getAgricultureVideoServiceWSDDServiceName());
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
        if ("AgricultureVideoService".equals(inputPortName)) {
            return getAgricultureVideoService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ymcviet.net/ymc-service/services/AgricultureVideoService", "AgriCultureVideoServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ymcviet.net/ymc-service/services/AgricultureVideoService", "AgricultureVideoService"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AgricultureVideoService".equals(portName)) {
            setAgricultureVideoServiceEndpointAddress(address);
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
