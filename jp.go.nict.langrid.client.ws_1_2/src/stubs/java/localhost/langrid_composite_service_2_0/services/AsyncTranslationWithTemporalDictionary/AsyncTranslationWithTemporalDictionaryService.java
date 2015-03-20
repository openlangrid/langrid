/**
 * AsyncTranslationWithTemporalDictionaryService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.langrid_composite_service_2_0.services.AsyncTranslationWithTemporalDictionary;

public interface AsyncTranslationWithTemporalDictionaryService extends java.rmi.Remote {
    public java.lang.String startTranslation(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation[] in3, java.lang.String in4) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
    public jp.go.nict.langrid.ws_1_2.translation.AsyncTranslationResult getCurrentResult(java.lang.String in0) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
}
