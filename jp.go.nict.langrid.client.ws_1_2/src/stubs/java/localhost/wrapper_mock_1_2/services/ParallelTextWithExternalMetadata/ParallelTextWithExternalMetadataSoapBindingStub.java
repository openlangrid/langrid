/**
 * ParallelTextWithExternalMetadataSoapBindingStub.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.wrapper_mock_1_2.services.ParallelTextWithExternalMetadata;

public class ParallelTextWithExternalMetadataSoapBindingStub extends org.apache.axis.client.Stub implements localhost.wrapper_mock_1_2.services.ParallelTextWithExternalMetadata.ParallelTextWithExternalMetadata {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchParallelTextsFromCandidates");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sourceLang"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "targetLang"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "text"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "matchingMethod"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "candidateIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "ArrayOf_xsd_string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "ArrayOf_tns1_ParallelTextWithId"));
        oper.setReturnClass(jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "searchParallelTextsFromCandidatesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotFoundException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedLanguagePairException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.InvalidParameterException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "InvalidParameterException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePairNotUniquelyDecidedException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoValidEndpointsException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoValidEndpointsException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServiceNotActiveException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotActiveException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.AccessLimitExceededException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "AccessLimitExceededException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ServerBusyException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServerBusyException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.NoAccessPermissionException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "fault"),
                      "jp.go.nict.langrid.ws_1_2.ProcessFailedException",
                      new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ProcessFailedException"), 
                      true
                     ));
        _operations[0] = oper;

    }

    public ParallelTextWithExternalMetadataSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ParallelTextWithExternalMetadataSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ParallelTextWithExternalMetadataSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/paralleltext/", "ParallelText");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.paralleltext.ParallelText.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/paralleltext/", "ParallelTextWithId");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId.class;
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

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePairNotUniquelyDecidedException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoAccessPermissionException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.NoAccessPermissionException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "NoValidEndpointsException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.NoValidEndpointsException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ProcessFailedException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ProcessFailedException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServerBusyException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ServerBusyException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotActiveException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ServiceNotActiveException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "ServiceNotFoundException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.ServiceNotFoundException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedLanguagePairException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "ArrayOf_tns1_ParallelTextWithId");
            cachedSerQNames.add(qName);
            cls = jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/paralleltext/", "ParallelTextWithId");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://localhost:8080/wrapper-mock-1.2/services/ParallelTextWithExternalMetadata", "ArrayOf_xsd_string");
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

    public jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[] searchParallelTextsFromCandidates(java.lang.String sourceLang, java.lang.String targetLang, java.lang.String text, java.lang.String matchingMethod, java.lang.String[] candidateIds) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException, jp.go.nict.langrid.ws_1_2.NoValidEndpointsException, jp.go.nict.langrid.ws_1_2.ServiceNotActiveException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.ServerBusyException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ProcessFailedException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://paralleltext.ws_1_2.wrapper.langrid.nict.go.jp", "searchParallelTextsFromCandidates"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sourceLang, targetLang, text, matchingMethod, candidateIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[]) org.apache.axis.utils.JavaUtils.convert(_resp, jp.go.nict.langrid.ws_1_2.paralleltext.ParallelTextWithId[].class);
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
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException) {
              throw (jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.InvalidParameterException) {
              throw (jp.go.nict.langrid.ws_1_2.InvalidParameterException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException) {
              throw (jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoValidEndpointsException) {
              throw (jp.go.nict.langrid.ws_1_2.NoValidEndpointsException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServiceNotActiveException) {
              throw (jp.go.nict.langrid.ws_1_2.ServiceNotActiveException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) {
              throw (jp.go.nict.langrid.ws_1_2.AccessLimitExceededException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ServerBusyException) {
              throw (jp.go.nict.langrid.ws_1_2.ServerBusyException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) {
              throw (jp.go.nict.langrid.ws_1_2.NoAccessPermissionException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof jp.go.nict.langrid.ws_1_2.ProcessFailedException) {
              throw (jp.go.nict.langrid.ws_1_2.ProcessFailedException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
