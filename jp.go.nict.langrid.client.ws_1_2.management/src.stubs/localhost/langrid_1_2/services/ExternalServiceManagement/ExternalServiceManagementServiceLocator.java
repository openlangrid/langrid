/**
 * ExternalServiceManagementServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.ExternalServiceManagement;

public class ExternalServiceManagementServiceLocator extends org.apache.axis.client.Service implements localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagementService {

    public ExternalServiceManagementServiceLocator() {
    }


    public ExternalServiceManagementServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ExternalServiceManagementServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // ExternalServiceManagementのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for ExternalServiceManagement)
    private java.lang.String ExternalServiceManagement_address = "http://localhost:8080/langrid-1.2/services/ExternalServiceManagement";

    public java.lang.String getExternalServiceManagementAddress() {
        return ExternalServiceManagement_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String ExternalServiceManagementWSDDServiceName = "ExternalServiceManagement";

    public java.lang.String getExternalServiceManagementWSDDServiceName() {
        return ExternalServiceManagementWSDDServiceName;
    }

    public void setExternalServiceManagementWSDDServiceName(java.lang.String name) {
        ExternalServiceManagementWSDDServiceName = name;
    }

    public localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagement getExternalServiceManagement() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ExternalServiceManagement_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getExternalServiceManagement(endpoint);
    }

    public localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagement getExternalServiceManagement(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagementSoapBindingStub _stub = new localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagementSoapBindingStub(portAddress, this);
            _stub.setPortName(getExternalServiceManagementWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setExternalServiceManagementEndpointAddress(java.lang.String address) {
        ExternalServiceManagement_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagement.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagementSoapBindingStub _stub = new localhost.langrid_1_2.services.ExternalServiceManagement.ExternalServiceManagementSoapBindingStub(new java.net.URL(ExternalServiceManagement_address), this);
                _stub.setPortName(getExternalServiceManagementWSDDServiceName());
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
        if ("ExternalServiceManagement".equals(inputPortName)) {
            return getExternalServiceManagement();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/ExternalServiceManagement", "ExternalServiceManagementService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/ExternalServiceManagement", "ExternalServiceManagement"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ExternalServiceManagement".equals(portName)) {
            setExternalServiceManagementEndpointAddress(address);
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
