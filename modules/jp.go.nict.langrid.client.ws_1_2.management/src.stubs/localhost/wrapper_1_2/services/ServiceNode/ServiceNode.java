/**
 * ServiceNode.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.wrapper_1_2.services.ServiceNode;

public interface ServiceNode extends java.rmi.Remote {
    public java.lang.String getEndpointUrl(java.lang.String serviceName) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public java.lang.String[] listServices() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public boolean hasService(java.lang.String serviceName) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
}
