/**
 * ServiceManagementSoapBindingStub.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_2_0.services.ServiceManagement;

public class ServiceManagementSoapBindingStub extends org.apache.axis.client.Stub implements localhost.langrid_2_0.services.ServiceManagement.ServiceManagement {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[20];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceWsdl");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getServiceWsdlReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchServices");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "startIndex"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "conditions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_MatchingCondition"), jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orders"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Order"), jp.go.nict.langrid.ws_1_2.foundation.Order[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scope"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntrySearchResult"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "searchServicesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceInstance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getServiceInstanceReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("activateService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotActivatableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deactivateService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotDeactivatableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotInactiveException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isServiceActive");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isServiceActiveReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getExternalInvocations");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns3_Invocation"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getExternalInvocationsReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchServicesWithCompactLanguageExpression");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "startIndex"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "conditions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_MatchingCondition"), jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orders"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Order"), jp.go.nict.langrid.ws_1_2.foundation.Order[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scope"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntryWithCompactLanguageExpressionSearchResult"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "searchServicesWithCompactLanguageExpressionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchCompositeServices");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "startIndex"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "conditions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_MatchingCondition"), jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orders"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Order"), jp.go.nict.langrid.ws_1_2.foundation.Order[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scope"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "CompositeServiceEntrySearchResult"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "searchCompositeServicesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addServiceAs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ownerUserId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "instance"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceAlreadyExistsException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceProfile");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getServiceProfileReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setServiceProfile");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceAttributes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributeNames"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_xsd_string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Attribute"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getServiceAttributesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setServiceAttributes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setServiceInstance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "instance"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotInactiveException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isServiceVisible");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isServiceVisibleReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setServiceVisible");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "visible"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("clear");
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "instance"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance"), jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceAlreadyExistsException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[19] = oper;

    }

    public ServiceManagementSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ServiceManagementSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ServiceManagementSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "CompositeServiceEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "CompositeServiceEntrySearchResult");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "Invocation");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceAlreadyExistsException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntrySearchResult");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntryWithCompactLanguageExpression");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpression.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntryWithCompactLanguageExpressionSearchResult");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotActivatableException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotDeactivatableException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceNotInactiveException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Attribute");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Attribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "MatchingCondition");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Order");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Order.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "SearchResult");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.SearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.AccessLimitExceededException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.InvalidParameterException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LangridException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LangridException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePath");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LanguagePath.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePathWithType");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LanguagePathWithType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.NoAccessPermissionException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ServiceConfigurationException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ServiceNotFoundException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.UnknownException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns1_LanguagePath");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LanguagePath[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePath");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns1_LanguagePathWithType");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LanguagePathWithType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePathWithType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Attribute");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Attribute");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_MatchingCondition");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "MatchingCondition");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns2_Order");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Order[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Order");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns3_CompositeServiceEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntry[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "CompositeServiceEntry");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns3_Invocation");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "Invocation");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns3_ServiceEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntry");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_tns3_ServiceEntryWithCompactLanguageExpression");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpression[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntryWithCompactLanguageExpression");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-2.0/services/ServiceManagement", "ArrayOf_xsd_string");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // 全てのtypeマッピング情報は登録されています / [en]-(All the type mapping information is registered)
            // それらは最初に呼出される時に登録されます / [en]-(when the first call is made.)
            // typeマッピング情報は実際には登録されています / [en]-(The type mapping information is actually registered in)
            // サービスのTypeMappingRegistryに登録されています, / [en]-(the TypeMappingRegistry of the service, which)
            // その理由は登録が最初の呼び出しに必要とされるときだけであるからです. / [en]-(is the reason why registration is only needed for the first call.)
            synchronized (this) {
                if (firstCall()) {
                    // シリアライザを登録する前にエンコードスタイルをセットしなくてはなりません / [en]-(must set encoding style before registering serializers)
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Callオブジェクトの取得失敗 / [en]-(Failure trying to get the Call object)", _t);
        }
    }

    public byte[] getServiceWsdl(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "getServiceWsdl"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (byte[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult searchServices(int startIndex, int maxCount, jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[] conditions, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders, java.lang.String scope) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "searchServices"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(startIndex), new java.lang.Integer(maxCount), conditions, orders, scope});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntrySearchResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) {
              throw (jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance getServiceInstance(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "getServiceInstance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void activateService(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "activateService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void deactivateService(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "deactivateService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void deleteService(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "deleteService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isServiceActive(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "isServiceActive"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[] getExternalInvocations(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "getExternalInvocations"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[]) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.Invocation[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult searchServicesWithCompactLanguageExpression(int startIndex, int maxCount, jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[] conditions, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders, java.lang.String scope) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "searchServicesWithCompactLanguageExpression"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(startIndex), new java.lang.Integer(maxCount), conditions, orders, scope});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntryWithCompactLanguageExpressionSearchResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) {
              throw (jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult searchCompositeServices(int startIndex, int maxCount, jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[] conditions, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders, java.lang.String scope) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "searchCompositeServices"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(startIndex), new java.lang.Integer(maxCount), conditions, orders, scope});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.CompositeServiceEntrySearchResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) {
              throw (jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void addServiceAs(java.lang.String ownerUserId, java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile profile, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance instance, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "addServiceAs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ownerUserId, serviceId, profile, instance, attributes});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile getServiceProfile(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "getServiceProfile"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setServiceProfile(java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile profile) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "setServiceProfile"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, profile});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.Attribute[] getServiceAttributes(java.lang.String serviceId, java.lang.String[] attributeNames) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "getServiceAttributes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, attributeNames});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.Attribute[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.Attribute[]) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setServiceAttributes(java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "setServiceAttributes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, attributes});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setServiceInstance(java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance instance) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "setServiceInstance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, instance});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isServiceVisible(java.lang.String serviceId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "isServiceVisible"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setServiceVisible(java.lang.String serviceId, boolean visible) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "setServiceVisible"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, new java.lang.Boolean(visible)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void clear() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "clear"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void addService(java.lang.String serviceId, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceProfile profile, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceInstance instance, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicemanagement.foundation.langrid.nict.go.jp", "addService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {serviceId, profile, instance, attributes});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnknownException) {
              throw (jp.go.nict.langrid.ws_1_2.UnknownException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
