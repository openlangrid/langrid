/**
 * KeyphraseExtractServiceServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract;

public class KeyphraseExtractServiceServiceLocator extends org.apache.axis.client.Service implements localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractServiceService {

    public KeyphraseExtractServiceServiceLocator() {
    }


    public KeyphraseExtractServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public KeyphraseExtractServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // KeyphraseExtractのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for KeyphraseExtract)
    private java.lang.String KeyphraseExtract_address = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/KeyphraseExtract";

    public java.lang.String getKeyphraseExtractAddress() {
        return KeyphraseExtract_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String KeyphraseExtractWSDDServiceName = "KeyphraseExtract";

    public java.lang.String getKeyphraseExtractWSDDServiceName() {
        return KeyphraseExtractWSDDServiceName;
    }

    public void setKeyphraseExtractWSDDServiceName(java.lang.String name) {
        KeyphraseExtractWSDDServiceName = name;
    }

    public localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractService getKeyphraseExtract() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(KeyphraseExtract_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getKeyphraseExtract(endpoint);
    }

    public localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractService getKeyphraseExtract(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractSoapBindingStub _stub = new localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractSoapBindingStub(portAddress, this);
            _stub.setPortName(getKeyphraseExtractWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setKeyphraseExtractEndpointAddress(java.lang.String address) {
        KeyphraseExtract_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractService.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractSoapBindingStub _stub = new localhost.jp_go_nict_langrid_webapps_mock.services.KeyphraseExtract.KeyphraseExtractSoapBindingStub(new java.net.URL(KeyphraseExtract_address), this);
                _stub.setPortName(getKeyphraseExtractWSDDServiceName());
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
        if ("KeyphraseExtract".equals(inputPortName)) {
            return getKeyphraseExtract();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/KeyphraseExtract", "KeyphraseExtractServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/KeyphraseExtract", "KeyphraseExtract"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("KeyphraseExtract".equals(portName)) {
            setKeyphraseExtractEndpointAddress(address);
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
