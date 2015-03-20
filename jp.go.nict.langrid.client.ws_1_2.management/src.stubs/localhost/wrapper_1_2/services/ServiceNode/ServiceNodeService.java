/**
 * ServiceNodeService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.wrapper_1_2.services.ServiceNode;

public interface ServiceNodeService extends javax.xml.rpc.Service {
    public java.lang.String getServiceNodeAddress();

    public localhost.wrapper_1_2.services.ServiceNode.ServiceNode getServiceNode() throws javax.xml.rpc.ServiceException;

    public localhost.wrapper_1_2.services.ServiceNode.ServiceNode getServiceNode(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
