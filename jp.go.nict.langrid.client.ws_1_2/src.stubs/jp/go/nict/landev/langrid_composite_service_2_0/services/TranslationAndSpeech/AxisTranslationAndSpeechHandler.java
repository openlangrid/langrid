/**
 * AxisTranslationAndSpeechHandler.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.landev.langrid_composite_service_2_0.services.TranslationAndSpeech;

public interface AxisTranslationAndSpeechHandler extends java.rmi.Remote {
    public jp.go.nict.langrid.ws_1_2.speech.Speech translateAndSpeak(java.lang.String sourceLang, java.lang.String targetLang, java.lang.String source, java.lang.String targetVoiceType, java.lang.String targetAudioType) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.NoValidEndpointsException, jp.go.nict.langrid.ws_1_2.ServiceNotActiveException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.ServerBusyException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
}
