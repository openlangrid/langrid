/**
 * AgriCultureImageServiceServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package net.ymcviet.ymc_service.services.AgricultureImageService;

public class AgriCultureImageServiceServiceLocator extends org.apache.axis.client.Service implements net.ymcviet.ymc_service.services.AgricultureImageService.AgriCultureImageServiceService {

    public AgriCultureImageServiceServiceLocator() {
    }


    public AgriCultureImageServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AgriCultureImageServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // AgricultureImageServiceのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for AgricultureImageService)
    private java.lang.String AgricultureImageService_address = "http://ymcviet.net/ymc-service/services/AgricultureImageService";

    public java.lang.String getAgricultureImageServiceAddress() {
        return AgricultureImageService_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String AgricultureImageServiceWSDDServiceName = "AgricultureImageService";

    public java.lang.String getAgricultureImageServiceWSDDServiceName() {
        return AgricultureImageServiceWSDDServiceName;
    }

    public void setAgricultureImageServiceWSDDServiceName(java.lang.String name) {
        AgricultureImageServiceWSDDServiceName = name;
    }

    public net.ymcviet.ymc_service.services.AgricultureImageService.AgriCultureImageService getAgricultureImageService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AgricultureImageService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAgricultureImageService(endpoint);
    }

    public net.ymcviet.ymc_service.services.AgricultureImageService.AgriCultureImageService getAgricultureImageService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.ymcviet.ymc_service.services.AgricultureImageService.AgricultureImageServiceSoapBindingStub _stub = new net.ymcviet.ymc_service.services.AgricultureImageService.AgricultureImageServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAgricultureImageServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAgricultureImageServiceEndpointAddress(java.lang.String address) {
        AgricultureImageService_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.ymcviet.ymc_service.services.AgricultureImageService.AgriCultureImageService.class.isAssignableFrom(serviceEndpointInterface)) {
                net.ymcviet.ymc_service.services.AgricultureImageService.AgricultureImageServiceSoapBindingStub _stub = new net.ymcviet.ymc_service.services.AgricultureImageService.AgricultureImageServiceSoapBindingStub(new java.net.URL(AgricultureImageService_address), this);
                _stub.setPortName(getAgricultureImageServiceWSDDServiceName());
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
        if ("AgricultureImageService".equals(inputPortName)) {
            return getAgricultureImageService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ymcviet.net/ymc-service/services/AgricultureImageService", "AgriCultureImageServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ymcviet.net/ymc-service/services/AgricultureImageService", "AgricultureImageService"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AgricultureImageService".equals(portName)) {
            setAgricultureImageServiceEndpointAddress(address);
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
