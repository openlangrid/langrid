/**
 * NodeManagementSoapBindingStub.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_1_2.services.NodeManagement;

public class NodeManagementSoapBindingStub extends org.apache.axis.client.Stub implements localhost.langrid_1_2.services.NodeManagement.NodeManagement {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[13];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("clear");
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile"), jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeAlreadyExistsException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSelfNodeId");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getSelfNodeIdReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchNodes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "startIndex"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "conditions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_MatchingCondition"), jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orders"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Order"), jp.go.nict.langrid.ws_1_2.foundation.Order[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scope"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeEntrySearchResult"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "searchNodesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotInactiveException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotInactiveException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addNodeAs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ownerUserId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile"), jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeAlreadyExistsException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getNodeProfile");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getNodeProfileReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setNodeProfile");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "profile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile"), jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getNodeAttributes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributeNames"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_xsd_string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Attribute"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getNodeAttributesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setNodeAttributes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Attribute"), jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
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
        oper.setName("activateNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deactivateNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isNodeActive");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isNodeActiveReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnknownException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnknownException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceConfigurationException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceConfigurationException"), 
                      true
                     ));
        _operations[12] = oper;

    }

    public NodeManagementSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public NodeManagementSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public NodeManagementSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeAlreadyExistsException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeEntrySearchResult");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotFoundException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeNotInactiveException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotInactiveException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class;
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

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns2_NodeEntry");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntry[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeEntry");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Attribute");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Attribute[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Attribute");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_MatchingCondition");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "MatchingCondition");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_tns3_Order");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.foundation.Order[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "Order");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/langrid-1.2/services/NodeManagement", "ArrayOf_xsd_string");
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

    public void clear() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "clear"));

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

    public void addNode(java.lang.String nodeId, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile profile, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "addNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId, profile, attributes});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String getSelfNodeId() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "getSelfNodeId"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
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

    public jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult searchNodes(int startIndex, int maxCount, jp.go.nict.langrid.ws_1_2.foundation.MatchingCondition[] conditions, jp.go.nict.langrid.ws_1_2.foundation.Order[] orders, java.lang.String scope) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "searchNodes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(startIndex), new java.lang.Integer(maxCount), conditions, orders, scope});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeEntrySearchResult.class);
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

    public void deleteNode(java.lang.String nodeId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotInactiveException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "deleteNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotInactiveException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotInactiveException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void addNodeAs(java.lang.String ownerUserId, java.lang.String nodeId, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile profile, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "addNodeAs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ownerUserId, nodeId, profile, attributes});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeAlreadyExistsException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile getNodeProfile(java.lang.String nodeId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "getNodeProfile"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile.class);
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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setNodeProfile(java.lang.String nodeId, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeProfile profile) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "setNodeProfile"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId, profile});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public jp.go.nict.langrid.ws_1_2.foundation.Attribute[] getNodeAttributes(java.lang.String nodeId, java.lang.String[] attributeNames) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "getNodeAttributes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId, attributeNames});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setNodeAttributes(java.lang.String nodeId, jp.go.nict.langrid.ws_1_2.foundation.Attribute[] attributes) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "setNodeAttributes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId, attributes});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void activateNode(java.lang.String nodeId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "activateNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void deactivateNode(java.lang.String nodeId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "deactivateNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isNodeActive(java.lang.String nodeId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnknownException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException, jp.go.nict.langrid.ws_1_2.ServiceConfigurationException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://nodemanagement.foundation.langrid.nict.go.jp", "isNodeActive"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeId});

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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) {
              throw (jp.go.nict.langrid.ws_1_2.foundation.nodemanagement.NodeNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceConfigurationException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
