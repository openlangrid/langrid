/**
 * OverUseMonitoring.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.OverUseMonitoring;

public interface OverUseMonitoring extends java.rmi.Remote {
    public jp.go.nict.langrid.ws_1_2.foundation.overusemonitoring.OverUseLimit[] listOverUseLimits(jp.go.nict.langrid.ws_1_2.foundation.Order[] orders) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public void setOverUseLimit(java.lang.String period, java.lang.String limitType, int limitValue) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public void clearOverUseLimits() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public void deleteOverUseUseLimit(java.lang.String period, java.lang.String limitType) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
    public jp.go.nict.langrid.ws_1_2.foundation.overusemonitoring.OverUseStateSearchResult searchOverUseState(int startIndex, int maxCount, java.util.Calendar startDateTime, java.util.Calendar endDateTime, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException;
}
