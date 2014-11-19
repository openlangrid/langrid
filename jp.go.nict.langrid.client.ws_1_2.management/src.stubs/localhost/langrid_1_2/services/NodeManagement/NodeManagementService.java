/**
 * NodeManagementService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.NodeManagement;

public interface NodeManagementService extends javax.xml.rpc.Service {
    public java.lang.String getNodeManagementAddress();

    public localhost.langrid_1_2.services.NodeManagement.NodeManagement getNodeManagement() throws javax.xml.rpc.ServiceException;

    public localhost.langrid_1_2.services.NodeManagement.NodeManagement getNodeManagement(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
