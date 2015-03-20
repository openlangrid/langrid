/**
 * ServiceMonitorServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.ServiceMonitor;

public class ServiceMonitorServiceLocator extends org.apache.axis.client.Service implements localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorService {

    public ServiceMonitorServiceLocator() {
    }


    public ServiceMonitorServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServiceMonitorServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // ServiceMonitorのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for ServiceMonitor)
    private java.lang.String ServiceMonitor_address = "http://localhost:8080/langrid-1.2/services/ServiceMonitor";

    public java.lang.String getServiceMonitorAddress() {
        return ServiceMonitor_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String ServiceMonitorWSDDServiceName = "ServiceMonitor";

    public java.lang.String getServiceMonitorWSDDServiceName() {
        return ServiceMonitorWSDDServiceName;
    }

    public void setServiceMonitorWSDDServiceName(java.lang.String name) {
        ServiceMonitorWSDDServiceName = name;
    }

    public localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitor getServiceMonitor() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServiceMonitor_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServiceMonitor(endpoint);
    }

    public localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitor getServiceMonitor(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorSoapBindingStub _stub = new localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorSoapBindingStub(portAddress, this);
            _stub.setPortName(getServiceMonitorWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServiceMonitorEndpointAddress(java.lang.String address) {
        ServiceMonitor_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitor.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorSoapBindingStub _stub = new localhost.langrid_1_2.services.ServiceMonitor.ServiceMonitorSoapBindingStub(new java.net.URL(ServiceMonitor_address), this);
                _stub.setPortName(getServiceMonitorWSDDServiceName());
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
        if ("ServiceMonitor".equals(inputPortName)) {
            return getServiceMonitor();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/ServiceMonitor", "ServiceMonitorService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/ServiceMonitor", "ServiceMonitor"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServiceMonitor".equals(portName)) {
            setServiceMonitorEndpointAddress(address);
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
