/**
 * NamedEntityTaggingServiceServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging;

public class NamedEntityTaggingServiceServiceLocator extends org.apache.axis.client.Service implements localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingServiceService {

    public NamedEntityTaggingServiceServiceLocator() {
    }


    public NamedEntityTaggingServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NamedEntityTaggingServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // NamedEntityTaggingのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for NamedEntityTagging)
    private java.lang.String NamedEntityTagging_address = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/NamedEntityTagging";

    public java.lang.String getNamedEntityTaggingAddress() {
        return NamedEntityTagging_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String NamedEntityTaggingWSDDServiceName = "NamedEntityTagging";

    public java.lang.String getNamedEntityTaggingWSDDServiceName() {
        return NamedEntityTaggingWSDDServiceName;
    }

    public void setNamedEntityTaggingWSDDServiceName(java.lang.String name) {
        NamedEntityTaggingWSDDServiceName = name;
    }

    public localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingService getNamedEntityTagging() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NamedEntityTagging_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNamedEntityTagging(endpoint);
    }

    public localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingService getNamedEntityTagging(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingSoapBindingStub _stub = new localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingSoapBindingStub(portAddress, this);
            _stub.setPortName(getNamedEntityTaggingWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNamedEntityTaggingEndpointAddress(java.lang.String address) {
        NamedEntityTagging_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingService.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingSoapBindingStub _stub = new localhost.jp_go_nict_langrid_webapps_mock.services.NamedEntityTagging.NamedEntityTaggingSoapBindingStub(new java.net.URL(NamedEntityTagging_address), this);
                _stub.setPortName(getNamedEntityTaggingWSDDServiceName());
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
        if ("NamedEntityTagging".equals(inputPortName)) {
            return getNamedEntityTagging();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/NamedEntityTagging", "NamedEntityTaggingServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/NamedEntityTagging", "NamedEntityTagging"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NamedEntityTagging".equals(portName)) {
            setNamedEntityTaggingEndpointAddress(address);
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
