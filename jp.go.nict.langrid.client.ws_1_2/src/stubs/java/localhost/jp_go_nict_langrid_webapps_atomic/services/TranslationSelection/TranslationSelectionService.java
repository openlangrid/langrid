/**
 * TranslationSelectionService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection;

public interface TranslationSelectionService extends java.rmi.Remote {
    public localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.SelectionResult select(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException, jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
}
