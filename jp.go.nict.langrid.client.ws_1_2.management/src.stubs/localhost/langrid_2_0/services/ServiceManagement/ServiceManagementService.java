/**
 * ServiceManagementService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_2_0.services.ServiceManagement;

public interface ServiceManagementService extends javax.xml.rpc.Service {
    public java.lang.String getServiceManagementAddress();

    public localhost.langrid_2_0.services.ServiceManagement.ServiceManagement getServiceManagement() throws javax.xml.rpc.ServiceException;

    public localhost.langrid_2_0.services.ServiceManagement.ServiceManagement getServiceManagement(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
