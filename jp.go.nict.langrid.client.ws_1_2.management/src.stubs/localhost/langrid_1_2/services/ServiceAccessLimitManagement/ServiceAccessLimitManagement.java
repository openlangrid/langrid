/**
 * ServiceAccessLimitManagement.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.ServiceAccessLimitManagement;

public interface ServiceAccessLimitManagement extends java.rmi.Remote {
    public void clear() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public void setAccessLimit(java.lang.String userId, java.lang.String serviceId, java.lang.String period, java.lang.String limitType, int limitCount) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public void deleteAccessLimit(java.lang.String userId, java.lang.String serviceId, java.lang.String period, java.lang.String limitType) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.foundation.serviceaccesslimitmanagement.AccessLimitNotFoundException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public jp.go.nict.langrid.ws_1_2.foundation.serviceaccesslimitmanagement.AccessLimitSearchResult searchAccessLimits(int startIndex, int maxCount, java.lang.String userId, java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public jp.go.nict.langrid.ws_1_2.foundation.serviceaccesslimitmanagement.AccessLimit[] getMyAccessLimits(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
}
